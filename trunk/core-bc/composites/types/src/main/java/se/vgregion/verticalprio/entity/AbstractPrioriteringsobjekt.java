package se.vgregion.verticalprio.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import se.vgregion.dao.domain.patterns.entity.AbstractEntity;

/**
 * @author Claes Lundahl, vgrid=clalu4
 * 
 */
@MappedSuperclass
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class AbstractPrioriteringsobjekt extends AbstractEntity<Long> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "vaentetid_besook_veckor_kod_id")
    private VaentetidsKod vaentetidBesookVeckor;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sektor_raad_id")
    private SektorRaad sektorRaad;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "kostnad_levnadsaar_kod_id")
    private KostnadLevnadsaarKod kostnadLevnadsaarKod;

    @Transient
    private Integer kostnad;

    @Transient
    private Integer volym;

    @Column(name = "senast_uppdaterad")
    private Date senastUppdaterad;

    @ManyToOne
    @JoinColumn(name = "vaardforms_kod_id")
    private VaardformsKod vaardform;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tillstaandets_svaarighetsgrad_kod_id")
    private TillstaandetsSvaarighetsgradKod tillstaandetsSvaarighetsgradKod;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "vaentetid_behandling_veckor_kod_id")
    private VaentetidsKod vaentetidBehandlingVeckor;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "rangordnings_kod_id")
    private RangordningsKod rangordningsKod;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "vaardnivaa_kod_id")
    private VaardnivaaKod vaardnivaaKod;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "haelsonekonomisk_evidens_kod_id")
    private HaelsonekonomiskEvidensKod haelsonekonomiskEvidensKod;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "aatgaerds_risk_kod_id")
    private AatgaerdsRiskKod aatgaerdsRiskKod;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "patientnytto_evidens_kod_id")
    private PatientnyttoEvidensKod patientnyttoEvidensKod;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "patientnytta_effekt_aatgaerds_kod_id")
    private PatientnyttaEffektAatgaerdsKod patientnyttaEffektAatgaerdsKod;

    @Column(name = "kommentar", length = 2000)
    private String kommentar;

    @Column(name = "indikation_gaf", length = 2000)
    private String indikationGaf;

    @Column(name = "godkaend")
    private Date godkaend;

    public Date getGodkaend() {
        return godkaend;
    }

    public void setGodkaend(Date godkaend) {
        this.godkaend = godkaend;
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

    public abstract Set<DiagnosKod> getDiagnoser();

    abstract void setDiagnoser(Set<DiagnosKod> diagnoser);

    public List<String> getDiagnosTexts() {
        return mkBeskrivningsText(getDiagnoser());
    }

    public List<String> getDiagnosKodTexts() {
        List<String> sb = new ArrayList<String>();
        for (DiagnosKod kod : getDiagnoser()) {
            sb.add(kod.getKod());
        }
        return sb;
    }

    private List<String> mkBeskrivningsText(Set<? extends AbstractKod> koder) {
        List<String> sb = new ArrayList<String>();
        if (koder == null || koder.isEmpty()) {
            return sb;
        }
        for (AbstractKod kod : koder) {
            sb.add(kod.getBeskrivning());
        }
        return sb;
    }

    public abstract Set<AatgaerdsKod> getAatgaerdskoder();

    abstract void setAatgaerdskoder(Set<AatgaerdsKod> aatgaerdskoder);

    public List<String> getAatgaerdskoderTexts() {
        return mkBeskrivningsText(getAatgaerdskoder());
    }

    public void setVaentetidBehandlingVeckor(VaentetidsKod vaentetidBehandlingVeckor) {
        this.vaentetidBehandlingVeckor = vaentetidBehandlingVeckor;
    }

    public VaentetidsKod getVaentetidBehandlingVeckor() {
        return vaentetidBehandlingVeckor;
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

    public void setKommentar(String kommentar) {
        this.kommentar = kommentar;
    }

    public String getKommentar() {
        return kommentar;
    }

    abstract void setAtcKoder(Set<AtcKod> atcKoder);

    public abstract Set<AtcKod> getAtcKoder();

    public List<String> getAtcText() {
        return mkBeskrivningsText(getAtcKoder());
    }

    public void setIndikationGaf(String indikationGaf) {
        this.indikationGaf = indikationGaf;
    }

    public String getIndikationGaf() {
        return indikationGaf;
    }

    public void setVaentetidBesookVeckor(VaentetidsKod vaentetidBesookVeckor) {
        this.vaentetidBesookVeckor = vaentetidBesookVeckor;
    }

    public VaentetidsKod getVaentetidBesookVeckor() {
        return vaentetidBesookVeckor;
    }

    public void setKostnad(Integer kostnad) {
        this.kostnad = kostnad;
    }

    public Integer getKostnad() {
        return kostnad;
    }

    public void setVolym(Integer volym) {
        this.volym = volym;
    }

    public Integer getVolym() {
        return volym;
    }

    /**
     * @param vaardform
     *            the vaardform to set
     */
    public void setVaardform(VaardformsKod vaardform) {
        this.vaardform = vaardform;
    }

    /**
     * @return the vaardform
     */
    public VaardformsKod getVaardform() {
        return vaardform;
    }

    public void setKostnadLevnadsaarKod(KostnadLevnadsaarKod kostnadLevnadsaarKod) {
        this.kostnadLevnadsaarKod = kostnadLevnadsaarKod;
    }

    public KostnadLevnadsaarKod getKostnadLevnadsaarKod() {
        return kostnadLevnadsaarKod;
    }

    public void setSenastUppdaterad(Date senastUppdaterad) {
        this.senastUppdaterad = senastUppdaterad;
    }

    public Date getSenastUppdaterad() {
        return senastUppdaterad;
    }

    public String getMessagesWhyNotSaveAble() {
        AbstractPrioriteringsobjekt prio = this;
        StringBuilder sb = new StringBuilder();
        if (prio.getDiagnoser().isEmpty()) {
            sb.append("<br/>Saknar diagnos(er).");
        }
        if (prio.getSektorRaad() == null) {
            sb.append("<br/>Sektorsråd saknas.");
        }

        if (sb.length() == 0) {
            return null;
        }

        return "Kunde inte spara på grund av: " + sb.toString();
    }

    public String mkMessagesWhyNotToApprovePrio() {
        AbstractPrioriteringsobjekt prio = this;
        StringBuilder sb = new StringBuilder();
        if (prio.getDiagnoser().isEmpty()) {
            sb.append("<br/>Lägg till minst en diagnos.");
        }
        if (prio.getAatgaerdskoder().isEmpty()) {
            sb.append("<br/>Lägg till minst en åtgärdskod.");
        }
        if (prio.getTillstaandetsSvaarighetsgradKod() == null) {
            sb.append("<br/>Ange tillståndets svårighetsgrad.");
        }
        if (prio.getAatgaerdsRiskKod() == null) {
            sb.append("<br/>Ange risk med åtgärd.");
        }
        if (prio.getPatientnyttaEffektAatgaerdsKod() == null) {
            sb.append("<br/>Ange patientnytta / effekt åtgärd.");
        }
        if (prio.getRangordningsKod() == null) {
            sb.append("<br/>Ange rangordning.");
        }
        if (prio.getPatientnyttoEvidensKod() == null) {
            sb.append("<br/>Ange evidens patientnytta / effekt åtgärd.");
        }
        if (prio.getVaentetidBesookVeckor() == null) {
            sb.append("<br/>Ange väntetid besök veckor.");
        }
        if (prio.getVaentetidBehandlingVeckor() == null) {
            sb.append("<br/>Ange väntetid veckor behandling.");
        }
        if (prio.getVaardnivaaKod() == null) {
            sb.append("<br/>Ange vårdnivå.");
        }
        if (prio.getVaardform() == null) {
            sb.append("<br/>Ange vårdform.");
        }
        if (prio.getSektorRaad() == null) {
            sb.append("<br/>Ange sektorsråd.");
        }
        if (sb.length() == 0) {
            return null;
        }

        return "Posten kunde ej godkännas." + sb.toString();
    }

    /**
     * Rangordning enligt formel är summan:
     * 
     * Kod för tillståndets svårighetsgrad -0.6 +
     * 
     * Kod för risk med åtgärd*0,2 +
     * 
     * Kod för patientnytta/effekt åtgärd *0,2 +
     * 
     * Kod för evidens för patientnytta /effekt åtgärd *0,2
     * 
     * 
     * Ex. Tillståndets svårighetsgrad: 9
     * 
     * Risk med åtgärd: 1
     * 
     * Patientnytta /effekt åtgärd: 2
     * 
     * Evidens för patientnytta /effekt åtgärd: 4
     * 
     * 9-0,6 + 1*0,2 + 2*0,2 + 4*0,2 = 9,8
     * 
     * @return
     */
    public Double getRangordningEnligtFormel() {
        try {
            Float tillstandSvaarighetsgrad = Float.parseFloat(getTillstaandetsSvaarighetsgradKod().getKod());
            Float riskMedAatgaerd = Float.parseFloat(getAatgaerdsRiskKod().getKod());
            Float patientnyttaEffektAatgerd = Float.parseFloat(getPatientnyttaEffektAatgaerdsKod().getKod());
            Float evidensPatientnytta = Float.parseFloat(getPatientnyttoEvidensKod().getKod());

            double result = (tillstandSvaarighetsgrad.floatValue() - 0.6);
            result += riskMedAatgaerd * 0.2;
            result += patientnyttaEffektAatgerd * 0.2;
            result += evidensPatientnytta * 0.2;

            return result;
        } catch (Exception e) {
            return null;
        }
    }

}
