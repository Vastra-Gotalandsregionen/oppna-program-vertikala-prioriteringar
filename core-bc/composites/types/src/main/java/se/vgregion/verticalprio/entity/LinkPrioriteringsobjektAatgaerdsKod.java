package se.vgregion.verticalprio.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author Claes Lundahl, vgrid=clalu4
 * 
 */
@Entity
@Table(name = "link_prioriteringsobjekt_aatgaerds_kod")
public class LinkPrioriteringsobjektAatgaerdsKod implements Serializable {

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private Id id;

    public void setId(Id id) {
        this.id = id;
    }

    public Id getId() {
        return id;
    }

    @Embeddable
    public static class Id extends AbstractLinkId {

        private static final long serialVersionUID = 1L;

        @Column(name = "prio_id", updatable = false)
        private Long prioId;

        @Column(name = "aatgaerds_kod_id", updatable = false, insertable = false)
        private Long kodId;

        @Override
        public Long getPrioId() {
            return prioId;
        }

        public void setPrioId(Long prioId) {
            this.prioId = prioId;
        }

        /**
         * @inheritDoc
         */
        @Override
        public Long getKodId() {
            return kodId;
        }

        public void setKodId(Long kodId) {
            this.kodId = kodId;
        }

    }

}
