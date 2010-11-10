package se.vgregion.verticalprio.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import se.vgregion.verticalprio.ConfColumnsForm;
import se.vgregion.verticalprio.MainForm;
import se.vgregion.verticalprio.model.Column;
import se.vgregion.verticalprio.model.Sector;

@Controller
// @SessionAttributes(value = { "form" })
public class VerticalPrioController {
	private static final Log log = LogFactory.getLog(VerticalPrioController.class);

	@RequestMapping(value = "/test", method = RequestMethod.GET)
	public String test() {
		log.info("in test() method");
		return "test";
	}

	@RequestMapping(value = "/main")
	public String main(HttpSession session/* Model model, @ModelAttribute(value = "form") MainForm form */) {
		MainForm form = getOrCreateSessionObj(session, "form", MainForm.class);

		if (form.getSectors().isEmpty()) {
			form.getSectors().addAll(getSectors());
		}

		if (form.getColumns().isEmpty()) {
			form.getColumns().addAll(getColumns());
		}
		return "main";
	}

	private <T> T getOrCreateSessionObj(HttpSession session, String name, Class<T> clazz) {
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

	@RequestMapping(value = "/init-conf-columns")
	public String initConfColumns(final HttpSession session) {
		ConfColumnsForm columnForm = getOrCreateSessionObj(session, "confCols", ConfColumnsForm.class);
		columnForm.getHiddenColumns().clear();
		columnForm.getVisibleColumns().clear();

		MainForm mainForm = getOrCreateSessionObj(session, "form", MainForm.class);

		for (Column column : mainForm.getColumns()) {
			if (column.isVisible()) {
				columnForm.getVisibleColumns().add(column);
			} else {
				columnForm.getHiddenColumns().add(column);
			}
		}

		return "conf-columns";
	}

	// @RequestMapping(value = "/conf-columns")
	public String confColumns(final HttpSession session) {
		System.out.println("In confColumns");
		return "main";
	}

	// @RequestMapping(value = "/check")
	@RequestMapping(value = "/conf-columns")
	public String check(final HttpSession session, @RequestParam int[] id) {
		MainForm form = (MainForm) session.getAttribute("form");
		for (int i = 0; i < id.length; i++) {
			int selected = id[i];
			Sector sector = form.getSectors().get(selected);
			sector.setSelected(!sector.isSelected());
		}
		return "main";
	}

	private List<Sector> getSectors() {
		List<Sector> result = new ArrayList<Sector>();
		for (int i = 0; i < 25; i++) {
			result.add(new Sector("Sector #" + i, i));
		}
		return result;
	}

	private List<Column> getColumns() {
		List<Column> result = new ArrayList<Column>();
		for (int i = 0; i < 15; i++) {
			result.add(new Column(i, "Column #" + i));
		}
		return result;
	}

}
