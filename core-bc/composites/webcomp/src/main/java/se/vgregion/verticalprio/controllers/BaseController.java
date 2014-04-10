package se.vgregion.verticalprio.controllers;

import org.apache.commons.beanutils.BeanMap;
import org.springframework.transaction.annotation.Transactional;
import se.vgregion.verticalprio.entity.Column;
import se.vgregion.verticalprio.entity.Prioriteringsobjekt;
import se.vgregion.verticalprio.entity.SektorRaad;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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


}
