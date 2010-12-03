package se.vgregion.verticalprio.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "sektor_raad")
public class SektorRaad extends AbstractHirarkiskKod<SektorRaad> {

    @Id
    Long id;

    /**
     * @inheritDoc
     */
    @Override
    public Long getId() {
        return id;
    }

    /**
     * @inheritDoc
     */
    @Override
    public void setId(Long id) {
        this.id = id;
    }

}
