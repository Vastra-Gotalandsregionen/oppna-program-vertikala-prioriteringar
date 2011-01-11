package se.vgregion.verticalprio.entity;

/**
 * This is the actual Priority object
 */
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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

    @Column(name = "vaentetid_besook_veckor")
    private Integer vaentetidBesookVeckor;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sektor_raad_id")
    private SektorRaad sektorRaad;

    @ManyToMany()
    @JoinTable(name = "link_prioriteringsobjekt_diagnos_kod", joinColumns = { @JoinColumn(name = "prio_id") }, inverseJoinColumns = { @JoinColumn(name = "diagnos_kod_id") })
    private List<DiagnosKod> diagnoser = new ArrayList<DiagnosKod>();

    @ManyToMany()
    @JoinTable(name = "link_prioriteringsobjekt_aatgaerds_kod", joinColumns = { @JoinColumn(name = "prio_id") }, inverseJoinColumns = { @JoinColumn(name = "aatgaerds_kod_id") })
    private List<AatgaerdsKod> aatgaerdskoder = new ArrayList<AatgaerdsKod>();

    @ManyToMany()
    @JoinTable(name = "link_prioriteringsobjekt_vaardforms_kod", joinColumns = { @JoinColumn(name = "prio_id") }, inverseJoinColumns = { @JoinColumn(name = "vaardforms_kod_id") })
    private List<VaardformsKod> vaardformskoder = new ArrayList<VaardformsKod>();

    @ManyToMany()
    @JoinTable(name = "link_prioriteringsobjekt_atc_kod", joinColumns = { @JoinColumn(name = "prio_id") }, inverseJoinColumns = { @JoinColumn(name = "atc_kod_id") })
    private List<AtcKod> atcKoder = new ArrayList<AtcKod>();

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

    @Column(name = "kommentar", length = 2000)
    private String kommentar;

    @Column(name = "indikation_gaf", length = 2000)
    private String indikationGaf;

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

    public List<String> getDiagnosTexts() {
        List<String> sb = new ArrayList<String>();
        for (DiagnosKod kod : getDiagnoser()) {
            sb.add(kod.getBeskrivning());
        }
        return sb;
    }

    public List<String> getDiagnosKodTexts() {
        List<String> sb = new ArrayList<String>();
        for (DiagnosKod kod : getDiagnoser()) {
            sb.add(kod.getKod());
        }
        return sb;
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

    public void setKommentar(String kommentar) {
        this.kommentar = kommentar;
    }

    public String getKommentar() {
        return kommentar;
    }

    public void setAtcKoder(List<AtcKod> atcKoder) {
        this.atcKoder = atcKoder;
    }

    public List<AtcKod> getAtcKoder() {
        return atcKoder;
    }

    public void setIndikationGaf(String indikationGaf) {
        this.indikationGaf = indikationGaf;
    }

    public String getIndikationGaf() {
        return indikationGaf;
    }

    public void setVaentetidBesookVeckor(Integer vaentetidBesookVeckor) {
        this.vaentetidBesookVeckor = vaentetidBesookVeckor;
    }

    public Integer getVaentetidBesookVeckor() {
        return vaentetidBesookVeckor;
    }

    // public static void main(String[] args) {
    // Prioriteringsobjekt prio = new Prioriteringsobjekt();
    // BeanMap bm = BeanMap.create(prio);
    // StringBuilder sb = new StringBuilder();
    //
    // for (Object key : bm.keySet()) {
    // String prop = (String) key;
    // sb.append("column = new se.vgregion.verticalprio.entity.Column();\n");
    // sb.append("column." + setterCall("name", quote(prop)) + "\n");
    // sb.append("column." + setterCall("label", quote("")) + "\n");
    // sb.append("column." + setterCall("displayOrder", "i++") + "\n");
    // sb.append("result.add(column); column.setId(i); column.setHideAble(true);\n\n");
    // }
    //
    // System.out.println(sb);
    // }
    //
    // private static String quote(String s) {
    // return '"' + s + '"';
    // }
    //
    // private static String setterCall(String prop, String arg) {
    // String result = "set" + prop.substring(0, 1).toUpperCase() + prop.substring(1) + "(";
    // result += arg + ");";
    // return result;
    // }
    //
    // private static String getterCall(String prop) {
    // String result = "set" + prop.substring(0, 1).toUpperCase() + prop.substring(1) + "();";
    // return result;
    // }

    private static List<se.vgregion.verticalprio.entity.Column> columns;

    public static List<se.vgregion.verticalprio.entity.Column> getDefaultColumns() {
        if (columns != null) {
            return columns;
        }
        se.vgregion.verticalprio.entity.Column column = null;
        List<se.vgregion.verticalprio.entity.Column> result = new ArrayList<se.vgregion.verticalprio.entity.Column>();
        int i = 0;

        column = new se.vgregion.verticalprio.entity.Column();
        column.setName("sektorRaad");
        column.setLabel("Sektorsråd");
        column.setDisplayOrder(i++);
        result.add(column);
        column.setId(i);
        column.setHideAble(false);

        column = new se.vgregion.verticalprio.entity.Column();
        column.setName("diagnosTexts");
        column.setLabel("Diagnostext");
        column.setColumnLabel("<a href='choose-codes-init?codeRefName=diagnosRef'>Diagnostext</a>");
        column.setDisplayOrder(i++);
        result.add(column);
        column.setId(i);
        column.setHideAble(false);

        column = new se.vgregion.verticalprio.entity.Column();
        column.setName("diagnosKodTexts");
        column.setLabel("Diagnoskodtext");
        column.setDisplayOrder(i++);
        result.add(column);
        column.setId(i);
        column.setHideAble(false);

        // Åtgärdsrisk saknas!

        column = new se.vgregion.verticalprio.entity.Column();
        column.setName("aatgaerdskoder");
        column.setLabel("Åtgärdskod");
        column.setColumnLabel("<a href='choose-codes-init?codeRefName=aatgaerdRef'>Åtgärdskod</a>");
        column.setDisplayOrder(i++);
        result.add(column);
        column.setId(i);
        column.setHideAble(false);

        column = new se.vgregion.verticalprio.entity.Column();
        column.setName("tillstaandetsSvaarighetsgradKod");
        column.setLabel("Tillståndets svårighetsgrad");
        column.setColumnLabel("<a href='choose-codes-init?codeRefName=tillstaandetsSvaarighetsgradRef'>Tillståndets svårighetsgrad</a>");
        column.setDisplayOrder(i++);
        result.add(column);
        column.setId(i);
        column.setHideAble(false);

        column = new se.vgregion.verticalprio.entity.Column();
        column.setName("rangordningsKod");
        column.setLabel("Rangordning");
        column.setColumnLabel("<a href='choose-codes-init?codeRefName=rangordningsRef'>Rangordning</a>");
        column.setDisplayOrder(i++);
        result.add(column);
        column.setId(i);
        column.setHideAble(false);

        // Lösa fält

        column = new se.vgregion.verticalprio.entity.Column();
        column.setName("indikationGaf");
        column.setLabel("Indikation / Gaf");
        column.setDisplayOrder(i++);
        result.add(column);
        column.setId(i);
        column.setHideAble(true);

        // Atc-text saknas!

        column = new se.vgregion.verticalprio.entity.Column();
        column.setName("atcKoder");
        column.setLabel("Atc-kod");
        column.setColumnLabel("<a href='choose-codes-init?codeRefName=atcKoderRef'>Atc-kod</a>");
        column.setDisplayOrder(i++);
        result.add(column);
        column.setId(i);
        column.setHideAble(true);

        column = new se.vgregion.verticalprio.entity.Column();
        column.setName("aatgaerdsRiskKod");
        column.setLabel("Åtgärdsrisk");
        column.setDisplayOrder(i++);
        result.add(column);
        column.setId(i);
        column.setHideAble(true);

        column = new se.vgregion.verticalprio.entity.Column();
        column.setName("patientnyttaEffektAatgaerdsKod");
        column.setLabel("Patientnytta effekt/åtgärd");
        column.setDisplayOrder(i++);
        result.add(column);
        column.setId(i);
        column.setHideAble(true);

        column = new se.vgregion.verticalprio.entity.Column();
        column.setName("patientnyttoEvidensKod");
        column.setLabel("Patientnytta evidens");
        column.setDisplayOrder(i++);
        result.add(column);
        column.setId(i);
        column.setHideAble(true);

        // Kostnad vunnet levnadsår Qaly saknas

        column = new se.vgregion.verticalprio.entity.Column();
        column.setName("haelsonekonomiskEvidensKod");
        column.setLabel("Hälsoekonomisk evidens");
        column.setDisplayOrder(i++);
        result.add(column);
        column.setId(i);
        column.setHideAble(true);

        column = new se.vgregion.verticalprio.entity.Column();
        column.setName("vaentetidBesookVeckor");
        column.setLabel("Besöksväntetid veckor");
        column.setDisplayOrder(i++);
        result.add(column);
        column.setId(i);
        column.setHideAble(true);

        column = new se.vgregion.verticalprio.entity.Column();
        column.setName("vaentetidsKod");
        column.setLabel("Väntettidskod");
        column.setDisplayOrder(i++);
        result.add(column);
        column.setId(i);
        column.setHideAble(true);

        column = new se.vgregion.verticalprio.entity.Column();
        column.setName("vaentetidVeckor");
        column.setLabel("Väntetid veckor");
        column.setDisplayOrder(i++);
        result.add(column);
        column.setId(i);
        column.setHideAble(true);

        column = new se.vgregion.verticalprio.entity.Column();
        column.setName("vaardnivaaKod");
        column.setLabel("Vårdnivå");
        column.setDisplayOrder(i++);
        result.add(column);
        column.setId(i);
        column.setHideAble(true);

        column = new se.vgregion.verticalprio.entity.Column();
        column.setName("vaardformskoder");
        column.setLabel("Vårdform");
        column.setColumnLabel("<a href='choose-codes-init?codeRefName=vaardformskoderRef'>Vårdform</a>");
        column.setDisplayOrder(i++);
        result.add(column);
        column.setId(i);
        column.setHideAble(true);

        // Rangornding enligt formel
        // Rangordning

        column = new se.vgregion.verticalprio.entity.Column();
        column.setName("kommentar");
        column.setLabel("Kommentar");
        column.setDisplayOrder(i++);
        result.add(column);
        column.setId(i);
        column.setHideAble(true);

        columns = result;

        return result;
    }

}
