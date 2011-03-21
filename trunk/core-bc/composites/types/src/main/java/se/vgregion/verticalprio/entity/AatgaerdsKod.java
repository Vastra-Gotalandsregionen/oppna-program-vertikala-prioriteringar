package se.vgregion.verticalprio.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
@Table(name = "aatgaerds_kod")
public class AatgaerdsKod extends AbstractKod {

    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "link_prioriteringsobjekt_aatgaerds_kod", joinColumns = { @JoinColumn(name = "aatgaerds_kod_id") }, inverseJoinColumns = { @JoinColumn(name = "prio_id") })
    private Set<Prioriteringsobjekt> prioriteringsobjekt = new HashSet<Prioriteringsobjekt>();

    @ManyToMany
    @Fetch(FetchMode.JOIN)
    @JoinTable(name = "link_prioriteringsobjekt_aatgaerds_kod", joinColumns = { @JoinColumn(name = "aatgaerds_kod_id") }, inverseJoinColumns = { @JoinColumn(name = "prio_id") })
    private Set<LitePrioriteringsobjekt> litePrioriteringsobjekt = new HashSet<LitePrioriteringsobjekt>();

    /**
     * @inheritDoc
     */
    @Override
    public String getLabel() {
        return getKod();
    }

    public void setPrioriteringsobjekt(Set<Prioriteringsobjekt> prioriteringsobjekt) {
        this.prioriteringsobjekt = prioriteringsobjekt;
    }

    public Set<Prioriteringsobjekt> getPrioriteringsobjekt() {
        return prioriteringsobjekt;
    }

    public void setLitePrioriteringsobjekt(Set<LitePrioriteringsobjekt> litePrioriteringsobjekt) {
        this.litePrioriteringsobjekt = litePrioriteringsobjekt;
    }

    public Set<LitePrioriteringsobjekt> getLitePrioriteringsobjekt() {
        return litePrioriteringsobjekt;
    }

}
