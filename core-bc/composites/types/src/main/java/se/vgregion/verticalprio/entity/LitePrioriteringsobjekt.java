package se.vgregion.verticalprio.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import se.vgregion.dao.domain.patterns.entity.AbstractEntity;

@Entity
@Table(name = "prioriteringsobjekt")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class LitePrioriteringsobjekt extends AbstractEntity<Long> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public Long getId() {
        return id;
    }

    /**
     * @inheritDoc
     */
    @Override
    public String toString() {
        return getId() + "";
    }

}