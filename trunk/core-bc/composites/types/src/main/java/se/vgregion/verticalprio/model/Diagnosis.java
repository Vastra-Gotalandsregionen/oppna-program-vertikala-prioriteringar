package se.vgregion.verticalprio.model;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "vgr_diagnosis")
public class Diagnosis extends AbstractCode<Diagnosis> {

    @ManyToOne
    private Diagnosis parent;

    public Diagnosis getParent() {
        return parent;
    }

    public void setParent(Diagnosis parent) {
        this.parent = parent;
    }

}
