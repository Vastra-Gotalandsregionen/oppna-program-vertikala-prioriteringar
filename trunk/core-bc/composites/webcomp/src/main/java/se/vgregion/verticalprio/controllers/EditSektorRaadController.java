package se.vgregion.verticalprio.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import se.vgregion.verticalprio.entity.SektorRaad;
import se.vgregion.verticalprio.entity.User;
import se.vgregion.verticalprio.repository.GenerisktHierarkisktKodRepository;
import se.vgregion.verticalprio.repository.GenerisktKodRepository;

@Controller
public class EditSektorRaadController {

	private static long idForUnsavedNewPosts = -2;

	@Resource(name = "userRepository")
	GenerisktKodRepository<User> userRepository;

	@Resource(name = "sektorRaadRepository")
	GenerisktHierarkisktKodRepository<SektorRaad> sektorRaadRepository;

	// @Transactional()
	// private void initHaveUserValue(SektorRaadBean bean) {
	// User user = new User();
	// user.getSektorRaad().add(SektorRaadBean.toSektorRaad(bean));
	// bean.setPrioCount(prioCount)
	//
	// for (SektorRaadBean child : bean.getBeanChildren()) {
	// initHaveUserValue(child);
	// }
	// }

	@Transactional()
	private void initLockedValue(SektorRaadBean bean, User user) {
		if (user.getUserEditor()) {
			// A user editor, or superuser, should be able to edit all data.
			// Quit immediately then because default the SectorRaadBean locked property is false.
			return;
		}
		Collection<SektorRaad> userSektors = user.getSektorRaad();

		boolean result = !userSektors.contains(SektorRaadBean.toSektorRaad(bean));
		bean.setLocked(result);
		if (!result) {
			for (SektorRaadBean child : bean.getBeanChildren()) {
				initLockedValue(child, user);
			}
		}
	}

	@RequestMapping(value = "sektorer")
	@Transactional()
	public String list(ModelMap modelMap, HttpSession session, ServletResponse response) {
		List<SektorRaadBean> sectors = SektorRaadBean.toSektorRaadBeans(sektorRaadRepository.getTreeRoots());

		User user = (User) session.getAttribute("user");

		for (SektorRaadBean bean : sectors) {
			initLockedValue(bean, user);
		}

		modelMap.addAttribute("sectors", sectors);
		session.setAttribute("sectors", sectors);
		response.setContentType("UTF-8");
		return "sectors";
	}

	@RequestMapping(value = "sektorer", params = { "delete" })
	@Transactional()
	public String delete(@RequestParam List<String> id, @RequestParam List<String> parentId,
	        @RequestParam List<String> kod, @RequestParam List<String> markedAsDeleted, @RequestParam Long delete,
	        @RequestParam List<String> prioCount, @RequestParam List<String> locked, ModelMap modelMap,
	        HttpSession session) {

		List<SektorRaadBean> sectors = toRaads(id, parentId, kod, markedAsDeleted, prioCount, locked);

		modelMap.addAttribute("sectors", sectors);
		session.setAttribute("sectors", sectors);

		markToDeleteWhenSave(delete, sectors);

		return "sectors";
	}

	private void markToDeleteWhenSave(Long id, List<SektorRaadBean> sectors) {
		int c = 0;
		for (SektorRaadBean sr : new ArrayList<SektorRaadBean>(sectors)) {
			if (sr.getBeanChildren() != null) {
				markToDeleteWhenSave(id, sr.getBeanChildren());
			}
			if (equals(id, sr.getId())) {
				if (id < 0) {
					sectors.remove(c);
				} else {
					sr.setMarkedAsDeleted(!sr.isMarkedAsDeleted());
				}
				return;
			}
			c++;
		}
	}

	@RequestMapping(value = "sektorer", params = { "insert" })
	@Transactional()
	public String insert(@RequestParam List<String> id, @RequestParam List<String> parentId,
	        @RequestParam List<String> kod, @RequestParam List<String> markedAsDeleted, @RequestParam Long insert,
	        @RequestParam List<String> prioCount, @RequestParam List<String> locked, ModelMap modelMap,
	        HttpSession session) {

		List<SektorRaadBean> sectors = toRaads(id, parentId, kod, markedAsDeleted, prioCount, locked);

		modelMap.addAttribute("sectors", sectors);
		session.setAttribute("sectors", sectors);

		if (insert.equals(-1l)) {
			SektorRaadBean newSektorRaad = new SektorRaadBean(idForUnsavedNewPosts--);
			System.out.println("newSektorRaad.getId(): " + newSektorRaad.getId());
			sectors.add(newSektorRaad);
		} else {
			insertNewSektorIntoTree(sectors, insert);
			System.out.println("sectors.size(2): " + sectors.size());
		}

		return "sectors";
	}

	private void insertNewSektorIntoTree(List<SektorRaadBean> sectors, Long id) {
		if (sectors == null) {
			return;
		}
		for (SektorRaadBean sr : sectors) {
			if (equals(id, sr.getId())) {
				if (sr.getBeanChildren() == null) {
					sr.setBeanChildren(new ArrayList<SektorRaadBean>());
				}
				SektorRaadBean newRaad = new SektorRaadBean();
				newRaad.setParent(sr);
				newRaad.setParentId(id);
				newRaad.setId(idForUnsavedNewPosts--);
				sr.getBeanChildren().add(newRaad);
				return;
			}
			insertNewSektorIntoTree(sr.getBeanChildren(), id);
		}
	}

	private Long toLongOrNull(String s) {
		if (s == null || s.isEmpty()) {
			return null;
		}
		return Long.parseLong(s);
	}

	private List<SektorRaadBean> toRaads(List<String> id, List<String> parentId, List<String> kod,
	        List<String> markedAsDeleted, List<String> prioCount, List<String> locked) {
		List<SektorRaadBean> result = new ArrayList<SektorRaadBean>();

		int c = 0;
		for (String itemId : id) {
			SektorRaadBean item = new SektorRaadBean();
			item.setId(toLongOrNull(itemId));
			item.setPrioCount(Integer.parseInt(prioCount.get(c)));
			item.setParentId(toLongOrNull(parentId.get(c)));
			// item.setKortBeskrivning(kortBeskrivning.get(c));
			// item.setBeskrivning(beskrivning.get(c));
			item.setKod(kod.get(c));
			item.setMarkedAsDeleted("true".equals(markedAsDeleted.get(c)));
			item.setLocked("true".equals(locked.get(c)));
			c++;
			result.add(item);
		}

		result = arrangeFlatDataAccordingToParentChildValues(result);

		return result;
	}

	private List<SektorRaadBean> arrangeFlatDataAccordingToParentChildValues(List<SektorRaadBean> data) {
		List<SektorRaadBean> result = arrangeFlatDataAccordingToParentChildValues(null, data);
		data.removeAll(result);

		for (SektorRaadBean sr : result) {
			sr.setBeanChildren(arrangeFlatDataAccordingToParentChildValues(sr.getId(), data));
			data.removeAll(sr.getBeanChildren());
		}

		return result;
	}

	private List<SektorRaadBean> arrangeFlatDataAccordingToParentChildValues(Long parentId,
	        List<SektorRaadBean> data) {
		List<SektorRaadBean> result = new ArrayList<SektorRaadBean>();

		for (SektorRaadBean sr : data) {
			if (equals(parentId, sr.getParentId())) {
				result.add(sr);
				if (sr.getId() != null) {
					sr.setBeanChildren(arrangeFlatDataAccordingToParentChildValues(sr.getId(), data));
				}
			}
		}

		return result;
	}

	private boolean equals(Long l1, Long l2) {
		if (l1 == l2) {
			return true;
		}
		if (l1 == null || l2 == null) {
			return false;
		}
		return l1.equals(l2);
	}

	@RequestMapping(value = "/sektorer", params = { "toMain" })
	public String toMain(HttpServletResponse response) throws IOException {
		response.sendRedirect("main");
		return null;
	}

	@RequestMapping(value = "/sektorer", params = { "save" })
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public String save(HttpServletResponse response, @RequestParam List<String> kod,
	        @RequestParam List<String> id, @RequestParam List<String> parentId,
	        @RequestParam List<String> markedAsDeleted, @RequestParam List<String> prioCount,
	        @RequestParam List<String> locked, ModelMap modelMap, HttpSession session) throws IOException {

		List<SektorRaadBean> sectors = toRaads(id, parentId, kod, markedAsDeleted, prioCount, locked);

		User user = (User) session.getAttribute("user");

		removeFromList(sectors);
		store(sectors, user);

		modelMap.addAttribute("sectors", null);
		session.setAttribute("sectors", null);
		session.setAttribute("form", null);

		response.sendRedirect("main");
		return null;
	}

	@Transactional
	private void removeFromList(List<SektorRaadBean> beans) {
		for (SektorRaadBean child : new ArrayList<SektorRaadBean>(beans)) {
			removeFromList(child.getBeanChildren());
			if (child.isMarkedAsDeleted()) {
				SektorRaad sr = SektorRaadBean.toSektorRaad(child);
				if (sr.getId() > 0) {
					// An id less than 0 signals that the object is not saved earlier. No actual operation against
					// the db is then needed.
					// sektorRaadRepository.remove(sr.getId());
				}
				beans.remove(child);
			}
		}
	}

	@Transactional
	private void store(List<SektorRaadBean> beans, User user) {

		List<SektorRaad> persistentData = sektorRaadRepository.getTreeRoots();
		List<SektorRaad> source = SektorRaadBean.toSektorRaads(beans);
		applyChange(source, persistentData, user);

		for (SektorRaad sr : persistentData) {
			sektorRaadRepository.merge(sr);
		}

		sektorRaadRepository.flush();
	}

	@Transactional
	private void applyChange(List<SektorRaad> sources, List<SektorRaad> targets, User user) {
		Set<Long> ids = new HashSet<Long>();
		for (SektorRaad sr : sources) {
			if (sr.getId() > 0) {
				ids.add(sr.getId());
			} else {
				sr.setId(null);
				targets.add(sr);
				sr.setUsers(new HashSet<User>());
				sr.getUsers().add(user);
				sr = sektorRaadRepository.persist(sr);
				sektorRaadRepository.flush();
				user.getSektorRaad().add(sr);
				userRepository.merge(user);
				userRepository.flush();
				ids.add(sr.getId());
			}
		}

		for (SektorRaad target : new ArrayList<SektorRaad>(targets)) {
			if (target.getId() != null && !ids.contains(target.getId())) {
				target.getUsers().clear();

				// If users have this assigned - remove those first.
				for (User targetUser : target.getUsers()) {
					targetUser.getSektorRaad().remove(target);
					userRepository.merge(targetUser);
					userRepository.flush();
				}

				targets.remove(target);
				// System.out.println("Removar " + target.getId());
				sektorRaadRepository.remove(target.getId());
			} else {
				applyChange(find(sources, target.getId()), target, user);
			}
		}

	}

	@Transactional
	private SektorRaad find(Collection<SektorRaad> collection, Long id) {
		for (SektorRaad sr : collection) {
			if (id.equals(sr.getId())) {
				return sr;
			}
		}
		return null;
	}

	@Transactional
	private void applyChange(SektorRaad source, SektorRaad target, User user) {
		target.setKod(source.getKod());
		applyChange(source.getChildren(), target.getChildren(), user);
	}
}
