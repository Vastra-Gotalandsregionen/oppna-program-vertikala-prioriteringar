package se.vgregion.verticalprio.repository;

import java.util.ArrayList;
import java.util.List;

import se.vgregion.verticalprio.entity.RangordningsKod;

/**
 * @author Claes Lundahl, vgrid=clalu4
 * 
 */
public class NestedRangordningsKod extends RangordningsKod implements HaveNestedEntities<RangordningsKod> {

    private List<RangordningsKod> nestedContent = new ArrayList<RangordningsKod>();

    /**
     * @inheritDoc
     */
    @Override
    public List<RangordningsKod> content() {
        return nestedContent;
    }

    /**
     * @return the nestedContent
     */
    public List<RangordningsKod> getNestedContent() {
        return nestedContent;
    }

    /**
     * @param nestedContent
     *            the nestedContent to set
     */
    public void setNestedContent(List<RangordningsKod> nestedContent) {
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
