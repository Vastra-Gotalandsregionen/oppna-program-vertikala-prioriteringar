package se.vgregion.verticalprio.repository;

import java.util.ArrayList;
import java.util.List;

import se.vgregion.verticalprio.entity.TillstaandetsSvaarighetsgradKod;

/**
 * @author Claes Lundahl, vgrid=clalu4
 * 
 */
public class NestedTillstaandetsSvaarighetsgradKod extends TillstaandetsSvaarighetsgradKod implements
        HaveNestedEntities<TillstaandetsSvaarighetsgradKod> {

    private List<TillstaandetsSvaarighetsgradKod> nestedContent = new ArrayList<TillstaandetsSvaarighetsgradKod>();

    /**
     * @inheritDoc
     */
    @Override
    public List<TillstaandetsSvaarighetsgradKod> content() {
        return nestedContent;
    }

    /**
     * @return the nestedContent
     */
    public List<TillstaandetsSvaarighetsgradKod> getNestedContent() {
        return nestedContent;
    }

    /**
     * @param nestedContent
     *            the nestedContent to set
     */
    public void setNestedContent(List<TillstaandetsSvaarighetsgradKod> nestedContent) {
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
