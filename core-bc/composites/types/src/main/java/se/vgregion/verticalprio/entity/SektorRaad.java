package se.vgregion.verticalprio.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

@SuppressWarnings("serial")
@Entity
@Table(name = "sektor_raad")
public class SektorRaad extends AbstractHirarkiskKod<SektorRaad> implements Cloneable {

    public SektorRaad() {
    }

    public SektorRaad(Long id) {
        setId(id);
    }

    @Transient
    private SektorRaad parent;

    @Transient
    private boolean able = true;

    /**
     * @inheritDoc
     */
    // @Override
    // public Long getId() {
    // return id;
    // }
    //
    // /**
    // * @inheritDoc
    // */
    // @Override
    // public void setId(Long id) {
    // this.id = id;
    // }

    public void setAble(boolean able) {
        this.able = able;
    }

    public boolean isAble() {
        return able;
    }

    public void setParent(SektorRaad parent) {
        this.parent = parent;
    }

    public SektorRaad getParent() {
        return parent;
    }

    /**
     * @inheritDoc
     */
    @Override
    public List<SektorRaad> getChildren() {
        List<SektorRaad> result = super.getChildren();

        if (result != null) {
            for (SektorRaad raad : result) {
                raad.setParent(this);
            }
        }

        return result;
    }

}
