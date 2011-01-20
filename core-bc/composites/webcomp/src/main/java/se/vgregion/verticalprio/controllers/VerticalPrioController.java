package se.vgregion.verticalprio.controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
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
import se.vgregion.verticalprio.entity.AbstractKod;
import se.vgregion.verticalprio.entity.Column;
import se.vgregion.verticalprio.entity.Prioriteringsobjekt;
import se.vgregion.verticalprio.entity.SektorRaad;
import se.vgregion.verticalprio.entity.User;
import se.vgregion.verticalprio.repository.GenerisktHierarkisktKodRepository;
import se.vgregion.verticalprio.repository.GenerisktKodRepository;
import se.vgregion.verticalprio.repository.NestedSektorRaad;

@Controller
@SessionAttributes(value = { "confCols", "form" })
public class VerticalPrioController extends ControllerBase {

    @Resource(name = "sektorRaadRepository")
    GenerisktHierarkisktKodRepository<SektorRaad> sektorRaadRepository;

    @Resource(name = "userRepository")
    GenerisktKodRepository<User> userRepository;

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
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public String main(HttpSession session) {
        session.setAttribute("editDir", new EditDirective(true, null));
        MainForm form = getMainForm(session);
        result(session);
        return "main";
    }

    @RequestMapping(value = "/login")
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public String login(HttpSession session, @RequestParam(required = false) String userName,
            @RequestParam(required = false) String password) {

        User bean = new User();
        bean.setVgrId(userName);
        bean.setPassword(userName);

        List<User> users = userRepository.findByExample(bean, 1);
        if (users.isEmpty()) {
            session.setAttribute("user", "login-failed");
        } else {
            session.setAttribute("user", users.get(0));

            // User user = getOrCreateSessionObj(session, "user", User.class);
            // user.setFirstName("John");
            // user.setLastName("Doe");
            // user.setEditor(!user.isEditor());
        }
        return main(session);
    }

    @RequestMapping(value = "/main", params = { "sortField" })
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public String alterSortOrder(HttpSession session, @RequestParam String sortField) {
        MainForm form = getMainForm(session);
        PrioriteringsobjektFindCondition condition = getOrCreateSessionObj(session, "prioCondition",
                PrioriteringsobjektFindCondition.class);

        for (Column column : form.getColumns()) {
            if (sortField.equals(column.getName())) {
                column.setSorting(!column.isSorting());
                if (!column.isSorting()) {
                    condition.getSortOrder().remove(sortField);
                    continue;
                }
                continue;
            }
        }

        condition.getSortOrder().clear();
        for (Column column : form.getColumns()) {
            if (column.isSorting()) {
                condition.getSortOrder().add(column.getName());
            }
        }

        result(session);
        return "main";
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

    // @RequestMapping(value = "/modify-prio")
    // @Transactional(propagation = Propagation.REQUIRED)
    // public String selectPrio(@RequestParam Prioriteringsobjekt command) {
    // System.out.println(command.getVaentetidVeckor());
    // return "select-prio";
    // }

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

        SektorRaad sector = getSectorById(id, form.getSectors());
        sector.setSelected(!sector.isSelected());
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

    @Transactional
    private List<SektorRaad> getSectors() {
        Collection<SektorRaad> result = sektorRaadRepository.getTreeRoots();
        return new ArrayList<SektorRaad>(result);
    }

    // @ModelAttribute("rows")
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<Prioriteringsobjekt> result(HttpSession session) {
        PrioriteringsobjektFindCondition condition = getOrCreateSessionObj(session, "prioCondition",
                PrioriteringsobjektFindCondition.class);

        MainForm mf = getMainForm(session);
        List<SektorRaad> raad = getMarkedLeafs(mf.getSectors());
        raad = flatten(raad);
        NestedSektorRaad sektorNest = new NestedSektorRaad(raad);
        condition.setSektorRaad(sektorNest);

        List<Prioriteringsobjekt> prios = new ArrayList<Prioriteringsobjekt>(prioRepository.findByExample(
                condition, null));

        for (Prioriteringsobjekt prio : prios) {
            BeanMap bm = new BeanMap(prio);
            Map<String, Object> values = new HashMap<String, Object>(bm);
            // Completely insane... but has to be done because otherwise
            // a lack of transaction will occur when rendering the referred child objects.
            // TODO: don't use lazy loading on collection or objects inside the Prioriteringsobjekt class.
            for (String key : values.keySet()) {
                Object value = values.get(key);
                if (value instanceof Collection) {
                    Collection<?> collection = (Collection<?>) value;
                    if (!collection.isEmpty()) {
                        Object item = collection.iterator().next();
                        if (item instanceof AbstractKod) {
                            Comparator<AbstractKod> comp = new Comparator<AbstractKod>() {
                                @Override
                                public int compare(AbstractKod o1, AbstractKod o2) {
                                    return o1.getKod().compareTo(o2.getKod());
                                }
                            };
                            List<AbstractKod> kods = (List<AbstractKod>) collection;
                            Collections.sort(kods, comp);
                        } else {
                            new ArrayList<Object>(collection);
                        }
                    }

                }
            }
        }

        session.setAttribute("rows", prios);

        return prios;
    }

}
