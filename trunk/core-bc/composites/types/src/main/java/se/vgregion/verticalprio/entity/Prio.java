package se.vgregion.verticalprio.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import se.vgregion.dao.domain.patterns.entity.AbstractEntity;

@Entity
@Table(name = "prio")
public class Prio extends AbstractEntity<Prio, Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @Column(name = "vaentetid_veckor")
    private Integer vaentetidVeckor;

    @ManyToOne
    @JoinColumn(name = "sektor_raad_id")
    private SektorRaad sektorRaad;

    @Transient
    private List<PrioDiagnosLaenk> prioDiagnosLaenks = new ArrayList<PrioDiagnosLaenk>();

    @Transient
    private List<AatgaerdsKod> aatgaerdsKods = new ArrayList<AatgaerdsKod>();

    @Transient
    private List<VaardformsKod> vaardformsKods = new ArrayList<VaardformsKod>();

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

    public void setPrioDiagnosLaenks(List<PrioDiagnosLaenk> prioDiagnosLaenks) {
        this.prioDiagnosLaenks = prioDiagnosLaenks;
    }

    public List<PrioDiagnosLaenk> getPrioDiagnosLaenks() {
        return prioDiagnosLaenks;
    }

    public List<AatgaerdsKod> getAatgaerdsKods() {
        return aatgaerdsKods;
    }

    public List<VaardformsKod> getVaardformsKods() {
        return vaardformsKods;
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
