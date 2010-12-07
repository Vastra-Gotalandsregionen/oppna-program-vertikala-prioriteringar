package se.vgregion.verticalprio.entity;

import java.io.Serializable;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class AbstractGeneratedIdKod extends AbstractKod implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

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
