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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import se.vgregion.verticalprio.ApplicationData;
import se.vgregion.verticalprio.ConfColumnsForm;
import se.vgregion.verticalprio.MainForm;
import se.vgregion.verticalprio.PrioriteringsobjektFindCondition;
import se.vgregion.verticalprio.entity.SektorRaad;
import se.vgregion.verticalprio.entity.User;
import se.vgregion.verticalprio.repository.GenerisktHierarkisktKodRepository;
import se.vgregion.verticalprio.repository.GenerisktKodRepository;
import se.vgregion.verticalprio.repository.PrioRepository;
import se.vgregion.verticalprio.repository.finding.DateNullLogic;

import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.PortletSession;
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
        result(request.getPortletSession());

        Map<String, Object> attributeMap = request.getPortletSession().getAttributeMap();

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
