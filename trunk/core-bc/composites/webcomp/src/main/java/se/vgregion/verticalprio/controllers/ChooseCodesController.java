package se.vgregion.verticalprio.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanMap;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import se.vgregion.verticalprio.ApplicationData;
import se.vgregion.verticalprio.PrioriteringsobjektFindCondition;
import se.vgregion.verticalprio.entity.AatgaerdsKod;
import se.vgregion.verticalprio.entity.AbstractHirarkiskKod;
import se.vgregion.verticalprio.entity.AbstractKod;
import se.vgregion.verticalprio.entity.AtcKod;
import se.vgregion.verticalprio.entity.DiagnosKod;
import se.vgregion.verticalprio.entity.RangordningsKod;
import se.vgregion.verticalprio.entity.TillstaandetsSvaarighetsgradKod;
import se.vgregion.verticalprio.entity.VaardformsKod;
import se.vgregion.verticalprio.repository.GenerisktHierarkisktKodRepository;
import se.vgregion.verticalprio.repository.GenerisktKodRepository;
import se.vgregion.verticalprio.repository.NestedArrayList;

/**
 * @author Claes Lundahl, vgrid=clalu4
 * 
 */
@Controller
public class ChooseCodesController extends ControllerBase {

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

    @Resource(name = "rangordningsKodRepository")
    GenerisktKodRepository<RangordningsKod> rangordningsKodRepository;

    @Resource(name = "tillstaandetsSvaarighetsgradKodRepository")
    GenerisktKodRepository<TillstaandetsSvaarighetsgradKod> tillstaandetsSvaarighetsgradKodRepository;

    private Map<String, GenerisktKodRepository<?>> nameToRepository;
    private Map<String, Class<?>> nameToKodClass;

    @RequestMapping(value = "/choose-codes-init")
    public String start(HttpSession session, ModelMap model) {
        PrioriteringsobjektFindCondition condition = getOrCreateSessionObj(session, "prioCondition",
                PrioriteringsobjektFindCondition.class);
        model.addAttribute("prioCondition", condition);
        return "choose-codes";
    }

    @RequestMapping(value = "/choose-codes")
    public String findAndOrSelect(HttpServletRequest request, HttpSession session, ModelMap model,
            @RequestParam String codeRefName) throws InstantiationException, IllegalAccessException {
        PrioriteringsobjektFindCondition condition = getOrCreateSessionObj(session, "prioCondition",
                PrioriteringsobjektFindCondition.class);
        model.addAttribute("prioCondition", condition);

        BeanMap bm = new BeanMap(condition);
        ManyCodesRef<AbstractKod> ref = (ManyCodesRef<AbstractKod>) bm.get(codeRefName);

        String[] selected = request.getParameterValues(codeRefName + ".selectedCodesId");

        ref.setSearchKodText(request.getParameter(codeRefName + ".searchKodText"));
        ref.setSearchBeskrivningText(request.getParameter(codeRefName + ".searchBeskrivningText"));

        AbstractKod example = (AbstractKod) getKodClass(codeRefName).newInstance();
        example.setBeskrivning(ref.getSearchBeskrivningText());
        example.setKod(ref.getSearchKodText());
        GenerisktKodRepository<AbstractKod> repo = (GenerisktKodRepository<AbstractKod>) getRepo(codeRefName);
        List<AbstractKod> findings = repo.findByExample(example, new Integer(20));
        new ArrayList<AbstractKod>(findings); // To ensure that jpa have everything loaded.
        ref.setFindings(findings);
        applicationData.initKodLists(condition);

        ref.getSelectedCodesId().clear();

        if (selected != null && selected.length > 0) {
            ref.setCodes(new NestedArrayList<AbstractKod>());

            for (String id : new HashSet<String>(Arrays.asList(selected))) {
                Long lid = Long.parseLong(id);
                ref.getCodes().add(repo.find(lid));
                ref.getSelectedCodesId().add(lid);
            }
            // Get rid of references deeper than the 'Kod' itself. JpqlMatchBuilder would generate conditions also
            // for these.
            if (example instanceof AbstractHirarkiskKod) {
                for (AbstractKod ak : ref.getCodes()) {
                    AbstractHirarkiskKod<?> ahk = (AbstractHirarkiskKod<?>) ak;
                    ahk.setParentId(null);
                    ahk.setChildren(null);
                }
            }
        } else {
            ref.getCodes().clear();
        }

        return "choose-codes";
    }

    private GenerisktKodRepository<?> getRepo(String propertyName) {
        if (nameToRepository == null) {
            nameToRepository = new HashMap<String, GenerisktKodRepository<?>>();
            nameToRepository.put("aatgaerdRef", aatgaerdsKodRepository);
            nameToRepository.put("atcKoderRef", atcKodRepository);
            nameToRepository.put("diagnosRef", diagnosKodRepository);
            nameToRepository.put("vaardformskoderRef", vaardformsKodRepository);
            nameToRepository.put("rangordningsRef", rangordningsKodRepository);
            nameToRepository.put("tillstaandetsSvaarighetsgradRef", tillstaandetsSvaarighetsgradKodRepository);
        }
        return nameToRepository.get(propertyName);
    }

    private Class<?> getKodClass(String propertyName) {
        if (nameToKodClass == null) {
            nameToKodClass = new HashMap<String, Class<?>>();
            nameToKodClass.put("aatgaerdRef", AatgaerdsKod.class);
            nameToKodClass.put("atcKoderRef", AtcKod.class);
            nameToKodClass.put("diagnosRef", DiagnosKod.class);
            nameToKodClass.put("vaardformskoderRef", VaardformsKod.class);
            nameToKodClass.put("rangordningsRef", RangordningsKod.class);
            nameToKodClass.put("tillstaandetsSvaarighetsgradRef", TillstaandetsSvaarighetsgradKod.class);
        }
        return nameToKodClass.get(propertyName);
    }

    /**
     * copyLongValues(request, "aatgaerdRef", pf.getAatgaerdRef()); copyLongValues(request, "atcKoderRef",
     * pf.getAtcKoderRef()); copyLongValues(request, "diagnosRef", pf.getDiagnosRef()); copyLongValues(request,
     * "vaardformskoderRef", pf.getVaardformskoderRef());
     */

}
