package se.vgregion.verticalprio.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import se.vgregion.dao.domain.patterns.entity.AbstractEntity;

@Entity
@Table(name = "prio_diagnosis_link")
public class PrioDiagnosisLink extends AbstractEntity<PrioDiagnosisLink, Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private DiagnosKod diagnosis;

    @ManyToOne
    private Prio prio;

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setDiagnosis(DiagnosKod diagnosis) {
        this.diagnosis = diagnosis;
    }

    public DiagnosKod getDiagnosis() {
        return diagnosis;
    }

    public void setPrio(Prio prio) {
        this.prio = prio;
    }

    public Prio getPrio() {
        return prio;
    }

}
