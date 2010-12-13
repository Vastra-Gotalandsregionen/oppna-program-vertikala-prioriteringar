package se.vgregion.verticalprio.controllers;

import java.util.ArrayList;
import java.util.HashSet;

import javax.annotation.Resource;

import org.apache.commons.beanutils.BeanMap;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import se.vgregion.verticalprio.ApplicationData;
import se.vgregion.verticalprio.entity.AbstractKod;
import se.vgregion.verticalprio.entity.Column;
import se.vgregion.verticalprio.entity.DiagnosKod;
import se.vgregion.verticalprio.entity.Prioriteringsobjekt;
import se.vgregion.verticalprio.repository.GenerisktHierarkisktKodRepository;
import se.vgregion.verticalprio.repository.GenerisktKodRepository;
import se.vgregion.verticalprio.repository.PrioRepository;

/**
 * @author Claes Lundahl, vgrid=clalu4
 * 
 */
@Controller
public class EditPrioriteringController extends ControllerBase {

    @Resource(name = "applicationData")
    ApplicationData applicationData;

    @Resource(name = "prioRepository")
    PrioRepository prioRepository;

    @Resource(name = "diagnosKodRepository")
    GenerisktHierarkisktKodRepository<DiagnosKod> diagnosKodRepository;

    @RequestMapping(value = "prio-open")
    @Transactional
    public String initView(ModelMap model, @RequestParam(required = false) Long id) {
        PrioriteringsobjektForm form = (PrioriteringsobjektForm) model.get("prio");
        if (form == null) {
            form = new PrioriteringsobjektForm();
        }
        model.addAttribute("prio", form);
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
        }

        return "prio-view";
    }

    @RequestMapping(value = "prio", params = { "save" })
    @Transactional
    public String save(/* ModelMap model */PrioriteringsobjektForm pf) {
        Prioriteringsobjekt prio = toPrioriteringsobjekt(pf);
        prioRepository.store(prio);
        return "main";
    }

    @RequestMapping(value = "prio", params = { "findDiagnoses" })
    @Transactional
    public String findDiagnoses(ModelMap model, PrioriteringsobjektForm pf) {
        if (pf == null) {
            pf = new PrioriteringsobjektForm();
        }

        model.addAttribute("prio", pf);

        DiagnosKod diagnos = new DiagnosKod();
        ManyCodesRef<DiagnosKod> dr = pf.getDiagnosRef();

        diagnos.setBeskrivning(dr.getSearchWord());
        dr.getFindings().clear();
        dr.getFindings().addAll(diagnosKodRepository.findByExample(diagnos, 20));

        if (dr.getSelectedCodesId() == null) {
            dr.setSelectedCodesId(new ArrayList<Long>());
        }
        dr.getCodes().clear();
        dr.getSelectedCodesId().remove(null);
        for (Long id : new HashSet<Long>(dr.getSelectedCodesId())) {
            DiagnosKod kod = diagnosKodRepository.find(id);
            pf.getDiagnoser().add(kod);
        }

        initKodLists(pf);
        return "prio-view";
    }

    private <T extends AbstractKod> void findCodesAction(ManyCodesRef<T> codeRef, GenerisktKodRepository<T> repo) {

    }

    private Prioriteringsobjekt toPrioriteringsobjekt(PrioriteringsobjektForm pf) {
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

        return prio;
    }

    private void initKodLists(PrioriteringsobjektForm pf) {
        BeanMap pfMap = new BeanMap(pf);
        BeanMap adMap = new BeanMap(applicationData);
        pfMap.putAllWriteable(adMap);
        for (Column column : getColumns()) {
            pf.getColumns().put(column.getName(), column);
        }
    }

}
