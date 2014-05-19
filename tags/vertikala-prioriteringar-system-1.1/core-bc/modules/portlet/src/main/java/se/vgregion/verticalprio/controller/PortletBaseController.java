package se.vgregion.verticalprio.controller;

import org.apache.commons.beanutils.BeanMap;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import se.vgregion.verticalprio.ApplicationData;
import se.vgregion.verticalprio.MainForm;
import se.vgregion.verticalprio.PrioriteringsobjektFindCondition;
import se.vgregion.verticalprio.controllers.BaseController;
import se.vgregion.verticalprio.controllers.MessageHome;
import se.vgregion.verticalprio.controllers.PrioriteringsobjektForm;
import se.vgregion.verticalprio.controllers.SektorRaadBean;
import se.vgregion.verticalprio.entity.Column;
import se.vgregion.verticalprio.entity.Prioriteringsobjekt;
import se.vgregion.verticalprio.entity.SektorRaad;
import se.vgregion.verticalprio.entity.User;
import se.vgregion.verticalprio.repository.GenerisktHierarkisktKodRepository;
import se.vgregion.verticalprio.repository.PrioRepository;
import se.vgregion.verticalprio.repository.finding.HaveNestedEntities;
import se.vgregion.verticalprio.repository.finding.JpqlMatchBuilder;
import se.vgregion.verticalprio.repository.finding.NestedSektorRaad;

import javax.portlet.PortletRequest;
import javax.portlet.PortletSession;
import javax.servlet.http.HttpSession;
import java.util.*;

public abstract class PortletBaseController extends BaseController {

    long idForUnsavedNewPosts = -1;

    public PortletBaseController() {
    }

    protected boolean isBlank(String s) {
        if (s == null) {
            return true;
        }
        if ("".equals(s.trim())) {
            return true;
        }
        return false;
    }

    protected <T> T getOrCreateSessionObj(PortletSession session, String name, Class<T> clazz) {
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

    /**
     * Produces the result list in the main view of the application. It uses a
     * {@link se.vgregion.verticalprio.PrioriteringsobjektFindCondition} as search condition - this object is stored in the session.
     *
     * @param session
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<Prioriteringsobjekt> result(PortletSession session) {
        PrioriteringsobjektFindCondition condition = getOrCreateSessionObj(session, "prioCondition",
                PrioriteringsobjektFindCondition.class);
        MainForm mf = getMainForm(session);

        if (mf.getAllSektorsRaad().isSelected()) {
            if (condition.getSektorRaad() != null) {
                // user selected to show all Prios regardless of SR.
                // Remove all conditions that specifies specific SRs, except those that should indicate order by
                // directive.
                HaveNestedEntities<SektorRaad> hne = condition.getSektorRaad();
                // clearNonSortingLogic(hne);
                hne.content().clear();
            }
        } else {
            List<SektorRaad> raad = getMarkedLeafs(mf.getSectors());
            raad = flatten(raad);

            NestedSektorRaad sektorNest = condition.getSektorRaad();

            // Find out if there are selected sectors, taking regards to that there might be HaveSortOrder-objects
            // inside.
            sektorNest.content().clear();
            if (sektorNest != null && sektorNest.content() != null) {
                sektorNest.content().addAll(raad);
            }

            if (raad.isEmpty()) {
                List<Prioriteringsobjekt> zeroResult = new ArrayList<Prioriteringsobjekt>();
                session.setAttribute("rows", zeroResult);
                return zeroResult;
            }
        }

        List<Prioriteringsobjekt> result = new ArrayList<Prioriteringsobjekt>();
        result.addAll(getPrioRepository().findLargeResult(condition));
        List<SektorRaad> sectors = getApplicationData().getSektorRaadList();

        for (Prioriteringsobjekt prio : result) {
            prio.setSektorRaad(findRoot(sectors, prio.getSektorRaad()));
        }

        session.setAttribute("rows", result);
        return result;
    }

    protected MainForm getMainForm(PortletSession session) {
        MainForm form = getOrCreateSessionObj(session, "form", MainForm.class);
        initMainForm(form, session);
        return form;
    }

    protected void initMainForm(MainForm form, PortletSession session) {
        if (form.getSectors().isEmpty()) {
            form.getSectors().addAll(getSectors(session));
        }

        if (form.getColumns().isEmpty()) {
            form.getColumns().addAll(Prioriteringsobjekt.getDefaultColumns());
        }
    }

    @SuppressWarnings("unchecked")
    @Transactional
    private List<SektorRaad> getSectors(PortletSession session) {
        final String sectorsKey = "sectors";
        Collection<SektorRaad> sectorCache = (Collection<SektorRaad>) session.getAttribute(sectorsKey);
        if (sectorCache == null) {
            List<SektorRaad> raads = getSektorRaadRepository().getTreeRoots();
            sectorCache = new ArrayList<SektorRaad>();
            session.setAttribute(sectorsKey, sectorCache);
            for (SektorRaad sr : raads) {
                sectorCache.add(sr.clone());
            }
        }
        return new ArrayList<SektorRaad>(sectorCache);
    }

    protected void initKodLists(PrioriteringsobjektForm pf) {
        getApplicationData().initKodLists(pf);
        for (Column column : getColumns()) {
            pf.getColumns().put(column.getName(), column);
        }
    }

    protected abstract GenerisktHierarkisktKodRepository getSektorRaadRepository();

    protected abstract ApplicationData getApplicationData();

    protected abstract PrioRepository getPrioRepository();



    /*@SuppressWarnings("unchecked")
    @Transactional
    protected List<SektorRaad> getSectors(HttpSession session) {
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
*/

    protected void insertNewSektorIntoTree(List<SektorRaadBean> sectors, Long id) {
        if (sectors == null) {
            return;
        }
        for (SektorRaadBean sr : sectors) {
            if (equals(id, sr.getId())) {
                if (sr.getBeanChildren() == null) {
                    sr.setBeanChildren(new ArrayList<SektorRaadBean>());
                }
                SektorRaadBean newRaad = new SektorRaadBean();
                newRaad.setParent(sr);
                newRaad.setParentId(id);
                newRaad.setId(idForUnsavedNewPosts--);
                sr.getBeanChildren().add(newRaad);
                return;
            }
            insertNewSektorIntoTree(sr.getBeanChildren(), id);
        }
    }

    protected Prioriteringsobjekt toPrioriteringsobjekt(PortletRequest request, PrioriteringsobjektForm pf,
                                                      PortletSession session) {
        initKodLists(pf);
        pf.asignCodesFromTheListsByCorrespondingIdAttributes();
        Prioriteringsobjekt prio;
        if (pf.getId() == null) {
            prio = new Prioriteringsobjekt();
        } else {
            prio = getPrioRepository().find(pf.getId());
        }

        PrioriteringsobjektForm sessionPrio = (PrioriteringsobjektForm) session.getAttribute("prio");
        copyKodCollectionsAndMetaDates(sessionPrio, prio);
        session.setAttribute("prio", pf);
        pf.setUnalteredVersion(sessionPrio.getUnalteredVersion());

        BeanMap prioMap = new BeanMap(prio);
        BeanMap formMap = new BeanMap(pf);
        prioMap.putAllWriteable(formMap);

        initKodLists(pf);

        return prio;
    }

    protected boolean isUserInSektorsRaadIfNotWarnWithMessage(User user, Prioriteringsobjekt prio,
                                                            PortletSession session) {

        if (user.getSektorRaad().contains(prio.getSektorRaad())) {
            return true;
        } else {
            String message = "Du saknar behörighet att utföra denna åtgärd på prioriteringsobjektet som tillhör Sektorsråd: ";
            SektorRaad sr = prio.getSektorRaad();
            if (sr == null) {
                return true;
            }
            if (sr.getParent() != null) {
                message += sr.getParent().getLabel() + " (" + sr.getLabel() + ") <br/>";
            } else {
                message += prio.getSektorRaad().getLabel() + ". <br/>";
            }

            if (!user.getSektorRaad().isEmpty()) {
                message += "<br>" + "Du är idag definierad inom följande Sektorsråd: ";
                StringBuilder buf = new StringBuilder();
                SortedSet<String> codeTexts = new TreeSet<String>();

                for (SektorRaad sektorsRaad : user.getSektorRaad()) {
                    if (sektorsRaad.getParent() == null) {
                        StringBuilder sb = new StringBuilder();
                        sb.append(sektorsRaad.getKod());
                        if (!sektorsRaad.getChildren().isEmpty()) {
                            List<String> childNames = new ArrayList<String>();
                            for (SektorRaad child : sektorsRaad.getChildren()) {
                                if (user.getSektorRaad().contains(child)) {
                                    childNames.add(child.getKod());
                                }
                            }
                            if (!childNames.isEmpty()) {
                                sb.append(" (");
                                sb.append(JpqlMatchBuilder.toString(childNames, ", "));
                                sb.append(")");
                            }
                        }
                        codeTexts.add(sb.toString());
                    }
                }

                for (String code : codeTexts) {
                    buf.append("<br/>-&nbsp;").append(code);
                }
                message += buf;
            } else {
                message += "Din användare är för närvarande inte medlem i något sektorsråd själv.";
            }

            MessageHome messageHome = getOrCreateSessionObj(session, "messageHome", MessageHome.class);
            messageHome.setMessage(message);
            return false;
        }
    }

    /**
     * @param session
     * @param id
     * @return
     */
    protected boolean validateIdIsSelected(PortletSession session, Long id) {
        if (id == null) {
            // No id has been selected
            String message = "Var vänlig och markera det prioriteringsobjekt du vill arbeta med innan du trycker på knappen.";
            MessageHome messageHome = getOrCreateSessionObj(session, "messageHome", MessageHome.class);
            messageHome.setMessage(message);
            return false;
        } else {
            return true;
        }
    }

}
