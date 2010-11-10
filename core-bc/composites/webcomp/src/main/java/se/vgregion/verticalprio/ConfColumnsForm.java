package se.vgregion.verticalprio;

import java.util.ArrayList;
import java.util.List;

import se.vgregion.verticalprio.model.Column;

public class ConfColumnsForm {

    private List<Column> visibleColumns = new ArrayList<Column>();

    private List<Column> hiddenColumns = new ArrayList<Column>();

    public List<Column> getVisibleColumns() {
        return visibleColumns;
    }

    public List<Column> getHiddenColumns() {
        return hiddenColumns;
    }

}
