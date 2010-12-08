package se.vgregion.verticalprio.controllers;

import javax.annotation.Resource;

import org.apache.commons.beanutils.BeanMap;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import se.vgregion.verticalprio.ApplicationData;
import se.vgregion.verticalprio.entity.Prioriteringsobjekt;
import se.vgregion.verticalprio.repository.PrioRepository;

/**
 * @author Claes Lundahl, vgrid=clalu4
 * 
 */
@Controller
public class EditPrioriteringController {

    @Resource(name = "applicationData")
    ApplicationData applicationData;

    @Resource(name = "prioRepository")
    PrioRepository prioRepository;

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
            BeanMap entityMap = new BeanMap(prioRepository.find(id));
            formMap.putAllWriteable(entityMap);
            form.putAllIdsFromCodesIfAnyIntoAttributeOnThisObject();
        }

        return "prio-view";
    }

    @RequestMapping(value = "prio-save")
    @Transactional
    public String save(/* ModelMap model */PrioriteringsobjektForm pf) {
        Prioriteringsobjekt prio = toPrioriteringsobjekt(pf);
        prioRepository.store(prio);
        return "main";
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
        BeanMap prioMap = new BeanMap(prio);
        BeanMap formMap = new BeanMap(pf);
        prioMap.putAllWriteable(formMap);
        return prio;
    }

    private void initKodLists(PrioriteringsobjektForm pf) {
        BeanMap pfMap = new BeanMap(pf);
        BeanMap adMap = new BeanMap(applicationData);
        pfMap.putAllWriteable(adMap);
    }

}
