package se.vgregion.verticalprio.controllers;

import org.apache.commons.beanutils.BeanMap;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import se.vgregion.verticalprio.MainForm;
import se.vgregion.verticalprio.PrioriteringsobjektFindCondition;
import se.vgregion.verticalprio.entity.*;
import se.vgregion.verticalprio.repository.GenerisktHierarkisktKodRepository;
import se.vgregion.verticalprio.repository.GenerisktKodRepository;
import se.vgregion.verticalprio.repository.finding.HaveNestedEntities;

import javax.servlet.http.HttpSession;
import java.util.*;

public abstract class BaseController {
    protected List<Column> columns = getDefaultColumns();
    protected Map<String, ChooseCodesController.ChooseListFormWithDomainProperty> formPrototypes = new HashMap<String, ChooseCodesController.ChooseListFormWithDomainProperty>();

    /**
     * Constructor for the class. Initialize the formPrototypes member with objects. These objects,
     * {@link se.vgregion.verticalprio.controllers.ChooseCodesController.ChooseListFormWithDomainProperty}, are later used to provide text to the gui and to point to the
     * collections that should receive the result of the dialog.
     */
    protected BaseController() {
        ChooseCodesController.ChooseListFormWithDomainProperty symptomDiagnosTextForm = new ChooseCodesController.ChooseListFormWithDomainProperty();
        symptomDiagnosTextForm.setDisplayKey("kodPlusBeskrivning");
        symptomDiagnosTextForm.setIdKey("id");
        symptomDiagnosTextForm.setFilterLabel("Sök diagnos med nyckelord");
        symptomDiagnosTextForm.setFilterLabelToolTip("Här kan du söka både på kod och på beskrivning");
        symptomDiagnosTextForm.setNotYetChosenLabel("Diagnoskoder");
        symptomDiagnosTextForm.setChosenLabel("Valda diagnoser");
        symptomDiagnosTextForm.setOkLabel("Välj diagnoser");
        symptomDiagnosTextForm.setOkUrl("main");
        symptomDiagnosTextForm.setCancelUrl("main");
        symptomDiagnosTextForm.setAllItemsPropertyName("diagnoser");
        formPrototypes.put("diagnosTexts", symptomDiagnosTextForm);

        ChooseCodesController.ChooseListFormWithDomainProperty symptomDiagnosKodForm = symptomDiagnosTextForm.clone();
        symptomDiagnosKodForm.setDisplayKey("kod");
        formPrototypes.put("diagnosKodTexts", symptomDiagnosKodForm);

        ChooseCodesController.ChooseListFormWithDomainProperty aatgardsTextForm = symptomDiagnosTextForm.clone();
        aatgardsTextForm.setAllItemsPropertyName("aatgaerdskoder");
        aatgardsTextForm.setFilterLabel("Sök åtgärder med nyckelord");
        aatgardsTextForm.setNotYetChosenLabel("Åtgärdskoder");
        aatgardsTextForm.setChosenLabel("Valda åtgärder");
        aatgardsTextForm.setOkLabel("Välj åtgärder");
        aatgardsTextForm.setOkUrl("main");
        aatgardsTextForm.setCancelUrl("main");
        aatgardsTextForm.setAllItemsPropertyName("aatgaerdskoder");
        formPrototypes.put("aatgaerdskoderTexts", aatgardsTextForm);

        ChooseCodesController.ChooseListFormWithDomainProperty aatgardsKodTextForm = aatgardsTextForm.clone();
        aatgardsKodTextForm.setDisplayKey("kod");
        formPrototypes.put("aatgaerdskoder", aatgardsKodTextForm);

        ChooseCodesController.ChooseListFormWithDomainProperty rangordningsKod = symptomDiagnosTextForm.clone();
        rangordningsKod.setAllItemsPropertyName("rangordningsKod");
        rangordningsKod.setFilterLabel("Sök rangordning med nyckelord");
        rangordningsKod.setNotYetChosenLabel("Rangordningskoder");
        rangordningsKod.setChosenLabel("Valda rangordningar");
        rangordningsKod.setOkLabel("Välj rangordningskoder");
        rangordningsKod.setOkUrl("main");
        rangordningsKod.setCancelUrl("main");
        rangordningsKod.setAllItemsPropertyName("rangordningsKod");
        formPrototypes.put("rangordningsKod", rangordningsKod);

        ChooseCodesController.ChooseListFormWithDomainProperty atcTextForm = symptomDiagnosTextForm.clone();
        atcTextForm.setFilterLabel("Sök ATC-koder med nyckelord");
        atcTextForm.setNotYetChosenLabel("ATC-koder");
        atcTextForm.setChosenLabel("Valda ATC-koder");
        atcTextForm.setOkLabel("Välj ATC-texter");
        atcTextForm.setOkUrl("main");
        atcTextForm.setCancelUrl("main");
        atcTextForm.setAllItemsPropertyName("atcKoder");
        formPrototypes.put("atcText", atcTextForm);

        ChooseCodesController.ChooseListFormWithDomainProperty atcKodForm = atcTextForm.clone();
        atcKodForm.setDisplayKey("kod");
        formPrototypes.put("atcKoder", atcKodForm);

        ChooseCodesController.ChooseListFormWithDomainProperty vaardnivaa = symptomDiagnosTextForm.clone();
        vaardnivaa.setFilterLabel("Sök vårdnivå med nyckelord");
        vaardnivaa.setNotYetChosenLabel("Vårdnivåer");
        vaardnivaa.setChosenLabel("Valda nivåer");
        vaardnivaa.setOkLabel("Välj nivåer");
        vaardnivaa.setOkUrl("main");
        vaardnivaa.setCancelUrl("main");
        vaardnivaa.setAllItemsPropertyName("vaardnivaaKod");
        formPrototypes.put("vaardnivaaKod", vaardnivaa);

        ChooseCodesController.ChooseListFormWithDomainProperty vaardform = symptomDiagnosTextForm.clone();
        vaardform.setFilterLabel("Sök vårdformer med nyckelord");
        vaardform.setNotYetChosenLabel("Vårdformer");
        vaardform.setChosenLabel("Valda vårdformer");
        vaardform.setOkLabel("Välj vårdformer");
        vaardform.setOkUrl("main");
        vaardform.setCancelUrl("main");
        vaardform.setAllItemsPropertyName("vaardform");
        formPrototypes.put("vaardform", vaardform);

        ChooseCodesController.ChooseListFormWithDomainProperty tillstaandetsSvaarighetsgrad = new ChooseCodesController.ChooseListFormWithDomainProperty();
        tillstaandetsSvaarighetsgrad.setFilterLabel("Sök tillståndets svårighetsgrad med nyckelord");
        tillstaandetsSvaarighetsgrad.setNotYetChosenLabel("Svårighetsgrader");
        tillstaandetsSvaarighetsgrad.setChosenLabel("Valda svårighetsgrader");
        tillstaandetsSvaarighetsgrad.setOkLabel("Välj svårighetsgrader");
        tillstaandetsSvaarighetsgrad.setOkUrl("main");
        tillstaandetsSvaarighetsgrad.setIdKey("id");
        tillstaandetsSvaarighetsgrad.setCancelUrl("main");
        tillstaandetsSvaarighetsgrad.setDisplayKey("kortBeskrivning");
        tillstaandetsSvaarighetsgrad.setAllItemsPropertyName("tillstaandetsSvaarighetsgradKod");
        formPrototypes.put("tillstaandetsSvaarighetsgradKod", tillstaandetsSvaarighetsgrad);

    }

    /**
     * Loops through a collection and returns the sector with the corresponding id value provided.
     *
     * @param id
     * @param sectors
     * @return The matched {@link se.vgregion.verticalprio.entity.SektorRaad} or null if no such match bould be made.
     */
    protected SektorRaad getSectorById(int id, List<SektorRaad> sectors) {
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
	 * Returns all nodes from a list of {@link se.vgregion.verticalprio.entity.SektorRaad} that have the 'selected' property set to true. And also
	 * includes all the nodes beneath those.
	 *
	 * @param raads
	 * @return
	 */
	protected List<SektorRaad> getMarkedLeafs(List<SektorRaad> raads) {
		List<SektorRaad> result = new ArrayList<SektorRaad>();
		if (raads == null) {
			return result;
		}
		for (SektorRaad raad : raads) {
			List<SektorRaad> markedChildren = getMarkedLeafs(raad.getChildren());

			if (raad.isSelected()) {
				result.add(raad);
			}
			result.addAll(markedChildren);
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
    protected List<SektorRaad> flatten(List<SektorRaad> raads) {
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
     * Takes a list of {@link se.vgregion.verticalprio.entity.SektorRaad} and makes a copy that only contains the id-property of the object. Reason
     * for this is to get objects that when used as condition in the {@link se.vgregion.verticalprio.repository.finding.JpqlMatchBuilder} only generates
     * constraints on the id. e.g. id = ? instead of (id = ? and kod = ? and beskrivning = ? and....).
     *
     * TODO: Look to see if this method could be removed. Since its creation the {@link se.vgregion.verticalprio.repository.finding.JpqlMatchBuilder} might be
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
     * If you have a {@link se.vgregion.verticalprio.entity.SektorRaad} and want its root node, this method gives you that.
     *
     * @param all
     *            All existing sectors. The Method search through these to find the root.
     * @param toFind
     * @return
     */
    protected SektorRaad findRoot(List<SektorRaad> all, SektorRaad toFind) {
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

    /**
     * Getter för columns attribute. Initializes the list if not already ready.
     *
     * @return List of columns described in the property file /column-texts.properties.
     */
    public List<Column> getColumns() {

        List<Column> clones = new ArrayList<Column>();

        for (Column column : columns) {
            BeanMap bm = new BeanMap(column);
            Column newColumn = new Column();
            BeanMap nBm = new BeanMap(newColumn);
            nBm.putAllWriteable(bm);
            clones.add(newColumn);
        }

        return clones;

        // return columns;

        // if (columns == null) {
        // Map<String, String> ppt = getPrioPropertyTexts();
        // int count = 0;
        // List<Column> result = new ArrayList<Column>(ppt.size());
        // for (String key : new TreeSet<String>(ppt.keySet())) {
        // Column column = new Column();
        // column.setName(key.substring(4));
        // column.setLabel(ppt.get(key));
        // column.setId(count++);
        // result.add(column);
        // }
        // columns = result;
        // }
        // return columns;
    }

    private List<Column> getDefaultColumns() {
        List<Column> columns = Prioriteringsobjekt.getDefaultColumns();

        // Map<String, Column> map = new HashMap<String, Column>();
        // for (Column column : columns) {
        // map.put(column.getName(), column);
        // }
        //
        // addHtmlLinkToColumnLabel(map, "diagnosTexts", "choose-codes-init?codeRefName=diagnosRef");
        // addHtmlLinkToColumnLabel(map, "aatgaerdskoder", "choose-codes-init?codeRefName=aatgaerdRef");
        // addHtmlLinkToColumnLabel(map, "atcKoder", "choose-codes-init?codeRefName=atcKoderRef");
        // addHtmlLinkToColumnLabel(map, "vaardformskoder", "choose-codes-init?codeRefName=vaardformskoderRef");
        // addHtmlLinkToColumnLabel(map, "rangordningsKod", "choose-codes-init?codeRefName=rangordningsRef");
        // addHtmlLinkToColumnLabel(map, "tillstaandetsSvaarighetsgradKod",
        // "choose-codes-init?codeRefName=tillstaandetsSvaarighetsgradRef");

        return columns;
    }



    protected void initPrio(Prioriteringsobjekt form) {
        form.getDiagnoser().toArray(); // Are not eager so we have to make sure they are
        form.getAatgaerdskoder().toArray(); // loaded before sending them to the jsp-layer.
        form.getAtcKoder().toArray();
        if (form.getChildren() != null && !form.getChildren().isEmpty()) {
            for (Prioriteringsobjekt child : form.getChildren()) {
                initPrio(child);
            }
        }
    }

    @Transactional
    protected void init(Collection<SektorRaad> raads) {
		if (raads != null) {
			for (SektorRaad raad : raads) {
				init(raad.getChildren());
			}
		}
	}

    protected List<SektorRaadBean> toRaads(List<String> id, List<String> parentId, List<String> kod,
                                         List<String> markedAsDeleted, List<String> prioCount, List<String> locked) {
        List<SektorRaadBean> result = new ArrayList<SektorRaadBean>();

        int c = 0;
        for (String itemId : id) {
            SektorRaadBean item = new SektorRaadBean();
            item.setId(toLongOrNull(itemId));
            item.setPrioCount(Integer.parseInt(prioCount.get(c)));
            item.setParentId(toLongOrNull(parentId.get(c)));
            // item.setKortBeskrivning(kortBeskrivning.get(c));
            // item.setBeskrivning(beskrivning.get(c));
            item.setKod(kod.get(c));
            item.setMarkedAsDeleted("true".equals(markedAsDeleted.get(c)));
            item.setLocked("true".equals(locked.get(c)));
            c++;
            result.add(item);
        }

        result = arrangeFlatDataAccordingToParentChildValues(result);

        return result;
    }


    protected List<SektorRaadBean> arrangeFlatDataAccordingToParentChildValues(List<SektorRaadBean> data) {
        List<SektorRaadBean> result = arrangeFlatDataAccordingToParentChildValues(null, data);
        data.removeAll(result);

        for (SektorRaadBean sr : result) {
            sr.setBeanChildren(arrangeFlatDataAccordingToParentChildValues(sr.getId(), data));
            data.removeAll(sr.getBeanChildren());
        }

        return result;
    }

    protected List<SektorRaadBean> arrangeFlatDataAccordingToParentChildValues(Long parentId,
                                                                             List<SektorRaadBean> data) {
        List<SektorRaadBean> result = new ArrayList<SektorRaadBean>();

        for (SektorRaadBean sr : data) {
            if (equals(parentId, sr.getParentId())) {
                result.add(sr);
                if (sr.getId() != null) {
                    sr.setBeanChildren(arrangeFlatDataAccordingToParentChildValues(sr.getId(), data));
                }
            }
        }

        return result;
    }

    protected Long toLongOrNull(String s) {
        if (s == null || s.isEmpty()) {
            return null;
        }
        return Long.parseLong(s);
    }

    protected boolean equals(Long l1, Long l2) {
        if (l1 == l2) {
            return true;
        }
        if (l1 == null || l2 == null) {
            return false;
        }
        return l1.equals(l2);
    }


    @Transactional
    protected void removeFromList(List<SektorRaadBean> beans) {
        for (SektorRaadBean child : new ArrayList<SektorRaadBean>(beans)) {
            removeFromList(child.getBeanChildren());
            if (child.isMarkedAsDeleted()) {
                SektorRaad sr = SektorRaadBean.toSektorRaad(child);
                if (sr.getId() > 0) {
                    // An id less than 0 signals that the object is not saved earlier. No actual operation against
                    // the db is then needed.
                    // sektorRaadRepository.remove(sr.getId());
                }
                beans.remove(child);
            }
        }
    }

    @Transactional
    protected void store(GenerisktHierarkisktKodRepository<SektorRaad> sektorRaadRepository, GenerisktKodRepository<User> userRepository, List<SektorRaadBean> beans, User user) {
        List<SektorRaad> persistentData = sektorRaadRepository.getTreeRoots();
        List<SektorRaad> source = SektorRaadBean.toSektorRaads(beans);
        applyChange(sektorRaadRepository, userRepository, source, persistentData, user);

        for (SektorRaad sr : persistentData) {
            sektorRaadRepository.merge(sr);
        }

        sektorRaadRepository.flush();
    }


    @Transactional
    protected void applyChange(GenerisktHierarkisktKodRepository<SektorRaad> sektorRaadRepository, GenerisktKodRepository<User> userRepository, List<SektorRaad> sources, List<SektorRaad> targets, User user) {
        Set<Long> ids = new HashSet<Long>();
        for (SektorRaad sr : sources) {
            if (sr.getId() > 0) {
                ids.add(sr.getId());
            } else {
                sr.setId(null);
                targets.add(sr);
                sr.setUsers(new HashSet<User>());
                sr.getUsers().add(user);
                sr = sektorRaadRepository.persist(sr);
                sektorRaadRepository.flush();
                sr = sektorRaadRepository.find(sr.getId());
                user.getSektorRaad().add(sr);
                user = userRepository.merge(user);
                userRepository.flush();
                //session.setAttribute("user", user);
                ids.add(sr.getId());
            }
        }

        for (SektorRaad target : new ArrayList<SektorRaad>(targets)) {
            if (target.getId() != null && !ids.contains(target.getId())) {
                if (target.getUsers() != null) {
                    target.getUsers().clear();

                    // If users have this assigned - remove those first.
                    for (User targetUser : target.getUsers()) {
                        targetUser.getSektorRaad().remove(target);
                        userRepository.merge(targetUser);
                        userRepository.flush();
                    }
                }

                targets.remove(target);
                sektorRaadRepository.remove(target.getId());
            } else {
                applyChange(sektorRaadRepository, userRepository, find(sources, target.getId()), target, user);
            }
        }

    }

    @Transactional
    protected SektorRaad find(Collection<SektorRaad> collection, Long id) {
        for (SektorRaad sr : collection) {
            if (id.equals(sr.getId())) {
                return sr;
            }
        }
        return null;
    }

    @Transactional
    protected void applyChange(GenerisktHierarkisktKodRepository<SektorRaad> sektorRaadRepository, GenerisktKodRepository<User> userRepository, SektorRaad source, SektorRaad target, User user) {
        target.setKod(source.getKod());
        applyChange(sektorRaadRepository, userRepository, source.getChildren(), target.getChildren(), user);
    }

    protected void markToDeleteWhenSave(Long id, List<SektorRaadBean> sectors) {
        int c = 0;
        for (SektorRaadBean sr : new ArrayList<SektorRaadBean>(sectors)) {
            if (sr.getBeanChildren() != null) {
                markToDeleteWhenSave(id, sr.getBeanChildren());
            }
            if (equals(id, sr.getId())) {
                if (id < 0) {
                    sectors.remove(c);
                } else {
                    sr.setMarkedAsDeleted(!sr.isMarkedAsDeleted());
                }
                return;
            }
            c++;
        }
    }

    /**
     * Method to validate that the user really have the right to use this controllers functionality.
     *
     * @param session
     */
    protected void checkSecurity(HttpSession session) {
        User activeUser = (User) session.getAttribute("user");
        if (!activeUser.getUserEditor()) {
            throw new RuntimeException();
        }
    }

    protected void copyKodCollectionsAndMetaDates(Prioriteringsobjekt source, Prioriteringsobjekt target) {
        if (source == target) {
            return;
        }
        clearAndFillCollection(source.getAatgaerdskoder(), target.getAatgaerdskoder());
        clearAndFillCollection(source.getDiagnoser(), target.getDiagnoser());
        clearAndFillCollection(source.getAtcKoder(), target.getAtcKoder());
        clearAndFillCollection(source.getChildren(), target.getChildren());

        if (source instanceof PrioriteringsobjektForm && target instanceof PrioriteringsobjektForm) {
            PrioriteringsobjektForm sourceForm = (PrioriteringsobjektForm) source;
            PrioriteringsobjektForm targetForm = (PrioriteringsobjektForm) target;

            targetForm.setColumns(sourceForm.getColumns());

            targetForm.setKostnadLevnadsaarKodList(new ArrayList<KostnadLevnadsaarKod>());
            targetForm.setAatgaerdsRiskKodList(new ArrayList<AatgaerdsRiskKod>());
            targetForm.setHaelsonekonomiskEvidensKodList(new ArrayList<HaelsonekonomiskEvidensKod>());
            targetForm.setPatientnyttaEffektAatgaerdsKodList(new ArrayList<PatientnyttaEffektAatgaerdsKod>());
            targetForm.setRangordningsKodList(new ArrayList<RangordningsKod>());
            targetForm.setVaardnivaaKodList(new ArrayList<VaardnivaaKod>());
            targetForm.setTillstaandetsSvaarighetsgradKodList(new ArrayList<TillstaandetsSvaarighetsgradKod>());

            targetForm.setVaardformList(sourceForm.getVaardformList());
            targetForm.setSektorRaadList(sourceForm.getSektorRaadList());
            targetForm.setVaentetidBehandlingVeckorList(sourceForm.getVaentetidBehandlingVeckorList());
            targetForm.setVaentetidBesookVeckorList(sourceForm.getVaentetidBesookVeckorList());
            targetForm.setDiagnoserList(sourceForm.getDiagnoserList());

            targetForm.setPatientnyttoEvidensKodList(sourceForm.getPatientnyttoEvidensKodList());

            clearAndFillCollection(sourceForm.getKostnadLevnadsaarKodList(), targetForm.getKostnadLevnadsaarKodList());
            clearAndFillCollection(sourceForm.getAatgaerdsRiskKodList(), targetForm.getAatgaerdsRiskKodList());
            clearAndFillCollection(sourceForm.getHaelsonekonomiskEvidensKodList(), targetForm.getHaelsonekonomiskEvidensKodList());
            clearAndFillCollection(sourceForm.getPatientnyttaEffektAatgaerdsKodList(), targetForm.getPatientnyttaEffektAatgaerdsKodList());
            clearAndFillCollection(sourceForm.getRangordningsKodList(), targetForm.getRangordningsKodList());
            clearAndFillCollection(sourceForm.getVaardnivaaKodList(), targetForm.getVaardnivaaKodList());
            clearAndFillCollection(sourceForm.getTillstaandetsSvaarighetsgradKodList(), targetForm.getTillstaandetsSvaarighetsgradKodList());

        }

        target.setGodkaend(source.getGodkaend());
        target.setSenastUppdaterad(source.getSenastUppdaterad());
    }

    protected  <T extends Object> void clearAndFillCollection(Collection<T> source, Collection<T> target) {
        if (source == null || target == null) {
            return;
        }
        if (source == target) {
            return;
        }
        target.clear();
        target.addAll(source);
    }

    protected ChooseFromListController.ChooseListForm initChooseListForm() {
        ChooseFromListController.ChooseListForm clf = new ChooseFromListController.ChooseListForm();
        clf.setFilterLabel("Sök diagnoser med nyckelord");
        clf.setNotYetChosenLabel("Ej valda diagnoser");
        clf.setChosenLabel("Valda diagnoser");
        clf.setFilterLabelToolTip("Här kan du söka både på kod och på beskrivning");
        clf.setType(DiagnosKod.class);
        return clf;
    }

    /**
     * Utility method to alter the SektorRaad objects in the User ready to be desplayed. It places all of the items
     * that are available in the User (regardsless if the user have them) and marks the ones that the user have
     * access to by setting the selected property in each SektorRaad.
     *
     * @param user
     */
    @Transactional(propagation = Propagation.REQUIRED)
    protected void initSectors(User user, GenerisktHierarkisktKodRepository<SektorRaad> sektorRaadRepository) {
        List<SektorRaad> allRaads = sektorRaadRepository.getTreeRoots();
        markAllAsSelected(allRaads, user.getSektorRaad());
        user.setSektorRaad(allRaads);
    }

    /**
     * Sets the property 'selected' to true in the first parameter list if the item is inside the second parameter
     * list.
     *
     * @param targets
     * @param flat
     */
    protected void markAllAsSelected(List<SektorRaad> targets, List<SektorRaad> flat) {
        if (targets != null) {
            for (SektorRaad target : targets) {
                if (flat.contains(target)) {
                    target.setSelected(true);
                } else {
                    target.setSelected(false);
                }
                markAllAsSelected(target.getChildren(), flat);
            }
        }
    }

    /**
     * Merges the 'deeper data' (ie the SektorRaad:s) into a user that have been received from the client. It also
     * puts the data into the session to be used the nedt time this operation have to be made.
     *
     * @param postedUser
     * @param session
     */
    protected void mirrorUserInSession(User postedUser, HttpSession session) {
        final String sessionKey = "otherUser";
        User userInSession = (User) session.getAttribute(sessionKey);
        if (userInSession == null) {
            session.setAttribute(sessionKey, postedUser);
            return;
        }
        postedUser.setSektorRaad(userInSession.getSektorRaad());

        if (postedUser.getApprover() == null) {
            postedUser.setApprover(false);
        }
        if (postedUser.getEditor() == null) {
            postedUser.setEditor(false);
        }
        if (postedUser.getUserEditor() == null) {
            postedUser.setUserEditor(false);
        }
        session.setAttribute(sessionKey, postedUser);
    }

    protected void check(Long sectorId, Collection<SektorRaad> raads) {
        for (SektorRaad raad : raads) {
            if (raad.getId().equals(sectorId)) {
                raad.setSelected(!raad.isSelected());
                return;
            }
            check(sectorId, raad.getChildren());
        }
    }

    /**
     * Checks the 'selected' property of the provided list and puts all with the value true in the return. Is used
     * to 'flatten' the tree-structured data of the provided SektorRaad List.
     *
     * @param source
     * @return
     */
    protected List<SektorRaad> flattenSelected(List<SektorRaad> source) {
        List<SektorRaad> result = new ArrayList<SektorRaad>();
        flattenSelected(source, result);
        return result;
    }

    protected void flattenSelected(List<SektorRaad> source, List<SektorRaad> target) {
        for (SektorRaad item : source) {
            if (item.isSelected()) {
                target.add(item);
            }
            flattenSelected(item.getChildren(), target);
        }
    }

    protected void markColumnAsSorting(String fieldName, MainForm mf) {
        for (Column column : mf.getColumns()) {
            column.setSorting(fieldName.equals(column.getName()));
        }
    }

    /**
     * Looks through the result list in the session for all existing codes of a certain type (residing in a
     * collection or in a reference variable).
     *
     * This would be the total set of items to choose for the user. Since the search always should narrow down in
     * findings.
     *
     * @param pfcs
     * @param allItemsPropertyName
     * @param sortProperty
     * @return
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    protected List<?> extractChildObjects(List<Prioriteringsobjekt> pfcs, String allItemsPropertyName,
                                          String sortProperty) {
//        SortedMap<Object, Object> tm = new TreeMap<Object, Object>();
        List<Object> values = new ArrayList<Object>();
        for (Prioriteringsobjekt prio : pfcs) {
            BeanMap prioMap = new BeanMap(prio);
            Object value = prioMap.get(allItemsPropertyName);
            if (value == null) {
                continue;
            }
            if (value instanceof Collection) {
                Collection<Object> col = (Collection<Object>) value;
                values.removeAll(col); // To avoid duplicates
                values.addAll(col);
            } else {
                if (!values.contains(value)) {
                    values.add(value);
                }
            }
        }
        return values;
    }

    /**
     * The condition should have a HaveNestedEntities object inside the PFC objects property with the name
     * propertyName. Or a collection with that interface or a single item wit that interface... The method returns
     * the result of its content().
     *
     * @param condition
     * @param propertyName
     * @return
     */
    protected Collection extractTargetCollection(PrioriteringsobjektFindCondition condition, String propertyName) {
        BeanMap bm = new BeanMap(condition);
        Object uncastHne = bm.get(propertyName);
        if (uncastHne != null && !(uncastHne instanceof HaveNestedEntities)) {
            System.out.println("Den här har ju inte rätt klass\n " + uncastHne.getClass());
        }
        HaveNestedEntities hne = (HaveNestedEntities) uncastHne;
        return hne.content();
    }
}
