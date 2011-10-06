package se.vgregion.verticalprio.repository.finding;

import java.util.HashSet;
import java.util.Set;

import se.vgregion.verticalprio.entity.VaardformsKod;

/**
 * @author Claes Lundahl, vgrid=clalu4
 * 
 */
public class NestedVaardformsKod extends VaardformsKod implements HaveNestedEntities<VaardformsKod> {

    private Set<VaardformsKod> nestedContent = new HashSet<VaardformsKod>();

    /**
     * @inheritDoc
     */
    @Override
    public Set<VaardformsKod> content() {
        return nestedContent;
    }

    /**
     * @return the nestedContent
     */
    public Set<VaardformsKod> getNestedContent() {
        return nestedContent;
    }

    /**
     * @param nestedContent
     *            the nestedContent to set
     */
    public void setNestedContent(Set<VaardformsKod> nestedContent) {
        this.nestedContent = nestedContent;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (VaardformsKod rk : nestedContent) {
            sb.append(rk.getKod() + ", ");
        }
        if (sb.length() > 0) {
            sb.delete(sb.length() - 2, sb.length());
        }
        return sb.toString();

    }

}
