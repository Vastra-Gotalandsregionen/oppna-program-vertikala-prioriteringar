package se.vgregion.verticalprio.entity;

/**
 * This is the actual Priority object
 */
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.apache.commons.collections.BeanMap;

@Entity
@Table(name = "prioriteringsobjekt")
public class Prioriteringsobjekt extends AbstractPrioriteringsobjekt {

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "link_prioriteringsobjekt_diagnos_kod", joinColumns = { @JoinColumn(name = "prio_id") }, inverseJoinColumns = { @JoinColumn(name = "diagnos_kod_id") })
    protected Set<DiagnosKod> diagnoser = new HashSet<DiagnosKod>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "link_prioriteringsobjekt_aatgaerds_kod", joinColumns = { @JoinColumn(name = "prio_id") }, inverseJoinColumns = { @JoinColumn(name = "aatgaerds_kod_id") })
    protected Set<AatgaerdsKod> aatgaerdskoder = new HashSet<AatgaerdsKod>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "link_prioriteringsobjekt_atc_kod", joinColumns = { @JoinColumn(name = "prio_id") }, inverseJoinColumns = { @JoinColumn(name = "atc_kod_id") })
    protected Set<AtcKod> atcKoder = new HashSet<AtcKod>();

    public void godkaen() throws IllegalAccessError {
        if (mkMessagesWhyNotToApprovePrio() != null) {
            throw new IllegalAccessError(mkMessagesWhyNotToApprovePrio());
        }
        setGodkaend(new Date());
    }

    public void underkann() {
        setGodkaend(null);
    }

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
        // column.setColumnLabel("<a href='choose-codes-init?codeRefName=diagnosRef'><img src='img/filter.gif'/></a>");
        // column.setColumnLabel("<a href='start-choosing-codes?fieldName=diagnosTexts'><img src='img/filter.gif'/></a>");
        column.setFilterAble(true);
        column.setDisplayOrder(i++);
        result.add(column);
        column.setId(i);
        column.setHideAble(false);
        column.setSortField("diagnoser.kod");
        column.setSortable(true);

        column = new se.vgregion.verticalprio.entity.Column();
        column.setName("diagnosKodTexts");
        column.setLabel("Symptom / Diagnoskod");
        // column.setColumnLabel("<a href='start-choosing-codes?fieldName=diagnosKodTexts'><img src='img/filter.gif'/></a>");
        column.setFilterAble(true);
        column.setDisplayOrder(i++);
        result.add(column);
        column.setId(i);
        column.setHideAble(true);
        // column.setSortField("");
        // column.setSortable(true);
        // Åtgärdstext saknas!

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
        column.setDescription("* Evidensstyrka 1. \n Starkt vetenskapligt underlag. En slutsats stöds av minst två studier med högt bevisvärde. Om det finns studier som talat emot slutsatsen kan dock evidensstyrkan bli lägre. \n\n"
                + "* Evidensstyrka 2. \n Måttligt starkt vetenskapligt underlag. Minst en studie med högt bevisvärde och två med medelhögt bevisvärde. Vid ny indikation inom närliggande terapiområde krävs minst en stor studie med högt bevisvärde, och viss stöddokumentation.\n\n"
                + "* Evidensstyrka 3. \n Begränsat vetenskapligt underlag. Minst två studier med medelhögt bevisvärde.\n\n"
                + "* Evidensstyrka 4. \n Otillräckligt vetenskapligt stöd. Studier som uppfyller krav på bevisvärde saknas alternativt motsägande underlag.\n\n");

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

        columns = result;

        return result;
    }

    public PrioriteringsobjektUtkast mkNewUtkast() {
        PrioriteringsobjektUtkast result = new PrioriteringsobjektUtkast();
        BeanMap resultMap = new BeanMap(result);
        BeanMap thisMap = new BeanMap(this);

        for (Object key : thisMap.keySet()) {
            if (resultMap.containsKey(key) && resultMap.getWriteMethod(key.toString()) != null) {
                Object value = thisMap.get(key);
                if (!(value instanceof Collection)) {
                    resultMap.put(key, value);
                }
            }
        }

        result.setId(null);
        result.setSkarpVersion(this);

        clearCloneAndSetCollectionItems(getAatgaerdskoder(), result.getAatgaerdskoder());
        clearCloneAndSetCollectionItems(getAtcKoder(), result.getAtcKoder());
        clearCloneAndSetCollectionItems(getDiagnoser(), result.getDiagnoser());

        return result;
    }

    @SuppressWarnings("unchecked")
    private <T extends AbstractKod> void clearCloneAndSetCollectionItems(Set<T> source, Set<T> target) {
        target.clear();
        for (T item : source) {
            target.add((T) item.clone());
        }
    }

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

}
