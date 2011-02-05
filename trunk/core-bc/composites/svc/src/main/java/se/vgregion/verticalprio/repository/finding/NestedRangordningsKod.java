package se.vgregion.verticalprio.repository.finding;

import java.util.HashSet;
import java.util.Set;

import se.vgregion.verticalprio.entity.RangordningsKod;

/**
 * @author Claes Lundahl, vgrid=clalu4
 * 
 */
public class NestedRangordningsKod extends RangordningsKod implements HaveNestedEntities<RangordningsKod> {

    private Set<RangordningsKod> nestedContent = new HashSet<RangordningsKod>();

    /**
     * @inheritDoc
     */
    @Override
    public Set<RangordningsKod> content() {
        return nestedContent;
    }

    /**
     * @return the nestedContent
     */
    public Set<RangordningsKod> getNestedContent() {
        return nestedContent;
    }

    /**
     * @param nestedContent
     *            the nestedContent to set
     */
    public void setNestedContent(Set<RangordningsKod> nestedContent) {
        this.nestedContent = nestedContent;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (RangordningsKod rk : nestedContent) {
            sb.append(rk.getKod() + ", ");
        }
        if (sb.length() > 0) {
            sb.delete(sb.length() - 2, sb.length());
        }
        return sb.toString();

    }

}
