package se.vgregion.verticalprio.repository.finding;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import se.vgregion.verticalprio.entity.SektorRaad;

/**
 * @author Claes Lundahl, vgrid=clalu4
 * 
 */
@SuppressWarnings("serial")
public class NestedSektorRaad extends SektorRaad implements HaveNestedEntities<SektorRaad> {

    private Set<SektorRaad> sektors = new HashSet<SektorRaad>();

    public NestedSektorRaad() {
    }

    public NestedSektorRaad(Collection<SektorRaad> raad) {
        sektors.addAll(raad);
    }

    /**
     * @inheritDoc
     */
    @Override
    public Set<SektorRaad> content() {
        return sektors;
    }

    @Override
    public String toString() {
        // StringBuilder sb = new StringBuilder();
        // for (SektorRaad sr : sektors) {
        // sb.append(sr.getKod() + ", ");
        // }
        // if (sb.length() > 0) {
        // sb.delete(sb.length() - 2, sb.length());
        // }
        // return sb.toString();
        return sektors.toString();
    }
}
