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
import se.vgregion.verticalprio.entity.AbstractPrioriteringsobjekt;
import se.vgregion.verticalprio.entity.AtcKod;
import se.vgregion.verticalprio.entity.Column;
import se.vgregion.verticalprio.entity.DiagnosKod;
import se.vgregion.verticalprio.entity.Prioriteringsobjekt;
import se.vgregion.verticalprio.entity.PrioriteringsobjektUtkast;
import se.vgregion.verticalprio.entity.SektorRaad;
import se.vgregion.verticalprio.entity.User;
import se.vgregion.verticalprio.entity.VaardformsKod;
import se.vgregion.verticalprio.repository.GenerisktFinderRepository;
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

    @Resource(name = "prioUtkastRepository")
    protected GenerisktFinderRepository<PrioriteringsobjektUtkast> prioUtkastRepository;

    @RequestMapping(value = "/main", params = { "delete-prio" })
    @Transactional
    public String initDeleteView(ModelMap model, HttpSession session, @RequestParam(required = false) Long id) {
        initView(model, session, id, false);
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
        String result = initView(model, session, null, null);
        model.addAttribute("editDir", new EditDirective(true, null));
        return result;
    }

    @RequestMapping(value = "/main", params = { "edit-prio" })
    @Transactional
    public String edit(ModelMap model, HttpSession session, @RequestParam(required = false) Long id) {
        String result = initView(model, session, id, true);
        model.addAttribute("editDir", new EditDirective(true, null));
        return result;
    }

    @Transactional
    private PrioriteringsobjektUtkast getUtkast(Prioriteringsobjekt prio) {
        PrioriteringsobjektUtkast rootConstraint = new PrioriteringsobjektUtkast();
        rootConstraint.setSkarpVersion(prio);
        List<PrioriteringsobjektUtkast> results = prioUtkastRepository.findByExample(rootConstraint, 1);
        if (results.isEmpty()) {
            return null;
        }
        return results.get(0);
    }

    @RequestMapping(value = "/main", params = { "select-prio" })
    @Transactional
    public String initView(ModelMap model, HttpSession session, @RequestParam(required = false) Long id,
            Boolean forEdit) {

        PrioriteringsobjektForm form = (PrioriteringsobjektForm) model.get("prio");
        if (form == null) {
            form = new PrioriteringsobjektForm();
        }

        model.addAttribute("prio", form);
        session.setAttribute("prio", form);
        model.addAttribute("editDir", new EditDirective(false, false));
        initKodLists(form);

        BeanMap formMap = new BeanMap(form);
        AbstractPrioriteringsobjekt prio = null;
        if (id != null) {
            prio = getPrioOrUtkast(id);
            User user = (User) session.getAttribute("user");
            if (user != null && prio instanceof Prioriteringsobjekt && (user.isApprover() || user.isEditor())) {
                form.setSkarpVersion((Prioriteringsobjekt) prio);
                // Keep in mind what object to use as the active one.
            }
        } else {
            prio = new PrioriteringsobjektUtkast();
        }

        BeanMap entityMap = new BeanMap(prio);
        formMap.putAllWriteable(entityMap);
        if (prio instanceof Prioriteringsobjekt) {
            // If this is an Prioriteringsobjekt then we don't, should, not need
            // the id. Either the post is just for showing to the user, then we don't need it. Or
            // we have just started editing a post that have no utkast in the db. Setting an id
            // would fool jpa later to believe it should try to update a post instead of creating one.
            form.setId(null);
        } else {
            form.setId(id); // Strange... yes?
            // putAllWriteable seems not to work for this class on Long:s at least (and in the antonio-env).
            // TODO: own implementation of BeanMap
        }
        form.putAllIdsFromCodesIfAnyIntoAttributeOnThisObject();
        // form.getDiagnoser().toArray(); // Are not eager so we have to make sure they are
        // form.getAatgaerdskoder().toArray(); // loaded before sending them to the jsp-layer.
        // form.getAtcKoder().toArray();
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

    @Transactional
    AbstractPrioriteringsobjekt getPrioOrUtkast(long id) {
        AbstractPrioriteringsobjekt result = null;
        result = prioRepository.find(id);
        if (result == null) {
            result = prioUtkastRepository.find(id);
        }
        return result;
    }

    @RequestMapping(value = "/delete-prio")
    @Transactional
    public String deletePrio(HttpServletRequest request, HttpServletResponse response, HttpSession session)
            throws IOException {
        PrioriteringsobjektForm pf = (PrioriteringsobjektForm) session.getAttribute("prio");
        AbstractPrioriteringsobjekt prio = getPrioOrUtkast(pf.getId());

        if (prio instanceof Prioriteringsobjekt) {
            prioRepository.remove((Prioriteringsobjekt) prio);
        } else {
            PrioriteringsobjektUtkast utkast = (PrioriteringsobjektUtkast) prio;
            prioUtkastRepository.remove(utkast);
            if (utkast.getSkarpVersion() != null) {
                prioRepository.remove(utkast.getSkarpVersion());
            }
        }

        session.setAttribute("prio", null);
        response.sendRedirect("main");
        return "main";
    }

    @RequestMapping(value = "prio", params = { "save" })
    @Transactional
    public String save(HttpServletRequest request, HttpServletResponse response, HttpSession session,
            @ModelAttribute("prio") PrioriteringsobjektForm pf) throws IOException {

        PrioriteringsobjektForm sessionPrio = (PrioriteringsobjektForm) session.getAttribute("prio");
        PrioriteringsobjektUtkast prio = toPrioriteringsobjektUtkast(request, pf, session);
        copyKodCollectionsAndMetaDates(sessionPrio, prio);

        prio.setSenastUppdaterad(new Date());

        String error = prio.getMessagesWhyNotSaveAble();
        if (error != null) {
            MessageHome mh = getOrCreateSessionObj(session, "messageHome", MessageHome.class);
            mh.setMessage(error);
            return "prio-view";
        } else {
            if (sessionPrio.getSkarpVersion() != null) {
                // If this is a completely new 'utkast', then we have to connect it to a
                // prioriteringsobjekt (if such exists, otherwise the line below will set null anyway).
                // See the initView function for the setting of sessionPrio.skarpVersion.
                prio.setSkarpVersion(sessionPrio.getSkarpVersion());
            }
            prioUtkastRepository.store(prio);
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

    @RequestMapping(value = "prio", params = { "choose-diagnoser" })
    @Transactional
    public String chooseDiagnoserKod(HttpSession session, HttpServletResponse response,
            HttpServletRequest request, ModelMap model, @ModelAttribute("prio") PrioriteringsobjektForm pf)
            throws IOException {
        ChooseListForm clf = new ChooseListForm();
        session.setAttribute(ChooseListForm.class.getSimpleName(), clf);
        clf.setFilterLabel("Sök diagnoser med nyckelord");
        clf.setNotYetChoosenLabel("Ej valda diagnoser");
        clf.setChoosenLabel("Valda diagnoser");
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
        clf.setNotYetChoosenLabel("Ej valda åtgärdskoder");
        clf.setChoosenLabel("Valda åtgärdskoder");

        return chooseKod(session, response, request, model, pf, "aatgaerdskoder");
    }

    @RequestMapping(value = "prio", params = { "choose-atcKoder" })
    @Transactional
    public String chooseAtcKoder(HttpSession session, HttpServletResponse response, HttpServletRequest request,
            ModelMap model, @ModelAttribute("prio") PrioriteringsobjektForm pf) throws IOException {
        ChooseListForm clf = new ChooseListForm();
        session.setAttribute(ChooseListForm.class.getSimpleName(), clf);
        clf.setFilterLabel("Sök ATC-koder med nyckelord");
        clf.setNotYetChoosenLabel("Ej valda ATC-koder");
        clf.setChoosenLabel("Valda ATC-koder");

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

        ChooseListForm clf = getOrCreateSessionObj(session, ChooseListForm.class.getSimpleName(),
                ChooseListForm.class);

        clf.setOkLabel("Bekräfta val");
        clf.setDisplayKey("kodPlusBeskrivning");
        clf.setIdKey("id");
        clf.setOkUrl("prio?goBack=10");
        clf.setCancelUrl("prio?goBack=10");

        Collection<AbstractKod> target = (Collection<AbstractKod>) bm.get(kodWithField);
        clf.setTarget(target);
        clf.getChoosen().addAll(target);

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

    private void copyKodCollectionsAndMetaDates(AbstractPrioriteringsobjekt source,
            AbstractPrioriteringsobjekt target) {
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

    private PrioriteringsobjektUtkast toPrioriteringsobjektUtkast(HttpServletRequest request,
            PrioriteringsobjektForm pf, HttpSession session) {
        AbstractPrioriteringsobjekt prio = toPrioriteringsobjekt(request, pf, session);

        PrioriteringsobjektUtkast result = new PrioriteringsobjektUtkast();
        BeanMap resultMap = new BeanMap(result);
        BeanMap prioMap = new BeanMap(prio);
        resultMap.putAllWriteable(prioMap);

        return result;
    }

    private AbstractPrioriteringsobjekt toPrioriteringsobjekt(HttpServletRequest request,
            PrioriteringsobjektForm pf, HttpSession session) {
        initKodLists(pf);

        pf.asignCodesFromTheListsByCorrespondingIdAttributes();

        AbstractPrioriteringsobjekt prio;
        if (pf.getId() == null) {
            prio = new PrioriteringsobjektUtkast();
        } else {
            prio = prioRepository.find(pf.getId());
            if (prio == null) {
                prio = prioUtkastRepository.find(pf.getId());
            }
        }

        AbstractPrioriteringsobjekt sessionPrio = (AbstractPrioriteringsobjekt) session.getAttribute("prio");
        copyKodCollectionsAndMetaDates(sessionPrio, prio);
        session.setAttribute("prio", pf);

        BeanMap prioMap = new BeanMap(prio);
        BeanMap formMap = new BeanMap(pf);
        prioMap.putAllWriteable(formMap);
        // These three lines copies attributes with the same name from pf to prio.

        // if (pf.getDiagnosRef().getSelectedCodesId() != null) {
        // for (Long id : pf.getDiagnosRef().getSelectedCodesId()) {
        // DiagnosKod diagnos = diagnosKodRepository.find(id);
        // prio.getDiagnoser().add(diagnos);
        // }
        // } else {
        // prio.getDiagnoser().clear();
        // }

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
