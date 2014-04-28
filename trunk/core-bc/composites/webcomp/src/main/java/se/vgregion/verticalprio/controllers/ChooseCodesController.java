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
public class ChooseCodesController extends WebControllerBase {

    public ChooseCodesController() {

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
		clf.setChosen(new ArrayList(target));

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
