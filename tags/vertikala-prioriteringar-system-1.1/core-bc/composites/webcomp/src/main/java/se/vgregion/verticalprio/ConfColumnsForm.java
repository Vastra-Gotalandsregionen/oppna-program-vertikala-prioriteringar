package se.vgregion.verticalprio;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import se.vgregion.verticalprio.entity.Column;

public class ConfColumnsForm implements Serializable {

    private static final long serialVersionUID = -1245810750781330768L;

    private List<Column> visibleColumns = new ArrayList<Column>();

    private List<Column> hiddenColumns = new ArrayList<Column>();

    public List<Column> getVisibleColumns() {
        return visibleColumns;
    }

    public List<Column> getHiddenColumns() {
        return hiddenColumns;
    }

}
