package se.vgregion.verticalprio.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "vgr_abstract_hierarchical_code")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class AbstractHirarkiskKod<T extends AbstractHirarkiskKod<?>> extends AbstractKod {

    @Transient
    private List<T> children = new ArrayList<T>();

    @ManyToOne
    private T parent;

    public List<T> getChildren() {
        return children;
    }

    public void setChildren(List<T> children) {
        this.children = children;
    }

    public T getParent() {
        return parent;
    }

    public void setParent(T parent) {
        this.parent = parent;
    }

}
