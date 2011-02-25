package se.vgregion.verticalprio.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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

/**
 * @author Claes Lundahl, vgrid=clalu4
 * 
 */
public class EditPrioriteringController extends ControllerBase {

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
        initView(model, session, id);
        model.addAttribute("editDir", new EditDirective(false, false));
        return "delete-prio-view";
    }

    @RequestMapping(value = "/main", params = { "approve-prio" })
    @Transactional
    public String approve(HttpServletRequest request, HttpServletResponse response, ModelMap model,
            HttpSession session, @RequestParam(required = true) Long id) throws IOException {
        User user = (User) session.getAttribute("user");
        if (user != null && user.isApprover()) {
            Prioriteringsobjekt prio = prioRepository.find(id);
            if (user.getSektorRaad().contains(prio.getSektorRaad())) {
                if (prio.getGodkaend() != null) {
                    prio.underkann();
                } else {
                    try {
                        prio.godkaen();
                    } catch (IllegalAccessError e) {
                        MessageHome messageHome = getOrCreateSessionObj(session, "messageHome", MessageHome.class);
                        messageHome.setMessage(e.getMessage());
                        return "main";
                    }
                }
                prioRepository.merge(prio);
            } else {
                String message = "Du saknar behörighet till prioriteringsobjektet och kan därför inte ändra dess status.";
                MessageHome messageHome = getOrCreateSessionObj(session, "messageHome", MessageHome.class);
                messageHome.setMessage(message);
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
        String result = initView(model, session, id);
        model.addAttribute("editDir", new EditDirective(true, null));
        return result;
    }

    @RequestMapping(value = "/main", params = { "select-prio" })
    @Transactional
    public String initView(ModelMap model, HttpSession session, @RequestParam(required = false) Long id) {
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
            prio = prioRepository.find(id);
        } else {
            prio = new Prioriteringsobjekt();
        }
        BeanMap entityMap = new BeanMap(prio);
        formMap.putAllWriteable(entityMap);
        form.putAllIdsFromCodesIfAnyIntoAttributeOnThisObject();
        form.getDiagnoser().toArray(); // Are not eager so we have to make sure they are
        form.getAatgaerdskoder().toArray(); // loaded before sending them to the jsp-layer.
        form.getAtcKoder().toArray();
        if (form.getId() == null) {
            form.setId(id); // Strange... yes?
            // putAllWriteable seems not to work for this class on Long:s at least (and in the antonio-env).
            // TODO: own implementation of BeanMap
        }

        init(form.getSektorRaadList());

        return "prio-view";
    }

    @Transactional
    private void init(Collection<SektorRaad> raads) {
        if (raads != null) {
            for (SektorRaad raad : raads) {
                init(raad.getChildren());
            }
        }
    }

    @RequestMapping(value = "/delete-prio")
    @Transactional
    public String deletePrio(HttpServletRequest request, HttpServletResponse response, HttpSession session)
            throws IOException {
        PrioriteringsobjektForm pf = (PrioriteringsobjektForm) session.getAttribute("prio");
        Prioriteringsobjekt prio = toPrioriteringsobjekt(request, pf, session);
        prioRepository.remove(prio);
        session.setAttribute("prio", null);
        String path = request.getRequestURI().replace("/delete-prio", "/main");
        response.sendRedirect(path);
        return "main";
    }

    @RequestMapping(value = "prio", params = { "save" })
    @Transactional
    public String save(HttpServletRequest request, HttpServletResponse response, HttpSession session,
            @ModelAttribute("prio") PrioriteringsobjektForm pf) throws IOException {

        PrioriteringsobjektForm sessionPrio = (PrioriteringsobjektForm) session.getAttribute("prio");
        Prioriteringsobjekt prio = toPrioriteringsobjekt(request, pf, session);
        copyKodCollectionsAndMetaDates(sessionPrio, prio);

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

    @RequestMapping(value = "prio", params = { "cancel" })
    public String cancel(HttpServletRequest request, HttpServletResponse response, HttpSession session,
            @ModelAttribute("prio") PrioriteringsobjektForm pf) throws IOException {

        response.sendRedirect("main");
        return "main";
    }

    @RequestMapping(value = "prio", params = { "goBack" })
    public String prio(ModelMap model, HttpSession session) {
        Prioriteringsobjekt prio = (Prioriteringsobjekt) session.getAttribute("prio");
        model.put("prio", prio);
        return "prio-view";
    }

    // @RequestMapping(value = "prio", params = { "findAatgerder" })
    // @Transactional
    // public String findAatgerder(HttpServletRequest request, ModelMap model, PrioriteringsobjektForm pf,
    // @RequestParam(required = false, value = "aatgaerdRef.selectedCodesId") List<String> selectedIds)
    // throws InstantiationException, IllegalAccessException {
    // return findCodesAction(model, pf, AatgaerdsKod.class, pf.getAatgaerdRef(), aatgaerdsKodRepository,
    // pf.getAatgaerdskoder(), request);
    // }

    /**
     * Sets selectedCodesId in the nested ManyCodesRef-objects inside the PrioriteringsobjektForm with values from
     * the request.
     * 
     * This method should be redundant. Spring mvc should do the setting of 'selectedCodesId' property. It did this
     * once, then after some changes it stopped.
     * 
     * TODO: Remove this method and make Spring Mvc do this instead.
     * 
     * @param pf
     * @param request
     */
    // private void initNestedValues(HttpServletRequest request, PrioriteringsobjektForm pf) {
    // copyLongValues(request, "aatgaerdRef", pf.getAatgaerdRef());
    // copyLongValues(request, "atcKoderRef", pf.getAtcKoderRef());
    // copyLongValues(request, "diagnosRef", pf.getDiagnosRef());
    // }

    // private void copyLongValues(HttpServletRequest request, String requestProperty, ManyCodesRef<?> target) {
    // String[] props = request.getParameterValues(requestProperty + ".selectedCodesId");
    // if (props == null) {
    // return;
    // }
    // for (String value : props) {
    // target.getSelectedCodesId().add(Long.parseLong(value));
    // }
    // }

    @RequestMapping(value = "prio", params = { "choose-diagnoser" })
    @Transactional
    public String chooseDiagnoserKod(HttpSession session, HttpServletResponse response,
            HttpServletRequest request, ModelMap model, @ModelAttribute("prio") PrioriteringsobjektForm pf)
            throws IOException {
        return chooseKod(session, response, request, model, pf, "diagnoser");
    }

    @RequestMapping(value = "prio", params = { "choose-aatgaerdskoder" })
    @Transactional
    public String chooseAatgardskoder(HttpSession session, HttpServletResponse response,
            HttpServletRequest request, ModelMap model, @ModelAttribute("prio") PrioriteringsobjektForm pf)
            throws IOException {
        return chooseKod(session, response, request, model, pf, "aatgaerdskoder");
    }

    @RequestMapping(value = "prio", params = { "choose-atcKoder" })
    @Transactional
    public String chooseAtcKoder(HttpSession session, HttpServletResponse response, HttpServletRequest request,
            ModelMap model, @ModelAttribute("prio") PrioriteringsobjektForm pf) throws IOException {
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

        BeanMap bm = new BeanMap(pf);

        ChooseListForm clf = new ChooseListForm();
        clf.setDisplayKey("beskrivning");
        clf.setIdKey("id");
        clf.setFilterLabel("Sök kod med nyckelord");
        clf.setNotYetChoosenLabel("Möjliga Koder");
        clf.setChoosenLabel("Valda Koder");
        clf.setOkLabel("Välj koder");
        clf.setOkUrl("prio?goBack=10");
        clf.setCancelUrl("prio?goBack=10");

        Collection<AbstractKod> target = (Collection<AbstractKod>) bm.get(kodWithField);
        clf.setTarget(target);
        clf.getChoosen().addAll(target);

        BeanMap applicationDataMap = new BeanMap(applicationData);

        List<AbstractKod> allItems = (List<AbstractKod>) applicationDataMap.get(kodWithField + "List");
        clf.setAllItems(allItems);

        clf.setAllToChoose(new ArrayList<AbstractKod>(allItems));
        session.setAttribute(ChooseListForm.class.getSimpleName(), clf);

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

    // @RequestMapping(value = "prio", params = { "findDiagnoses" })
    // @Transactional
    // public String findDiagnoses(HttpServletRequest request, ModelMap model,
    // @ModelAttribute(value = "prio") PrioriteringsobjektForm pf,
    // @RequestParam(required = false, value = "diagnosRef.selectedCodesId") List<String> selectedCodesId)
    // throws InstantiationException, IllegalAccessException {
    //
    // initNestedValues(request, pf);
    // return findCodesAction(model, pf, DiagnosKod.class, pf.getDiagnosRef(), diagnosKodRepository,
    // pf.getDiagnoser(), request);
    // }
    //
    // @RequestMapping(value = "prio", params = { "findAtcKoder" })
    // @Transactional
    // public String findAtckoder(HttpServletRequest request, ModelMap model, PrioriteringsobjektForm pf,
    // @RequestParam(required = false, value = "atcKoderRef.selectedCodesId") List<String> selectedIds)
    // throws InstantiationException, IllegalAccessException {
    // return findCodesAction(model, pf, AtcKod.class, pf.getAtcKoderRef(), atcKodRepository, pf.getAtcKoder(),
    // request);
    // }

    // @Transactional
    // private <T extends AbstractKod> String findCodesAction(ModelMap model, PrioriteringsobjektForm pf,
    // Class<T> clazz, ManyCodesRef<T> mcr, GenerisktKodRepository<T> repo, Set<T> target,
    // HttpServletRequest request) throws InstantiationException, IllegalAccessException {
    // model.addAttribute("editDir", new EditDirective(true, null));
    // if (pf == null) {
    // pf = new PrioriteringsobjektForm();
    // }
    // initNestedValues(request, pf);
    // model.addAttribute("prio", pf);
    // T kod = clazz.newInstance();
    //
    // kod.setBeskrivning(mcr.getSearchBeskrivningText());
    // kod.setKod(mcr.getSearchKodText());
    // mcr.getFindings().clear();
    // mcr.getFindings().addAll(repo.findByExample(kod, 20));
    //
    // initKodLists(pf);
    // initAllManyToOneCodes(pf);
    // return "prio-view";
    // }

    // private void initAllManyToOneCodes(PrioriteringsobjektForm pf) {
    // initManyToOneCode(pf.getDiagnosRef(), pf.getDiagnoser(), diagnosKodRepository);
    // initManyToOneCode(pf.getAatgaerdRef(), pf.getAatgaerdskoder(), aatgaerdsKodRepository);
    // initManyToOneCode(pf.getAtcKoderRef(), pf.getAtcKoder(), atcKodRepository);
    // }

    private <T extends AbstractKod> void initManyToOneCode(ManyCodesRef<T> dr, Set<T> target,
            GenerisktKodRepository<T> repo) {
        for (Long id : new HashSet<Long>(dr.getSelectedCodesId())) {
            T code = repo.find(id);
            target.add(code);
        }
    }

    private void copyKodCollectionsAndMetaDates(Prioriteringsobjekt source, Prioriteringsobjekt target) {
        clearAndFillCollection(source.getAatgaerdskoder(), target.getAatgaerdskoder());
        clearAndFillCollection(source.getDiagnoser(), target.getDiagnoser());
        clearAndFillCollection(source.getAtcKoder(), target.getAtcKoder());
        target.setGodkaend(source.getGodkaend());
        target.setSenastUppdaterad(source.getSenastUppdaterad());
    }

    private <T extends AbstractKod> void clearAndFillCollection(Collection<T> source, Collection<T> target) {
        if (source == null || target == null) {
            return;
        }
        target.clear();
        target.addAll(source);
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

        Prioriteringsobjekt sessionPrio = (Prioriteringsobjekt) session.getAttribute("prio");
        copyKodCollectionsAndMetaDates(sessionPrio, prio);
        session.setAttribute("prio", pf);

        BeanMap prioMap = new BeanMap(prio);
        BeanMap formMap = new BeanMap(pf);
        prioMap.putAllWriteable(formMap);

        // These three lines copies attributes with the same name from pf to prio.

        if (pf.getDiagnosRef().getSelectedCodesId() != null) {
            for (Long id : pf.getDiagnosRef().getSelectedCodesId()) {
                DiagnosKod diagnos = diagnosKodRepository.find(id);
                prio.getDiagnoser().add(diagnos);
            }
        } else {
            prio.getDiagnoser().clear();
        }

        // initNestedValues(request, pf);
        initKodLists(pf);
        // initAllManyToOneCodes(pf);

        return prio;
    }

    private void initKodLists(PrioriteringsobjektForm pf) {
        applicationData.initKodLists(pf);
        for (Column column : getColumns()) {
            pf.getColumns().put(column.getName(), column);
        }
    }

}
