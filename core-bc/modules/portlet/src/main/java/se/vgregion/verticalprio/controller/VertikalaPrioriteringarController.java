package se.vgregion.verticalprio.controller;

import org.apache.commons.beanutils.BeanMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import se.vgregion.verticalprio.*;
import se.vgregion.verticalprio.controllers.EditDirective;
import se.vgregion.verticalprio.controllers.PrioriteringsobjektForm;
import se.vgregion.verticalprio.controllers.SektorRaadBean;
import se.vgregion.verticalprio.entity.Prioriteringsobjekt;
import se.vgregion.verticalprio.entity.SektorRaad;
import se.vgregion.verticalprio.entity.User;
import se.vgregion.verticalprio.repository.GenerisktHierarkisktKodRepository;
import se.vgregion.verticalprio.repository.GenerisktKodRepository;
import se.vgregion.verticalprio.repository.PrioRepository;
import se.vgregion.verticalprio.repository.finding.DateNullLogic;

import javax.portlet.ActionResponse;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.PortletSession;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;

/**
 * @author Patrik Bergström
 */
@Controller
@RequestMapping(value = "VIEW")
@SessionAttributes(value = { "confCols", "form" })
public class VertikalaPrioriteringarController extends PortletBaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(VertikalaPrioriteringarController.class);

    @Autowired
    private GenerisktKodRepository<User> userRepository;

    @Autowired
    @Qualifier("sektorRaadRepository")
    private GenerisktHierarkisktKodRepository<SektorRaad> sektorRaadRepository;

    @Autowired
    protected PrioRepository prioRepository;

    @Autowired
    protected ApplicationData applicationData;

    public VertikalaPrioriteringarController() {
    }

    @ModelAttribute("form")
    public MainForm initState(PortletSession session) {
        MainForm form = getOrCreateSessionObj(session, "form", MainForm.class);
        initMainForm(form, session);

        return form;
    }

    @ModelAttribute("confCols")
    public ConfColumnsForm initConfColumnsForm(PortletSession session) {
        return getOrCreateSessionObj(session, "confCols", ConfColumnsForm.class);
    }

    @RenderMapping
    public String main(PortletRequest request, Model model) {
        PortletSession session = request.getPortletSession();

        session.setAttribute("editDir", new EditDirective(true, null));

        result(session);

        Map<String, Object> attributeMap = session.getAttributeMap();

        model.addAllAttributes(attributeMap);

        return "main";
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @ActionMapping(params = "action=login")
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public void login(PortletRequest request, PortletSession session, @RequestParam(required = false) String userName,
                        @RequestParam(required = false) String password) {

        if (isBlank(userName) || isBlank(password)) {
            return ;
        }

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
    }

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
     * @param sectorId
     * @param response
     * @return
     * @throws java.io.IOException
     */
    @ActionMapping(params = { "action=check" })
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public void check(final PortletSession session, @RequestParam(required = false) Integer sectorId,
                      PortletResponse response, @ModelAttribute("form") MainForm form)
            throws IOException {

        if (sectorId != null && sectorId.longValue() == -1l) {
            boolean b = form.getAllSektorsRaad().isSelected();
            form.getAllSektorsRaad().setSelected(!b);

            for (SektorRaad sr : form.getSectors()) {
                sr.setSelectedDeeply(false);
            }

        } else if (sectorId != null) {
            form.getAllSektorsRaad().setSelected(false);
            SektorRaad sector = getSectorById(sectorId, form.getSectors());
            sector.setSelected(!sector.isSelected());
            sector.setOpenDeeply(true);
        }

    }

    @ActionMapping(params = {"action=doRowAction", "select-prio"})
    public void selectPrio(ActionResponse response, @RequestParam("id") Long id) {
        response.setRenderParameter("view", "prio-view");
        response.setRenderParameter("id", id + "");
    }

    @RenderMapping(params = "view=prio-view")
    @Transactional
    public String showPrioView(ModelMap model, PortletSession session, @RequestParam("id") Long id) {
/*
        if (!validateIdIsSelected(session, id)) {
            return "main";
        }
*/

        Map<String, Object> attributeMap = session.getAttributeMap();

        model.addAllAttributes(attributeMap);

        PrioriteringsobjektForm form = (PrioriteringsobjektForm) model.get("prio");
        if (form == null) {
            form = new PrioriteringsobjektForm();
        }
        model.addAttribute("prio", form);
        session.setAttribute("prio", form);
        model.addAttribute("editDir", new EditDirective(false, false));
        initKodLists(form);

        BeanMap formMap = new BeanMap(form);
        Prioriteringsobjekt prio = null;
        if (id != null) {
            prio = new Prioriteringsobjekt();
            prio.setId(id);
            prio = prioRepository.findByExample(prio, 1).get(0);
            prio.getDiagnoser().toArray();
            prio.getAatgaerdskoder().toArray();
            prio.getAtcKoder().toArray();
            prio.getChildren().toArray();
        } else {

            prio = new Prioriteringsobjekt();
        }
        BeanMap entityMap = new BeanMap(prio);
        formMap.putAllWriteable(entityMap);
        form.putAllIdsFromCodesIfAnyIntoAttributeOnThisObject();
        initPrio(form);

        if (form.getId() == null) {
            form.setId(id); // Strange... yes?
            // putAllWriteable seems not to work for this class on Long:s at least (and in the antonio-env).
            // TODO: own implementation of BeanMap
        }

        init(form.getSektorRaadList());

        PrioriteringsobjektForm unalteredVersion = new PrioriteringsobjektForm();
        Util.copyValuesAndSetsFromBeanToBean(form, unalteredVersion);
        form.setUnalteredVersion(unalteredVersion);

        return "prio-view";
    }

    @ActionMapping(params = { "action=doRowAction", "edit-sectors" })
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public void handleSectors(PortletSession session, ActionResponse response, final Model model) throws IOException {
        User user = (User) session.getAttribute("user");
        if (user.getUserEditor() || user.isApprover()) {
            List<SektorRaadBean> sectors = SektorRaadBean.toSektorRaadBeans(sektorRaadRepository.getTreeRoots());
            for (SektorRaadBean bean : sectors) {
                SektorRaadBean.initLockedValue(bean, user);
            }
            //model.addAttribute("sectors", sectors);
            session.setAttribute("sectors", sectors);
            response.setRenderParameter("view", "edit-sectors");
        }
    }

    @RenderMapping(params = { "view=edit-sectors" })
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public String viewSectors(final PortletSession session, final Model model) {
        List<SektorRaadBean> sectors = (List<SektorRaadBean>) session.getAttribute("sectors");
        model.addAttribute("sectors", sectors);
        return "sectors";
    }

    @ActionMapping(params = { "action=doSectorAction", "insert-sector" })
    @Transactional()
    public void insert(@RequestParam("id") List<String> id,
                         @RequestParam("parentId") List<String> parentId,
                         @RequestParam("kod") List<String> kod,
                         @RequestParam("markedAsDeleted") List<String> markedAsDeleted,
                         @RequestParam("insert-sector") Long insert,
                         @RequestParam("prioCount") List<String> prioCount,
                         @RequestParam("prioCount") List<String> locked,
                         Model modelMap,
                         PortletSession session,
                         ActionResponse response) {

        List<SektorRaadBean> sectors = toRaads(id, parentId, kod, markedAsDeleted, prioCount, locked);

        modelMap.addAttribute("sectors", sectors);
        session.setAttribute("sectors", sectors);

        if (insert.equals(-1l)) {
            SektorRaadBean newSektorRaad = new SektorRaadBean(idForUnsavedNewPosts--);
            sectors.add(newSektorRaad);
        } else {
            insertNewSektorIntoTree(sectors, insert);
        }

        session.setAttribute("sectors", sectors);
        response.setRenderParameter("view", "edit-sectors");
    }

    @ActionMapping(params = { "action=doSectorAction", "save" })
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void save(ActionResponse response, @RequestParam List<String> kod,
                       @RequestParam("id") List<String> id,
                       @RequestParam("parentId") List<String> parentId,
                       @RequestParam("markedAsDeleted") List<String> markedAsDeleted,
                       @RequestParam("prioCount") List<String> prioCount,
                       @RequestParam("locked") List<String> locked,
                       Model modelMap, PortletSession session) throws IOException {

        List<SektorRaadBean> sectors = toRaads(id, parentId, kod, markedAsDeleted, prioCount, locked);

        User user = (User) session.getAttribute("user");

        removeFromList(sectors);
        store(sektorRaadRepository, userRepository, sectors, user);

        modelMap.addAttribute("sectors", null);
        session.setAttribute("sectors", null);
        session.setAttribute("form", null);

        response.setRenderParameter("view", "");
    }

    @Override
    protected GenerisktHierarkisktKodRepository getSektorRaadRepository() {
        return sektorRaadRepository;
    }

    @Override
    protected ApplicationData getApplicationData() {
        return applicationData;
    }

    @Override
    protected PrioRepository getPrioRepository() {
        return prioRepository;
    }

}
