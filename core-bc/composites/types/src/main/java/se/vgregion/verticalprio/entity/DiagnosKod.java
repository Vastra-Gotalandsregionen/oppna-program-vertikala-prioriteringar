package se.vgregion.verticalprio.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "diagnos_kod")
public class DiagnosKod extends AbstractHirarkiskKod<DiagnosKod> {

    private static final long serialVersionUID = 1L;

    // @OneToMany(fetch = FetchType.LAZY)
    // @JoinTable(name = "link_prioriteringsobjekt_diagnos_kod", joinColumns = { @JoinColumn(name =
    // "diagnos_kod_id") }, inverseJoinColumns = { @JoinColumn(name = "prio_id") })
    // private Set<Prioriteringsobjekt> prioriteringsobjekt = new HashSet<Prioriteringsobjekt>();
    //
    // @ManyToMany
    // @Fetch(FetchMode.JOIN)
    // @JoinTable(name = "link_prioriteringsobjekt_diagnos_kod", joinColumns = { @JoinColumn(name =
    // "diagnos_kod_id") }, inverseJoinColumns = { @JoinColumn(name = "prio_id") })
    // private Set<LitePrioriteringsobjekt> litePrioriteringsobjekt = new HashSet<LitePrioriteringsobjekt>();

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

    // public void setPrioriteringsobjekt(Set<Prioriteringsobjekt> prioriteringsobjekt) {
    // this.prioriteringsobjekt = prioriteringsobjekt;
    // }
    //
    // public Set<Prioriteringsobjekt> getPrioriteringsobjekt() {
    // return prioriteringsobjekt;
    // }
    //
    // public void setLitePrioriteringsobjekt(Set<LitePrioriteringsobjekt> litePrioriteringsobjekt) {
    // this.litePrioriteringsobjekt = litePrioriteringsobjekt;
    // }
    //
    // public Set<LitePrioriteringsobjekt> getLitePrioriteringsobjekt() {
    // return litePrioriteringsobjekt;
    // }

}
