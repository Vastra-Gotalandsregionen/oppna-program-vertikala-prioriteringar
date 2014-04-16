package se.vgregion.verticalprio.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanMap;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import se.vgregion.verticalprio.MainForm;
import se.vgregion.verticalprio.PrioriteringsobjektFindCondition;
import se.vgregion.verticalprio.controllers.ChooseFromListController.ChooseListForm;
import se.vgregion.verticalprio.entity.Column;
import se.vgregion.verticalprio.entity.DiagnosKod;
import se.vgregion.verticalprio.entity.Prioriteringsobjekt;
import se.vgregion.verticalprio.entity.SektorRaad;
import se.vgregion.verticalprio.entity.User;
import se.vgregion.verticalprio.repository.GenerisktFinderRepository;
import se.vgregion.verticalprio.repository.GenerisktKodRepository;
import se.vgregion.verticalprio.repository.finding.DateNullLogic;
import se.vgregion.verticalprio.repository.finding.HaveNestedEntities;
import se.vgregion.verticalprio.repository.finding.NestedSektorRaad;

@Controller
@SessionAttributes(value = { "confCols", "form" })
public class VerticalPrioController extends EditPrioriteringController {

	@Resource(name = "userRepository")
	GenerisktKodRepository<User> userRepository;

	@Resource(name = "diagnosRepository")
	GenerisktFinderRepository<DiagnosKod> diagnosRepository;

	@RequestMapping(value = "/main")
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public String main(HttpSession session) {
		session.setAttribute("editDir", new EditDirective(true, null));
		result(session);
		return "main";
	}

	@RequestMapping(value = "/main", params = { "logout" })
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public String logout(HttpSession session, HttpServletResponse response) throws IOException {
		session.setAttribute("user", null);
		session.setAttribute("loginResult", null);
		PrioriteringsobjektFindCondition condition = getOrCreateSessionObj(session, "prioCondition",
		        PrioriteringsobjektFindCondition.class);
		condition.setGodkaend(new DateNullLogic(true));
		response.sendRedirect("main");
        session.invalidate();
		return null;
	}

	@RequestMapping(value = "/main", params = { "edit-users" })
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public String handleUsers(HttpSession session, HttpServletResponse response) throws IOException {
		User user = (User) session.getAttribute("user");
		if (user.getUserEditor()) {
			response.sendRedirect("users");
		}
		return null;
	}

	@RequestMapping(value = "/main", params = { "edit-sectors" })
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public String handleSectors(HttpSession session, HttpServletResponse response) throws IOException {
		User user = (User) session.getAttribute("user");
		if (user.getUserEditor() || user.isApprover()) {
			response.sendRedirect("sektorer");
		}
		return null;
	}

	private boolean isBlank(String s) {
		if (s == null) {
			return true;
		}
		if ("".equals(s.trim())) {
			return true;
		}
		return false;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/main", params = { "login" })
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public String login(HttpSession session, @RequestParam(required = false) String userName,
	        @RequestParam(required = false) String password) {

		if (isBlank(userName) || isBlank(password)) {
			return main(session);
		}

		User example = new User();
		example.setVgrId(userName);
		example.setPassword(password);

		List<User> users = userRepository.findByExample(example, 1);
		if (users.isEmpty() || "".equals(password)) {
			session.setAttribute("user", null);
			session.setAttribute("loginResult", false);
		} else {
			User user = users.get(0);
			PrioriteringsobjektFindCondition condition = getOrCreateSessionObj(session, "prioCondition",
			        PrioriteringsobjektFindCondition.class);
			if (user.isEditor() || user.isApprover()) {
				condition.setGodkaend(new DateNullLogic(false));
			} else {
				condition.setGodkaend(new DateNullLogic(true));
			}

			Map userValues = new HashMap(new BeanMap(user)); // Insane... makes all lazy properties initialized.
			for (Object o : userValues.values()) {
				if (o instanceof Collection) {
					Collection c = (Collection) o;
					for (Object i : c) {
						new HashMap(new BeanMap(i));
					}
				}
			}

			session.setAttribute("user", user);
			session.setAttribute("loginResult", true);
		}
		return main(session);
	}

	@RequestMapping(value = "/main", params = { "excel" })
	public String excelTableInMainWindow(HttpServletResponse response) {
		return excelTable(response);
	}

	@RequestMapping(value = "/table.csv")
	public String excelTable(HttpServletResponse response) {
		response.setContentType("text/csv");
		return "excel-table.csv";
	}

	@RequestMapping(value = "/main", params = { "sortField" })
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public String alterSortOrder(HttpSession session, @RequestParam String sortField) {
		MainForm form = getMainForm(session);
		PrioriteringsobjektFindCondition condition = getOrCreateSessionObj(session, "prioCondition",
		        PrioriteringsobjektFindCondition.class);

		markColumnAsSorting(sortField, form);

		if ("rangordningsKod".equals(sortField)) {
			condition.sortByRangordningsKod();
		} else if ("tillstaandetsSvaarighetsgradKod".equals(sortField)) {
			condition.sortByTillstaandetsSvaarighetsgradKod();
		} else if ("diagnosKodTexts".equals(sortField)) {
			condition.sortByDiagnoser();
		} else if ("sektorRaad".equals(sortField)) {
			condition.sortBySektorsRaad();
		}

		result(session);
		return "main";
	}

	private void markColumnAsSorting(String fieldName, MainForm mf) {
		for (Column column : mf.getColumns()) {
			column.setSorting(fieldName.equals(column.getName()));
		}
	}

	@RequestMapping(value = "/commit-conf-columns")
	public String commColumnsCommit(final HttpSession session, HttpServletResponse response) throws IOException {
		MainForm form = getMainForm(session);
		SortedSet<Column> target = (SortedSet<Column>) session.getAttribute("selectedColumns");

		for (Column column : form.getColumns()) {
			column.setVisible(target.contains(column) || !column.isHideAble());
		}

		response.sendRedirect("main");
		return null;
	}

	@RequestMapping(value = "/main", params = { "init-conf-columns" })
	public String confColumnsStart(final HttpSession session, HttpServletResponse response) throws IOException {
		MainForm form = getMainForm(session);

		ChooseListForm clf = getOrCreateSessionObj(session, ChooseListForm.class.getSimpleName(),
		        ChooseListForm.class);

		clf.setNotYetChosenLabel("Dolda kolumner");
		clf.setChosenLabel("Synliga kolumner");
		clf.setOkLabel("Ok");

		clf.setDisplayKey("label");
		clf.setIdKey("id");
		clf.setFilterLabel(null);
		clf.setOkUrl("commit-conf-columns");
		clf.setCancelUrl("main");

		List<Column> allColumns = new ArrayList<Column>();
		List<Column> selected = new ArrayList<Column>();
		List<Column> notYetSelected = new ArrayList<Column>();

		SortedSet<Column> target = new TreeSet<Column>();
		session.setAttribute("selectedColumns", target);
		clf.setTarget(target);

		User user = (User) session.getAttribute("user");

		for (Column column : form.getColumns()) {
			if (column.isHideAble() && (!column.isDemandsEditRights() || user != null && user.isEditor())) {
				allColumns.add(column);
				if (column.isVisible()) {
					selected.add(column);
				} else {
					notYetSelected.add(column);
				}
			}
		}

		clf.setAllItems(allColumns);
		clf.setAllToChoose(new ArrayList<Column>());
		clf.setChosen(selected);

		response.sendRedirect("choose-from-list");
		return null;
	}

	/**
	 * The action for selecting a specific node of in the three of {@link SektorRaad}. It gets the three out of the
	 * session and then finds the node denoted by the Id-argument sent from the client. When found it toggles
	 * (selected = !selected) the value of the 'select' property on this node.
	 * 
	 * Special case in this is when the user have clicked the allSektorsRaad property (hosted on the
	 * {@link MainForm} form object). Then it clears away all other selections in favor of this one. If any other
	 * node is selected then it reversely un-selects this property.
	 * 
	 * @param session
	 * @param id
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/check", params = { "sectorId" })
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public String check(final HttpSession session, @RequestParam Integer sectorId, HttpServletResponse response)
	        throws IOException {
		MainForm form = getMainForm(session);

		if (sectorId.longValue() == -1l) {
			boolean b = form.getAllSektorsRaad().isSelected();
			form.getAllSektorsRaad().setSelected(!b);

			for (SektorRaad sr : form.getSectors()) {
				sr.setSelectedDeeply(false);
				// sr.setOpenDeeply(false);
			}

		} else {
			form.getAllSektorsRaad().setSelected(false);
			SektorRaad sector = getSectorById(sectorId, form.getSectors());
			sector.setSelected(!sector.isSelected());
			sector.setOpenDeeply(true);
		}

		response.sendRedirect("main");
		return "main";
	}

	@RequestMapping(value = "/check", params = { "openId" })
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public String open(final HttpSession session, @RequestParam Integer openId, HttpServletResponse response)
	        throws IOException {
		MainForm form = getMainForm(session);

		// if (openId.longValue() == -1l) {
		// boolean b = form.getAllSektorsRaad().isSelected();
		// form.getAllSektorsRaad().setSelected(!b);
		//
		// for (SektorRaad sr : form.getSectors()) {
		// sr.setSelectedDeeply(false);
		// }
		//
		// } else {
		form.getAllSektorsRaad().setSelected(false);
		SektorRaad sector = getSectorById(openId, form.getSectors());
		sector.setOpen(!sector.isOpen());
		// }

		response.sendRedirect("main");
		return "main";
	}

    // private boolean containsMarkedChildren(List<SektorRaad> raads) {
	// for (SektorRaad raad : raads) {
	// if (raad.isSelectedDeeply())
	// }
	// return false;
	// }

    /**
	 * Produces the result list in the main view of the application. It uses a
	 * {@link PrioriteringsobjektFindCondition} as search condition - this object is stored in the session.
	 * 
	 * @param session
	 * @return
	 */
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<Prioriteringsobjekt> result(HttpSession session) {
		PrioriteringsobjektFindCondition condition = getOrCreateSessionObj(session, "prioCondition",
		        PrioriteringsobjektFindCondition.class);
		MainForm mf = getMainForm(session);

		if (mf.getAllSektorsRaad().isSelected()) {
			if (condition.getSektorRaad() != null) {
				// user selected to show all Prios regardless of SR.
				// Remove all conditions that specifies specific SRs, except those that should indicate order by
				// directive.
				HaveNestedEntities<SektorRaad> hne = condition.getSektorRaad();
				// clearNonSortingLogic(hne);
				hne.content().clear();
			}
		} else {
			List<SektorRaad> raad = getMarkedLeafs(mf.getSectors());
			raad = flatten(raad);

			NestedSektorRaad sektorNest = condition.getSektorRaad();

			// Find out if there are selected sectors, taking regards to that there might be HaveSortOrder-objects
			// inside.
			sektorNest.content().clear();
			if (sektorNest != null && sektorNest.content() != null) {
				sektorNest.content().addAll(raad);
			}

			if (raad.isEmpty()) {
				List<Prioriteringsobjekt> zeroResult = new ArrayList<Prioriteringsobjekt>();
				session.setAttribute("rows", zeroResult);
				return zeroResult;
			}
		}

		List<Prioriteringsobjekt> result = new ArrayList<Prioriteringsobjekt>();
		result.addAll(prioRepository.findLargeResult(condition));
		List<SektorRaad> sectors = applicationData.getSektorRaadList();

		for (Prioriteringsobjekt prio : result) {
			prio.setSektorRaad(findRoot(sectors, prio.getSektorRaad()));
		}

		session.setAttribute("rows", result);
		return result;
	}

}
