package se.vgregion.verticalprio.model;

public class Sector {
    // private static final Log log = LogFactory.getLog(Sector.class);

    public Sector() {
    }

    public Sector(String label, int id) {
        this.label = label;
        this.id = id;
    }

    private int id = 0;

    private String label;

    private boolean selected;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isSelected() {
        return selected;
    }
}