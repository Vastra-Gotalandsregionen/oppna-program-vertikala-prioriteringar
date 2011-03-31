package se.vgregion.verticalprio.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * @author Claes Lundahl, vgrid=clalu4
 * 
 */
@Embeddable
public class LinkPrioriteringsobjektDiagnosKodId implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "prio_id", updatable = false)
    private Long prioId;

    @Column(name = "diagnos_kod_id", updatable = false, insertable = false)
    private Long diagnosKodId;

    public Long getPrioId() {
        return prioId;
    }

    public void setPrioId(Long prioId) {
        this.prioId = prioId;
    }

    public Long getDiagnosKodId() {
        return diagnosKodId;
    }

    public void setDiagnosKodId(Long diagnosKodId) {
        this.diagnosKodId = diagnosKodId;
    }

    /**
     * @inheritDoc
     */
    @Override
    public int hashCode() {
        return getHashCode(diagnosKodId) + getHashCode(prioId);
    }

    private int getHashCode(Long l) {
        if (l != null) {
            return l.hashCode();
        } else {
            return 0;
        }
    }

    /**
     * @inheritDoc
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        LinkPrioriteringsobjektDiagnosKodId other = (LinkPrioriteringsobjektDiagnosKodId) obj;
        return equals(prioId, other.prioId) && equals(diagnosKodId, other.diagnosKodId);
    }

    private boolean equals(Long first, Long second) {
        if (first == second) {
            return true;
        }
        if (first == null || second == null) {
            return false;
        }
        return first.equals(second);
    }

}
