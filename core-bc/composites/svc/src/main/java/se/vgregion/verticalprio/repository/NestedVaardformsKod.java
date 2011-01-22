package se.vgregion.verticalprio.repository;

import java.util.ArrayList;
import java.util.List;

import se.vgregion.verticalprio.entity.VaardformsKod;

/**
 * @author Claes Lundahl, vgrid=clalu4
 * 
 */
public class NestedVaardformsKod extends VaardformsKod implements HaveNestedEntities<VaardformsKod> {

    private List<VaardformsKod> nestedContent = new ArrayList<VaardformsKod>();

    /**
     * @inheritDoc
     */
    @Override
    public List<VaardformsKod> content() {
        return nestedContent;
    }

    /**
     * @return the nestedContent
     */
    public List<VaardformsKod> getNestedContent() {
        return nestedContent;
    }

    /**
     * @param nestedContent
     *            the nestedContent to set
     */
    public void setNestedContent(List<VaardformsKod> nestedContent) {
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
