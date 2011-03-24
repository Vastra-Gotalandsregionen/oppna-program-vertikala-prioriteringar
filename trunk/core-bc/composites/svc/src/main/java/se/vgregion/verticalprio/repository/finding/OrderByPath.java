package se.vgregion.verticalprio.repository.finding;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang.text.StrBuilder;


/**
 * @author Claes Lundahl, vgrid=clalu4
 * 
 */
public class OrderByPath implements Serializable {

    private static final long serialVersionUID = 1L;

    private static long totalCount = 0;

    private final long id = totalCount++;

    private boolean ascending = true;

    private String[] path;

    public OrderByPath() {

    }

    public OrderByPath(String path) {
        setPath(path);
    }

    public OrderByPath(String path, boolean ascending) {
        setAscending(ascending);

    }

    public void setAscending(boolean ascending) {
        this.ascending = ascending;
    }

    public boolean isAscending() {
        return ascending;
    }

    public void setPath(String path) {
        this.path = path.split(Pattern.quote("/"));

    }

    public String getPath() {
        return JpqlMatchBuilder.toString(Arrays.asList(path), "/");
    }

    /**
     * @inheritDoc
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof OrderByPath)) {
            return false;
        }
        OrderByPath obp = (OrderByPath) obj;
        if (!equals(path, obp.path)) {
            return false;
        }
        if (ascending != obp.ascending) {
            return false;
        }
        return true;
    }

    private boolean equals(Object o1, Object o2) {
        if (o1 == o2) {
            return true;
        }
        if (o1 == null || o2 == null) {
            return false;
        }
        return o1.equals(o2);
    }

    public String toJpqlJoinPart(String startAlias) {
        List<String> remainingPath = new ArrayList<String>(Arrays.asList(path));
        return toJpqlJoinPart(startAlias, remainingPath, 0);
    }

    private String toJpqlJoinPart(String startAlias, List<String> remainingPath, int count) {
        StrBuilder sb = new StrBuilder();
        if (remainingPath.size() <= 1) {
            return "";
        }

        // sb.append("left join " + startAlias + "." + remainingPath.remove(0));
        sb.append(startAlias + "." + remainingPath.remove(0));
        String nextAlias = "ob" + getAliasCount() + "c" + count;
        sb.append(" " + nextAlias + " ");
        sb.append(toJpqlJoinPart(startAlias, remainingPath, count + 1));

        return sb.toString().trim();
    }

    public static List<String> toJpqlJoinParts(Iterable<OrderByPath> paths, String alias) {
        List<String> result = new ArrayList<String>();
        for (OrderByPath path : paths) {
            result.add(path.toJpqlJoinPart(alias));
        }
        return result;
    }

    public String toJpqlSelectPart(String alias) {
        if (path.length == 1) {
            return alias + "." + path[0];
        }
        String result = "ob" + getAliasCount() + "c" + (path.length - 2);
        result += "." + path[path.length - 1];
        return result;
    }

    public static List<String> toJpqlSelectParts(Iterable<OrderByPath> paths, String alias) {
        List<String> result = new ArrayList<String>();
        for (OrderByPath path : paths) {
            result.add(path.toJpqlSelectPart(alias));
        }
        return result;
    }

    public String toJpqlOrderByPart(String alias) {
        String result = toJpqlSelectPart(alias);
        result += ascending ? " asc" : " desc";
        return result;
    }

    public long getAliasCount() {
        return id;
    }

    public static List<String> toJpqlOrderByParts(Iterable<OrderByPath> paths, String alias) {
        List<String> result = new ArrayList<String>();
        for (OrderByPath path : paths) {
            result.add(path.toJpqlOrderByPart(alias));
        }
        return result;
    }

}
