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
public class EditSektorRaadController extends BaseController {

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
        SektorRaadBean.initLockedValue(bean, user);
    }

	@RequestMapping(value = "sektorer")
	@Transactional(readOnly = true)
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
			sectors.add(newSektorRaad);
		} else {
			insertNewSektorIntoTree(sectors, insert);
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
		store(sektorRaadRepository, userRepository, sectors, user);

		modelMap.addAttribute("sectors", null);
		session.setAttribute("sectors", null);
		session.setAttribute("form", null);

		response.sendRedirect("main");
		return null;
	}



}
