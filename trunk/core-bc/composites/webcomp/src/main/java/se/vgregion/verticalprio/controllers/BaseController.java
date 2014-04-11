package se.vgregion.verticalprio.controllers;

import org.apache.commons.beanutils.BeanMap;
import org.springframework.transaction.annotation.Transactional;
import se.vgregion.verticalprio.entity.Column;
import se.vgregion.verticalprio.entity.Prioriteringsobjekt;
import se.vgregion.verticalprio.entity.SektorRaad;
import se.vgregion.verticalprio.entity.User;
import se.vgregion.verticalprio.repository.GenerisktHierarkisktKodRepository;
import se.vgregion.verticalprio.repository.GenerisktKodRepository;
import se.vgregion.verticalprio.repository.finding.HaveExplicitTypeToFind;
import se.vgregion.verticalprio.repository.finding.HaveOrderByPaths;
import se.vgregion.verticalprio.repository.finding.OrderByPath;

import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * @author Patrik Bergström
 */
public abstract class BaseController {
    protected List<Column> columns = getDefaultColumns();

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



}
