package se.vgregion.verticalprio.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "sektor_raad")
public class SektorRaad extends AbstractKod<SektorRaad> {

    @Transient
    private boolean selected;

    @Transient
    private List<SektorRaad> children = new ArrayList<SektorRaad>();

    @ManyToOne
    private SektorRaad parent;

    public SektorRaad() {
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setParent(SektorRaad parent) {
        this.parent = parent;
    }

    public SektorRaad getParent() {
        return parent;
    }

    public List<SektorRaad> getChildren() {
        return children;
    }

}
