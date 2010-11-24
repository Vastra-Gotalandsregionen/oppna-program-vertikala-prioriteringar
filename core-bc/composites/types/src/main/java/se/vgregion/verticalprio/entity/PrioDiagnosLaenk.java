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
@Table(name = "prio_diagnos_laenk")
public class PrioDiagnosLaenk extends AbstractEntity<PrioDiagnosLaenk, Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "diagnos_kod_id")
    private DiagnosKod diagnosKod;

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

    public void setDiagnosKod(DiagnosKod diagnosis) {
        this.diagnosKod = diagnosis;
    }

    public DiagnosKod getDiagnosKod() {
        return diagnosKod;
    }

    public void setPrio(Prio prio) {
        this.prio = prio;
    }

    public Prio getPrio() {
        return prio;
    }

}
