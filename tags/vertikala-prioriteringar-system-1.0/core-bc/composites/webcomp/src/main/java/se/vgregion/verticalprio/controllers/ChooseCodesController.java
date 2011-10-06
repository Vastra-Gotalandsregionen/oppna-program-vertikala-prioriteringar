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
import se.vgregion.verticalprio.entity.AbstractKod;
import se.vgregion.verticalprio.entity.Prioriteringsobjekt;
import se.vgregion.verticalprio.repository.finding.HaveNestedEntities;

/**
 * @author Claes Lundahl, vgrid=clalu4
 * 
 */
@Controller
public class ChooseCodesController extends ControllerBase {

	Map<String, ChooseListFormWithDomainProperty> formPrototypes = new HashMap<String, ChooseListFormWithDomainProperty>();

	/**
	 * Constructor for the class. Initialize the formPrototypes member with objects. These objects,
	 * {@link ChooseListFormWithDomainProperty}, are later used to provide text to the gui and to point to the
	 * collections that should receive the result of the dialog.
	 */
	public ChooseCodesController() {
		ChooseListFormWithDomainProperty symptomDiagnosTextForm = new ChooseListFormWithDomainProperty();
		symptomDiagnosTextForm.setDisplayKey("kodPlusBeskrivning");
		symptomDiagnosTextForm.setIdKey("id");
		symptomDiagnosTextForm.setFilterLabel("Sök diagnos med nyckelord");
		symptomDiagnosTextForm.setFilterLabelToolTip("Här kan du söka både på kod och på beskrivning");
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

		ChooseListFormWithDomainProperty vaardnivaa = symptomDiagnosTextForm.clone();
		vaardnivaa.setFilterLabel("Sök vårdnivå med nyckelord");
		vaardnivaa.setNotYetChoosenLabel("Vårdnivåer");
		vaardnivaa.setChoosenLabel("Valda nivåer");
		vaardnivaa.setOkLabel("Välj nivåer");
		vaardnivaa.setOkUrl("main");
		vaardnivaa.setCancelUrl("main");
		vaardnivaa.setAllItemsPropertyName("vaardnivaaKod");
		formPrototypes.put("vaardnivaaKod", vaardnivaa);

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
		tillstaandetsSvaarighetsgrad.setIdKey("id");
		tillstaandetsSvaarighetsgrad.setCancelUrl("main");
		tillstaandetsSvaarighetsgrad.setDisplayKey("kortBeskrivning");
		tillstaandetsSvaarighetsgrad.setAllItemsPropertyName("tillstaandetsSvaarighetsgradKod");
		formPrototypes.put("tillstaandetsSvaarighetsgradKod", tillstaandetsSvaarighetsgrad);

	}

	/**
	 * Looks through the result list in the session for all existing codes of a certain type (residing in a
	 * collection or in a reference variable).
	 * 
	 * This would be the total set of items to choose for the user. Since the search always should narrow down in
	 * findings.
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
	}

	/**
	 * Initialization of the view. It looks up the 'configuration' to use - texts and position of the target value
	 * reference.
	 * 
	 * Then collects all possible selections from the result table already in the session.
	 * 
	 * @param session
	 * @param response
	 * @param fieldName
	 * @return
	 * @throws IOException
	 */
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

		target.clear();

		response.sendRedirect("main");
		return null;
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
		Object uncastHne = bm.get(propertyName);
		if (uncastHne != null && !(uncastHne instanceof HaveNestedEntities)) {
			System.out.println("Den här har ju inte rätt klass\n " + uncastHne.getClass());
		}
		HaveNestedEntities hne = (HaveNestedEntities) uncastHne;
		return hne.content();
	}

	/**
	 * A class that extends the {@link ChooseListForm} adding specifics concerning selecting codes (objects
	 * extending the {@link AbstractKod} class) that is referenced by {@link Prioriteringsobjekt}.
	 * 
	 * @author Claes Lundahl, vgrid=clalu4.VGREGION
	 * 
	 */
	public static class ChooseListFormWithDomainProperty extends ChooseListForm {

		/**
		 * This property points to a property (getter/setter) on the {@link Prioriteringsobjekt} class. From this
		 * field all codes are acquired that the user is supposed to choose from.
		 */
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
