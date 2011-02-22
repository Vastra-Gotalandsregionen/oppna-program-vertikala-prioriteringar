package se.vgregion.verticalprio.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanMap;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import se.vgregion.verticalprio.PrioriteringsobjektFindCondition;
import se.vgregion.verticalprio.controllers.ChooseFromListController.ChooseListForm;
import se.vgregion.verticalprio.entity.Prioriteringsobjekt;
import se.vgregion.verticalprio.repository.finding.HaveNestedEntities;
import se.vgregion.verticalprio.repository.finding.HaveQuerySortOrder;

/**
 * @author Claes Lundahl, vgrid=clalu4
 * 
 */
@Controller
public class ChooseCodesController extends ControllerBase {

    // @Resource(name = "applicationData")
    // ApplicationData applicationData;
    //
    // @Resource(name = "diagnosKodRepository")
    // GenerisktHierarkisktKodRepository<DiagnosKod> diagnosKodRepository;
    //
    // @Resource(name = "aatgaerdsKodRepository")
    // GenerisktKodRepository<AatgaerdsKod> aatgaerdsKodRepository;
    //
    // @Resource(name = "vaardformsKodRepository")
    // GenerisktKodRepository<VaardformsKod> vaardformsKodRepository;
    //
    // @Resource(name = "atcKodRepository")
    // GenerisktKodRepository<AtcKod> atcKodRepository;
    //
    // @Resource(name = "rangordningsKodRepository")
    // GenerisktKodRepository<RangordningsKod> rangordningsKodRepository;
    //
    // @Resource(name = "tillstaandetsSvaarighetsgradKodRepository")
    // GenerisktKodRepository<TillstaandetsSvaarighetsgradKod> tillstaandetsSvaarighetsgradKodRepository;
    //
    // private static final Integer MAX_ROWS_TO_FIND = 20;
    //
    // private Map<String, GenerisktKodRepository<?>> nameToRepository;
    // private Map<String, Class<?>> nameToKodClass;

    Map<String, ChooseListFormWithDomainProperty> formPrototypes = new HashMap<String, ChooseListFormWithDomainProperty>();

    /**
     * 
     */
    public ChooseCodesController() {
        ChooseListFormWithDomainProperty symptomDiagnosTextForm = new ChooseListFormWithDomainProperty();
        symptomDiagnosTextForm.setDisplayKey("beskrivning");
        symptomDiagnosTextForm.setIdKey("id");
        symptomDiagnosTextForm.setFilterLabel("Sök diagnos med nyckelord");
        symptomDiagnosTextForm.setNotYetChoosenLabel("Diagnoskoder");
        symptomDiagnosTextForm.setChoosenLabel("Valda diagnoser");
        symptomDiagnosTextForm.setOkLabel("Välj diagnoser");
        symptomDiagnosTextForm.setOkUrl("main");
        symptomDiagnosTextForm.setCancelUrl("main");
        symptomDiagnosTextForm.setAllItemsPropertyName("diagnoser");
        formPrototypes.put("diagnosTexts", symptomDiagnosTextForm);

        ChooseListFormWithDomainProperty symptomDiagnosKodForm = symptomDiagnosTextForm.clone();
        symptomDiagnosKodForm.setDisplayKey("kod");
        formPrototypes.put("diagnosKodTexts", symptomDiagnosKodForm);

        ChooseListFormWithDomainProperty aatgardsTextForm = symptomDiagnosTextForm.clone();
        aatgardsTextForm.setAllItemsPropertyName("aatgaerdskoder");
        aatgardsTextForm.setFilterLabel("Sök åtgärder med nyckelord");
        aatgardsTextForm.setNotYetChoosenLabel("Åtgärdskoder");
        aatgardsTextForm.setChoosenLabel("Valda åtgärder");
        aatgardsTextForm.setOkLabel("Välj åtgärder");
        aatgardsTextForm.setOkUrl("main");
        aatgardsTextForm.setCancelUrl("main");
        aatgardsTextForm.setAllItemsPropertyName("aatgaerdskoder");
        formPrototypes.put("aatgaerdskoderTexts", aatgardsTextForm);

        ChooseListFormWithDomainProperty aatgardsKodTextForm = aatgardsTextForm.clone();
        aatgardsTextForm.setDisplayKey("kod");
        formPrototypes.put("aatgaerdskoder", aatgardsTextForm);

        ChooseListFormWithDomainProperty rangordningsKod = symptomDiagnosTextForm.clone();
        rangordningsKod.setAllItemsPropertyName("rangordningsKod");
        rangordningsKod.setFilterLabel("Sök rangordning med nyckelord");
        rangordningsKod.setNotYetChoosenLabel("Rangordningskoder");
        rangordningsKod.setChoosenLabel("Valda rangordningar");
        rangordningsKod.setOkLabel("Välj rangordningskoder");
        rangordningsKod.setOkUrl("main");
        rangordningsKod.setCancelUrl("main");
        rangordningsKod.setAllItemsPropertyName("rangordningsKod");
        formPrototypes.put("rangordningsKod", rangordningsKod);

        ChooseListFormWithDomainProperty atcTextForm = symptomDiagnosTextForm.clone();
        atcTextForm.setFilterLabel("Sök ATC-koder med nyckelord");
        atcTextForm.setNotYetChoosenLabel("ATC-koder");
        atcTextForm.setChoosenLabel("Valda ATC-koder");
        atcTextForm.setOkLabel("Välj ATC-texter");
        atcTextForm.setOkUrl("main");
        atcTextForm.setCancelUrl("main");
        atcTextForm.setAllItemsPropertyName("atcKoder");
        formPrototypes.put("atcText", atcTextForm);

        ChooseListFormWithDomainProperty atcKodForm = atcTextForm.clone();
        atcKodForm.setDisplayKey("kod");
        formPrototypes.put("atcKoder", atcKodForm);

        // vaardform
        ChooseListFormWithDomainProperty vaardform = symptomDiagnosTextForm.clone();
        vaardform.setFilterLabel("Sök vårdformer med nyckelord");
        vaardform.setNotYetChoosenLabel("Vårdformer");
        vaardform.setChoosenLabel("Valda vårdformer");
        vaardform.setOkLabel("Välj vårdformer");
        vaardform.setOkUrl("main");
        vaardform.setCancelUrl("main");
        vaardform.setAllItemsPropertyName("vaardform");
        formPrototypes.put("vaardform", vaardform);

        ChooseListFormWithDomainProperty tillstaandetsSvaarighetsgrad = new ChooseListFormWithDomainProperty();
        tillstaandetsSvaarighetsgrad.setFilterLabel("Sök tillståndets svårighetsgrad med nyckelord");
        tillstaandetsSvaarighetsgrad.setNotYetChoosenLabel("Svårighetsgrader");
        tillstaandetsSvaarighetsgrad.setChoosenLabel("Valda svårighetsgrader");
        tillstaandetsSvaarighetsgrad.setOkLabel("Välj svårighetsgrader");
        tillstaandetsSvaarighetsgrad.setOkUrl("main");
        tillstaandetsSvaarighetsgrad.setCancelUrl("main");
        tillstaandetsSvaarighetsgrad.setAllItemsPropertyName("tillstaandetsSvaarighetsgradKod");
        formPrototypes.put("tillstaandetsSvaarighetsgrad", tillstaandetsSvaarighetsgrad);

    }

    /**
     * 
     * @param pfcs
     * @param allItemsPropertyName
     * @param sortProperty
     * @return
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private List<?> extractChildObjects(List<Prioriteringsobjekt> pfcs, String allItemsPropertyName,
            String sortProperty) {
        SortedMap<Object, Object> tm = new TreeMap<Object, Object>();
        List<Object> values = new ArrayList<Object>();
        for (Prioriteringsobjekt prio : pfcs) {
            BeanMap prioMap = new BeanMap(prio);
            Object value = prioMap.get(allItemsPropertyName);
            if (value == null) {
                continue;
            }
            if (value instanceof Collection) {
                Collection<Object> col = (Collection<Object>) value;
                values.addAll(col);
            } else {
                values.add(value);
            }
        }
        return values;
        // return sort(values, sortProperty);
    }

    // @RequestMapping(value = "/choose-codes-init")
    // public String start(HttpSession session, ModelMap model) {
    // PrioriteringsobjektFindCondition condition = getOrCreateSessionObj(session, "prioCondition",
    // PrioriteringsobjektFindCondition.class);
    //
    // model.addAttribute("prioCondition", condition);
    // model.addAttribute("editDir", new EditDirective(true, true));
    //
    // return "choose-codes";
    // }

    @RequestMapping(value = "/start-choosing-codes", params = { "fieldName" })
    public String start(HttpSession session, HttpServletResponse response, @RequestParam String fieldName)
            throws IOException {
        PrioriteringsobjektFindCondition condition = getOrCreateSessionObj(session, "prioCondition",
                PrioriteringsobjektFindCondition.class);

        ChooseListFormWithDomainProperty clf = formPrototypes.get(fieldName);
        clf = clf.clone();
        String name = ChooseListForm.class.getSimpleName();
        session.setAttribute(name, clf);

        List<Prioriteringsobjekt> rows = (List<Prioriteringsobjekt>) session.getAttribute("rows");
        List allItems = extractChildObjects(rows, clf.getAllItemsPropertyName(), clf.getDisplayKey());
        clf.setAllItems(allItems);
        // clf.setAllToChoose(new ArrayList(allItems));
        Collection target = extractTargetCollection(condition, clf.getAllItemsPropertyName());
        clf.setTarget(target);
        clf.setChoosen(new ArrayList(target));

        String path = "choose-from-list";
        response.sendRedirect(path);
        return null;
    }

    @RequestMapping(value = "/deselect-codes", params = { "fieldName" })
    public String clear(HttpSession session, HttpServletResponse response, @RequestParam String fieldName)
            throws IOException {
        PrioriteringsobjektFindCondition condition = getOrCreateSessionObj(session, "prioCondition",
                PrioriteringsobjektFindCondition.class);

        ChooseListFormWithDomainProperty clf = formPrototypes.get(fieldName);
        Collection target = extractTargetCollection(condition, clf.getAllItemsPropertyName());

        clearAllNotHaveQuerySortOrder(target);

        response.sendRedirect("main");
        return null;
    }

    private <T> void clearAllNotHaveQuerySortOrder(Collection<T> items) {
        for (T item : new ArrayList<T>(items)) {
            if (!(item instanceof HaveQuerySortOrder)) {
                items.remove(item);
            }
            if (item instanceof HaveNestedEntities) {
                HaveNestedEntities hne = (HaveNestedEntities) item;
                clearAllNotHaveQuerySortOrder(hne.content());
            }
        }
    }

    /**
     * The condition should have a HaveNestedEntities object inside the PFC objects property with the name
     * propertyName. Or a collection with that interface or a single item wit that interface... The method returns
     * the result of its content().
     * 
     * @param condition
     * @param propertyName
     * @return
     */
    private Collection extractTargetCollection(PrioriteringsobjektFindCondition condition, String propertyName) {
        BeanMap bm = new BeanMap(condition);
        HaveNestedEntities hne = (HaveNestedEntities) bm.get(propertyName);
        return hne.content();
    }

    // private List sort(Collection values, String sortProperty) {
    // SortedMap tm = new TreeMap();
    // for (Object value : values) {
    // BeanMap valueMap = new BeanMap(value);
    // Object key = valueMap.get(sortProperty);
    // tm.put(key, value);
    // }
    // return new ArrayList(tm.values());
    // }

    // @RequestMapping(value = "/start-choosing-codes")
    // public String startChoosing(HttpSession session, HttpServletResponse response, @RequestParam String
    // property,
    // @RequestParam String idKey, @RequestParam String descKey) throws IOException {
    //
    // ChooseListForm clf = new ChooseListForm();
    // String name = ChooseListForm.class.getSimpleName();
    // session.setAttribute(name, clf);
    //
    // clf.setDisplayKey("beskrivning");
    // clf.setIdKey("id");
    // List<DiagnosKod> dks = new ArrayList<DiagnosKod>(diagnosKodRepository.findAll());
    // clf.setAllItems(dks);
    // clf.setFilterLabel("Sök diagnos med nyckelord");
    // clf.setNotYetChoosenLabel("Diagnoskoder");
    // clf.setChoosenLabel("Valda diagnoser");
    // clf.setOkLabel("Välj diagnoser");
    // clf.setTarget(new ArrayList());
    // clf.setOkUrl("main");
    // clf.setCancelUrl("main");
    //
    // String path = "choose-from-list";
    // response.sendRedirect(path);
    // return "";
    // }

    // @RequestMapping(value = "/choose-codes")
    // public String findAndOrSelect(HttpServletRequest request, HttpSession session, ModelMap model,
    // @RequestParam String codeRefName, HttpServletResponse response) throws InstantiationException,
    // IllegalAccessException, IOException {
    //
    // // session.setAttribute("displayKey", "beskrivning");
    // // session.setAttribute("idKey", "id");
    //
    // // session.setAttribute("chooseFromList", dks);
    //
    // String path = "choose-from-list";
    // response.sendRedirect(path);
    //
    // PrioriteringsobjektFindCondition condition = getOrCreateSessionObj(session, "prioCondition",
    // PrioriteringsobjektFindCondition.class);
    // model.addAttribute("prioCondition", condition);
    // model.addAttribute("editDir", new EditDirective(true, true));
    //
    // BeanMap bm = new BeanMap(condition);
    // ManyCodesRef<AbstractKod> ref = (ManyCodesRef<AbstractKod>) bm.get(codeRefName);
    //
    // String[] selected = request.getParameterValues(codeRefName + ".selectedCodesId");
    //
    // ref.setSearchKodText(request.getParameter(codeRefName + ".searchKodText"));
    // ref.setSearchBeskrivningText(request.getParameter(codeRefName + ".searchBeskrivningText"));
    //
    // AbstractKod example = (AbstractKod) getKodClass(codeRefName).newInstance();
    // example.setBeskrivning(ref.getSearchBeskrivningText());
    // example.setKod(ref.getSearchKodText());
    // GenerisktKodRepository<AbstractKod> repo = (GenerisktKodRepository<AbstractKod>) getRepo(codeRefName);
    // List<AbstractKod> findings = repo.findByExample(example, MAX_ROWS_TO_FIND);
    // new ArrayList<AbstractKod>(findings); // To ensure that jpa have everything loaded.
    // ref.setFindings(findings);
    // applicationData.initKodLists(condition);
    //
    // ref.getSelectedCodesId().clear();
    //
    // if (selected != null && selected.length > 0) {
    // ref.setCodes(new NestedHashSet<AbstractKod>());
    //
    // for (String id : new HashSet<String>(Arrays.asList(selected))) {
    // Long lid = Long.parseLong(id);
    // ref.getCodes().add(repo.find(lid));
    // ref.getSelectedCodesId().add(lid);
    // }
    // // Get rid of references deeper than the 'Kod' itself. JpqlMatchBuilder would generate conditions also
    // // for these.
    // if (example instanceof AbstractHirarkiskKod) {
    // for (AbstractKod ak : ref.getCodes()) {
    // AbstractHirarkiskKod<?> ahk = (AbstractHirarkiskKod<?>) ak;
    // ahk.setParentId(null);
    // ahk.setChildren(null);
    // }
    // }
    // } else {
    // ref.getCodes().clear();
    // }
    // model.addAttribute("overrideEdit", true);
    // return "choose-codes";
    // }
    //
    // private GenerisktKodRepository<?> getRepo(String propertyName) {
    // if (nameToRepository == null) {
    // nameToRepository = new HashMap<String, GenerisktKodRepository<?>>();
    // nameToRepository.put("aatgaerdRef", aatgaerdsKodRepository);
    // nameToRepository.put("atcKoderRef", atcKodRepository);
    // nameToRepository.put("diagnosRef", diagnosKodRepository);
    // nameToRepository.put("vaardformRef", vaardformsKodRepository);
    // nameToRepository.put("rangordningsRef", rangordningsKodRepository);
    // nameToRepository.put("tillstaandetsSvaarighetsgradRef", tillstaandetsSvaarighetsgradKodRepository);
    // }
    // return nameToRepository.get(propertyName);
    // }
    //
    // private Class<?> getKodClass(String propertyName) {
    // if (nameToKodClass == null) {
    // nameToKodClass = new HashMap<String, Class<?>>();
    // nameToKodClass.put("aatgaerdRef", AatgaerdsKod.class);
    // nameToKodClass.put("atcKoderRef", AtcKod.class);
    // nameToKodClass.put("diagnosRef", DiagnosKod.class);
    // nameToKodClass.put("vaardformRef", VaardformsKod.class);
    // nameToKodClass.put("rangordningsRef", RangordningsKod.class);
    // nameToKodClass.put("tillstaandetsSvaarighetsgradRef", TillstaandetsSvaarighetsgradKod.class);
    // }
    // return nameToKodClass.get(propertyName);
    // }

    public static class ChooseListFormWithDomainProperty extends ChooseListForm {

        String allItemsPropertyName;

        /**
         * @inheritDoc
         */
        @Override
        public ChooseListFormWithDomainProperty clone() {
            return (ChooseListFormWithDomainProperty) super.clone();
        }

        /**
         * @return the allItemsPropertyName
         */
        public String getAllItemsPropertyName() {
            return allItemsPropertyName;
        }

        /**
         * @param allItemsPropertyName
         *            the allItemsPropertyName to set
         */
        public void setAllItemsPropertyName(String allItemsPropertyName) {
            this.allItemsPropertyName = allItemsPropertyName;
        }
    }

}
