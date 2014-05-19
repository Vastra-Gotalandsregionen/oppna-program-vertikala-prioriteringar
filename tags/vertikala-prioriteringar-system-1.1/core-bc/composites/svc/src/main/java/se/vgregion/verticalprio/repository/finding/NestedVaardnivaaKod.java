package se.vgregion.verticalprio.repository.finding;

import java.util.HashSet;
import java.util.Set;

import se.vgregion.verticalprio.entity.VaardnivaaKod;

/**
 * @author Claes Lundahl, vgrid=clalu4
 * 
 */
public class NestedVaardnivaaKod extends VaardnivaaKod implements HaveNestedEntities<VaardnivaaKod> {

    private Set<VaardnivaaKod> nestedContent = new HashSet<VaardnivaaKod>();

    /**
     * @inheritDoc
     */
    @Override
    public Set<VaardnivaaKod> content() {
        return nestedContent;
    }

    /**
     * @return the nestedContent
     */
    public Set<VaardnivaaKod> getNestedContent() {
        return nestedContent;
    }

    /**
     * @param nestedContent
     *            the nestedContent to set
     */
    public void setNestedContent(Set<VaardnivaaKod> nestedContent) {
        this.nestedContent = nestedContent;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (VaardnivaaKod rk : nestedContent) {
            sb.append(rk.getKod() + ", ");
        }
        if (sb.length() > 0) {
            sb.delete(sb.length() - 2, sb.length());
        }
        return sb.toString();

    }

}
