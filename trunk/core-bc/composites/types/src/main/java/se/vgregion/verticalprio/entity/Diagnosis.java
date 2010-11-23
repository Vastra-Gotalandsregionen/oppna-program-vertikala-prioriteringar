package se.vgregion.verticalprio.entity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "diagnosis")
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
