package se.vgregion.verticalprio.controller;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
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
import org.springframework.web.portlet.bind.annotation.ResourceMapping;
import se.vgregion.verticalprio.*;
import se.vgregion.verticalprio.Util;
import se.vgregion.verticalprio.controllers.*;
import se.vgregion.verticalprio.entity.*;
import se.vgregion.verticalprio.repository.GenerisktHierarkisktKodRepository;
import se.vgregion.verticalprio.repository.GenerisktKodRepository;
import se.vgregion.verticalprio.repository.PrioRepository;
import se.vgregion.verticalprio.repository.finding.DateNullLogic;
import se.vgregion.verticalprio.repository.finding.NestedRangordningsKod;
import se.vgregion.verticalprio.repository.finding.NestedVaardformsKod;

import javax.portlet.*;
import java.io.IOException;
import java.io.OutputStream;
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
    public String main(PortletRequest request, Model model,
                       @RequestParam(value = "sortField", required = false) String sortField)
            throws SystemException, PortalException {
        PortletSession session = request.getPortletSession();

        if (session.getAttribute("loggedOut") != null) {
            session.setAttribute("loggedOut", null);
            return "main";
        }

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

        if (sortField != null && !"".equals(sortField)) {
            alterSortOrder(session, sortField);
        }

        session.setAttribute("editDir", new EditDirective(true, null));
        result(session);
        Map<String, Object> attributeMap = session.getAttributeMap();
        model.addAllAttributes(attributeMap);
        return "main";
    }

    private void alterSortOrder(PortletSession session, String sortField) {
        MainForm form = getMainForm(session);
        PrioriteringsobjektFindCondition condition = getOrCreateSessionObj(session, "prioCondition",
                PrioriteringsobjektFindCondition.class);

        markColumnAsSorting(sortField, form);

        if ("rangordningsKod".equals(sortField)) {
            condition.sortByRangordningsKod();
        } else if ("tillstaandetsSvaarighetsgradKod".equals(sortField)) {
            condition.sortByTillstaandetsSvaarighetsgradKod();
        } else if ("diagnosKodTexts".equals(sortField)) {
            condition.sortByDiagnoser();
        } else if ("sektorRaad".equals(sortField)) {
            condition.sortBySektorsRaad();
        } else if ("id".equals(sortField)) {
            condition.sortById();
        }
    }

//    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
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
        userRepository.flush();
        initSearch(user, session);
        return user;
    }

    protected com.liferay.portal.model.User fetchLiferayUser(PortletRequest request) throws PortalException, SystemException {
        return PortalUtil.getUser(request);
    }

    @ActionMapping(params = {"view=logout"})
    public void logOut(PortletSession session, ActionResponse response) {
        session.setAttribute("user", null);
        session.setAttribute("loginResult", false);
        session.setAttribute("loggedOut", true);
        response.setRenderParameter("view", "logout");
    }

    @RenderMapping("view=logout")
    public String main() {
        return "main";
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @ActionMapping(params = "action=login")
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public void login(PortletRequest request, PortletSession session,
                      @RequestParam(value = "userName", required = false) String userName,
                      @RequestParam(value = "password", required = false) String password) {
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
    public void check(final PortletSession session,
                      @RequestParam(value = "sectorId", required = false) Integer sectorId,
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
                       @RequestParam("prioCount") List<String> locked, // todo Really???
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

        MainForm form = getMainForm(session);
        form.getSectors().clear();
        form.getSectors().addAll(sectors);

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
    public void selectUserSectors(Model modelMap, @ModelAttribute User otherUser,
                                  @RequestParam("sectorId") Long sectorId,
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

    @Transactional
    @ActionMapping(params = {"action=doRowAction", "approve-prio"})
    public void approvePrio(PortletRequest request, ActionResponse response, PortletSession session,
                            ModelMap modelMap, @RequestParam(value = "id", required = false) Long id) {
        if (!validateIdIsSelected(session, id)) {
            return;
        }

        User user = (User) session.getAttribute("user");
        if (user != null && user.isApprover()) {
            Prioriteringsobjekt prio = prioRepository.find(id);
            if (isUserInSektorsRaadIfNotWarnWithMessage(user, prio, session)) {

                try {
                    prio.godkaen();
                    Prioriteringsobjekt approvedVersion = null;
                    if (prio.getChildren().isEmpty()) {
                        // this prio has no previously approved children
                        // Lets create a child that is approved.
                        approvedVersion = new Prioriteringsobjekt();
                    } else {
                        // we only allow (for now) to have one approved version of a prio object
                        // TODO if need to save old versions of approved prio objects this code needs to be changed
                        approvedVersion = prio.getChildren().iterator().next();
                    }
                    Long nullOrApprovedId = approvedVersion.getId();
                    prio.setGodkaend(null); // TODO should not be needed
                    Util.copyValuesAndSetsFromBeanToBean(prio, approvedVersion);
                    approvedVersion.getChildren().clear();
                    approvedVersion.setId(nullOrApprovedId);
                    approvedVersion.setGodkaend(new Date());
                    prio.getChildren().add(approvedVersion);
                    prioRepository.store(approvedVersion);
                    prioRepository.store(prio);
                } catch (IllegalAccessError e) {
                    MessageHome messageHome = getOrCreateSessionObj(session, "messageHome", MessageHome.class);
                    messageHome.setMessage(e.getMessage());
                }

                prioRepository.merge(prio);
            }
        }
    }

    @Transactional
    @ActionMapping(params = {"action=doRowAction", "delete-prio"})
    public void deletePrio(PortletSession session, @RequestParam(value = "id", required = false) Long id) {
        prioRepository.remove(id);
        session.removeAttribute("prio");
    }

    @ActionMapping(params = {"action=doRowAction", "init-conf-columns"})
    public void chooseColumns(ActionResponse response, PortletSession session) {
        MainForm form = getMainForm(session);

        ChooseFromListController.ChooseListForm clf = getOrCreateSessionObj(session, ChooseFromListController.ChooseListForm.class.getSimpleName(),
                ChooseFromListController.ChooseListForm.class);

        clf.setNotYetChosenLabel("Dolda kolumner");
        clf.setChosenLabel("Synliga kolumner");
        clf.setOkLabel("Ok");

        clf.setDisplayKey("label");
        clf.setIdKey("id");
        clf.setFilterLabel(null);
        clf.setOkUrl("commit-conf-columns");
        clf.setCancelUrl("main");

        clf.setType(Column.class);

        List<Column> allColumns = new ArrayList<Column>();
        List<Column> selected = new ArrayList<Column>();
        List<Column> notYetSelected = new ArrayList<Column>();

        SortedSet<Column> target = new TreeSet<Column>();
        session.setAttribute("selectedColumns", target);
        clf.setTarget(target);

        User user = (User) session.getAttribute("user");

        for (Column column : form.getColumns()) {
            if (column.isHideAble() && (!column.isDemandsEditRights() || user != null && user.isEditor())) {
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
        clf.setChosen(selected);

        response.setRenderParameter("view", "choose-from-list");
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
                               @RequestParam("id") String prioId, @ModelAttribute("prio") PrioriteringsobjektForm pf)
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
    public String viewChooseFromList(PortletSession session,
                                     ModelMap model,
                                     @RequestParam(value = "filterText", required = false) String filterText,
                                     @RequestParam(value = "prioId", required = false) String prioId)
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

        sortList(clf.getAllToChoose());
        sortList(clf.getChosen());

        addSessionAttributesToModel(session, model);

        if (prioId != null) {
            model.addAttribute("prioId", prioId);
        }

        return "choose-from-list";
    }

    private void sortList(List list) {
        if (list == null || list.size() == 0) {
            return;
        }

        Collections.sort(list);
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

        if (prioId != null && !"".equals(prioId)) {
            response.setRenderParameter("view", "edit-prio-view");
            response.setRenderParameter("id", prioId);
        }
    }

    @ActionMapping(params = {"action=chooseFromList", "ok"})
    public void confirmChooseFromList(PortletRequest request, PortletSession session, ActionResponse response,
                                      ModelMap model, @RequestParam("prioId") String prioId) {
        ChooseFromListController.ChooseListForm chooseListForm = (ChooseFromListController.ChooseListForm)
                session.getAttribute(ChooseFromListController.ChooseListForm.class.getSimpleName());

        List<? extends Object> theChosen = chooseListForm.getChosen();

        PrioriteringsobjektForm pf = (PrioriteringsobjektForm) session.getAttribute("prio");

        Class<? extends Object> type = chooseListForm.getType();
        if (type == null) {
            throw new IllegalStateException("Type of ChooseListForm must not be null.");
        }

        if (chooseListForm instanceof ChooseCodesController.ChooseListFormWithDomainProperty) {
            PrioriteringsobjektFindCondition prioCondition =
                    (PrioriteringsobjektFindCondition) session.getAttribute("prioCondition");

            if (type.equals(DiagnosKod.class)) {
                prioCondition.getDiagnoser().clear();
                prioCondition.getDiagnoser().addAll(chooseListForm.getChosen());
            } else if (type.equals(AatgaerdsKod.class)) {
                prioCondition.getAatgaerdskoder().clear();
                prioCondition.getAatgaerdskoder().addAll(chooseListForm.getChosen());
            } else if (type.equals(TillstaandetsSvaarighetsgradKod.class)) {
                List chosen = chooseListForm.getChosen();
                Set<TillstaandetsSvaarighetsgradKod> nestedContent = prioCondition.getTillstaandetsSvaarighetsgradKod().getNestedContent();
                nestedContent.clear();
                nestedContent.addAll(chosen);
            } else if (type.equals(RangordningsKod.class)) {
                List chosen = chooseListForm.getChosen();
                Set<RangordningsKod> nestedContent = ((NestedRangordningsKod) prioCondition.getRangordningsKod()).getNestedContent();
                nestedContent.clear();
                nestedContent.addAll(chosen);
            } else if (type.equals(AtcKod.class)) {
                List chosen = chooseListForm.getChosen();
                Set<AtcKod> atcKoder = prioCondition.getAtcKoder();
                atcKoder.clear();
                atcKoder.addAll(chosen);
            } else if (type.equals(VaardnivaaKod.class)) {
                List chosen = chooseListForm.getChosen();
                Set<VaardnivaaKod> nestedContent = prioCondition.getVaardnivaaKod().getNestedContent();
                nestedContent.clear();
                nestedContent.addAll(chosen);
            } else if (type.equals(VaardformsKod.class)) {
                List chosen = chooseListForm.getChosen();
                Set<VaardformsKod> nestedContent = ((NestedVaardformsKod) prioCondition.getVaardform()).getNestedContent();
                nestedContent.clear();
                nestedContent.addAll(chosen);
            } else {
                throw new IllegalStateException("Unexpected type [" + type.toString() + "] for ChooseListForm instance.");
            }
        } else {

            if (type.equals(DiagnosKod.class)) {
                pf.setDiagnoser(new HashSet<DiagnosKod>((List<? extends DiagnosKod>) theChosen));
            } else if (type.equals(AatgaerdsKod.class)) {
                pf.setAatgaerdskoder(new HashSet<AatgaerdsKod>((List<? extends AatgaerdsKod>) theChosen));
            } else if (type.equals(AtcKod.class)) {
                pf.setAtcKoder(new HashSet<AtcKod>((List<? extends AtcKod>) theChosen));
            } else if (type.equals(Column.class)) {
                // Special case, don't set render parameters to get to main view.
                session.removeAttribute(ChooseFromListController.ChooseListForm.class.getSimpleName());
                MainForm form = (MainForm) session.getAttribute("form");
                for (Column column : form.getColumns()) {
                    column.setVisible(chooseListForm.getChosen().contains(column) || !column.isHideAble());
                }
                return;
            } else if (type.equals("Filter")) {
                // Special case for filtering. Do nothing.
                return;
            } else {
                throw new IllegalStateException("Unexpected type [" + type.toString() + "] for ChooseListForm instance.");
            }

            model.addAttribute("prio", pf);
            session.removeAttribute(ChooseFromListController.ChooseListForm.class.getSimpleName());
            response.setRenderParameter("view", "edit-prio-view");
            response.setRenderParameter("id", prioId);
        }

    }

    @RenderMapping(params = {"view=choose-column-filter", "fieldName" })
    public String toFilterView(PortletSession session, PortletResponse response,
                               @RequestParam("fieldName") String fieldName,
                               ModelMap model)
            throws IOException {
        PrioriteringsobjektFindCondition condition = getOrCreateSessionObj(session, "prioCondition",
                PrioriteringsobjektFindCondition.class);

        ChooseCodesController.ChooseListFormWithDomainProperty clf = formPrototypes.get(fieldName);
        clf = clf.clone();
        String name = ChooseFromListController.ChooseListForm.class.getSimpleName();
        session.setAttribute(name, clf);

        List<Prioriteringsobjekt> rows = (List<Prioriteringsobjekt>) session.getAttribute("rows");
        List allItems = extractChildObjects(rows, clf.getAllItemsPropertyName(), clf.getDisplayKey());
        clf.setAllItems(allItems);
        Collection target = extractTargetCollection(condition, clf.getAllItemsPropertyName());
        clf.setTarget(target);
        clf.setChosen(new ArrayList(target));

        if ("diagnosTexts".equals(fieldName) || "diagnosKodTexts".equals(fieldName)) {
            clf.setType(DiagnosKod.class);
        } else if ("aatgaerdskoderTexts".equals(fieldName) || "aatgaerdskoder".equals(fieldName)) {
            clf.setType(AatgaerdsKod.class);
        } else if ("tillstaandetsSvaarighetsgradKod".equals(fieldName)) {
            clf.setType(TillstaandetsSvaarighetsgradKod.class);
        } else if ("rangordningsKod".equals(fieldName)) {
            clf.setType(RangordningsKod.class);
        } else if ("atcText".equals(fieldName) || "atcKoder".equals(fieldName)) {
            clf.setType(AtcKod.class);
        } else if ("vaardnivaaKod".equals(fieldName)) {
            clf.setType(VaardnivaaKod.class);
        } else if ("vaardform".equals(fieldName)) {
            clf.setType(VaardformsKod.class);
        }

        return viewChooseFromList(session, model, null, null);
    }

    @ActionMapping(params = {"action=deselect-column-filter", "fieldName" })
    public void deselectFilter(PortletSession session, PortletResponse response,
                               @RequestParam("fieldName") String fieldName,
                               ModelMap model) {

        PrioriteringsobjektFindCondition condition = getOrCreateSessionObj(session, "prioCondition",
                PrioriteringsobjektFindCondition.class);

        if ("diagnosTexts".equals(fieldName) || "diagnosKodTexts".equals(fieldName)) {
            condition.getDiagnoser().clear();
        } else if ("aatgaerdskoderTexts".equals(fieldName) || "aatgaerdskoder".equals(fieldName)) {
            condition.getAatgaerdskoder().clear();
        } else if ("tillstaandetsSvaarighetsgradKod".equals(fieldName)) {
            condition.getTillstaandetsSvaarighetsgradKod().getNestedContent().clear();
        } else if ("rangordningsKod".equals(fieldName)) {
            ((NestedRangordningsKod) condition.getRangordningsKod()).getNestedContent().clear();
        } else if ("rangordningsKod".equals(fieldName)) {
            ((NestedRangordningsKod) condition.getRangordningsKod()).getNestedContent().clear();
        } else if ("atcText".equals(fieldName) || "atcKoder".equals(fieldName)) {
            condition.getAtcKoder().clear();
        } else if ("vaardnivaaKod".equals(fieldName)) {
            condition.getVaardnivaaKod().getNestedContent().clear();
        } else if ("vaardform".equals(fieldName)) {
            ((NestedVaardformsKod) condition.getVaardform()).getNestedContent().clear();
        }
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

        List<Object> notYetChosen = new ArrayList<Object>();

        if (notYetChosenKeys.size() > 0) {

            List<Object> allToChoose = clf.getAllToChoose();
            Iterator<Object> iterator = allToChoose.iterator();
            while (iterator.hasNext()) {
                Object kodOrColumn = iterator.next();
                if (notYetChosenKeys.contains(String.valueOf(getId(kodOrColumn)))) {
                    notYetChosen.add(kodOrColumn);
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

        List<Object> toRemove = new ArrayList<Object>();

        if (chosenKeysToRemove.size() > 0) {

            List<Object> chosen = clf.getChosen();

            Iterator<Object> iterator = chosen.iterator();
            while (iterator.hasNext()) {
                Object next = iterator.next();
                if (chosenKeysToRemove.contains(String.valueOf(getId(next)))) {
                    toRemove.add(next);
                }
            }
                session.setAttribute("toRemoveKoder", toRemove);
        }

        response.setRenderParameter("view", "choose-from-list");
        response.setRenderParameter("prioId", prioId);
    }

    private Long getId(Object object) {
        if (object instanceof AbstractKod) {
            AbstractKod kod = (AbstractKod) object;
            return kod.getId();
        } else if (object instanceof Column) {
            return Long.valueOf(((Column) object).getId());
        } else {
            throw new IllegalArgumentException("Unexpected type [" + object.getClass() + "].");
        }
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

    @ActionMapping(params = {"action=doUserAction", "openId"})
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
    public void toggleOpenSectorNodeForUser(final PortletSession session,
                                            @RequestParam("openId") Integer openId,
                                            ActionResponse response,
                                            PortletRequest request)
            throws IOException {
        User otherUser = (User) session.getAttribute("otherUser");
        SektorRaad sector = getSectorById(openId, otherUser.getSektorRaad());
        sector.setOpen(!sector.isOpen());

        request.setAttribute("otherUser", session.getAttribute("otherUser"));
        response.setRenderParameter("view", "edit-user");
    }


    @ActionMapping(params = {"action=check", "openId"})
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
    public void toggleOpenSectorNodeForUser2(final PortletSession session,
                                             @RequestParam("openId") Integer openId,
                                             ActionResponse response,
                                             PortletRequest request)
            throws IOException {
        MainForm form = getMainForm(session);

        SektorRaad sector = getSectorById(openId, form.getSectors());
        sector.setOpen(!sector.isOpen());

        //request.setAttribute("otherUser", session.getAttribute("otherUser"));
        response.setRenderParameter("view", "main");
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

    @ResourceMapping("toExcel")
    public void excelTableInMainWindow(ResourceResponse response, PortletSession session) throws IOException {
        final OutputStream out = response.getPortletOutputStream();
        response.setContentType("text/csv");
        MainForm form = initState(session);

        out.write(
                se.vgregion.verticalprio.el.Util.toCsvTable(
                        form.getColumns(),
                        (List<Prioriteringsobjekt>) session.getAttribute("rows"),
                        (User) session.getAttribute("user")).getBytes("ISO-8859-1")
        );

        response.setContentType("application/excel;charset=ISO-8859-1");
        response.setProperty("Content-Disposition", "inline; filename=prioriteringar.csv");

        out.flush();
        out.close();
    }

}
