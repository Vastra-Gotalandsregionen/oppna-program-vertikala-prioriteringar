package se.vgregion.verticalprio.repository.finding;

import java.util.ArrayList;
import java.util.List;

import se.vgregion.verticalprio.entity.RangordningsKod;
import se.vgregion.verticalprio.entity.SektorRaad;

/**
 * @author Claes Lundahl, vgrid=clalu4
 * 
 */
public class SortingSektorRaad extends SektorRaad implements HaveQuerySortOrder {

    private final List<SortOrderField> fields = new ArrayList<HaveQuerySortOrder.SortOrderField>();

    /**
     * @inheritDoc
     */
    @Override
    public List<SortOrderField> listSortOrders() {
        return fields;
    }
}
