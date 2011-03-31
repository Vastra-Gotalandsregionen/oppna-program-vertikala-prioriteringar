package se.vgregion.verticalprio.entity;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author Claes Lundahl, vgrid=clalu4
 * 
 */
@Entity
@Table(name = "link_prioriteringsobjekt_diagnos_kod")
public class LinkPrioriteringsobjektDiagnosKod implements Serializable {

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private LinkPrioriteringsobjektDiagnosKodId id;

    public void setId(LinkPrioriteringsobjektDiagnosKodId id) {
        this.id = id;
    }

    public LinkPrioriteringsobjektDiagnosKodId getId() {
        return id;
    }

}
