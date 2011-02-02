package se.vgregion.verticalprio.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@SuppressWarnings("serial")
@Entity
@Table(name = "sektor_raad")
public class SektorRaad extends AbstractHirarkiskKod<SektorRaad> {

    public SektorRaad() {
    }

    public SektorRaad(Long id) {
        setId(id);
    }

//    @Id
//    Long id;

    @Transient
    private boolean able = true;

    /**
     * @inheritDoc
     */
//    @Override
//    public Long getId() {
//        return id;
//    }
//
//    /**
//     * @inheritDoc
//     */
//    @Override
//    public void setId(Long id) {
//        this.id = id;
//    }

    public void setAble(boolean able) {
        this.able = able;
    }

    public boolean isAble() {
        return able;
    }

}
