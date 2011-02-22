package se.vgregion.verticalprio.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "diagnos_kod")
public class DiagnosKod extends AbstractHirarkiskKod<DiagnosKod> {

    private static final long serialVersionUID = 1L;

    @Transient
    private boolean open;

    /**
     * @return the open
     */
    public boolean isOpen() {
        return open;
    }

    /**
     * @param open
     *            the open to set
     */
    public void setOpen(boolean open) {
        this.open = open;
    }

    /*
     * public List<DiagnosKod> getOpenDiagnoses() { List<DiagnosKod> openDiagnoses = new ArrayList<DiagnosKod>();
     * List<DiagnosKod> children = getChildren(); if (children == null) { return openDiagnoses; } for (DiagnosKod
     * child : children) { if (child.isOpen()) { openDiagnoses.add(child); } } return openDiagnoses; }
     */

}
