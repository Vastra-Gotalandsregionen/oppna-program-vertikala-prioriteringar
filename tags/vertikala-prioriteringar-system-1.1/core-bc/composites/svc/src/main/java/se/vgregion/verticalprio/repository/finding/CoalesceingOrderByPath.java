package se.vgregion.verticalprio.repository.finding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Claes Lundahl, vgrid=clalu4
 * 
 */
public class CoalesceingOrderByPath extends OrderByPath {

    private static final long serialVersionUID = 1L;

    private static int count = 0;

    private static int currentCount = count++;

    private List<OrderByPath> orders;

    public CoalesceingOrderByPath(OrderByPath... paths) {
        orders = Arrays.asList(paths);
    }

    /**
     * @inheritDoc
     */
    @Override
    public List<String> toJpqlJoinPart(String startAlias) {
        List<String> parts = new ArrayList<String>();
        for (OrderByPath obp : orders) {
            parts.addAll(obp.toJpqlJoinPart(startAlias));
        }
        return parts;
    }

    /**
     * @inheritDoc
     */
    @Override
    public String toJpqlSelectPart(String alias) {
        List<String> parts = new ArrayList<String>();
        for (OrderByPath obp : orders) {
            parts.add(obp.toJpqlSelectPart(alias));
        }
        return "coalesce(" + (JpqlMatchBuilder.toString(parts, ", ")) + ") as sorting" + currentCount;
    }

    /**
     * @inheritDoc
     */
    @Override
    public String toJpqlOrderByPart(String alias) {
        String result = "sorting" + currentCount;
        result += super.isAscending() ? " asc" : " desc";
        return result;
    }

}
