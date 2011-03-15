package se.vgregion.verticalprio.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanMap;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import se.vgregion.verticalprio.MainForm;
import se.vgregion.verticalprio.PrioriteringsobjektFindCondition;
import se.vgregion.verticalprio.controllers.ChooseFromListController.ChooseListForm;
import se.vgregion.verticalprio.entity.Column;
import se.vgregion.verticalprio.entity.Prioriteringsobjekt;
import se.vgregion.verticalprio.entity.SektorRaad;
import se.vgregion.verticalprio.entity.User;
import se.vgregion.verticalprio.repository.GenerisktKodRepository;
import se.vgregion.verticalprio.repository.finding.DateNullLogic;
import se.vgregion.verticalprio.repository.finding.HaveNestedEntities;
import se.vgregion.verticalprio.repository.finding.HaveQuerySortOrder;
import se.vgregion.verticalprio.repository.finding.JpqlMatchBuilder;
import se.vgregion.verticalprio.repository.finding.NestedSektorRaad;

@Controller
@SessionAttributes(value = { "confCols", "form" })
public class VerticalPrioController extends EditPrioriteringController {

    @Resource(name = "userRepository")
    GenerisktKodRepository<User> userRepository;

    @RequestMapping(value = "/main")
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public String main(HttpSession session) {
        session.setAttribute("editDir", new EditDirective(true, null));
        result(session);
        return "main";
    }

    @RequestMapping(value = "/main", params = { "logout" })
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public String logout(HttpSession session, HttpServletResponse response) throws IOException {
        session.setAttribute("user", null);
        session.setAttribute("loginResult", null);
        PrioriteringsobjektFindCondition condition = getOrCreateSessionObj(session, "prioCondition",
                PrioriteringsobjektFindCondition.class);
        condition.setGodkaend(new DateNullLogic(true));
        response.sendRedirect("main");
        return null;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @RequestMapping(value = "/main", params = { "login" })
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
            PrioriteringsobjektFindCondition condition = getOrCreateSessionObj(session, "prioCondition",
                    PrioriteringsobjektFindCondition.class);
            if (user.isEditor() || user.isApprover()) {
                condition.setGodkaend(new DateNullLogic(false));
            } else {
                condition.setGodkaend(new DateNullLogic(true));
            }

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

        markColumnAsSorting(sortField, form);

        if ("rangordningsKod".equals(sortField)) {
            condition.sortByRangordningsKod();
        } else if ("tillstaandetsSvaarighetsgradKod".equals(sortField)) {
            condition.sortByTillstaandetsSvaarighetsgradKod();
        } else if ("diagnosTexts".equals(sortField)) {
            condition.sortByDiagnoser();
        }

        result(session);
        return "main";
    }

    private void markColumnAsSorting(String fieldName, MainForm mf) {
        for (Column column : mf.getColumns()) {
            column.setSorting(fieldName.equals(column.getName()));
        }
    }

    @RequestMapping(value = "/commit-conf-columns")
    public String commColumnsCommit(final HttpSession session, HttpServletResponse response) throws IOException {
        MainForm form = getMainForm(session);
        SortedSet<Column> target = (SortedSet<Column>) session.getAttribute("selectedColumns");

        for (Column column : form.getColumns()) {
            column.setVisible(target.contains(column) || !column.isHideAble());
        }

        response.sendRedirect("main");
        return null;
    }

    @RequestMapping(value = "/main", params = { "init-conf-columns" })
    public String confColumnsStart(final HttpSession session, HttpServletResponse response) throws IOException {
        MainForm form = getMainForm(session);

        ChooseListForm clf = getOrCreateSessionObj(session, ChooseListForm.class.getSimpleName(),
                ChooseListForm.class);

        clf.setNotYetChoosenLabel("Gömda kolumner");
        clf.setChoosenLabel("Synliga kolumner");
        clf.setOkLabel("Välj kolumner");

        clf.setDisplayKey("label");
        clf.setIdKey("id");
        clf.setFilterLabel(null);
        clf.setOkUrl("commit-conf-columns");
        clf.setCancelUrl("main");

        List<Column> allColumns = new ArrayList<Column>();
        List<Column> selected = new ArrayList<Column>();
        List<Column> notYetSelected = new ArrayList<Column>();

        SortedSet<Column> target = new TreeSet<Column>();
        session.setAttribute("selectedColumns", target);
        clf.setTarget(target);

        for (Column column : form.getColumns()) {
            if (column.isHideAble()) {
                allColumns.add(column);
                if (column.isVisible()) {
                    selected.add(column);
                } else {
                    notYetSelected.add(column);
                }
            }
        }

        clf.setAllItems(allColumns);
        clf.setAllToChoose(new ArrayList<Column>());
        clf.setChoosen(selected);

        response.sendRedirect("choose-from-list");
        return null;
    }

    // @RequestMapping(value = "/approve")
    // @Transactional(propagation = Propagation.REQUIRED)
    // public String approve(final HttpSession session, @RequestParam Long id) {
    //
    // return main(session);
    // }

    // @RequestMapping(value = "/select-prio")
    // @Transactional(propagation = Propagation.REQUIRED)
    // public String selectPrio(final HttpSession session, @RequestParam Integer selected) {
    // System.out.println("VerticalPrioController.selectPrio()");
    // List<SektorRaad> sectors = sektorRaadRepository.getTreeRoots();
    // for (SektorRaad sector : sectors.get(0).getChildren()) {
    //
    // for (int i = 0; i < 10; i++) {
    // Prioriteringsobjekt prio = new Prioriteringsobjekt();
    // prio.setSektorRaad(sector);
    // BeanMap bm = new BeanMap(prio);
    // for (Object key : bm.keySet()) {
    // if (bm.getType(key.toString()).equals(String.class)) {
    // bm.put(key.toString(), sector.getKod() + " " + i);
    // }
    // }
    // prioRepository.store(prio);
    // }
    // }
    //
    // return "select-prio";
    // }

    /**
     * The action for selecting a specific node of in the three of {@link SektorRaad}. It gets the three out of the
     * session and then finds the node denoted by the Id-argument sent from the client. When found it toggles
     * (selected = !selected) the value of the 'select' property on this node.
     * 
     * Special case in this is when the user have clicked the allSektorsRaad property (hosted on the
     * {@link MainForm} form object). Then it clears away all other selections in favor of this one. If any other
     * node is selected then it reversely un-selects this property.
     * 
     * @param session
     * @param id
     * @param response
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/check")
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public String check(final HttpSession session, @RequestParam Integer id, HttpServletResponse response)
            throws IOException {
        MainForm form = getMainForm(session);

        if (id.longValue() == -1l) {
            boolean b = form.getAllSektorsRaad().isSelected();
            form.getAllSektorsRaad().setSelected(!b);

            for (SektorRaad sr : form.getSectors()) {
                sr.setSelectedDeeply(false);
            }

        } else {
            form.getAllSektorsRaad().setSelected(false);
            SektorRaad sector = getSectorById(id, form.getSectors());
            sector.setSelected(!sector.isSelected());
        }

        response.sendRedirect("main");
        return "main";
    }

    /**
     * Loops through a collection and returns the sector with the corresponding id value provided.
     * 
     * @param id
     * @param sectors
     * @return The matched {@link SektorRaad} or null if no such match bould be made.
     */
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

    /**
     * Returns all nodes from a list of {@link SektorRaad} that have the 'selected' property set to true. And also
     * includes all the nodes beneath those.
     * 
     * @param raads
     * @return
     */
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

    /**
     * Takes a list of root nodes and returns a list of all the roots and of their children (and children's
     * children and so on).
     * 
     * @param raads
     * @return
     */
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

    /**
     * Takes a list of {@link SektorRaad} and makes a copy that only contains the id-property of the object. Reason
     * for this is to get objects that when used as condition in the {@link JpqlMatchBuilder} only generates
     * constraints on the id. e.g. id = ? instead of (id = ? and kod = ? and beskrivning = ? and....).
     * 
     * TODO: Look to see if this method could be removed. Since its creation the {@link JpqlMatchBuilder} might be
     * smart enough to do the corresponding change in the conditions itself.
     * 
     * @param raads
     * @return
     */
    private List<SektorRaad> toBlankWithIdOnly(List<SektorRaad> raads) {
        List<SektorRaad> result = new ArrayList<SektorRaad>();
        for (SektorRaad sr : raads) {
            SektorRaad newRaad = new SektorRaad(sr.getId());
            result.add(newRaad);
        }
        return result;
    }

    /**
     * Produces the result list in the main view of the application. It uses a
     * {@link PrioriteringsobjektFindCondition} as search condition - this object is stored in the session.
     * 
     * @param session
     * @return
     */
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
                HaveNestedEntities<SektorRaad> hne = condition.getSektorRaad();
                clearAwayNonSortingLogic(hne);
            }
        } else {
            List<SektorRaad> raad = getMarkedLeafs(mf.getSectors());
            raad = flatten(raad);

            NestedSektorRaad sektorNest = condition.getSektorRaad();

            // Find out if there are selected sectors, taking regards to that there might be HaveSortOrder-objects
            // inside.
            clearAwayNonSortingLogic(sektorNest);
            sektorNest.content().addAll(raad);

            if (raad.isEmpty()) {
                List<Prioriteringsobjekt> zeroResult = new ArrayList<Prioriteringsobjekt>();
                session.setAttribute("rows", zeroResult);
                return zeroResult;
            }
        }

        List<Prioriteringsobjekt> result = new ArrayList<Prioriteringsobjekt>();

        List<? extends Prioriteringsobjekt> prios = new ArrayList<Prioriteringsobjekt>(
                prioRepository.findByExample(condition, null));
        result.addAll(prios);

        List<SektorRaad> sectors = applicationData.getSektorRaadList();

        for (Prioriteringsobjekt prio : result) {
            prio.setSektorRaad(findRoot(sectors, prio.getSektorRaad()));
        }

        session.setAttribute("rows", result);
        return result;
    }

    /**
     * Removes all nodes in a {@link HaveNestedEntities} but those that implements the {@link HaveQuerySortOrder}.
     * Makes it possible to remove condition logic and preserves any existing sorting at the same time.
     * 
     * @param hne
     */
    private void clearAwayNonSortingLogic(HaveNestedEntities<?> hne) {
        for (Object sr : new ArrayList<Object>(hne.content())) {
            if (!(sr instanceof HaveQuerySortOrder)) {
                hne.content().remove(sr);
            }
        }
    }

    /**
     * If you have a {@link SektorRaad} and want its root node, this method gives you that.
     * 
     * @param all
     *            All existing sectors. The Method search through these to find the root.
     * @param toFind
     * @return
     */
    private SektorRaad findRoot(List<SektorRaad> all, SektorRaad toFind) {
        for (SektorRaad sr : all) {
            if (sr.getId() == null || toFind == null || toFind.getId() == null) {
                return null;
            }
            if (sr != null && sr.getId().equals(toFind.getId())) {
                return sr;
            }
        }
        for (SektorRaad sr : all) {
            SektorRaad result = findRoot(sr.getChildren(), toFind);
            if (result != null) {
                return result;
            }
        }
        return null;
    }

}
