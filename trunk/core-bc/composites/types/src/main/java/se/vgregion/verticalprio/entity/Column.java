package se.vgregion.verticalprio.entity;

import java.io.Serializable;
import java.util.Comparator;

/**
 * This class is used for letting the user save his/hers selected columns. So that each logged in user can have
 * his/hers gui look
 * 
 * @author Claes Lundahl, vgrid=clalu4
 * 
 */
public class Column implements Serializable, Comparable<Column> {
    // private static final Log log = LogFactory.getLog(Column.class);
    private int id;

    private int displayOrder;

    private boolean hideAble;

    private String label;

    private String columnLabel;

    private String name;

    private String sortField;

    private boolean visible = true;

    private boolean sorting;

    private boolean sortable = false;

    private String description;

    private boolean filterAble = false;

    private boolean possibleInOverview = true;

    public Column() {
        // TODO Auto-generated constructor stub
    }

    public Column(int id, String label) {
        setId(id);
        setLabel(label);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isVisible() {
        return possibleInOverview && visible;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Column)) {
            return false;
        }
        Column other = (Column) obj;
        return other.id == id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setHideAble(boolean hideAble) {
        this.hideAble = hideAble;
    }

    public boolean isHideAble() {
        return hideAble;
    }

    public void setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }

    /**
     * @inheritDoc
     */
    @Override
    public String toString() {
        return String.valueOf(name);
    }

    public void setColumnLabel(String columnLabel) {
        this.columnLabel = columnLabel;
    }

    public String getColumnLabel() {
        return columnLabel;
    }

    public void setSorting(boolean sorting) {
        this.sorting = sorting;
    }

    public boolean isSorting() {
        return sorting;
    }

    public void setSortable(boolean sortable) {
        this.sortable = sortable;
    }

    public boolean isSortable() {
        return sortable;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setSortField(String sortField) {
        this.sortField = sortField;
    }

    public String getSortField() {
        return sortField;
    }

    public void setFilterAble(boolean filterAble) {
        this.filterAble = filterAble;
    }

    public boolean isFilterAble() {
        return filterAble;
    }

    public static class OrderComparer implements Comparator<Column> {
        /**
         * @inheritDoc
         */
        @Override
        public int compare(se.vgregion.verticalprio.entity.Column o1, se.vgregion.verticalprio.entity.Column o2) {
            return o1.getDisplayOrder() - o2.getDisplayOrder();
        }

    }

    /**
     * @inheritDoc
     */
    @Override
    public int compareTo(Column o) {
        return getDisplayOrder() - o.getDisplayOrder();
    }

    public void setPossibleInOverview(boolean possibleInOverview) {
        this.possibleInOverview = possibleInOverview;
    }

    public boolean isPossibleInOverview() {
        return possibleInOverview;
    }

}
