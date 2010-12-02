package se.vgregion.verticalprio.controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import se.vgregion.verticalprio.ConfColumnsForm;
import se.vgregion.verticalprio.MainForm;
import se.vgregion.verticalprio.entity.Column;
import se.vgregion.verticalprio.entity.Prioriteringsobjekt;
import se.vgregion.verticalprio.entity.SektorRaad;
import se.vgregion.verticalprio.repository.GenerisktHierarkisktKodRepository;
import se.vgregion.verticalprio.repository.PrioRepository;

@Controller
@SessionAttributes("form")
public class VerticalPrioController extends ControllerBase {

    @Resource(name = "sektorRaadRepository")
    private GenerisktHierarkisktKodRepository<SektorRaad> sektorRaadRepository;

    @Autowired
    private PrioRepository prioRepository;

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
    public List<Prioriteringsobjekt> result(HttpSession session) {
        List<Prioriteringsobjekt> prios = new ArrayList<Prioriteringsobjekt>();
        MainForm form = getMainForm(session);
        // Do something qualified with the information in the form to get the data desired.
        List<Column> columns = form.getColumns();
        for (long i = 0; i < 100; i++) {
            Prioriteringsobjekt prio = new Prioriteringsobjekt();
            prio.setId(i);
            prios.add(prio);
            /*
             * BeanMap bm = new BeanMap(prio); for (Column column : columns) { bm.put(column.getName(), "" + i); }
             */
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
            moveColsFromOneListToOtherByNames(hiddenColumns, columnForm.getHiddenColumns(),
                    columnForm.getVisibleColumns());
        } else if ("hide".equals(command)) {
            moveColsFromOneListToOtherByNames(visibleColumns, columnForm.getVisibleColumns(),
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

    private void moveColsFromOneListToOtherByNames(Collection<String> names, List<Column> from, List<Column> to) {
        if (names == null) {
            return;
        }
        for (Column col : new ArrayList<Column>(from)) {
            if (names.contains(col.getName() + "")) {
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

    @RequestMapping(value = "/select-prio")
    @Transactional(propagation = Propagation.REQUIRED)
    public String selectPrio(final HttpSession session, @RequestParam Integer selected) {
        System.out.println("VerticalPrioController.selectPrio()");
        List<SektorRaad> sectors = sektorRaadRepository.getTreeRoots();
        for (SektorRaad sector : sectors.get(0).getChildren()) {

            for (int i = 0; i < 10; i++) {
                Prioriteringsobjekt prio = new Prioriteringsobjekt();
                prio.setSektorRaad(sector);
                BeanMap bm = new BeanMap(prio);
                for (Object key : bm.keySet()) {
                    if (bm.getType(key.toString()).equals(String.class)) {
                        bm.put(key.toString(), sector.getKod() + " " + i);
                    }
                }
                prioRepository.store(prio);
            }
        }

        return "select-prio";
    }

    @RequestMapping(value = "/check")
    public String check(final HttpSession session, @RequestParam Integer id) {
        MainForm form = getMainForm(session);

        SektorRaad sector = getSectorById(id, form.getSectors());
        sector.setSelected(!sector.isSelected());
        return "main";
    }

    private SektorRaad getSectorById(int id, List<SektorRaad> sectors) {
        for (SektorRaad sector : sectors) {
            if (id == sector.getId()) {
                return sector;
            }
            SektorRaad subSector = getSectorById(id, sector.getChildren());
            if (subSector != null) {
                return subSector;
            }
        }
        return null;
    }

    private long dummySectorCounter = 0;

    @Transactional
    private List<SektorRaad> getSectors() {

        // Collection<SektorRaad> result = sektorRaadRepository.getTreeRoots();
        // return new ArrayList<SektorRaad>(result);

        List<SektorRaad> result = new ArrayList<SektorRaad>();
        for (long i = 0; i < 25; i++) {
            SektorRaad sector = new SektorRaad();
            // new SektorRaad("Sector #" + dummySectorCounter, dummySectorCounter++);
            sector.setKod("Code " + dummySectorCounter);
            sector.setId(dummySectorCounter++);
            result.add(sector);
            sector.setChildren(new ArrayList<SektorRaad>());
            sector.getChildren().addAll(mkSubSectors(3));
        }
        return result;
    }

    private List<SektorRaad> mkSubSectors(int deep) {

        List<SektorRaad> result = new ArrayList<SektorRaad>();
        if (deep < 1) {
            return result;
        }

        for (int i = 0; i < 3; i++) {
            SektorRaad sector = new SektorRaad();
            // new SektorRaad("Sub-Sector #" + dummySectorCounter, dummySectorCounter++);
            sector.setKod("subsr " + dummySectorCounter);
            sector.setId(dummySectorCounter++);
            result.add(sector);
            sector.setChildren(new ArrayList<SektorRaad>());
            sector.getChildren().addAll(mkSubSectors(deep - 1));
        }
        return result;
    }
}
