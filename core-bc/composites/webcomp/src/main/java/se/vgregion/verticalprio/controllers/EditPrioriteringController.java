package se.vgregion.verticalprio.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanMap;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import se.vgregion.verticalprio.ApplicationData;
import se.vgregion.verticalprio.Util;
import se.vgregion.verticalprio.controllers.ChooseFromListController.ChooseListForm;
import se.vgregion.verticalprio.entity.AatgaerdsKod;
import se.vgregion.verticalprio.entity.AbstractKod;
import se.vgregion.verticalprio.entity.AtcKod;
import se.vgregion.verticalprio.entity.Column;
import se.vgregion.verticalprio.entity.DiagnosKod;
import se.vgregion.verticalprio.entity.Prioriteringsobjekt;
import se.vgregion.verticalprio.entity.SektorRaad;
import se.vgregion.verticalprio.entity.User;
import se.vgregion.verticalprio.entity.VaardformsKod;
import se.vgregion.verticalprio.repository.GenerisktHierarkisktKodRepository;
import se.vgregion.verticalprio.repository.GenerisktKodRepository;
import se.vgregion.verticalprio.repository.PrioRepository;
import se.vgregion.verticalprio.repository.finding.JpqlMatchBuilder;

/**
 * @author Claes Lundahl, vgrid=clalu4
 * 
 */
public class EditPrioriteringController extends WebControllerBase {

	@Resource(name = "applicationData")
	protected ApplicationData applicationData;

	@Resource(name = "diagnosKodRepository")
	GenerisktHierarkisktKodRepository<DiagnosKod> diagnosKodRepository;

	@Resource(name = "aatgaerdsKodRepository")
	GenerisktKodRepository<AatgaerdsKod> aatgaerdsKodRepository;

	@Resource(name = "vaardformsKodRepository")
	GenerisktKodRepository<VaardformsKod> vaardformsKodRepository;

	@Resource(name = "atcKodRepository")
	GenerisktKodRepository<AtcKod> atcKodRepository;

	@Resource(name = "prioRepository")
	protected PrioRepository prioRepository;

	@RequestMapping(value = "/main", params = { "delete-prio" })
	@Transactional
	public String initDeleteView(ModelMap model, HttpSession session, @RequestParam(required = false) Long id) {

		User user = (User) session.getAttribute("user");
		if (!validateIdIsSelected(session, id)
		        || !isUserInSektorsRaadIfNotWarnWithMessage(user, prioRepository.find(id), session)) {
			return "main";
		}

		initView(model, session, id);
		PrioriteringsobjektForm form = (PrioriteringsobjektForm) model.get("prio");
		if (form.getChild() != null) {
			// If this have a child - then this object should be deleted before.
			// It means that this is a draft.
			model.addAttribute("prio", null);
			session.setAttribute("prio", null);
			initView(model, session, form.getChild().getId());
		}
		model.addAttribute("editDir", new EditDirective(false, false));
		return "delete-prio-view";
	}

	/**
	 * @param session
	 * @param id
	 * @return
	 */
	private boolean validateIdIsSelected(HttpSession session, Long id) {
		if (id == null) {
			// No id has been selected
			String message = "Var vänlig och markera det prioriteringsobjekt du vill arbeta med innan du trycker på knappen.";
			MessageHome messageHome = getOrCreateSessionObj(session, "messageHome", MessageHome.class);
			messageHome.setMessage(message);
			return false;
		} else {
			return true;
		}
	}

	private boolean isUserInSektorsRaadIfNotWarnWithMessage(User user, Prioriteringsobjekt prio,
	        HttpSession session) {

		if (user.getSektorRaad().contains(prio.getSektorRaad())) {
			return true;
		} else {
			String message = "Du saknar behörighet att utföra denna åtgärd på prioriteringsobjektet som tillhör Sektorsråd: ";
			SektorRaad sr = prio.getSektorRaad();
			if (sr == null) {
				return true;
			}
			if (sr.getParent() != null) {
				message += sr.getParent().getLabel() + " (" + sr.getLabel() + ") <br/>";
			} else {
				message += prio.getSektorRaad().getLabel() + ". <br/>";
			}

			if (!user.getSektorRaad().isEmpty()) {
				message += "<br>" + "Du är idag definierad inom följande Sektorsråd: ";
				StringBuilder buf = new StringBuilder();
				SortedSet<String> codeTexts = new TreeSet<String>();

				for (SektorRaad sektorsRaad : user.getSektorRaad()) {
					if (sektorsRaad.getParent() == null) {
						StringBuilder sb = new StringBuilder();
						sb.append(sektorsRaad.getKod());
						if (!sektorsRaad.getChildren().isEmpty()) {
							List<String> childNames = new ArrayList<String>();
							for (SektorRaad child : sektorsRaad.getChildren()) {
								if (user.getSektorRaad().contains(child)) {
									childNames.add(child.getKod());
								}
							}
							if (!childNames.isEmpty()) {
								sb.append(" (");
								sb.append(JpqlMatchBuilder.toString(childNames, ", "));
								sb.append(")");
							}
						}
						codeTexts.add(sb.toString());
					}
				}

				for (String code : codeTexts) {
					buf.append("<br/>-&nbsp;").append(code);
				}
				message += buf;
			} else {
				message += "Din användare är för närvarande inte medlem i något sektorsråd själv.";
			}

			MessageHome messageHome = getOrCreateSessionObj(session, "messageHome", MessageHome.class);
			messageHome.setMessage(message);
			return false;
		}
	}

	@RequestMapping(value = "/main", params = { "approve-prio" })
	@Transactional
	public String approve(HttpServletRequest request, HttpServletResponse response, ModelMap model,
	        HttpSession session, @RequestParam(required = false) Long id) throws IOException {

		if (!validateIdIsSelected(session, id)) {
			return "main";
		}

		User user = (User) session.getAttribute("user");
		if (user != null && user.isApprover()) {
			Prioriteringsobjekt prio = prioRepository.find(id);
			if (isUserInSektorsRaadIfNotWarnWithMessage(user, prio, session)) {

				try {
					prio.godkaen();
					Prioriteringsobjekt approvedVersion = null;
					if (prio.getChildren().isEmpty()) {
						// this prio has no previously approved children
						// Lets create a child that is approved.
						approvedVersion = new Prioriteringsobjekt();
					} else {
						// we only allow (for now) to have one approved version of a prio object
						// TODO if need to save old versions of approved prio objects this code needs to be changed
						approvedVersion = prio.getChildren().iterator().next();
					}
					Long nullOrApprovedId = approvedVersion.getId();
					prio.setGodkaend(null); // TODO should not be needed
					Util.copyValuesAndSetsFromBeanToBean(prio, approvedVersion);
					approvedVersion.getChildren().clear();
					approvedVersion.setId(nullOrApprovedId);
					approvedVersion.setGodkaend(new Date());
					prio.getChildren().add(approvedVersion);

					prioRepository.store(approvedVersion);
					prioRepository.store(prio);
				} catch (IllegalAccessError e) {
					MessageHome messageHome = getOrCreateSessionObj(session, "messageHome", MessageHome.class);
					messageHome.setMessage(e.getMessage());
					return "main";
				}

				prioRepository.merge(prio);
			} else {
				return "main";
			}
		}
		response.sendRedirect("main");
		return "main";
	}

	@RequestMapping(value = "/main", params = { "prio-create" })
	@Transactional
	public String create(ModelMap model, HttpSession session) {
		String result = initView(model, session, null);
		model.addAttribute("editDir", new EditDirective(true, null));
		return result;
	}

	@RequestMapping(value = "/main", params = { "edit-prio" })
	@Transactional
	public String edit(ModelMap model, HttpSession session, @RequestParam(required = false) Long id) {
		User user = (User) session.getAttribute("user");
		if (!validateIdIsSelected(session, id)
		        || !isUserInSektorsRaadIfNotWarnWithMessage(user, prioRepository.find(id), session)) {
			return "main";
		}

		String result = initView(model, session, id);
		model.addAttribute("editDir", new EditDirective(true, null));
		return result;
	}

	@RequestMapping(value = "/main", params = { "select-prio" })
	@Transactional
	public String view(ModelMap model, HttpSession session, @RequestParam(required = false) Long id) {
		if (!validateIdIsSelected(session, id)) {
			return "main";
		}
		return initView(model, session, id);
	}

	String initView(ModelMap model, HttpSession session, Long id) {
		PrioriteringsobjektForm form = (PrioriteringsobjektForm) model.get("prio");
		if (form == null) {
			form = new PrioriteringsobjektForm();
		}
		model.addAttribute("prio", form);
		session.setAttribute("prio", form);
		model.addAttribute("editDir", new EditDirective(false, false));
		initKodLists(form);

		BeanMap formMap = new BeanMap(form);
		Prioriteringsobjekt prio = null;
		if (id != null) {
			prio = new Prioriteringsobjekt();
			prio.setId(id);
			prio = prioRepository.findByExample(prio, 1).get(0);
			prio.getDiagnoser().toArray();
			prio.getAatgaerdskoder().toArray();
			prio.getAtcKoder().toArray();
			prio.getChildren().toArray();
		} else {

			prio = new Prioriteringsobjekt();
		}
		BeanMap entityMap = new BeanMap(prio);
		formMap.putAllWriteable(entityMap);
		form.putAllIdsFromCodesIfAnyIntoAttributeOnThisObject();
		initPrio(form);

		if (form.getId() == null) {
			form.setId(id); // Strange... yes?
			// putAllWriteable seems not to work for this class on Long:s at least (and in the antonio-env).
			// TODO: own implementation of BeanMap
		}

		init(form.getSektorRaadList());

		PrioriteringsobjektForm unalteredVersion = new PrioriteringsobjektForm();
		Util.copyValuesAndSetsFromBeanToBean(form, unalteredVersion);
		form.setUnalteredVersion(unalteredVersion);
		return "prio-view";
	}

    @RequestMapping(value = "/delete-prio", params = { "cancel" })
	public String cancelDelete(HttpServletResponse response) throws IOException {
		response.sendRedirect("main");
		return "main";
	}

	@RequestMapping(value = "/delete-prio", params = { "ok" })
	@Transactional
	public String deletePrio(HttpServletRequest request, HttpServletResponse response, HttpSession session)
	        throws IOException {
		PrioriteringsobjektForm pf = (PrioriteringsobjektForm) session.getAttribute("prio");

		prioRepository.remove(pf.getId());
		session.setAttribute("prio", null);

		response.sendRedirect("main");
		return "main";
	}

	@RequestMapping(value = "prio", params = { "save" })
	@Transactional
	public String save(HttpServletRequest request, HttpServletResponse response, HttpSession session,
	        @ModelAttribute("prio") PrioriteringsobjektForm pf) throws IOException {

		PrioriteringsobjektForm sessionPrio = (PrioriteringsobjektForm) session.getAttribute("prio");
		Prioriteringsobjekt prio = toPrioriteringsobjekt(request, pf, session);
		copyKodCollectionsAndMetaDates(sessionPrio, prio);
		prio.getChildren().addAll(sessionPrio.getChildren());

		prio.setSenastUppdaterad(new Date());

		String error = prio.getMessagesWhyNotSaveAble();
		if (error != null) {
			MessageHome mh = getOrCreateSessionObj(session, "messageHome", MessageHome.class);
			mh.setMessage(error);
			return "prio-view";
		} else {
			prioRepository.store(prio);
		}
		response.sendRedirect("main");
		return "main";
	}

	@RequestMapping(value = "prio", params = { "copy" })
	public String copy(HttpServletRequest request, HttpServletResponse response, HttpSession session,
	        @ModelAttribute("prio") PrioriteringsobjektForm pf, ModelMap model) throws IOException {

		PrioriteringsobjektForm sessionPrio = (PrioriteringsobjektForm) session.getAttribute("prio");

		copyKodCollectionsAndMetaDates(sessionPrio, pf);

		pf.setId(null);
		pf.setGodkaend(null);
		pf.getChildren().clear();
		pf.setSenastUppdaterad(new Date());

		Prioriteringsobjekt prioConv = toPrioriteringsobjekt(request, pf, session);

		return "prio-view";
	}

	@RequestMapping(value = "prio", params = { "cancel" })
	public String cancel(HttpServletRequest request, HttpServletResponse response, HttpSession session,
	        @ModelAttribute("prio") PrioriteringsobjektForm pf) throws IOException {

		response.sendRedirect("main");
		return "main";
	}

	@RequestMapping(value = "prio", params = { "goBack" })
	public String prio(ModelMap model, HttpSession session) {
		PrioriteringsobjektForm prio = (PrioriteringsobjektForm) session.getAttribute("prio");
		model.put("prio", prio);
		return "prio-view";
	}

	@RequestMapping(value = "prio", params = { "choose-diagnoser" })
	@Transactional
	public String chooseDiagnoserKod(HttpSession session, HttpServletResponse response,
	        HttpServletRequest request, ModelMap model, @ModelAttribute("prio") PrioriteringsobjektForm pf)
	        throws IOException {
        ChooseListForm clf = initChooseListForm();
        session.setAttribute(ChooseListForm.class.getSimpleName(), clf);
        return chooseKod(session, response, request, model, pf, "diagnoser");
	}

    @RequestMapping(value = "prio", params = { "choose-aatgaerdskoder" })
	@Transactional
	public String chooseAatgardskoder(HttpSession session, HttpServletResponse response,
	        HttpServletRequest request, ModelMap model, @ModelAttribute("prio") PrioriteringsobjektForm pf)
	        throws IOException {
		ChooseListForm clf = new ChooseListForm();
		session.setAttribute(ChooseListForm.class.getSimpleName(), clf);
		clf.setFilterLabel("Sök åtgärdskoder med nyckelord");
		clf.setNotYetChosenLabel("Ej valda åtgärdskoder");
		clf.setChosenLabel("Valda åtgärdskoder");

		return chooseKod(session, response, request, model, pf, "aatgaerdskoder");
	}

	@RequestMapping(value = "prio", params = { "choose-atcKoder" })
	@Transactional
	public String chooseAtcKoder(HttpSession session, HttpServletResponse response, HttpServletRequest request,
	        ModelMap model, @ModelAttribute("prio") PrioriteringsobjektForm pf) throws IOException {
		ChooseListForm clf = new ChooseListForm();
		session.setAttribute(ChooseListForm.class.getSimpleName(), clf);
		clf.setFilterLabel("Sök ATC-koder med nyckelord");
		clf.setNotYetChosenLabel("Ej valda ATC-koder");
		clf.setChosenLabel("Valda ATC-koder");

		return chooseKod(session, response, request, model, pf, "atcKoder");
	}

	private String chooseKod(HttpSession session, HttpServletResponse response, HttpServletRequest request,
	        ModelMap model, PrioriteringsobjektForm pf, String kodWithField) throws IOException {
		initKodLists(pf);
		pf.asignCodesFromTheListsByCorrespondingIdAttributes();

		PrioriteringsobjektForm sessionPrio = (PrioriteringsobjektForm) session.getAttribute("prio");
		copyKodCollectionsAndMetaDates(sessionPrio, pf);
		session.setAttribute("prio", pf);
		model.addAttribute("prio", pf);
		pf.setUnalteredVersion(sessionPrio.getUnalteredVersion());

		BeanMap bm = new BeanMap(pf);

		ChooseListForm clf = getOrCreateSessionObj(session, ChooseListForm.class.getSimpleName(),
		        ChooseListForm.class);
		clf.setOkLabel("Bekräfta val");
		clf.setDisplayKey("kodPlusBeskrivning");
		clf.setIdKey("id");
		clf.setOkUrl("prio?goBack=10");
		clf.setCancelUrl("prio?goBack=10");

		Collection<AbstractKod> target = (Collection<AbstractKod>) bm.get(kodWithField);
		clf.setTarget(target);
		clf.getChosen().addAll(target);

		BeanMap applicationDataMap = new BeanMap(applicationData);

		List<AbstractKod> allItems = (List<AbstractKod>) applicationDataMap.get(kodWithField + "List");
		clf.setAllItems(allItems);

		clf.setAllToChoose(new ArrayList<AbstractKod>(allItems));

		response.sendRedirect("choose-from-list");
		return null;
	}

	/**
	 * Gets a list of [property name of collection with enties:id of entetie]. Then loops throug them and removes
	 * those with the id in the collection belonging to the {@link PrioriteringsobjektForm}.
	 * 
	 * @param removeCode
	 * @param pf
	 * @return
	 */
	@RequestMapping(value = "prio", params = { "removeCodes" })
	public String removeCodes(@RequestParam(required = false) List<String> removeCode, ModelMap model,
	        HttpSession session, @ModelAttribute("prio") PrioriteringsobjektForm pf) {
		initKodLists(pf);
		pf.asignCodesFromTheListsByCorrespondingIdAttributes();

		PrioriteringsobjektForm sessionPrio = (PrioriteringsobjektForm) session.getAttribute("prio");
		copyKodCollectionsAndMetaDates(sessionPrio, pf);
		model.put("prio", pf);
		pf.setUnalteredVersion(sessionPrio.getUnalteredVersion());

		if (removeCode != null && !removeCode.isEmpty()) {
			String splitting = Pattern.quote(":");
			String key = removeCode.get(0).split(splitting)[0];
			BeanMap bm = new BeanMap(pf);
			Set<Long> ids = new HashSet<Long>();

			for (String id : removeCode) {
				String[] fieldAndId = id.split(splitting);
				if (fieldAndId[0].equals(key)) {
					id = fieldAndId[1];
					ids.add(Long.parseLong(id));
				}
			}
			Collection<AbstractKod> codes = (Collection<AbstractKod>) bm.get(key);
			for (AbstractKod ak : new ArrayList<AbstractKod>(codes)) {
				if (ids.contains(ak.getId())) {
					codes.remove(ak);
				}
			}
			Collection<AbstractKod> sessionCodes = (Collection<AbstractKod>) new BeanMap(sessionPrio).get(key);
			sessionCodes.retainAll(codes);
		}
		return "prio-view";
	}

    private Prioriteringsobjekt toPrioriteringsobjekt(HttpServletRequest request, PrioriteringsobjektForm pf,
	        HttpSession session) {
		initKodLists(pf);
		pf.asignCodesFromTheListsByCorrespondingIdAttributes();
		Prioriteringsobjekt prio;
		if (pf.getId() == null) {
			prio = new Prioriteringsobjekt();
		} else {
			prio = prioRepository.find(pf.getId());
		}

		PrioriteringsobjektForm sessionPrio = (PrioriteringsobjektForm) session.getAttribute("prio");
		copyKodCollectionsAndMetaDates(sessionPrio, prio);
		session.setAttribute("prio", pf);
		pf.setUnalteredVersion(sessionPrio.getUnalteredVersion());

		BeanMap prioMap = new BeanMap(prio);
		BeanMap formMap = new BeanMap(pf);
		prioMap.putAllWriteable(formMap);

		initKodLists(pf);

		return prio;
	}

    private void initKodLists(PrioriteringsobjektForm pf) {
        applicationData.initKodLists(pf);
        for (Column column : getColumns()) {
            pf.getColumns().put(column.getName(), column);
        }
    }

}
