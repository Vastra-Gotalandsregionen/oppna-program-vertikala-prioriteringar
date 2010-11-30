package se.vgregion.verticalprio.entity;

/**
 * This is the actual Priority object
 */
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import se.vgregion.dao.domain.patterns.entity.AbstractEntity;

@Entity
@Table(name = "prioriteringsobjekt")
public class Prioriteringsobjekt extends AbstractEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "vaentetid_veckor")
    private Integer vaentetidVeckor;

    @ManyToOne
    @JoinColumn(name = "sektor_raad_id")
    private SektorRaad sektorRaad;

    @ManyToMany
    @JoinTable(name = "link_prioriteringsobjekt_diagnos_kod", joinColumns = { @JoinColumn(name = "prio_id") }, inverseJoinColumns = { @JoinColumn(name = "diagnos_kod_id") })
    private List<DiagnosKod> diagnoser = new ArrayList<DiagnosKod>();

    @ManyToMany
    @JoinTable(name = "link_prioriteringsobjekt_aatgaerds_kod", joinColumns = { @JoinColumn(name = "prio_id") }, inverseJoinColumns = { @JoinColumn(name = "aatgaerds_kod_id") })
    private List<AatgaerdsKod> aatgaerdskoder = new ArrayList<AatgaerdsKod>();

    @ManyToMany
    @JoinTable(name = "link_prioriteringsobjekt_vaardforms_kod", joinColumns = { @JoinColumn(name = "prio_id") }, inverseJoinColumns = { @JoinColumn(name = "vaardforms_kod_id") })
    private List<VaardformsKod> vaardformskoder = new ArrayList<VaardformsKod>();

    @ManyToOne
    @JoinColumn(name = "tillstaandets_svaarighetsgrad_kod_id")
    private TillstaandetsSvaarighetsgradKod tillstaandetsSvaarighetsgradKod;

    @ManyToOne
    @JoinColumn(name = "vaentetids_kod_id")
    private VaentetidsKod vaentetidsKod;

    @ManyToOne
    @JoinColumn(name = "rangordnings_kod_id")
    private RangordningsKod rangordningsKod;

    @ManyToOne
    @JoinColumn(name = "vaardnivaa_kod_id")
    private VaardnivaaKod vaardnivaaKod;

    @ManyToOne
    @JoinColumn(name = "haelsonekonomisk_evidens_kod_id")
    private HaelsonekonomiskEvidensKod haelsonekonomiskEvidensKod;

    @ManyToOne
    @JoinColumn(name = "aatgaerds_risk_kod_id")
    private AatgaerdsRiskKod aatgaerdsRiskKod;

    @ManyToOne
    @JoinColumn(name = "patientnytto_evidens_kod_id")
    private PatientnyttoEvidensKod patientnyttoEvidensKod;

    @ManyToOne
    @JoinColumn(name = "patientnytta_effekt_aatgaerds_kod_id")
    private PatientnyttaEffektAatgaerdsKod patientnyttaEffektAatgaerdsKod;

    @Column(name = "vaardgivare")
    private String vaardgivare;

    public String getVaardgivare() {
        return vaardgivare;
    }

    public void setVaardgivare(String vaardgivare) {
        this.vaardgivare = vaardgivare;
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTillstaandetsSvaarighetsgradKod(TillstaandetsSvaarighetsgradKod tillstaandetsSvaarighetsgrad) {
        this.tillstaandetsSvaarighetsgradKod = tillstaandetsSvaarighetsgrad;
    }

    public TillstaandetsSvaarighetsgradKod getTillstaandetsSvaarighetsgradKod() {
        return tillstaandetsSvaarighetsgradKod;
    }

    public void setSektorRaad(SektorRaad sektorRaad) {
        this.sektorRaad = sektorRaad;
    }

    public SektorRaad getSektorRaad() {
        return sektorRaad;
    }

    public List<DiagnosKod> getDiagnoser() {
        return diagnoser;
    }

    public void setDiagnoser(List<DiagnosKod> diagnoser) {
        this.diagnoser = diagnoser;
    }

    public List<AatgaerdsKod> getAatgaerdskoder() {
        return aatgaerdskoder;
    }

    public void setAatgaerdskoder(List<AatgaerdsKod> aatgaerdskoder) {
        this.aatgaerdskoder = aatgaerdskoder;
    }

    public List<VaardformsKod> getVaardformskoder() {
        return vaardformskoder;
    }

    public void setVaardformskoder(List<VaardformsKod> vaardformskoder) {
        this.vaardformskoder = vaardformskoder;
    }

    public void setVaentetidsKod(VaentetidsKod vaentetidsKod) {
        this.vaentetidsKod = vaentetidsKod;
    }

    public VaentetidsKod getVaentetidsKod() {
        return vaentetidsKod;
    }

    public void setRangordningsKod(RangordningsKod rangordningsKod) {
        this.rangordningsKod = rangordningsKod;
    }

    public RangordningsKod getRangordningsKod() {
        return rangordningsKod;
    }

    public void setVaardnivaaKod(VaardnivaaKod vaardnivaaKod) {
        this.vaardnivaaKod = vaardnivaaKod;
    }

    public VaardnivaaKod getVaardnivaaKod() {
        return vaardnivaaKod;
    }

    public void setHaelsonekonomiskEvidensKod(HaelsonekonomiskEvidensKod haelsonekonomiskEvidensKod) {
        this.haelsonekonomiskEvidensKod = haelsonekonomiskEvidensKod;
    }

    public HaelsonekonomiskEvidensKod getHaelsonekonomiskEvidensKod() {
        return haelsonekonomiskEvidensKod;
    }

    public void setAatgaerdsRiskKod(AatgaerdsRiskKod aatgaerdsRiskKod) {
        this.aatgaerdsRiskKod = aatgaerdsRiskKod;
    }

    public AatgaerdsRiskKod getAatgaerdsRiskKod() {
        return aatgaerdsRiskKod;
    }

    public void setPatientnyttoEvidensKod(PatientnyttoEvidensKod patientnyttoEvidensKod) {
        this.patientnyttoEvidensKod = patientnyttoEvidensKod;
    }

    public PatientnyttoEvidensKod getPatientnyttoEvidensKod() {
        return patientnyttoEvidensKod;
    }

    public void setPatientnyttaEffektAatgaerdsKod(PatientnyttaEffektAatgaerdsKod patientnyttaEffektAatgaerdKod) {
        this.patientnyttaEffektAatgaerdsKod = patientnyttaEffektAatgaerdKod;
    }

    public PatientnyttaEffektAatgaerdsKod getPatientnyttaEffektAatgaerdsKod() {
        return patientnyttaEffektAatgaerdsKod;
    }

    public void setVaentetidVeckor(Integer vaentetidVeckor) {
        this.vaentetidVeckor = vaentetidVeckor;
    }

    public Integer getVaentetidVeckor() {
        return vaentetidVeckor;
    }

}
