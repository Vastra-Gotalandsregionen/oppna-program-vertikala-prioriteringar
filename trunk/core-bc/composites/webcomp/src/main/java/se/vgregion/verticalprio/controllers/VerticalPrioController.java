package se.vgregion.verticalprio.controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import se.vgregion.verticalprio.ConfColumnsForm;
import se.vgregion.verticalprio.MainForm;
import se.vgregion.verticalprio.model.Column;
import se.vgregion.verticalprio.model.Prio;
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

    private MainForm getMainForm(HttpSession session) {
        MainForm form = getOrCreateSessionObj(session, "form", MainForm.class);
        initMainForm(form);
        return form;
    }

    @RequestMapping(value = "/main")
    public String main(HttpSession session) {
        MainForm form = getMainForm(session);
        System.out.println("in main method");
        return "main";
    }

    @ModelAttribute("rows")
    public List<Prio> result(HttpSession session) {
        List<Prio> prios = new ArrayList<Prio>();
        MainForm form = getMainForm(session);
        // Do something qualified with the information in the form to get the data desired.
        List<Column> columns = form.getColumns();
        for (int i = 0; i < 100; i++) {
            Prio prio = new Prio();
            prios.add(prio);
            BeanMap bm = new BeanMap(prio);
            for (Column column : columns) {
                bm.put(column.getName(), "" + i);
            }
        }
        return prios;
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
        } else if ("cancel".equals(command)) {
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

        MainForm mainForm = getMainForm(session);

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
        MainForm form = getMainForm(session);

        Sector sector = getSectorById(id, form.getSectors());
        sector.setSelected(!sector.isSelected());
        return "main";
    }

    private Sector getSectorById(int id, List<Sector> sectors) {
        for (Sector sector : sectors) {
            if (id == sector.getId()) {
                return sector;
            }
            Sector subSector = getSectorById(id, sector.getChildren());
            if (subSector != null) {
                return subSector;
            }
        }
        return null;
    }

    private int dummySectorCounter = 0;

    private List<Sector> getSectors() {
        List<Sector> result = new ArrayList<Sector>();
        for (int i = 0; i < 25; i++) {
            Sector sector = new Sector("Sector #" + dummySectorCounter, dummySectorCounter++);
            result.add(sector);
            sector.getChildren().addAll(mkSubSectors(3));
        }
        return result;
    }

    private List<Sector> mkSubSectors(int deep) {

        List<Sector> result = new ArrayList<Sector>();
        if (deep < 1) {
            return result;
        }

        for (int i = 0; i < 3; i++) {
            Sector sector = new Sector("Sub-Sector #" + dummySectorCounter, dummySectorCounter++);
            result.add(sector);
            sector.getChildren().addAll(mkSubSectors(deep - 1));
        }
        return result;
    }

    private List<Column> getColumns() {
        List<Column> result = new ArrayList<Column>();
        // for (int i = 0; i < 15; i++) {
        // result.add(new Column(i, "Column #" + i));
        // }

        try {
            Properties namesTexts = new Properties();
            namesTexts.load(getClass().getResourceAsStream("/column-texts.properties"));

            for (Object key : namesTexts.keySet()) {
                Column column = new Column();
                column.setLabel(namesTexts.getProperty(key.toString()));
                column.setName(key.toString());
                column.setId(key.hashCode());
                result.add(column);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return result;
    }

}
