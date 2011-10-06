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
public class EditUsersController {

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
		if (checkUserIsSelected(session, response, id)) {
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
		if (!checkUserIsSelected(session, response, id)) {
			return listUsers(modelMap, session);
		}
		User user = usersRepository.findByExample(new User(id), 1).get(0);
		modelMap.addAttribute("otherUser", user);
		session.setAttribute("otherUser", user);
		initSectors(user);
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
		initSectors(user);
		return "user-form";
	}

	/**
	 * Utility method to alter the SektorRaad objects in the User ready to be desplayed. It places all of the items
	 * that are available in the User (regardsless if the user have them) and marks the ones that the user have
	 * access to by setting the selected property in each SektorRaad.
	 * 
	 * @param user
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	private void initSectors(User user) {
		List<SektorRaad> allRaads = sektorRaadRepository.getTreeRoots();
		markAllAsSelected(allRaads, user.getSektorRaad());
		user.setSektorRaad(allRaads);
	}

	/**
	 * Sets the property 'selected' to true in the first parameter list if the item is inside the second parameter
	 * list.
	 * 
	 * @param targets
	 * @param flat
	 */
	private void markAllAsSelected(List<SektorRaad> targets, List<SektorRaad> flat) {
		if (targets != null) {
			for (SektorRaad target : targets) {
				if (flat.contains(target)) {
					target.setSelected(true);
				} else {
					target.setSelected(false);
				}
				markAllAsSelected(target.getChildren(), flat);
			}
		}
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
	 * Method to validate that the user really have the right to use this controllers functionality.
	 * 
	 * @param session
	 */
	private void checkSecurity(HttpSession session) {
		User activeUser = (User) session.getAttribute("user");
		if (!activeUser.getUserEditor()) {
			throw new RuntimeException();
		}
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
	 * Checks the 'selected' property of the provided list and puts all with the value true in the return. Is used
	 * to 'flatten' the tree-structured data of the provided SektorRaad List.
	 * 
	 * @param source
	 * @return
	 */
	private List<SektorRaad> flattenSelected(List<SektorRaad> source) {
		List<SektorRaad> result = new ArrayList<SektorRaad>();
		flattenSelected(source, result);
		return result;
	}

	private void flattenSelected(List<SektorRaad> source, List<SektorRaad> target) {
		for (SektorRaad item : source) {
			if (item.isSelected()) {
				target.add(item);
			}
			flattenSelected(item.getChildren(), target);
		}
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

	private void check(Long sectorId, Collection<SektorRaad> raads) {
		for (SektorRaad raad : raads) {
			if (raad.getId().equals(sectorId)) {
				raad.setSelected(!raad.isSelected());
				return;
			}
			check(sectorId, raad.getChildren());
		}
	}

	/**
	 * Merges the 'deeper data' (ie the SektorRaad:s) into a user that have been received from the client. It also
	 * puts the data into the session to be used the nedt time this operation have to be made.
	 * 
	 * @param postedUser
	 * @param session
	 */
	private void mirrorUserInSession(User postedUser, HttpSession session) {
		final String sessionKey = "otherUser";
		User userInSession = (User) session.getAttribute(sessionKey);
		if (userInSession == null) {
			session.setAttribute(sessionKey, postedUser);
			return;
		}
		postedUser.setSektorRaad(userInSession.getSektorRaad());

		if (postedUser.getApprover() == null) {
			postedUser.setApprover(false);
		}
		if (postedUser.getEditor() == null) {
			postedUser.setEditor(false);
		}
		if (postedUser.getUserEditor() == null) {
			postedUser.setUserEditor(false);
		}
		session.setAttribute(sessionKey, postedUser);
	}

	/**
	 * An class to use as search criteria. Is used to provide sorting to the resulting listing.
	 * 
	 * @author Claes Lundahl, vgrid=clalu4.VGREGION
	 * 
	 */
	static class ExampleUser extends User implements HaveOrderByPaths, HaveExplicitTypeToFind {

		private List<OrderByPath> paths = new ArrayList<OrderByPath>();

		public ExampleUser() {
			super();
			paths.add(new OrderByPath("firstName"));
			paths.add(new OrderByPath("lastName"));
		}

		@Override
		public List<OrderByPath> paths() {
			return paths;
		}

		@Override
		public Class<?> type() {
			return User.class;
		}

	}

	/**
	 * Checks to see if the default id of User is selected - that means no selection is made. Then it adds a
	 * message to the session and redirects to the main users.jsp page.
	 * 
	 * @param session
	 * @param response
	 * @param id
	 * @throws IOException
	 */
	private boolean checkUserIsSelected(HttpSession session, HttpServletResponse response, Long id)
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
