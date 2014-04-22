package se.vgregion.verticalprio.controller;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.model.*;
import com.liferay.portal.util.PortalUtil;
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
import se.vgregion.verticalprio.entity.*;
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
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public String main(PortletRequest request, Model model) throws SystemException, PortalException {
        PortletSession session = request.getPortletSession();

        if (session.getAttribute("user") == null) {
            com.liferay.portal.model.User liferayUser = fetchLiferayUser(request);
            if (liferayUser != null) {
                User example = new User();
                example.setVgrId(liferayUser.getScreenName());
                List<User> users = userRepository.findByExample(example, 1);
                if (users.size() == 1) {
                    User user = users.get(0);
                    initSearch(user, session);
                } else {
                    makeNewUserForLoggedInLiferay(liferayUser, session);
                }
            }
        }

        session.setAttribute("editDir", new EditDirective(true, null));
        result(session);
        Map<String, Object> attributeMap = session.getAttributeMap();
        model.addAllAttributes(attributeMap);
        return "main";
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    private User makeNewUserForLoggedInLiferay(com.liferay.portal.model.User liferayUser, PortletSession session) {
        User user = new User();
        user.setVgrId(liferayUser.getScreenName());
        user.setFirstName(liferayUser.getFirstName());
        user.setLastName(liferayUser.getLastName());
        user.setApprover(false);
        user.setEditor(false);
        user.setPassword(liferayUser.getPassword());
        user.setUserEditor(false);
        userRepository.persist(user);
        initSearch(user, session);
        return user;
    }


    protected com.liferay.portal.model.User fetchLiferayUser(PortletRequest request) throws PortalException, SystemException {
        return PortalUtil.getUser(request);
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
            initSearch(user, session);
        }
    }

    private void initSearch(User user, PortletSession session) {
        PrioriteringsobjektFindCondition condition = getOrCreateSessionObj(session, "prioCondition",
                PrioriteringsobjektFindCondition.class);
        if (user.isEditor() || user.isApprover()) {
            condition.setGodkaend(new DateNullLogic(false));
        } else {
            condition.setGodkaend(new DateNullLogic(true));
        }

        initFields(user);

        session.setAttribute("user", user);
        session.setAttribute("loginResult", true);
    }

    private void initFields(Object obj) {
        Map userValues = new HashMap(new BeanMap(obj)); // Insane... makes all lazy properties initialized.
        for (Object o : userValues.values()) {
            if (o instanceof Collection) {
                Collection c = (Collection) o;
                for (Object i : c) {
                    new HashMap(new BeanMap(i));
                }
            }
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
        model.addAttribute("editDir", new EditDirective(true, null));

        if (model.get("prio") == null) {
            prioViewCommon(model, session, id);
        }

        addSessionAttributesToModel(session, model);

        return "prio-view";
    }

    private void prioViewCommon(ModelMap model, PortletSession session, Long id) {
//        addSessionAttributesToModel(session, model);

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
    public void editUser(PortletSession session, ActionResponse response, final Model modelMap,
                         @RequestParam("id") Long id) throws IOException {
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
    public void saveUser(ModelMap modelMap, @ModelAttribute User user, PortletSession session,
                         ActionResponse response) {
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

    @ActionMapping(params = {"action=doRowAction", "prio-create"})
    public void createPrio(PortletRequest request, ActionResponse response, ModelMap modelMap) {
        response.setRenderParameter("view", "edit-prio-view");
    }

    @ActionMapping(params = {"action=prioViewForm", "choose-diagnoser"})
    public void chooseDiagnoser(PortletRequest request, ActionResponse response, PortletSession session,
                                ModelMap model, @ModelAttribute("prio") PrioriteringsobjektForm pf,
                                @RequestParam("id") String prioId) throws IOException {

        PrioriteringsobjektForm sessionPrio = (PrioriteringsobjektForm) session.getAttribute("prio");
        copyKodCollectionsAndMetaDates(sessionPrio, pf);
        session.setAttribute("prio", pf);

        ChooseFromListController.ChooseListForm clf = new ChooseFromListController.ChooseListForm();
        session.setAttribute(ChooseFromListController.ChooseListForm.class.getSimpleName(), clf);

        clf.setFilterLabel("Sök diagnoser med nyckelord");
        clf.setNotYetChosenLabel("Ej valda diagnoser");
        clf.setChosenLabel("Valda diagnoser");
        clf.setFilterLabelToolTip("Här kan du söka både på kod och på beskrivning");
        clf.setType(DiagnosKod.class);

        chooseKod(session, response, request, model, pf, "diagnoser");

        response.setRenderParameter("view", "choose-from-list");
        response.setRenderParameter("prioId", prioId);
    }

    @ActionMapping(params = {"action=prioViewForm", "choose-aatgaerdskoder"})
    public void chooseAatgaerdskoder(PortletRequest request, ActionResponse response, PortletSession session,
                                     ModelMap model, @ModelAttribute("prio") PrioriteringsobjektForm pf,
                                     @RequestParam("id") String prioId) throws IOException {

        PrioriteringsobjektForm sessionPrio = (PrioriteringsobjektForm) session.getAttribute("prio");
        copyKodCollectionsAndMetaDates(sessionPrio, pf);
        session.setAttribute("prio", pf);

        ChooseFromListController.ChooseListForm clf = new ChooseFromListController.ChooseListForm();
        session.setAttribute(ChooseFromListController.ChooseListForm.class.getSimpleName(), clf);

        clf.setFilterLabel("Sök åtgärdskoder med nyckelord");
        clf.setNotYetChosenLabel("Ej valda åtgärdskoder");
        clf.setChosenLabel("Valda åtgärdskoder");
        clf.setType(AatgaerdsKod.class);

        chooseKod(session, response, request, model, pf, "aatgaerdskoder");

        response.setRenderParameter("view", "choose-from-list");
        response.setRenderParameter("prioId", prioId);
    }

    @ActionMapping(params = {"action=prioViewForm", "choose-atcKoder"})
    public void chooseAtcKoder(PortletRequest request, PortletSession session, ActionResponse response, ModelMap model,
                               @RequestParam("id") String prioId ,@ModelAttribute("prio") PrioriteringsobjektForm pf)
            throws IOException {

        PrioriteringsobjektForm sessionPrio = (PrioriteringsobjektForm) session.getAttribute("prio");
        copyKodCollectionsAndMetaDates(sessionPrio, pf);
        session.setAttribute("prio", pf);

        ChooseFromListController.ChooseListForm clf = new ChooseFromListController.ChooseListForm();
        session.setAttribute(ChooseFromListController.ChooseListForm.class.getSimpleName(), clf);

        clf.setFilterLabel("Sök ATC-koder med nyckelord");
        clf.setNotYetChosenLabel("Ej valda ATC-koder");
        clf.setChosenLabel("Valda ATC-koder");
        clf.setType(AtcKod.class);

        chooseKod(session, response, request, model, pf, "atcKoder");

        response.setRenderParameter("view", "choose-from-list");
        response.setRenderParameter("prioId", prioId);
    }

    @Transactional
    @ActionMapping(params = {"action=prioViewForm", "removeCodes"})
    public void removeKoder(PortletRequest request, PortletSession session, ActionResponse response,
                                @RequestParam("id") String id, @RequestParam("removeCode") List<String> codesToRemove,
                                @ModelAttribute("prio") PrioriteringsobjektForm pf, ModelMap model) {

        response.setRenderParameter("view", "edit-prio-view");
        response.setRenderParameter("id", id);

        PrioriteringsobjektForm sessionPrio = (PrioriteringsobjektForm) session.getAttribute("prio");
        copyKodCollectionsAndMetaDates(sessionPrio, pf);
        session.setAttribute("prio", pf);

        Set<? extends AbstractKod> koder;

        Class<? extends AbstractKod> type;
        // Determine the type
        String removeCodes = request.getParameter("removeCodes");
        if (removeCodes.contains("åtgärder")) {
            type = AatgaerdsKod.class;
        } else if (removeCodes.contains("diagnoser")) {
            type = DiagnosKod.class;
        } else if (removeCodes.contains("Ta bort valda koder")) { // Decided to be more specific here since the value is less specific in itself
            type = AtcKod.class;
        } else {
            throw new IllegalArgumentException("Cannot determine the type of entity to remove");
        }

        if (type == null) {
            throw new IllegalStateException("Type of ChooseListForm must not be null.");
        }

        String keyWord;
        if (type.equals(DiagnosKod.class)) {
            koder = pf.getDiagnoser();
            keyWord = "diagnoser:";
        } else if (type.equals(AatgaerdsKod.class)) {
            koder = pf.getAatgaerdskoder();
            keyWord = "aatgaerdskoder:";
        } else if (type.equals(AtcKod.class)) {
            koder = pf.getAtcKoder();
            keyWord = "atcKoder:";
        } else {
            throw new IllegalStateException("Unexpected type [" + type.toString() + "] for ChooseListForm instance.");
        }

        Iterator<? extends AbstractKod> iterator = koder.iterator();
        while (iterator.hasNext()) {
            AbstractKod next = iterator.next();
            if (codesToRemove.contains(keyWord + next.getId())) {
                iterator.remove();
            }
        }

        model.addAttribute("prio", pf);
    }

    @RenderMapping(params = "view=choose-from-list")
    public String viewChooseFromList(PortletRequest request, PortletResponse response, PortletSession session,
                                     ModelMap model, /*@ModelAttribute("prio") PrioriteringsobjektForm pf,*/
                                     @RequestParam(value = "filterText", required = false) String filterText,
                                     @RequestParam(value = "prioId", required = true) String prioId)
            throws IOException {

        String simpleName = ChooseFromListController.ChooseListForm.class.getSimpleName();
        ChooseFromListController.ChooseListForm clf =
                (ChooseFromListController.ChooseListForm) session.getAttribute(simpleName);

        addNotYetChosenKoder(session, clf);
        removeToRemoveKoder(session, clf);

        if (filterText != null && !"".equals(filterText)) {
            clf.setFilterText(filterText);

            filterText = filterText.toLowerCase(Locale.getDefault());
            List allToChoose = clf.getAllToChoose();

            filterList(filterText, allToChoose);

            allToChoose.removeAll(clf.getChosen());
        } else {
            clf.getAllToChoose().clear();
            clf.getAllToChoose().addAll(clf.getAllItems());
            clf.getAllToChoose().removeAll(clf.getChosen());
            clf.setFilterText(null);
        }

        addSessionAttributesToModel(session, model);

        model.addAttribute("prioId", prioId);

        return "choose-from-list";
    }

    protected void filterList(String filterText, List allToChoose) {
        Iterator iterator = allToChoose.iterator();
        while (iterator.hasNext()) {
            AbstractKod next = (AbstractKod) iterator.next();
            if (next.getBeskrivning() == null || !next.getBeskrivning().toLowerCase(Locale.getDefault())
                    .contains(filterText)) {
                iterator.remove();
            }
        }
    }

    protected void removeToRemoveKoder(PortletSession session, ChooseFromListController.ChooseListForm clf) {
        List<AbstractKod> toRemoveKoder = (List<AbstractKod>) session.getAttribute("toRemoveKoder");
        if (toRemoveKoder != null) {
            clf.getChosen().removeAll(toRemoveKoder);
            clf.getAllToChoose().removeAll(toRemoveKoder); // Avoid duplicates. Like a set operation.
            clf.getAllToChoose().addAll(toRemoveKoder);
            session.removeAttribute("toRemoveKoder");
        }
    }

    protected void addNotYetChosenKoder(PortletSession session, ChooseFromListController.ChooseListForm clf) {
        List<AbstractKod> notYetChosenKoder = (List<AbstractKod>) session.getAttribute("notYetChosenKoder");
        if (notYetChosenKoder != null) {
            clf.getChosen().addAll(notYetChosenKoder);
            clf.getAllToChoose().removeAll(notYetChosenKoder);
            session.removeAttribute("notYetChosenKoder");
        }
    }

    @ActionMapping(params = {"action=chooseFromList", "filter"})
    public void filterChooseFromList(ActionResponse response,
                                     @RequestParam("filterText") String filterText,
                                     @RequestParam("prioId") String prioId) {
        response.setRenderParameter("view", "choose-from-list");
        response.setRenderParameter("filterText", filterText);
        response.setRenderParameter("prioId", prioId);
    }

    @ActionMapping(params = {"action=chooseFromList", "cancel"})
    public void cancelChooseFromList(PortletRequest request, PortletSession session, ActionResponse response,
                                     ModelMap model, @RequestParam("prioId") String prioId) {
        session.removeAttribute(ChooseFromListController.ChooseListForm.class.getSimpleName());
        response.setRenderParameter("view", "edit-prio-view");
        response.setRenderParameter("id", prioId);
    }

    @Transactional
    @ActionMapping(params = {"action=chooseFromList", "ok"})
    public void confirmChooseFromList(PortletRequest request, PortletSession session, ActionResponse response,
                                     ModelMap model, @RequestParam("prioId") String prioId) {
        ChooseFromListController.ChooseListForm chooseListForm = (ChooseFromListController.ChooseListForm)
                session.getAttribute(ChooseFromListController.ChooseListForm.class.getSimpleName());

        List<? extends AbstractKod> theChosen = chooseListForm.getChosen();

        PrioriteringsobjektForm pf = (PrioriteringsobjektForm) session.getAttribute("prio");

        Class<? extends AbstractKod> type = chooseListForm.getType();
        if (type == null) {
            throw new IllegalStateException("Type of ChooseListForm must not be null.");
        }

        if (type.equals(DiagnosKod.class)) {
            pf.setDiagnoser(new HashSet<DiagnosKod>((List<? extends DiagnosKod>) theChosen));
        } else if (type.equals(AatgaerdsKod.class)) {
            pf.setAatgaerdskoder(new HashSet<AatgaerdsKod>((List<? extends AatgaerdsKod>) theChosen));
        } else if (type.equals(AtcKod.class)) {
            pf.setAtcKoder(new HashSet<AtcKod>((List<? extends AtcKod>) theChosen));
        } else {
            throw new IllegalStateException("Unexpected type [" + type.toString() + "] for ChooseListForm instance.");
        }

        model.addAttribute("prio", pf);
        session.removeAttribute(ChooseFromListController.ChooseListForm.class.getSimpleName());
        response.setRenderParameter("view", "edit-prio-view");
        response.setRenderParameter("id", prioId);
    }

    @ActionMapping(params = {"action=chooseFromList", "add"})
    public void addChooseFromList(PortletSession session, ActionResponse response,
                                  @RequestParam(value = "filterText", required = false) String filterText,
                                  @RequestParam(value = "notYetChosenKeys", required = false) List<String> notYetChosenKeys,
                                  @RequestParam(value = "prioId", required = false) String prioId) {
        if (filterText != null) {
            response.setRenderParameter("filterText", filterText);
        }

        ChooseFromListController.ChooseListForm clf = (ChooseFromListController.ChooseListForm)
                session.getAttribute("ChooseListForm");

        List<AbstractKod> notYetChosen = new ArrayList<AbstractKod>();

        if (notYetChosenKeys.size() > 0) {

            List<AbstractKod> allToChoose = clf.getAllToChoose();
            Iterator<AbstractKod> iterator = allToChoose.iterator();
            while (iterator.hasNext()) {
                AbstractKod kod = iterator.next();
                if (notYetChosenKeys.contains(String.valueOf(kod.getId()))) {
                    notYetChosen.add(kod);
                }
            }
        }

        session.setAttribute("notYetChosenKoder", notYetChosen);

        response.setRenderParameter("view", "choose-from-list");
        response.setRenderParameter("prioId", prioId);
    }

    @ActionMapping(params = {"action=chooseFromList", "addAll"})
    public void addAllChooseFromList(PortletSession session, ActionResponse response,
                                  @RequestParam(value = "filterText", required = false) String filterText,
                                  @RequestParam(value = "prioId", required = false) String prioId) {
        if (filterText != null) {
            response.setRenderParameter("filterText", filterText);
        }

        ChooseFromListController.ChooseListForm clf = (ChooseFromListController.ChooseListForm)
                session.getAttribute("ChooseListForm");

        List allItemsToAdd = new ArrayList(clf.getAllItems());
        if (filterText != null && !"".equals(filterText)) {
            filterList(filterText, allItemsToAdd);
        }

        allItemsToAdd.removeAll(clf.getChosen()); // Already added

        session.setAttribute("notYetChosenKoder", allItemsToAdd);

        response.setRenderParameter("view", "choose-from-list");
        response.setRenderParameter("prioId", prioId);
    }

    @ActionMapping(params = {"action=chooseFromList", "remove"})
    public void removeChooseFromList(PortletSession session, ActionResponse response,
                                  @RequestParam(value = "filterText", required = false) String filterText,
                                  @RequestParam(value = "chosenKeys", required = false) List<String> chosenKeysToRemove,
                                  @RequestParam(value = "prioId", required = false) String prioId) {
        if (filterText != null) {
            response.setRenderParameter("filterText", filterText);
        }

        ChooseFromListController.ChooseListForm clf = (ChooseFromListController.ChooseListForm)
                session.getAttribute("ChooseListForm");

        List<AbstractKod> toRemove = new ArrayList<AbstractKod>();

        if (chosenKeysToRemove.size() > 0) {

            List<AbstractKod> chosen = clf.getChosen();

            Iterator<AbstractKod> iterator = chosen.iterator();
            while (iterator.hasNext()) {
                AbstractKod next = iterator.next();
                if (chosenKeysToRemove.contains(String.valueOf(next.getId()))) {
                    toRemove.add(next);
                }
            }
            session.setAttribute("toRemoveKoder", toRemove);
        }

        response.setRenderParameter("view", "choose-from-list");
        response.setRenderParameter("prioId", prioId);
    }

    @ActionMapping(params = {"action=chooseFromList", "removeAll"})
    public void removeAllChooseFromList(PortletSession session, ActionResponse response,
                                  @RequestParam(value = "filterText", required = false) String filterText,
                                  @RequestParam(value = "prioId", required = false) String prioId) {
        if (filterText != null) {
            response.setRenderParameter("filterText", filterText);
        }

        ChooseFromListController.ChooseListForm clf = (ChooseFromListController.ChooseListForm)
                session.getAttribute("ChooseListForm");

        session.setAttribute("toRemoveKoder", clf.getChosen());

        response.setRenderParameter("view", "choose-from-list");
        response.setRenderParameter("prioId", prioId);
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

        ChooseFromListController.ChooseListForm clf = getOrCreateSessionObj(
                session,
                ChooseFromListController.ChooseListForm.class.getSimpleName(),
                ChooseFromListController.ChooseListForm.class);

        clf.setOkLabel("Bekräfta val");
        clf.setDisplayKey("kodPlusBeskrivning");
        clf.setIdKey("id");
        clf.setOkUrl("prio?goBack=10");
        clf.setCancelUrl("prio?goBack=10");

        Collection<AbstractKod> target = (Collection<AbstractKod>) bm.get(kodWithField);
        clf.setTarget(target);
        List chosen = clf.getChosen();
        chosen.removeAll(target); // Avoid duplicates
        chosen.addAll(target);

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

    @ActionMapping(params = { "action=doUserAction", "openId" })
    @Transactional(propagation = Propagation.MANDATORY, readOnly = true)
    public void toggleOpenSectorNodeForUser(final PortletSession session, @RequestParam Integer openId, ActionResponse response)
            throws IOException {
        MainForm form = getMainForm(session);

        form.getAllSektorsRaad().setSelected(false);
        SektorRaad sector = getSectorById(openId, form.getSectors());
        sector.setOpen(!sector.isOpen());

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
