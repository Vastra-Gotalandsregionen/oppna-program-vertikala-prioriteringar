package se.vgregion.verticalprio.controllers;

import se.vgregion.verticalprio.entity.SektorRaad;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Patrik Bergström
 */
public class BaseController {
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
}
