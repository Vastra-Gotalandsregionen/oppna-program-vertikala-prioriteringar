package se.vgregion.verticalprio.entity;

/**
 * This is the actual Priority object
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import se.vgregion.dao.domain.patterns.entity.AbstractEntity;

@Entity
@Table(name = "prioriteringsobjekt")
public class Prioriteringsobjekt extends AbstractEntity<Long> implements Serializable {

	private static final long serialVersionUID = 1L;

	public Prioriteringsobjekt() {
	}

	public Prioriteringsobjekt(Long id) {
		setId(id);
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@OneToMany()
	@JoinColumn(name = "parent_id")
	@Fetch(FetchMode.JOIN)
	private Set<Prioriteringsobjekt> children = new HashSet<Prioriteringsobjekt>();

	@Column(name = "parent_id", updatable = false)
	private Long parentId;

	@ManyToOne()
	@Fetch(FetchMode.JOIN)
	@JoinColumn(name = "vaentetid_besook_veckor_kod_id")
	private VaentetidsKod vaentetidBesookVeckor;

	@ManyToOne()
	@Fetch(FetchMode.JOIN)
	@JoinColumn(name = "sektor_raad_id")
	private SektorRaad sektorRaad;

	@ManyToOne()
	@Fetch(FetchMode.JOIN)
	@JoinColumn(name = "kostnad_levnadsaar_kod_id")
	private KostnadLevnadsaarKod kostnadLevnadsaarKod;

	@Transient
	private Integer kostnad;

	@Transient
	private Integer volym;

	@Column(name = "godkaend")
	private Date godkaend;

	@Column(name = "senast_uppdaterad")
	private Date senastUppdaterad;

	@ManyToMany()
	@JoinTable(name = "link_prioriteringsobjekt_diagnos_kod", joinColumns = { @JoinColumn(name = "prio_id") }, inverseJoinColumns = { @JoinColumn(name = "diagnos_kod_id") })
	private Set<DiagnosKod> diagnoser = new HashSet<DiagnosKod>();

	// @OneToMany
	// @JoinColumn(name = "prio_id", referencedColumnName = "id")
	// private List<LinkPrioriteringsobjektDiagnosKod> linkPrioriteringsobjektDiagnosKoder;

	@ManyToMany()
	@JoinTable(name = "link_prioriteringsobjekt_aatgaerds_kod", joinColumns = { @JoinColumn(name = "prio_id") }, inverseJoinColumns = { @JoinColumn(name = "aatgaerds_kod_id") })
	private Set<AatgaerdsKod> aatgaerdskoder = new HashSet<AatgaerdsKod>();

	@ManyToMany()
	@JoinTable(name = "link_prioriteringsobjekt_atc_kod", joinColumns = { @JoinColumn(name = "prio_id") }, inverseJoinColumns = { @JoinColumn(name = "atc_kod_id") })
	private Set<AtcKod> atcKoder = new HashSet<AtcKod>();

	@ManyToOne
	@Fetch(FetchMode.JOIN)
	@JoinColumn(name = "vaardforms_kod_id")
	private VaardformsKod vaardform;

	@ManyToOne()
	@Fetch(FetchMode.JOIN)
	@JoinColumn(name = "tillstaandets_svaarighetsgrad_kod_id")
	private TillstaandetsSvaarighetsgradKod tillstaandetsSvaarighetsgradKod;

	@ManyToOne()
	@Fetch(FetchMode.JOIN)
	@JoinColumn(name = "vaentetid_behandling_veckor_kod_id")
	private VaentetidsKod vaentetidBehandlingVeckor;

	@ManyToOne()
	@Fetch(FetchMode.JOIN)
	@JoinColumn(name = "rangordnings_kod_id")
	private RangordningsKod rangordningsKod;

	@ManyToOne()
	@Fetch(FetchMode.JOIN)
	@JoinColumn(name = "vaardnivaa_kod_id")
	private VaardnivaaKod vaardnivaaKod;

	@ManyToOne()
	@Fetch(FetchMode.JOIN)
	@JoinColumn(name = "haelsonekonomisk_evidens_kod_id")
	private HaelsonekonomiskEvidensKod haelsonekonomiskEvidensKod;

	@ManyToOne()
	@Fetch(FetchMode.JOIN)
	@JoinColumn(name = "aatgaerds_risk_kod_id")
	private AatgaerdsRiskKod aatgaerdsRiskKod;

	@ManyToOne()
	@Fetch(FetchMode.JOIN)
	@JoinColumn(name = "patientnytto_evidens_kod_id")
	private PatientnyttoEvidensKod patientnyttoEvidensKod;

	@ManyToOne()
	@Fetch(FetchMode.JOIN)
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

	public Set<DiagnosKod> getDiagnoser() {
		return diagnoser;
	}

	public void setDiagnoser(Set<DiagnosKod> diagnoser) {
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

	public Set<AatgaerdsKod> getAatgaerdskoder() {
		return aatgaerdskoder;
	}

	public void setAatgaerdskoder(Set<AatgaerdsKod> aatgaerdskoder) {
		this.aatgaerdskoder = aatgaerdskoder;
	}

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

	public void setAtcKoder(Set<AtcKod> atcKoder) {
		this.atcKoder = atcKoder;
	}

	public Set<AtcKod> getAtcKoder() {
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
		column.setName("id");
		column.setLabel("Id");
		column.setDisplayOrder(i++);
		result.add(column);
		column.setId(i);
		column.setHideAble(true);
		column.setSortable(true);
		column.setSorting(false);

		column = new se.vgregion.verticalprio.entity.Column();
		column.setName("sektorRaad");
		column.setLabel("Sektorsråd");
		column.setDisplayOrder(i++);
		result.add(column);
		column.setId(i);
		column.setHideAble(false);
		column.setSortable(true);
		column.setSorting(false);

		column = new se.vgregion.verticalprio.entity.Column();
		column.setName("diagnosTexts");
		column.setLabel("Symptom / Diagnostext");
		column.setFilterAble(true);
		column.setDisplayOrder(i++);
		result.add(column);
		column.setId(i);
		column.setHideAble(false);

		column = new se.vgregion.verticalprio.entity.Column();
		column.setName("diagnoser");
		column.setLabel("Diagnoser");
		column.setSortField("diagnoser.kod");
		result.add(column);
		column.setId(i++);
		column.setHideAble(false);
		column.setPossibleInOverview(false);

		column = new se.vgregion.verticalprio.entity.Column();
		column.setName("diagnosKodTexts");
		column.setLabel("Symptom / Diagnoskod");
		column.setFilterAble(true);
		column.setDisplayOrder(i++);
		result.add(column);
		column.setId(i);
		column.setHideAble(true);
		column.setSortable(true);

		column = new se.vgregion.verticalprio.entity.Column();
		column.setName("aatgaerdskoderTexts");
		column.setLabel("Åtgärdstext");
		// column.setColumnLabel("<a href='start-choosing-codes?fieldName=aatgaerdskoderTexts'><img src='img/filter.gif'/></a>");
		column.setFilterAble(true);
		column.setDisplayOrder(i++);
		result.add(column);
		column.setId(i);
		column.setHideAble(false);

		column = new se.vgregion.verticalprio.entity.Column();
		column.setName("aatgaerdskoder");
		column.setLabel("Åtgärdskod");
		// column.setColumnLabel("<a href='choose-codes-init?codeRefName=aatgaerdRef'><img src='img/filter.gif'/></a>");
		// column.setColumnLabel("<a href='start-choosing-codes?fieldName=aatgaerdskoder'><img src='img/filter.gif'/></a>");
		column.setFilterAble(true);
		column.setDisplayOrder(i++);
		result.add(column);
		column.setId(i);
		column.setHideAble(true);

		column = new se.vgregion.verticalprio.entity.Column();
		column.setName("tillstaandetsSvaarighetsgradKod");
		column.setLabel("Tillståndets svårighetsgrad");
		// column.setColumnLabel("<a href='choose-codes-init?codeRefName=tillstaandetsSvaarighetsgradRef'><img src='img/filter.gif'/></a>");
		// column.setColumnLabel("<a href='start-choosing-codes?fieldName=tillstaandetsSvaarighetsgrad'><img src='img/filter.gif'/></a>");
		column.setFilterAble(true);
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
		column.setSortable(true);

		column = new se.vgregion.verticalprio.entity.Column();
		column.setName("rangordningsKod");
		column.setLabel("Rang- ordning");
		// column.setColumnLabel("<a href='choose-codes-init?codeRefName=rangordningsRef'><img src='img/filter.gif'/></a>");
		// column.setColumnLabel("<a href='start-choosing-codes?fieldName=rangordningsKod'><img src='img/filter.gif'/></a>");
		column.setFilterAble(true);
		column.setDisplayOrder(i++);
		result.add(column);
		column.setId(i);
		column.setHideAble(false);
		column.setDescription("1 Rangordning 1. \n" + "2 Rangordning 2. \n" + "3 Rangordning 3. \n"
		        + "4 Rangordning 4. \n" + "5 Rangordning 5. \n" + "6 Rangordning 6. \n" + "7 Rangordning 7. \n"
		        + "8 Rangordning 8. \n" + "9 Rangordning 9. \n" + "10 Rangordning 10. \n" + "11 FoU. \n"
		        + "12 Icke göra. \n");
		column.setSortable(true);

		// Lösa fält

		column = new se.vgregion.verticalprio.entity.Column();
		column.setName("indikationGaf");
		column.setLabel("Indikation - GAF");
		column.setDisplayOrder(i++);
		result.add(column);
		column.setId(i);
		column.setHideAble(true);
		column.setDescription("Indikation för viss åtgärd\nMax 3 nivåer");

		column = new se.vgregion.verticalprio.entity.Column();
		column.setName("atcText");
		column.setLabel("ATC-text");
		// column.setColumnLabel("<a href='start-choosing-codes?fieldName=atcText'><img src='img/filter.gif'/></a>");
		// column.setColumnLabel("<a href='choose-codes-init?codeRefName=atcKoderRef'><img src='img/filter.gif'/></a>");
		column.setFilterAble(true);
		column.setDisplayOrder(i++);
		result.add(column);
		column.setId(i);
		column.setHideAble(true);

		column = new se.vgregion.verticalprio.entity.Column();
		column.setName("atcKoder");
		column.setLabel("ATC-kod");
		// column.setColumnLabel("<a href='choose-codes-init?codeRefName=atcKoderRef'><img src='img/filter.gif'/></a>");
		// column.setColumnLabel("<a href='start-choosing-codes?fieldName=atcKoder'><img src='img/filter.gif'/></a>");
		column.setFilterAble(true);
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
		column.setDescription("1 Sjuklighet, död kan förhindras. Tillståndet kan botas. \n"
		        + "2 Sjukligheten påverkas mycket, överlevnaden förlängs. \n"
		        + "3 Sjukligheten påverkas i måttlig utsträckning. \n"
		        + "4 Sjukligheten påverkas i liten utsträckning. \n");

		column = new se.vgregion.verticalprio.entity.Column();
		column.setName("patientnyttoEvidensKod");
		column.setLabel("Evidens patientnytta / effekt åtgärd");
		column.setDisplayOrder(i++);
		result.add(column);
		column.setId(i);
		column.setHideAble(true);
		column.setDescription("* Evidensstyrka 1. \n Starkt vetenskapligt underlag. En slutsats stöds av minst två studier med högt bevisvärde. Om det finns studier som talat emot slutsatsen kan dock evidensstyrkan bli lägre. \n\n"
		        + "* Evidensstyrka 2. \n Måttligt starkt vetenskapligt underlag. Minst en studie med högt bevisvärde och två med medelhögt bevisvärde. Vid ny indikation inom närliggande terapiområde krävs minst en stor studie med högt bevisvärde, och viss stöddokumentation.\n\n"
		        + "* Evidensstyrka 3. \n Begränsat vetenskapligt underlag. Minst två studier med medelhögt bevisvärde.\n\n"
		        + "* Evidensstyrka 4. \n Otillräckligt vetenskapligt stöd. Studier som uppfyller krav på bevisvärde saknas alternativt motsägande underlag.\n\n");

		column = new se.vgregion.verticalprio.entity.Column();
		column.setName("kostnadLevnadsaarKod");
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
		column.setDescription("Medicinsk väntetid\nVälj 0-52 veckor eller mer än 52 veckor");

		column = new se.vgregion.verticalprio.entity.Column();
		column.setName("vaentetidBehandlingVeckor");
		column.setLabel("Väntetid veckor behandling");
		column.setDisplayOrder(i++);
		result.add(column);
		column.setId(i);
		column.setHideAble(true);
		column.setDescription("Medicinsk väntetid\nVälj 0-52 veckor eller mer än 52 veckor");

		column = new se.vgregion.verticalprio.entity.Column();
		column.setName("vaardnivaaKod");
		column.setLabel("Vårdnivå");
		column.setFilterAble(true);
		column.setDisplayOrder(i++);
		result.add(column);
		column.setId(i);
		column.setHideAble(true);

		column = new se.vgregion.verticalprio.entity.Column();
		column.setName("vaardform");
		column.setLabel("Vårdform");
		// column.setColumnLabel("<a href='choose-codes-init?codeRefName=vaardformRef'><img src='img/filter.gif'/></a>");
		// column.setColumnLabel("<a href='start-choosing-codes?fieldName=vaardform'><img src='img/filter.gif'/></a>");
		column.setFilterAble(true);
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

		column = new se.vgregion.verticalprio.entity.Column();
		column.setName("godkaend");
		column.setLabel("Godkänd");
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
		column.setDemandsEditRights(true);

		columns = result;

		return result;
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

	public Date getGodkaend() {
		return godkaend;
	}

	public void setGodkaend(Date godkaend) {
		this.godkaend = godkaend;
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
		Prioriteringsobjekt prio = this;
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
		Prioriteringsobjekt prio = this;
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

	public void godkaen() throws IllegalAccessError {
		if (mkMessagesWhyNotToApprovePrio() != null) {
			throw new IllegalAccessError(mkMessagesWhyNotToApprovePrio());
		}
		setGodkaend(new Date());
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

	public void setChildren(Set<Prioriteringsobjekt> children) {
		this.children = children;
	}

	public Set<Prioriteringsobjekt> getChildren() {
		return children;
	}

	public Prioriteringsobjekt getChild() {
		if (children == null || children.isEmpty()) {
			return null;
		}
		return children.iterator().next();
	}

	public boolean isDraft() {
		return getParentId() == null;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public Long getParentId() {
		return parentId;
	}

}
