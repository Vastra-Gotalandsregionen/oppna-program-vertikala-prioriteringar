package se.vgregion.verticalprio.controllers;

import org.springframework.transaction.annotation.Transactional;
import se.vgregion.verticalprio.MainForm;
import se.vgregion.verticalprio.entity.SektorRaad;
import se.vgregion.verticalprio.repository.GenerisktHierarkisktKodRepository;
import se.vgregion.verticalprio.repository.PrioRepository;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class WebControllerBase extends BaseController {

	@Resource(name = "prioRepository")
	protected PrioRepository prioRepository;

	// private SortedMap<String, String> prioPropertyTexts;

    // protected String columnTextsPropertiesFileName = "/column-texts.properties";

	@Resource(name = "sektorRaadRepository")
	GenerisktHierarkisktKodRepository<SektorRaad> sektorRaadRepository;

    // private void addHtmlLinkToColumnLabel(Map<String, Column> map, String key, String linkText) {
	// Column column = map.get(key);
	// String template = "<a href='${link}'>${text}</a>";
	// template = template.replace("${link}", linkText);
	// template = template.replace("${text}", column.getLabel());
	// column.setLabel(template);
	// }

	// /**
	// * Getter for the prioPropertyTexts property. It initializes the map with the value in the property file
	// * pointed to by the columnTextsPropertiesFileName attribute (/column-texts.properties).
	// *
	// * @return Names of the {@link Prioriteringsobjekt} attributes mapped to their labels.
	// */
	// protected SortedMap<String, String> getPrioPropertyTexts() {
	// if (prioPropertyTexts == null) {
	// try {
	// Properties namesTexts = new Properties();
	// namesTexts.load(getClass().getResourceAsStream(columnTextsPropertiesFileName));
	// SortedMap<String, String> map = new TreeMap<String, String>();
	//
	// for (Object key : namesTexts.keySet()) {
	// String sk = (String) key;
	// map.put(sk, namesTexts.getProperty(sk));
	// }
	//
	// prioPropertyTexts = map;
	// } catch (Exception e) {
	// throw new RuntimeException(e);
	// }
	// }
	// return prioPropertyTexts;
	// }

    protected <T> T getOrCreateSessionObj(HttpSession session, String name, Class<T> clazz) {
		try {
			T result = (T) session.getAttribute(name);

			if (result == null) {
				result = clazz.newInstance();
				session.setAttribute(name, result);
			}

			return result;
		} catch (InstantiationException ie) {
			throw new RuntimeException(ie);
		} catch (IllegalAccessException iae) {
			throw new RuntimeException(iae);
		}
	}

	@SuppressWarnings("unchecked")
	@Transactional
	private List<SektorRaad> getSectors(HttpSession session) {
		final String sectorsKey = "sectors";
		Collection<SektorRaad> sectorCache = (Collection<SektorRaad>) session.getAttribute(sectorsKey);
		if (sectorCache == null) {
			List<SektorRaad> raads = sektorRaadRepository.getTreeRoots();
			sectorCache = new ArrayList<SektorRaad>();
			session.setAttribute(sectorsKey, sectorCache);
			for (SektorRaad sr : raads) {
				sectorCache.add(sr.clone());
			}
		}
		return new ArrayList<SektorRaad>(sectorCache);
	}

	protected MainForm getMainForm(HttpSession session) {
		MainForm form = getOrCreateSessionObj(session, "form", MainForm.class);
		initMainForm(form, session);
		return form;
	}

	protected void initMainForm(MainForm form, HttpSession session) {
		if (form.getSectors().isEmpty()) {
			form.getSectors().addAll(getSectors(session));
		}

		if (form.getColumns().isEmpty()) {
			form.getColumns().addAll(getColumns());
		}
	}

}
