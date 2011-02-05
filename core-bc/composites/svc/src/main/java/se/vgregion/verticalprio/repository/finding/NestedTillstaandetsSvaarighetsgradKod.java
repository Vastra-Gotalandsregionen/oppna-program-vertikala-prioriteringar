package se.vgregion.verticalprio.repository.finding;

import java.util.HashSet;
import java.util.Set;

import se.vgregion.verticalprio.entity.TillstaandetsSvaarighetsgradKod;

/**
 * @author Claes Lundahl, vgrid=clalu4
 * 
 */
public class NestedTillstaandetsSvaarighetsgradKod extends TillstaandetsSvaarighetsgradKod implements
        HaveNestedEntities<TillstaandetsSvaarighetsgradKod> {

    private Set<TillstaandetsSvaarighetsgradKod> nestedContent = new HashSet<TillstaandetsSvaarighetsgradKod>();

    /**
     * @inheritDoc
     */
    @Override
    public Set<TillstaandetsSvaarighetsgradKod> content() {
        return nestedContent;
    }

    /**
     * @return the nestedContent
     */
    public Set<TillstaandetsSvaarighetsgradKod> getNestedContent() {
        return nestedContent;
    }

    /**
     * @param nestedContent
     *            the nestedContent to set
     */
    public void setNestedContent(Set<TillstaandetsSvaarighetsgradKod> nestedContent) {
        this.nestedContent = nestedContent;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (TillstaandetsSvaarighetsgradKod rk : nestedContent) {
            sb.append(rk.getKod() + ", ");
        }
        if (sb.length() > 0) {
            sb.delete(sb.length() - 2, sb.length());
        }
        return sb.toString();

    }

}
