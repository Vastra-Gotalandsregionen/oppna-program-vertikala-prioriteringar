package se.vgregion.verticalprio.controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanMap;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import se.vgregion.verticalprio.ConfColumnsForm;
import se.vgregion.verticalprio.MainForm;
import se.vgregion.verticalprio.PrioriteringsobjektFindCondition;
import se.vgregion.verticalprio.entity.Column;
import se.vgregion.verticalprio.entity.Prioriteringsobjekt;
import se.vgregion.verticalprio.entity.SektorRaad;
import se.vgregion.verticalprio.entity.User;
import se.vgregion.verticalprio.repository.GenerisktHierarkisktKodRepository;
import se.vgregion.verticalprio.repository.GenerisktKodRepository;
import se.vgregion.verticalprio.repository.finding.HaveNestedEntities;
import se.vgregion.verticalprio.repository.finding.HaveQuerySortOrder;
import se.vgregion.verticalprio.repository.finding.HaveQuerySortOrder.SortOrderField;
import se.vgregion.verticalprio.repository.finding.NestedRangordningsKod;
import se.vgregion.verticalprio.repository.finding.NestedSektorRaad;
import se.vgregion.verticalprio.repository.finding.SortingDiagnosKod;
import se.vgregion.verticalprio.repository.finding.SortingRangordningsKod;
import se.vgregion.verticalprio.repository.finding.SortingSektorRaad;

@Controller
@SessionAttributes(value = { "confCols", "form" })
public class VerticalPrioController extends ControllerBase {

    @Resource(name = "sektorRaadRepository")
    GenerisktHierarkisktKodRepository<SektorRaad> sektorRaadRepository;

    @Resource(name = "userRepository")
    GenerisktKodRepository<User> userRepository;

    private void initMainForm(MainForm form, HttpSession session) {
        if (form.getSectors().isEmpty()) {
            form.getSectors().addAll(getSectors(session));
        }

        if (form.getColumns().isEmpty()) {
            form.getColumns().addAll(getColumns());
        }
    }

    private MainForm getMainForm(HttpSession session) {
        MainForm form = getOrCreateSessionObj(session, "form", MainForm.class);
        initMainForm(form, session);
        return form;
    }

    @RequestMapping(value = "/main")
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public String main(HttpSession session) {
        session.setAttribute("editDir", new EditDirective(true, null));
        result(session);
        return "main";
    }

    @RequestMapping(value = "/logout")
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public String logout(HttpSession session) {
        session.setAttribute("user", null);
        session.setAttribute("loginResult", null);
        return main(session);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @RequestMapping(value = "/login")
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public String login(HttpSession session, @RequestParam(required = false) String userName,
            @RequestParam(required = false) String password) {

        User example = new User();
        example.setVgrId(userName);
        example.setPassword(password);

        List<User> users = userRepository.findByExample(example, 1);
        if (users.isEmpty() || "".equals(password)) {
            session.setAttribute("user", null);
            session.setAttribute("loginResult", false);
        } else {
            User user = users.get(0);

            Map userValues = new HashMap(new BeanMap(user)); // Insane... makes all lazy properties initialized.
            for (Object o : userValues.values()) {
                if (o instanceof Collection) {
                    Collection c = (Collection) o;
                    for (Object i : c) {
                        new HashMap(new BeanMap(i));
                    }
                }
            }

            session.setAttribute("user", user);
            session.setAttribute("loginResult", true);
        }
        return main(session);
    }

    @RequestMapping(value = "/main", params = { "sortField" })
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public String alterSortOrder(HttpSession session, @RequestParam String sortField) {
        MainForm form = getMainForm(session);
        PrioriteringsobjektFindCondition condition = getOrCreateSessionObj(session, "prioCondition",
                PrioriteringsobjektFindCondition.class);

        condition.clearSorting();

        if ("rangordningsKod".equals(sortField)) {
            // Sektorsråd // Diagnoskod
            NestedRangordningsKod nrk = (NestedRangordningsKod) condition.getRangordningsKod();
            if (nrk == null) {
                condition.setRangordningsKod(nrk = new NestedRangordningsKod());
            }
            SortingRangordningsKod srk = new SortingRangordningsKod();
            nrk.content().add(srk);
            srk.listSortOrders().add(mkSortOrderField("kod"));

            NestedSektorRaad nsr = (NestedSektorRaad) condition.getSektorRaad();
            if (nsr == null) {
                condition.setSektorRaad(nsr = new NestedSektorRaad());
            }
            SortingSektorRaad ssr = new SortingSektorRaad();
            ssr.listSortOrders().add(mkSortOrderField("kod"));
            nsr.content().add(ssr);

            SortingDiagnosKod sdk = new SortingDiagnosKod();
            sdk.listSortOrders().add(mkSortOrderField("kod"));
            condition.getDiagnoser().add(sdk);
        }

        result(session);
        return "main";
    }

    int sortOrderCount = 0;

    private SortOrderField mkSortOrderField(String name) {
        SortOrderField sof = new SortOrderField();
        sof.setName("kod");
        sof.setOrder(sortOrderCount++);
        return sof;
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
            Collections.sort(getMainForm(session).getColumns(), new Column.OrderComparer());
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
            if (column.isHideAble()) {
                if (column.isVisible()) {
                    columnForm.getVisibleColumns().add(column);
                } else {
                    columnForm.getHiddenColumns().add(column);
                }
            }
        }

        return "conf-columns";
    }

    @RequestMapping(value = "/approve")
    @Transactional(propagation = Propagation.REQUIRED)
    public String approve(final HttpSession session, @RequestParam Long id) {

        return main(session);
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
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public String check(final HttpSession session, @RequestParam Integer id) {
        MainForm form = getMainForm(session);

        if (id.longValue() == -1l) {
            boolean b = form.getAllSektorsRaad().isSelected();
            form.getAllSektorsRaad().setSelected(!b);
            if (b) {
                for (SektorRaad sr : form.getSectors()) {
                    sr.setSelectedDeeply(false);
                }
            }
        } else {
            form.getAllSektorsRaad().setSelected(false);
            SektorRaad sector = getSectorById(id, form.getSectors());
            sector.setSelected(!sector.isSelected());
        }

        result(session);
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

    List<SektorRaad> getMarkedLeafs(List<SektorRaad> raads) {
        List<SektorRaad> result = new ArrayList<SektorRaad>();
        if (raads == null) {
            return result;
        }
        for (SektorRaad raad : raads) {
            List<SektorRaad> markedChildren = getMarkedLeafs(raad.getChildren());
            if (raad.isSelected() && markedChildren.size() == 0) {
                result.add(raad);
            } else {
                result.addAll(markedChildren);
            }
        }
        return result;
    }

    private List<SektorRaad> flatten(List<SektorRaad> raads) {
        List<SektorRaad> result = new ArrayList<SektorRaad>();
        flatten(raads, result);
        result = toBlankWithIdOnly(result);
        return result;
    }

    private void flatten(List<SektorRaad> raads, List<SektorRaad> result) {
        if (raads != null) {
            for (SektorRaad sr : raads) {
                result.add(sr);
                flatten(sr.getChildren(), result);
            }
        }
    }

    private List<SektorRaad> toBlankWithIdOnly(List<SektorRaad> raads) {
        List<SektorRaad> result = new ArrayList<SektorRaad>();
        for (SektorRaad sr : raads) {
            SektorRaad newRaad = new SektorRaad(sr.getId());
            result.add(newRaad);
        }
        return result;
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

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<Prioriteringsobjekt> result(HttpSession session) {
        PrioriteringsobjektFindCondition condition = getOrCreateSessionObj(session, "prioCondition",
                PrioriteringsobjektFindCondition.class);
        MainForm mf = getMainForm(session);

        if (mf.getAllSektorsRaad().isSelected()) {
            if (condition.getSektorRaad() != null) {
                // user selected to show all Prios regardless of SR.
                // Remove all conditions that specifies specific SRs, except those that should indicate order by
                // directive.
                HaveNestedEntities<SektorRaad> hne = (HaveNestedEntities<SektorRaad>) condition.getSektorRaad();
                for (SektorRaad sr : new ArrayList<SektorRaad>(hne.content())) {
                    if (!(sr instanceof HaveQuerySortOrder)) {
                        hne.content().remove(sr);
                    }
                }
            }
            // mf.getSectors().clear();
            // mf.getSectors().addAll(sektorRaadRepository.getTreeRoots());
        } else {
            List<SektorRaad> raad = getMarkedLeafs(mf.getSectors());
            raad = flatten(raad);
            NestedSektorRaad sektorNest = new NestedSektorRaad(raad);
            condition.setSektorRaad(sektorNest);
            if (sektorNest.content().isEmpty()) {
                List<Prioriteringsobjekt> zeroResult = new ArrayList<Prioriteringsobjekt>();
                session.setAttribute("rows", zeroResult);
                return zeroResult;
            }
        }

        List<Prioriteringsobjekt> prios = new ArrayList<Prioriteringsobjekt>(prioRepository.findByExample(
                condition, null));

        session.setAttribute("rows", prios);

        return prios;
    }
}
