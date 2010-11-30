package se.vgregion.verticalprio.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "sektor_raad")
public class SektorRaad extends AbstractHirarkiskKod<SektorRaad> {

    @Transient
    private boolean selected;

    public SektorRaad() {
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isSelected() {
        return selected;
    }

}
