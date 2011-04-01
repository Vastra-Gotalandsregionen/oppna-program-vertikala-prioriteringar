package se.vgregion.verticalprio.entity;

import java.io.Serializable;

import javax.persistence.Embeddable;

/**
 * @author Claes Lundahl, vgrid=clalu4
 * 
 */
@Embeddable
public abstract class AbstractLinkId implements Serializable {

    public abstract Long getKodId();

    public abstract Long getPrioId();

    /**
     * @inheritDoc
     */
    @Override
    public int hashCode() {
        return getHashCode(getKodId()) + getHashCode(getPrioId());
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
        AbstractLinkId other = (AbstractLinkId) obj;
        return equals(getPrioId(), other.getPrioId()) && equals(getKodId(), other.getKodId());
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
