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

@Controller
public class EditUsersController {

	@Resource(name = "userRepository")
	GenerisktKodRepository<User> usersRepository;

	@Resource(name = "sektorRaadRepository")
	GenerisktHierarkisktKodRepository<SektorRaad> sektorRaadRepository;

	@RequestMapping(value = "/users")
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public String listUsers(ModelMap modelMap, HttpSession session) {
		checkSecurity(session);
		List<User> users = usersRepository.findByExample(new ExampleUser(), null);
		modelMap.addAttribute("users", users);
		return "users";
	}

	@RequestMapping(value = "/user", params = { "delete" })
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public String delete(ModelMap modelMap, @RequestParam Long id, HttpSession session) {
		// User user = usersRepository.findByExample(new User(id), 1).get(0);
		usersRepository.remove(id);
		usersRepository.flush();
		return listUsers(modelMap, session);
	}

	@RequestMapping(value = "/user", params = { "edit" })
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public String edit(ModelMap modelMap, @RequestParam Long id, HttpSession session) {
		User user = usersRepository.findByExample(new User(id), 1).get(0);
		modelMap.addAttribute("otherUser", user);
		session.setAttribute("otherUser", user);
		initSectors(user);
		return "user-form";
	}

	@RequestMapping(value = "/user", params = { "create" })
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public String create(ModelMap modelMap, HttpSession session) {
		User user = new User();
		modelMap.addAttribute("otherUser", user);
		session.setAttribute("otherUser", user);
		initSectors(user);
		return "user-form";
	}

	@Transactional(propagation = Propagation.REQUIRED)
	private void initSectors(User user) {
		List<SektorRaad> allRaads = sektorRaadRepository.getTreeRoots();
		markAllAsSelected(allRaads, user.getSektorRaad());
		user.setSektorRaad(allRaads);
	}

	private void markAllAsSelected(List<SektorRaad> targets, List<SektorRaad> flat) {
		for (SektorRaad target : targets) {
			if (flat.contains(target)) {
				target.setSelected(true);
			} else {
				target.setSelected(false);
			}
			markAllAsSelected(target.getChildren(), flat);
		}
	}

	@RequestMapping(value = "/user", params = { "toMain" })
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public String backToMain(HttpServletResponse response) throws IOException {
		response.sendRedirect("main");
		return null;
	}

	private void checkSecurity(HttpSession session) {
		User activeUser = (User) session.getAttribute("user");
		if (!activeUser.getUserEditor()) {
			throw new RuntimeException();
		}
	}

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

	@RequestMapping(value = "/user", params = { "cancel" })
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public String cancelEdit(ModelMap modelMap, HttpSession session) {
		return listUsers(modelMap, session);
	}

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

}
