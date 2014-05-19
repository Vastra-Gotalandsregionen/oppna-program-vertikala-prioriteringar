package se.vgregion.verticalprio.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import se.vgregion.verticalprio.entity.SektorRaad;
import se.vgregion.verticalprio.entity.User;
import se.vgregion.verticalprio.repository.GenerisktHierarkisktKodRepository;
import se.vgregion.verticalprio.repository.GenerisktKodRepository;
import se.vgregion.verticalprio.repository.finding.HaveExplicitTypeToFind;
import se.vgregion.verticalprio.repository.finding.HaveOrderByPaths;
import se.vgregion.verticalprio.repository.finding.OrderByPath;

/**
 * Controller to handle administration of user accounts.
 * 
 * Basic crud-operations are provided in this class.
 * 
 * Used jsp:s are users.jsp (to list all of the users) user-form.jsp to edit.
 * 
 * @author Claes Lundahl, vgrid=clalu4.VGREGION
 */

@Controller
public class EditUsersController extends BaseController {

	@Resource(name = "userRepository")
	GenerisktKodRepository<User> usersRepository;

	@Resource(name = "sektorRaadRepository")
	GenerisktHierarkisktKodRepository<SektorRaad> sektorRaadRepository;

	private final long noUserSelectedId = -1234567890;

	/**
	 * Action for listing all of the users in the system table.
	 * 
	 * @param modelMap
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/users")
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public String listUsers(ModelMap modelMap, HttpSession session) {
		checkSecurity(session);
		List<User> users = usersRepository.findByExample(new ExampleUser(), null);
		modelMap.addAttribute("users", users);
		return "users";

	}

	/**
	 * Removes a user.
	 * 
	 * @param modelMap
	 * @param id
	 * @param session
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/user", params = { "delete" })
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public String delete(ModelMap modelMap, @RequestParam Long id, HttpSession session,
	        HttpServletResponse response) throws IOException {
		if (checkUserIsSelected(session, id)) {
			usersRepository.remove(id);
			usersRepository.flush();
		}
		return listUsers(modelMap, session);
	}

	/**
	 * Loads a User into the session and model, making it ready for edit in the view.
	 * 
	 * @param modelMap
	 * @param id
	 * @param session
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/user", params = { "edit" })
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public String edit(ModelMap modelMap, @RequestParam Long id, HttpSession session, HttpServletResponse response)
	        throws IOException {
		this.checkSecurity(session);
		if (!checkUserIsSelected(session, id)) {
			return listUsers(modelMap, session);
		}
		User user = usersRepository.findByExample(new User(id), 1).get(0);
		modelMap.addAttribute("otherUser", user);
		session.setAttribute("otherUser", user);
		initSectors(user, sektorRaadRepository);
		usersRepository.clear();
		return "user-form";
	}

	/**
	 * Instantiates a new User and puts it into the session and model - making it available for the ui to edit
	 * before saving.
	 * 
	 * @param modelMap
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/user", params = { "create" })
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public String create(ModelMap modelMap, HttpSession session) {
		User user = new User();
		modelMap.addAttribute("otherUser", user);
		session.setAttribute("otherUser", user);
		initSectors(user, sektorRaadRepository);
		return "user-form";
	}



	/**
	 * Web action redirecting to the main page of the application.
	 * 
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/user", params = { "toMain" })
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public String backToMain(HttpServletResponse response) throws IOException {
		response.sendRedirect("main");
		return null;
	}

	/**
	 * Saves the user. Combines data from the request with data inside the session (where 'deeper' data is stored -
	 * the SektorRaad memberships).
	 * 
	 * @param modelMap
	 * @param user
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/user", params = { "save" })
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public String save(ModelMap modelMap, @ModelAttribute User user, HttpSession session) {
		checkSecurity(session);

		mirrorUserInSession(user, session);
		session.setAttribute("otherUser", null);
		modelMap.addAttribute("otherUser", null);

		List<SektorRaad> raads = flattenSelected(user.getSektorRaad());
		user.setSektorRaad(raads);

		usersRepository.store(user);
		usersRepository.flush();
		return listUsers(modelMap, session);
	}

	/**
	 * Moves the user back to the users.jsp view - is called from the details view of edit/create User.
	 * 
	 * @param modelMap
	 * @param session
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/user", params = { "cancel" })
	// @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public String cancelEdit(ModelMap modelMap, HttpSession session, HttpServletResponse response)
	        throws IOException {

		modelMap.remove("otherUser");
		session.removeAttribute("otherUser");
		usersRepository.clear();

		response.sendRedirect("users");
		return null;
	}

	/**
	 * Togles the 'selected' property of items in the users tree of SektorRaad:s.
	 * 
	 * @param modelMap
	 * @param otherUser
	 * @param sectorId
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/user", params = { "sectorId" })
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public String selectSectors(ModelMap modelMap, @ModelAttribute User otherUser, @RequestParam Long sectorId,
	        HttpSession session) {
		mirrorUserInSession(otherUser, session);
		check(sectorId, otherUser.getSektorRaad());
		modelMap.addAttribute("otherUser", otherUser);
		return "user-form";
	}

	/**
	 * Checks to see if the default id of User is selected - that means no selection is made. Then it adds a
	 * message to the session and redirects to the main users.jsp page.
	 * 
	 * @param session
	 * @param id
	 * @throws IOException
	 */
	private boolean checkUserIsSelected(HttpSession session, Long id)
	        throws IOException {
		if (id == null || id.longValue() == noUserSelectedId) {
			MessageHome messageHome = new MessageHome();
			messageHome
			        .setMessage("Välj en post före den här opperationen (radioknappen längst till vänster i tabellen).");
			session.setAttribute("messageHome", messageHome);
			return false;
		} else {
			return true;
		}
	}

}
