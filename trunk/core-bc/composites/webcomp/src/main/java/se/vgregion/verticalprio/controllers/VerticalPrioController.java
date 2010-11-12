package se.vgregion.verticalprio.controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import se.vgregion.verticalprio.ConfColumnsForm;
import se.vgregion.verticalprio.MainForm;
import se.vgregion.verticalprio.model.Column;
import se.vgregion.verticalprio.model.Sector;

@Controller
@SessionAttributes("form")
public class VerticalPrioController {
    private static final Log log = LogFactory.getLog(VerticalPrioController.class);

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String test() {
        System.out.println("in test() method");
        return "test";
    }

    private void initMainForm(MainForm form) {
        if (form.getSectors().isEmpty()) {
            form.getSectors().addAll(getSectors());
        }

        if (form.getColumns().isEmpty()) {
            form.getColumns().addAll(getColumns());
        }
    }

    @RequestMapping(value = "/main")
    public String main(HttpSession session) {
        MainForm form = getOrCreateSessionObj(session, "form", MainForm.class);
        initMainForm(form);
        System.out.println("in main method");
        return "main";
    }

    /*
     * @ModelAttribute("types") public String get1() { System.out.println("in get1 method"); String types = "hej";
     * return types; }
     */

    /*
     * @RequestMapping(value = "/main", method = RequestMethod.GET) public void get2() {
     * System.out.println("in get2 method"); }
     */
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

    @RequestMapping(value = "/conf-columns")
    public String confColumns(final HttpSession session, @RequestParam String command,
            @RequestParam(required = false) List<String> visibleColumns,
            @RequestParam(required = false) List<String> hiddenColumns) {
        ConfColumnsForm columnForm = getOrCreateSessionObj(session, "confCols", ConfColumnsForm.class);
        if ("show".equals(command)) {
            colsFromOneListToOtherByIds(hiddenColumns, columnForm.getHiddenColumns(),
                    columnForm.getVisibleColumns());
        } else if ("hide".equals(command)) {
            colsFromOneListToOtherByIds(visibleColumns, columnForm.getVisibleColumns(),
                    columnForm.getHiddenColumns());
        } else if ("save".equals(command)) {
            for (Column column : columnForm.getHiddenColumns()) {
                column.setVisible(false);
            }
            for (Column column : columnForm.getVisibleColumns()) {
                column.setVisible(true);
            }
            return "main";
        }

        return "conf-columns";
    }

    private void colsFromOneListToOtherByIds(Collection<String> ids, List<Column> from, List<Column> to) {
        if (ids == null) {
            return;
        }
        for (Column col : new ArrayList<Column>(from)) {
            if (ids.contains(col.getId() + "")) {
                from.remove(col);
                to.add(col);
            }
        }
    }

    @RequestMapping(value = "/init-conf-columns")
    public String initConfColumns(final HttpSession session) {
        ConfColumnsForm columnForm = getOrCreateSessionObj(session, "confCols", ConfColumnsForm.class);
        columnForm.getHiddenColumns().clear();
        columnForm.getVisibleColumns().clear();

        MainForm mainForm = getOrCreateSessionObj(session, "form", MainForm.class);
        initMainForm(mainForm);

        for (Column column : mainForm.getColumns()) {
            if (column.isVisible()) {
                columnForm.getVisibleColumns().add(column);
            } else {
                columnForm.getHiddenColumns().add(column);
            }
        }

        return "conf-columns";
    }

    @RequestMapping(value = "/check")
    public String check(final HttpSession session, @RequestParam Integer id) {
        // MainForm form = (MainForm) session.getAttribute("form");
        MainForm form = getOrCreateSessionObj(session, "form", MainForm.class);

        Sector sector = form.getSectors().get(id);
        sector.setSelected(!sector.isSelected());
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
