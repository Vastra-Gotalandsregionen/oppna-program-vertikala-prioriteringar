package se.vgregion.verticalprio.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import se.vgregion.dao.domain.patterns.entity.AbstractEntity;

@Entity
@Table(name = "prio_vaardforms_laenk")
public class PrioVaardformsLaenk extends AbstractEntity<PrioVaardformsLaenk, Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "vaardforms_kod_id")
    private VaardformsKod vaardformsKod;

    @ManyToOne
    @JoinColumn(name = "prio_id")
    private Prio prio;

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setPrio(Prio prio) {
        this.prio = prio;
    }

    public Prio getPrio() {
        return prio;
    }

    public void setVaardformsKod(VaardformsKod vaardformsKod) {
        this.vaardformsKod = vaardformsKod;
    }

    public VaardformsKod getVaardformsKod() {
        return vaardformsKod;
    }

}
