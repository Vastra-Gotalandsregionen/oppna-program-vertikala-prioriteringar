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

    @ManyToOne
    @JoinColumn(name = "vaentetid_besook_veckor_kod_id")
    private VaentetidsKod vaentetidBesookVeckor;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sektor_raad_id")
    private SektorRaad sektorRaad;

    @Column(name = "qualy")
    private Integer qualy;

    @Column(name = "rangordning_enligt_formel")
    private Integer rangordningEnligtFormel;

    private Integer kostnad;

    private Integer volym;

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
    @JoinColumn(name = "vaentetid_behandling_veckor_kod_id")
    private VaentetidsKod vaentetidBehandlingVeckor;

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

    @Column(name = "kommentar", length = 2000)
    private String kommentar;

    @Column(name = "indikation_gaf", length = 2000)
    private String indikationGaf;

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
        return mkBeskrivningsText(getDiagnoser());
    }

    public List<String> getDiagnosKodTexts() {
        List<String> sb = new ArrayList<String>();
        for (DiagnosKod kod : getDiagnoser()) {
            sb.add(kod.getKod());
        }
        return sb;
    }

    private List<String> mkBeskrivningsText(List<? extends AbstractKod> koder) {
        List<String> sb = new ArrayList<String>();
        if (koder == null || koder.isEmpty()) {
            return sb;
        }
        for (AbstractKod kod : koder) {
            sb.add(kod.getBeskrivning());
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

    public void setAtcKoder(List<AtcKod> atcKoder) {
        this.atcKoder = atcKoder;
    }

    public List<AtcKod> getAtcKoder() {
        return atcKoder;
    }

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
        column.setLabel("Symptom / Diagnostext");
        column.setColumnLabel("<a href='choose-codes-init?codeRefName=diagnosRef'>Symptom / Diagnostext</a>");
        column.setDisplayOrder(i++);
        result.add(column);
        column.setId(i);
        column.setHideAble(false);

        column = new se.vgregion.verticalprio.entity.Column();
        column.setName("diagnosKodTexts");
        column.setLabel("Symptom / Diagnoskod");
        column.setDisplayOrder(i++);
        result.add(column);
        column.setId(i);
        column.setHideAble(false);

        // Åtgärdstext saknas!

        column = new se.vgregion.verticalprio.entity.Column();
        column.setName("aatgaerdskoder");
        column.setLabel("Åtgärdskod");
        column.setColumnLabel("<a href='choose-codes-init?codeRefName=aatgaerdRef'>Åtgärdskod</a>");
        column.setDisplayOrder(i++);
        result.add(column);
        column.setId(i);
        column.setHideAble(true);

        column = new se.vgregion.verticalprio.entity.Column();
        column.setName("tillstaandetsSvaarighetsgradKod");
        column.setLabel("Tillståndets svårighetsgrad");
        column.setColumnLabel("<a href='choose-codes-init?codeRefName=tillstaandetsSvaarighetsgradRef'>Tillståndets svårighetsgrad</a>");
        column.setDisplayOrder(i++);
        result.add(column);
        column.setId(i);
        column.setHideAble(false);
        column.setDescription("* 1 Omedelbart livshot. \n"
                + "* 2 Risk för mycket allvarlig skada, för tidig död, betydande invaliditet, outhärdlig situation. \n"
                + "* 3 Risk för allvarlig skada, bestående men eller mycket låg livskvalitet. \n"
                + "* 4 Risk för förväntad försämring, ej vidmakthållen funktion-ADL-nivå. \n"
                + "* 5 Risk för betydande olägenhet, ökad sjuklighet, förlängd sjukdomsperiod, sänkt livskvalitet. \n"
                + "* 6 Risk för olägenhet, skada, bestående men eller låg livskvalitet. \n"
                + "* 7 Sannolik ökad risk för försämrad hälsoupplevelse eller icke optimal livskvalitet. \n"
                + "* 8 Möjligen ökad risk sjuklighet, försämring av funktionsnivå eller livskvalitet. \n"
                + "* 9 Risk för sänkt livskvalitet enligt patientens uppfattning och vetenskap och beprövad kunskap inte motsäger detta. \n");

        column = new se.vgregion.verticalprio.entity.Column();
        column.setName("rangordningsKod");
        column.setLabel("Rangordning");
        column.setColumnLabel("<a href='choose-codes-init?codeRefName=rangordningsRef'>Rangordning</a>");
        column.setDisplayOrder(i++);
        result.add(column);
        column.setId(i);
        column.setHideAble(false);
        column.setDescription("1 Rangordning 1. \n" + "2 Rangordning 2. \n" + "3 Rangordning 3. \n"
                + "4 Rangordning 4. \n" + "5 Rangordning 5. \n" + "6 Rangordning 6. \n" + "7 Rangordning 7. \n"
                + "8 Rangordning 8. \n" + "9 Rangordning 9. \n" + "10 Rangordning 10. \n" + "11 FoU. \n"
                + "12 Icke göra. \n");

        // Lösa fält

        column = new se.vgregion.verticalprio.entity.Column();
        column.setName("indikationGaf");
        column.setLabel("Indikation - GAF");
        column.setDisplayOrder(i++);
        result.add(column);
        column.setId(i);
        column.setHideAble(true);

        column = new se.vgregion.verticalprio.entity.Column();
        column.setName("atcText");
        column.setLabel("ATC-text");
        column.setColumnLabel("<a href='choose-codes-init?codeRefName=atcKoderRef'>ATC-text</a>");
        column.setDisplayOrder(i++);
        result.add(column);
        column.setId(i);
        column.setHideAble(true);

        column = new se.vgregion.verticalprio.entity.Column();
        column.setName("atcKoder");
        column.setLabel("ATC-kod");
        column.setColumnLabel("<a href='choose-codes-init?codeRefName=atcKoderRef'>ATC-kod</a>");
        column.setDisplayOrder(i++);
        result.add(column);
        column.setId(i);
        column.setHideAble(true);

        column = new se.vgregion.verticalprio.entity.Column();
        column.setName("aatgaerdsRiskKod");
        column.setLabel("Risk med åtgärd");
        column.setDisplayOrder(i++);
        result.add(column);
        column.setId(i);
        column.setHideAble(true);
        column.setDescription("1 Liten risk för allvarliga biverkningar/komplikationer. \n"
                + "2 Måttlig risk för allvarliga biverkningar / komplikationer. \n"
                + "3 Biverkningar / komplikationer som kan kräva sjukvårdsinsatser. \n"
                + "4 Mycket hög risk för allvarliga biverkningar / komplikationer som påverkar livskvalitet och funktion. Biverkningar / komplikationer som ofta kväver sjukvårdsinsatser. \n");

        column = new se.vgregion.verticalprio.entity.Column();
        column.setName("patientnyttaEffektAatgaerdsKod");
        column.setLabel("Patientnytta / effekt åtgärd");
        column.setDisplayOrder(i++);
        result.add(column);
        column.setId(i);
        column.setHideAble(true);
        column.setDescription("* Evidensstyrka 1. Starkt vetenskapligt underlag. En slutsats stöds av minst två studier med högt bevisvärde. Om det finns studier som talat emot slutsatsen kan dock evidensstyrkan bli lägre. \n"
                + "* Evidensstyrka 2. Måttligt starkt vetenskapligt underlag. Minst en studie med högt bevisvärde och två med medelhögt bevisvärde. Vid ny indikation inom närliggande terapiområde krävs minst en stor studie med högt bevisvärde, och viss stöddokumentation. \n"
                + "* Evidensstyrka 3. Begränsat vetenskapligt underlag. Minst två studier med medelhögt bevisvärde. \n"
                + "* Evidensstyrka 4. Otillräckligt vetenskapligt stöd. Studier som uppfyller krav på bevisvärde saknas alternativt motsägande underlag.");

        column = new se.vgregion.verticalprio.entity.Column();
        column.setName("patientnyttoEvidensKod");
        column.setLabel("Evidens patientnytta / effekt åtgärd");
        column.setDisplayOrder(i++);
        result.add(column);
        column.setId(i);
        column.setHideAble(true);
        column.setDescription("1 Sjuklighet, död kan förhindras. Tillståndet kan botas. \n"
                + "2 Sjukligheten påverkas mycket, överlevnaden förlängs. \n"
                + "3 Sjukligheten påverkas i måttlig utsträckning. \n"
                + "4 Sjukligheten påverkas i liten utsträckning. \n");

        column = new se.vgregion.verticalprio.entity.Column();
        column.setName("qualy");
        column.setLabel("Kostnad vunnet levnadsår Qaly");
        column.setDisplayOrder(i++);
        result.add(column);
        column.setId(i);
        column.setHideAble(true);
        column.setDescription("1 <100.000 kr/Qaly alt vunnet levnadsår. \n"
                + "2 <500.000 kr/Qaly alt vunnet levnadsår. \n"
                + "3 < eller= 1.000.000 kr/Qaly alt vunnet levnadsår. \n" + "4 Ej bedömbar. \n");

        column = new se.vgregion.verticalprio.entity.Column();
        column.setName("haelsonekonomiskEvidensKod");
        column.setLabel("Hälso- ekonomisk evidens");
        column.setDisplayOrder(i++);
        result.add(column);
        column.setId(i);
        column.setHideAble(true);
        column.setDescription("1 Studie av god kvalitet med robusta resultat. \n"
                + "2 Studie av godtagbar kvalitet. \n" + "3 Egna kalkyler. \n" + "4 Egna bedömningar. \n");

        column = new se.vgregion.verticalprio.entity.Column();
        column.setName("vaentetidBesookVeckor");
        column.setLabel("Väntetid veckor besök");
        column.setDisplayOrder(i++);
        result.add(column);
        column.setId(i);
        column.setHideAble(true);

        column = new se.vgregion.verticalprio.entity.Column();
        column.setName("vaentetidBehandlingVeckor");
        column.setLabel("Väntetid veckor behandling");
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

        column = new se.vgregion.verticalprio.entity.Column();
        column.setName("rangordningEnligtFormel");
        column.setLabel("Rangordning enligt formel");
        column.setDisplayOrder(i++);
        result.add(column);
        column.setId(i);
        column.setHideAble(true);

        column = new se.vgregion.verticalprio.entity.Column();
        column.setName("kommentar");
        column.setLabel("Kommentar");
        column.setDisplayOrder(i++);
        result.add(column);
        column.setId(i);
        column.setHideAble(true);

        column = new se.vgregion.verticalprio.entity.Column();
        column.setName("kostnad");
        column.setLabel("Kostnad");
        column.setDisplayOrder(i++);
        result.add(column);
        column.setId(i);
        column.setHideAble(true);

        column = new se.vgregion.verticalprio.entity.Column();
        column.setName("volym");
        column.setLabel("Volym");
        column.setDisplayOrder(i++);
        result.add(column);
        column.setId(i);
        column.setHideAble(true);

        columns = result;

        return result;
    }

    public void setQualy(Integer qualy) {
        this.qualy = qualy;
    }

    public Integer getQualy() {
        return qualy;
    }

    public void setRangordningEnligtFormel(Integer rangordningEnligtFormel) {
        this.rangordningEnligtFormel = rangordningEnligtFormel;
    }

    public Integer getRangordningEnligtFormel() {
        return rangordningEnligtFormel;
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

}
