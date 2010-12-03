package se.vgregion.verticalprio.entity;

import java.util.List;

import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

@MappedSuperclass
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class AbstractHirarkiskKod<T extends AbstractHirarkiskKod<?>> extends AbstractKod {

    @OneToMany()
    @JoinColumn(name = "parent_id")
    private List<T> children; // = new ArrayList<T>();

    @Transient
    private boolean selected;

    @javax.persistence.Column(name = "parent_id")
    private Long parentId;

    public List<T> getChildren() {
        return children;
    }

    public void setChildren(List<T> children) {
        this.children = children;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isSelected() {
        return selected;
    }

}
