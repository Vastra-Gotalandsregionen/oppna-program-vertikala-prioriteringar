package se.vgregion.verticalprio.model;

public class Column {
    // private static final Log log = LogFactory.getLog(Column.class);
    private int id;

    private String label;

    private boolean visible = true;

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
        return visible;
    }
}
