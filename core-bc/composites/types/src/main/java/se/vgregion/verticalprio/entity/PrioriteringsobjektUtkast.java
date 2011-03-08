package se.vgregion.verticalprio.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * @author Claes Lundahl, vgrid=clalu4
 * 
 */
@Entity
@Table(name = "prioriteringsobjekt_utkast")
public class PrioriteringsobjektUtkast extends AbstractPrioriteringsobjekt {

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "prioriteringsobjekt_id")
    private Prioriteringsobjekt skarpVersion;

    @javax.persistence.Column(name = "prioriteringsobjekt_id", insertable = false, updatable = false)
    private Long prioriteringsobjektId;

    public void setSkarpVersion(Prioriteringsobjekt skarpVersion) {
        this.skarpVersion = skarpVersion;
    }

    public Prioriteringsobjekt getSkarpVersion() {
        return skarpVersion;
    }

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "link_prioriteringsobjekt_utkast_diagnos_kod", joinColumns = { @JoinColumn(name = "prio_utkast_id") }, inverseJoinColumns = { @JoinColumn(name = "diagnos_kod_id") })
    private Set<DiagnosKod> diagnoser = new HashSet<DiagnosKod>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "link_prioriteringsobjekt_utkast_aatgaerds_kod", joinColumns = { @JoinColumn(name = "prio_utkast_id") }, inverseJoinColumns = { @JoinColumn(name = "aatgaerds_kod_id") })
    private Set<AatgaerdsKod> aatgaerdskoder = new HashSet<AatgaerdsKod>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "link_prioriteringsobjekt_utkast_atc_kod", joinColumns = { @JoinColumn(name = "prio_utkast_id") }, inverseJoinColumns = { @JoinColumn(name = "atc_kod_id") })
    private Set<AtcKod> atcKoder = new HashSet<AtcKod>();

    @Override
    public Set<DiagnosKod> getDiagnoser() {
        return diagnoser;
    }

    @Override
    public void setDiagnoser(Set<DiagnosKod> diagnoser) {
        this.diagnoser = diagnoser;
    }

    @Override
    public Set<AatgaerdsKod> getAatgaerdskoder() {
        return aatgaerdskoder;
    }

    @Override
    public void setAatgaerdskoder(Set<AatgaerdsKod> aatgaerdskoder) {
        this.aatgaerdskoder = aatgaerdskoder;
    }

    @Override
    public Set<AtcKod> getAtcKoder() {
        return atcKoder;
    }

    @Override
    public void setAtcKoder(Set<AtcKod> atcKoder) {
        this.atcKoder = atcKoder;
    }

    public void setPrioriteringsobjektId(Long prioriteringsobjektId) {
        this.prioriteringsobjektId = prioriteringsobjektId;
    }

    public Long getPrioriteringsobjektId() {
        return prioriteringsobjektId;
    }

}
