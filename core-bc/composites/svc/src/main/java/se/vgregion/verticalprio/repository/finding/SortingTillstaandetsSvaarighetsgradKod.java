package se.vgregion.verticalprio.repository.finding;

import java.util.ArrayList;
import java.util.List;

import se.vgregion.verticalprio.entity.TillstaandetsSvaarighetsgradKod;

/**
 * @author Claes Lundahl, vgrid=clalu4
 * 
 */
public class SortingTillstaandetsSvaarighetsgradKod extends TillstaandetsSvaarighetsgradKod implements
        HaveQuerySortOrder {

    private final List<SortOrderField> fields = new ArrayList<HaveQuerySortOrder.SortOrderField>();

    /**
     * @inheritDoc
     */
    @Override
    public List<SortOrderField> listSortOrders() {
        return fields;
    }

    /**
     * @inheritDoc
     */
    @Override
    public String toString() {
        return SortingUtil.toString(this);
    }
}
