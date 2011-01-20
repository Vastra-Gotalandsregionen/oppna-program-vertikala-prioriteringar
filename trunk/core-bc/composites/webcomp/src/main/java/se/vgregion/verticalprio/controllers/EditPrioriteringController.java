package se.vgregion.verticalprio.controllers;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanMap;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import se.vgregion.verticalprio.ApplicationData;
import se.vgregion.verticalprio.entity.AatgaerdsKod;
import se.vgregion.verticalprio.entity.AbstractKod;
import se.vgregion.verticalprio.entity.AtcKod;
import se.vgregion.verticalprio.entity.Column;
import se.vgregion.verticalprio.entity.DiagnosKod;
import se.vgregion.verticalprio.entity.Prioriteringsobjekt;
import se.vgregion.verticalprio.entity.VaardformsKod;
import se.vgregion.verticalprio.repository.GenerisktHierarkisktKodRepository;
import se.vgregion.verticalprio.repository.GenerisktKodRepository;

/**
 * @author Claes Lundahl, vgrid=clalu4
 * 
 */
@Controller
public class EditPrioriteringController extends ControllerBase {

    @Resource(name = "applicationData")
    ApplicationData applicationData;

    @Resource(name = "diagnosKodRepository")
    GenerisktHierarkisktKodRepository<DiagnosKod> diagnosKodRepository;

    @Resource(name = "aatgaerdsKodRepository")
    GenerisktKodRepository<AatgaerdsKod> aatgaerdsKodRepository;

    @Resource(name = "vaardformsKodRepository")
    GenerisktKodRepository<VaardformsKod> vaardformsKodRepository;

    @Resource(name = "atcKodRepository")
    GenerisktKodRepository<AtcKod> atcKodRepository;

    @RequestMapping(value = "/prio-open", params = { "delete-prio" })
    @Transactional
    public String initDeleteView(ModelMap model, HttpSession session, @RequestParam(required = false) Long id) {
        initView(model, session, id);
        model.addAttribute("editDir", new EditDirective(false, false));

        return "delete-prio-view";
    }

    @RequestMapping(value = "/prio-create")
    @Transactional
    public String create(ModelMap model, HttpSession session) {
        String result = initView(model, session, null);
        model.addAttribute("editDir", new EditDirective(true, null));
        return result;
    }

    @RequestMapping(value = "/prio-open")
    @Transactional
    public String initView(ModelMap model, HttpSession session, @RequestParam(required = false) Long id) {
        PrioriteringsobjektForm form = (PrioriteringsobjektForm) model.get("prio");
        if (form == null) {
            form = new PrioriteringsobjektForm();
        }
        model.addAttribute("prio", form);
        model.addAttribute("editDir", new EditDirective(true, null));
        initKodLists(form);

        if (id != null) {
            BeanMap formMap = new BeanMap(form);
            Prioriteringsobjekt prio = prioRepository.find(id);
            BeanMap entityMap = new BeanMap(prio);
            formMap.putAllWriteable(entityMap);
            form.putAllIdsFromCodesIfAnyIntoAttributeOnThisObject();
            form.getDiagnoser().toArray(); // Are not eager so we have to make sure they are
            form.getAatgaerdskoder().toArray(); // loaded before sending them to the jsp-layer.
            form.getVaardformskoder().toArray();
            form.getAtcKoder().toArray();
            if (form.getId() == null) {
                form.setId(id); // Strange... yes?
                // putAllWriteable seems not to work for this class on Long:s at least (and in the antonio-env).
                // TODO: own implementation of BeanMap
            }
        }

        return "prio-view";
    }

    @RequestMapping(value = "/delete-prio")
    @Transactional
    public String deletePrio(HttpServletRequest request, HttpServletResponse response, PrioriteringsobjektForm pf)
            throws IOException {
        Prioriteringsobjekt prio = toPrioriteringsobjekt(request, pf);
        prioRepository.remove(prio);
        String path = request.getRequestURI().replace("/delete-prio", "/main");
        response.sendRedirect(path);
        return "main";
    }

    @RequestMapping(value = "prio", params = { "save" })
    @Transactional
    public String save(HttpServletRequest request, HttpServletResponse response, PrioriteringsobjektForm pf)
            throws IOException {
        Prioriteringsobjekt prio = toPrioriteringsobjekt(request, pf);
        prioRepository.store(prio);
        String path = request.getRequestURI().replace("/prio", "/main");
        response.sendRedirect(path);
        return "main";
    }

    @RequestMapping(value = "prio", params = { "findAatgerder" })
    @Transactional
    public String findAatgerder(HttpServletRequest request, ModelMap model, PrioriteringsobjektForm pf,
            @RequestParam(required = false, value = "aatgaerdRef.selectedCodesId") List<String> selectedIds)
            throws InstantiationException, IllegalAccessException {
        return findCodesAction(model, pf, AatgaerdsKod.class, pf.getAatgaerdRef(), aatgaerdsKodRepository,
                pf.getAatgaerdskoder(), request);
    }

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
    private void initNestedValues(HttpServletRequest request, PrioriteringsobjektForm pf) {
        copyLongValues(request, "aatgaerdRef", pf.getAatgaerdRef());
        copyLongValues(request, "atcKoderRef", pf.getAtcKoderRef());
        copyLongValues(request, "diagnosRef", pf.getDiagnosRef());
        copyLongValues(request, "vaardformskoderRef", pf.getVaardformskoderRef());
    }

    /**
     * See initNestedValues for context.
     * 
     * @param request
     * @param requestProperty
     * @param target
     */
    private void copyLongValues(HttpServletRequest request, String requestProperty, ManyCodesRef<?> target) {
        String[] props = request.getParameterValues(requestProperty + ".selectedCodesId");
        if (props == null) {
            return;
        }
        for (String value : props) {
            target.getSelectedCodesId().add(Long.parseLong(value));
        }
    }

    @RequestMapping(value = "prio", params = { "findDiagnoses" })
    @Transactional
    public String findDiagnoses(HttpServletRequest request, ModelMap model,
            @ModelAttribute(value = "prio") PrioriteringsobjektForm pf,
            @RequestParam(required = false, value = "diagnosRef.selectedCodesId") List<String> selectedCodesId)
            throws InstantiationException, IllegalAccessException {

        initNestedValues(request, pf);
        return findCodesAction(model, pf, DiagnosKod.class, pf.getDiagnosRef(), diagnosKodRepository,
                pf.getDiagnoser(), request);
    }

    @RequestMapping(value = "prio", params = { "findVaardformer" })
    @Transactional
    public String findVaardformskoder(HttpServletRequest request, ModelMap model, PrioriteringsobjektForm pf,
            @RequestParam(required = false, value = "vaardformskoderRef.selectedCodesId") List<String> selectedIds)
            throws InstantiationException, IllegalAccessException {
        return findCodesAction(model, pf, VaardformsKod.class, pf.getVaardformskoderRef(),
                vaardformsKodRepository, pf.getVaardformskoder(), request);
    }

    @RequestMapping(value = "prio", params = { "findAtcKoder" })
    @Transactional
    public String findAtckoder(HttpServletRequest request, ModelMap model, PrioriteringsobjektForm pf,
            @RequestParam(required = false, value = "atcKoderRef.selectedCodesId") List<String> selectedIds)
            throws InstantiationException, IllegalAccessException {
        return findCodesAction(model, pf, AtcKod.class, pf.getAtcKoderRef(), atcKodRepository, pf.getAtcKoder(),
                request);
    }

    @Transactional
    private <T extends AbstractKod> String findCodesAction(ModelMap model, PrioriteringsobjektForm pf,
            Class<T> clazz, ManyCodesRef<T> mcr, GenerisktKodRepository<T> repo, List<T> target,
            HttpServletRequest request) throws InstantiationException, IllegalAccessException {
        model.addAttribute("editDir", new EditDirective(true, null));
        if (pf == null) {
            pf = new PrioriteringsobjektForm();
        }
        initNestedValues(request, pf);
        model.addAttribute("prio", pf);
        T kod = clazz.newInstance();

        kod.setBeskrivning(mcr.getSearchBeskrivningText());
        kod.setKod(mcr.getSearchKodText());
        mcr.getFindings().clear();
        mcr.getFindings().addAll(repo.findByExample(kod, 20));

        initKodLists(pf);
        initAllManyToOneCodes(pf);
        return "prio-view";
    }

    private void initAllManyToOneCodes(PrioriteringsobjektForm pf) {
        initManyToOneCode(pf.getDiagnosRef(), pf.getDiagnoser(), diagnosKodRepository);
        initManyToOneCode(pf.getAatgaerdRef(), pf.getAatgaerdskoder(), aatgaerdsKodRepository);
        initManyToOneCode(pf.getVaardformskoderRef(), pf.getVaardformskoder(), vaardformsKodRepository);
        initManyToOneCode(pf.getAtcKoderRef(), pf.getAtcKoder(), atcKodRepository);
    }

    private <T extends AbstractKod> void initManyToOneCode(ManyCodesRef<T> dr, List<T> target,
            GenerisktKodRepository<T> repo) {
        for (Long id : new HashSet<Long>(dr.getSelectedCodesId())) {
            T code = repo.find(id);
            target.add(code);
        }
    }

    private Prioriteringsobjekt toPrioriteringsobjekt(HttpServletRequest request, PrioriteringsobjektForm pf) {
        initKodLists(pf);
        pf.asignCodesFromTheListsByCorrespondingIdAttributes();
        Prioriteringsobjekt prio;
        if (pf.getId() == null) {
            prio = new Prioriteringsobjekt();
        } else {
            prio = prioRepository.find(pf.getId());
        }

        // These three lines copies attributes with the same name from pf to prio.
        BeanMap prioMap = new BeanMap(prio);
        BeanMap formMap = new BeanMap(pf);
        prioMap.putAllWriteable(formMap);

        if (pf.getDiagnosRef().getSelectedCodesId() != null) {
            for (Long id : pf.getDiagnosRef().getSelectedCodesId()) {
                DiagnosKod diagnos = diagnosKodRepository.find(id);
                prio.getDiagnoser().add(diagnos);
            }
        } else {
            prio.getDiagnoser().clear();
        }

        initNestedValues(request, pf);
        initKodLists(pf);
        initAllManyToOneCodes(pf);

        return prio;
    }

    private void initKodLists(PrioriteringsobjektForm pf) {
        applicationData.initKodLists(pf);
        for (Column column : getColumns()) {
            pf.getColumns().put(column.getName(), column);
        }
    }

}
