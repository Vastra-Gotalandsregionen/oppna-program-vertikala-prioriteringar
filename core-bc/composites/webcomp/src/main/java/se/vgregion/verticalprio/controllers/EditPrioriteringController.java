package se.vgregion.verticalprio.controllers;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.beanutils.BeanMap;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import se.vgregion.verticalprio.ApplicationData;
import se.vgregion.verticalprio.entity.AbstractKod;

/**
 * @author Claes Lundahl, vgrid=clalu4
 * 
 */
@Controller
@RequestMapping(value = "/prio")
@SessionAttributes("prio")
public class EditPrioriteringController {

    @Resource(name = "applicationData")
    ApplicationData applicationData;

    @RequestMapping(value = "/open")
    public String initView(ModelMap model) {
        PrioriteringsobjektForm pf = (PrioriteringsobjektForm) model.get("prio");
        if (pf == null) {
            pf = new PrioriteringsobjektForm();
        }
        model.addAttribute("prio", pf);
        // pf.setRangordningsKodList(applicationData.getEveryRangordningsKod());
        initKodLists(pf);
        return "prio-view";
    }

    @RequestMapping(value = "/save")
    public String save(ModelMap model, PrioriteringsobjektForm form) {
        PrioriteringsobjektForm pf = (PrioriteringsobjektForm) model.get("prio");

        return "main";
    }

    private void initKodLists(PrioriteringsobjektForm pf) {
        BeanMap pfMap = new BeanMap(pf);
        BeanMap adMap = new BeanMap(applicationData);
        pfMap.putAllWriteable(adMap);
    }

    private AbstractKod getKodById(List<AbstractKod> codes, Long id) {
        if (id == null || codes == null) {
            return null;
        }
        for (AbstractKod ak : codes) {
            if (id.equals(ak.getId())) {
                return ak;
            }
        }
        return null;
    }

}
