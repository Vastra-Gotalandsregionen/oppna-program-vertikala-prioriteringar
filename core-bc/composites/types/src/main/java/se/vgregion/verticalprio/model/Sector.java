package se.vgregion.verticalprio.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "sector")
public class Sector extends AbstractCode<Sector> {

    public Sector() {
    }

    public Sector(String label, Long id) {
        setLabel(label);
        setId(id);
    }

    @Transient
    private boolean selected;

    @Transient
    private List<Sector> children = new ArrayList<Sector>();

    @ManyToOne
    private Sector parent;

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setParent(Sector parent) {
        this.parent = parent;
    }

    public Sector getParent() {
        return parent;
    }

    public List<Sector> getChildren() {
        return children;
    }
}
