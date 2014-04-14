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
import se.vgregion.verticalprio.controllers.*;
import se.vgregion.verticalprio.entity.AbstractKod;
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
import javax.swing.*;
import java.io.IOException;
import java.util.*;

@Controller
@RequestMapping(value = "VIEW")
@SessionAttributes(value = {"confCols", "form"})
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
    private long noUserSelectedId = -1;

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

    @SuppressWarnings({"unchecked", "rawtypes"})
    @ActionMapping(params = "action=login")
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public void login(PortletRequest request, PortletSession session, @RequestParam(required = false) String userName,
                      @RequestParam(required = false) String password) {

        if (isBlank(userName) || isBlank(password)) {
            return;
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
     * <p/>
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
    @ActionMapping(params = {"action=check"})
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
    public void selectPrio(ActionResponse response, @RequestParam(value = "id", required = false) Long id) {
        if (id != null) {
            response.setRenderParameter("view", "prio-view");
            response.setRenderParameter("id", id + "");
        } else {
            response.setRenderParameter("view", "main");
        }
    }

    @ActionMapping(params = {"action=doRowAction", "edit-prio"})
    public void editPrio(ActionResponse response, @RequestParam(value = "id", required = false) Long id) {
        if (id != null) {
            response.setRenderParameter("view", "edit-prio-view");
            response.setRenderParameter("id", id + "");
        } else {
            response.setRenderParameter("view", "main");
        }
    }

    @RenderMapping(params = "view=prio-view")
    @Transactional
    public String showPrioView(ModelMap model,
                               PortletSession session,
                               @RequestParam(value = "id", required = false) Long id) {
/*
        if (!validateIdIsSelected(session, id)) {
            return "main";
        }
*/
        prioViewCommon(model, session, id);

        model.addAttribute("editDir", new EditDirective(false, false));

        return "prio-view";
    }

    @RenderMapping(params = "view=edit-prio-view")
    @Transactional
    public String showEditPrioView(ModelMap model,
                                   PortletSession session,
                                   @RequestParam(value = "id", required = false) Long id) {
/*
        if (!validateIdIsSelected(session, id)) {
            return "main";
        }
*/
        model.addAttribute("editDir", new EditDirective(true, false));

        prioViewCommon(model, session, id);

        return "prio-view";
    }

    private void prioViewCommon(ModelMap model, PortletSession session, Long id) {
        addSessionAttributesToModel(session, model);

        PrioriteringsobjektForm form = (PrioriteringsobjektForm) model.get("prio");
        if (form == null) {
            form = new PrioriteringsobjektForm();
        }
        model.addAttribute("prio", form);
        session.setAttribute("prio", form);
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
    }

    private void addSessionAttributesToModel(PortletSession session, ModelMap model) {
        Map<String, Object> attributeMap = session.getAttributeMap();

        model.addAllAttributes(attributeMap);
    }

    @ActionMapping(params = {"action=doRowAction", "edit-sectors"})
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

    @RenderMapping(params = {"view=edit-sectors"})
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public String viewSectors(final PortletSession session, final Model model) {
        List<SektorRaadBean> sectors = (List<SektorRaadBean>) session.getAttribute("sectors");
        model.addAttribute("sectors", sectors);
        return "sectors";
    }

    @ActionMapping(params = {"action=doSectorAction", "insert-sector"})
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

    @ActionMapping(params = {"action=doSectorAction", "save"})
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

    @ActionMapping(params = {"action=doSectorAction", "delete"})
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void delete(@RequestParam("id") List<String> id,
                       @RequestParam("parentId") List<String> parentId,
                       @RequestParam("kod") List<String> kod,
                       @RequestParam("markedAsDeleted") List<String> markedAsDeleted,
                       @RequestParam("delete") Long delete,
                       @RequestParam("prioCount") List<String> prioCount,
                       @RequestParam("locked") List<String> locked,
                       Model modelMap,
                       PortletSession session,
                       ActionResponse response) {

        List<SektorRaadBean> sectors = toRaads(id, parentId, kod, markedAsDeleted, prioCount, locked);
        modelMap.addAttribute("sectors", sectors);
        session.setAttribute("sectors", sectors);
        markToDeleteWhenSave(delete, sectors);
        response.setRenderParameter("view", "edit-sectors");
    }


    @ActionMapping(params = {"action=doRowAction", "edit-users"})
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public void editUsers(PortletSession session, ActionResponse response, final Model model) throws IOException {
        response.setRenderParameter("view", "edit-users");
    }

    @RenderMapping(params = {"view=edit-users"})
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public String viewUsers(final PortletSession session, final Model model) {
        checkSecurity(session);
        List<User> users = userRepository.findByExample(new ExampleUser(), null);
        model.addAttribute("users", users);
        return "users";
    }

    @ActionMapping(params = {"action=doUserAction", "edit"})
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public void editUser(PortletSession session, ActionResponse response, final Model modelMap, @RequestParam("id") Long id) throws IOException {
        if (!checkUserIsSelected(session, id)) {
            response.setRenderParameter("view", "edit-users");
            return;
        }
        User user = userRepository.findByExample(new User(id), 1).get(0);
        modelMap.addAttribute("otherUser", user);
        session.setAttribute("otherUser", user);
        initSectors(user, sektorRaadRepository);
        userRepository.clear();

        response.setRenderParameter("view", "edit-user");
    }

    @RenderMapping(params = {"view=edit-user"})
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public String viewUser(final PortletSession session, final Model model) {
        checkSecurity(session);
        return "user-form";
    }

    @ActionMapping(params = {"action=doUserAction", "sectorId"})
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public void selectUserSectors(Model modelMap, @ModelAttribute User otherUser, @RequestParam Long sectorId,
                                  PortletSession session, ActionResponse response) {
        mirrorUserInSession(otherUser, new HttpSessionBox(session));
        check(sectorId, otherUser.getSektorRaad());
        modelMap.addAttribute("otherUser", otherUser);
        response.setRenderParameter("view", "edit-user-sectors");
    }

    @RenderMapping(params = {"view=edit-user-sectors"})
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public String viewUserSectors(final PortletSession session, final Model model) {
        checkSecurity(session);
        return "user-form";
    }

    @ActionMapping(params = {"action=doUserAction", "save"})
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public void saveUser(ModelMap modelMap, @ModelAttribute User user, PortletSession session, ActionResponse response) {
        checkSecurity(session);

        mirrorUserInSession(user, new HttpSessionBox(session));
        session.setAttribute("otherUser", null);
        modelMap.addAttribute("otherUser", null);

        List<SektorRaad> raads = flattenSelected(user.getSektorRaad());
        user.setSektorRaad(raads);

        userRepository.store(user);
        userRepository.flush();
        response.setRenderParameter("view", "edit-users");
    }

    @ActionMapping(params = {"action=doUserAction", "cancel"})
    public void cancelUser(ActionResponse response) {
        response.setRenderParameter("view", "edit-users");
    }

    /**
     * Checks to see if the default id of User is selected - that means no selection is made. Then it adds a
     * message to the session and redirects to the main users.jsp page.
     *
     * @param session
     * @param id
     * @throws IOException
     */
    private boolean checkUserIsSelected(PortletSession session, Long id)
            throws IOException {
        if (id == null || id.longValue() == noUserSelectedId) {
            MessageHome messageHome = new MessageHome();
            messageHome
                    .setMessage("Välj en post före den här opperationen (radioknappen längst till vänster i tabellen).");
            session.setAttribute("messageHome", messageHome);
            return false;
        } else {
            return true;
        }
    }

    /**
     * Method to validate that the user really have the right to use this controllers functionality.
     *
     * @param session
     */
    protected void checkSecurity(PortletSession session) {
        User activeUser = (User) session.getAttribute("user");
        if (!activeUser.getUserEditor()) {
            throw new RuntimeException();
        }
    }

    @ActionMapping(params = {"action=prioViewForm", "cancel"})
    public void cancelPrioForm(ActionResponse response) {
        response.setRenderParameter("view", "main");
    }

    @ActionMapping(params = {"action=prioViewForm", "save"})
    @Transactional
    public void savePrioForm(PortletRequest request,
                             ActionResponse response,
                             PortletSession session,
                             @ModelAttribute("prio") PrioriteringsobjektForm pf)
            throws IOException {

        PrioriteringsobjektForm sessionPrio = (PrioriteringsobjektForm) session.getAttribute("prio");
        Prioriteringsobjekt prio = toPrioriteringsobjekt(request, pf, session);
        copyKodCollectionsAndMetaDates(sessionPrio, prio);
        prio.getChildren().addAll(sessionPrio.getChildren());

        prio.setSenastUppdaterad(new Date());

        String error = prio.getMessagesWhyNotSaveAble();
        if (error != null) {
            MessageHome mh = getOrCreateSessionObj(session, "messageHome", MessageHome.class);
            mh.setMessage(error);
            response.setRenderParameter("view", "prio-view");
        } else {
            prioRepository.store(prio);
        }
        response.setRenderParameter("view", "main");
    }

    @ActionMapping(params = {"action=prioViewForm", "choose-diagnoser"})
    public void chooseDiagnoser(ActionResponse response) {
        response.setRenderParameter("view", "choose-from-list");
    }

    @RenderMapping(params = "view=choose-from-list")
    public String viewChooseFromList(PortletRequest request, PortletResponse response, PortletSession session,
                                     ModelMap model, @ModelAttribute("prio") PrioriteringsobjektForm pf)
            throws IOException {
        ChooseFromListController.ChooseListForm clf = initChooseListForm();
        session.setAttribute(ChooseFromListController.ChooseListForm.class.getSimpleName(), clf);

        chooseKod(session, response, request, model, pf, "diagnoser");

        addSessionAttributesToModel(session, model);
        return "choose-from-list";
    }

    private void chooseKod(PortletSession session, PortletResponse response, PortletRequest request,
                           ModelMap model, PrioriteringsobjektForm pf, String kodWithField) throws IOException {
        initKodLists(pf);
        pf.asignCodesFromTheListsByCorrespondingIdAttributes();

        PrioriteringsobjektForm sessionPrio = (PrioriteringsobjektForm) session.getAttribute("prio");
        copyKodCollectionsAndMetaDates(sessionPrio, pf);
        session.setAttribute("prio", pf);
        model.addAttribute("prio", pf);
        pf.setUnalteredVersion(sessionPrio.getUnalteredVersion());

        BeanMap bm = new BeanMap(pf);

        ChooseFromListController.ChooseListForm clf = getOrCreateSessionObj(session, ChooseFromListController.ChooseListForm.class.getSimpleName(),
                ChooseFromListController.ChooseListForm.class);
        clf.setOkLabel("Bekräfta val");
        clf.setDisplayKey("kodPlusBeskrivning");
        clf.setIdKey("id");
        clf.setOkUrl("prio?goBack=10");
        clf.setCancelUrl("prio?goBack=10");

        Collection<AbstractKod> target = (Collection<AbstractKod>) bm.get(kodWithField);
        clf.setTarget(target);
        clf.getChoosen().addAll(target);

        BeanMap applicationDataMap = new BeanMap(applicationData);

        List<AbstractKod> allItems = (List<AbstractKod>) applicationDataMap.get(kodWithField + "List");
        clf.setAllItems(allItems);

        clf.setAllToChoose(new ArrayList<AbstractKod>(allItems));
    }

    /**
     * Instantiates a new User and puts it into the session and model - making it available for the ui to edit
     * before saving.
     *
     * @param modelMap
     * @param session
     * @return
     */
    @ActionMapping(params = {"action=doUserAction", "create"})
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void createUser(Model modelMap, PortletSession session, ActionResponse response) {
        User user = new User();
        modelMap.addAttribute("otherUser", user);
        session.setAttribute("otherUser", user);
        initSectors(user, sektorRaadRepository);

        response.setRenderParameter("view", "edit-user");
    }

    /**
     * Removes a user.
     *
     * @param modelMap
     * @param id
     * @param session
     * @return
     * @throws IOException
     */
    @ActionMapping(params = {"action=doUserAction", "delete"})
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void delete(ModelMap modelMap, @RequestParam("id") Long id,
                       PortletSession session,
                       ActionResponse response) throws IOException {

        if (checkUserIsSelected(session, id)) {
            userRepository.remove(id);
            userRepository.flush();
        }

        response.setRenderParameter("view", "edit-users");
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
