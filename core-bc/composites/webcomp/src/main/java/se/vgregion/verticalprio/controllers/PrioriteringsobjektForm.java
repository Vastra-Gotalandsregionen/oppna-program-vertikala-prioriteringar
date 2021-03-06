package se.vgregion.verticalprio.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Transient;

import org.apache.commons.beanutils.BeanMap;

import se.vgregion.verticalprio.entity.AatgaerdsRiskKod;
import se.vgregion.verticalprio.entity.AbstractHirarkiskKod;
import se.vgregion.verticalprio.entity.AbstractKod;
import se.vgregion.verticalprio.entity.Column;
import se.vgregion.verticalprio.entity.DiagnosKod;
import se.vgregion.verticalprio.entity.HaelsonekonomiskEvidensKod;
import se.vgregion.verticalprio.entity.KostnadLevnadsaarKod;
import se.vgregion.verticalprio.entity.PatientnyttaEffektAatgaerdsKod;
import se.vgregion.verticalprio.entity.PatientnyttoEvidensKod;
import se.vgregion.verticalprio.entity.Prioriteringsobjekt;
import se.vgregion.verticalprio.entity.RangordningsKod;
import se.vgregion.verticalprio.entity.SektorRaad;
import se.vgregion.verticalprio.entity.TillstaandetsSvaarighetsgradKod;
import se.vgregion.verticalprio.entity.VaardformsKod;
import se.vgregion.verticalprio.entity.VaardnivaaKod;
import se.vgregion.verticalprio.entity.VaentetidsKod;

/**
 * @author Claes Lundahl, vgrid=clalu4
 * 
 */
public class PrioriteringsobjektForm extends Prioriteringsobjekt {

    @Transient
    private Long kostnadLevnadsaarKodId;
    @Transient
    private List<KostnadLevnadsaarKod> kostnadLevnadsaarKodList;
    @Transient
    private Long patientnyttaEffektAatgaerdsKodId;
    @Transient
    private List<PatientnyttaEffektAatgaerdsKod> patientnyttaEffektAatgaerdsKodList;
    @Transient
    private Long sektorRaadId;
    @Transient
    private List<SektorRaad> sektorRaadList;
    @Transient
    private Long patientnyttoEvidensKodId;
    @Transient
    private List<PatientnyttoEvidensKod> patientnyttoEvidensKodList;
    @Transient
    private Long tillstaandetsSvaarighetsgradKodId;
    @Transient
    private List<TillstaandetsSvaarighetsgradKod> tillstaandetsSvaarighetsgradKodList;
    @Transient
    private Long vaentetidBesookVeckorId;
    @Transient
    private List<VaentetidsKod> vaentetidBesookVeckorList;
    @Transient
    private Long vaentetidBehandlingVeckorId;
    @Transient
    private List<VaentetidsKod> vaentetidBehandlingVeckorList;
    @Transient
    private Long haelsonekonomiskEvidensKodId;
    @Transient
    private List<HaelsonekonomiskEvidensKod> haelsonekonomiskEvidensKodList;
    @Transient
    private Long vaardnivaaKodId;
    @Transient
    private List<VaardnivaaKod> vaardnivaaKodList;
    @Transient
    private Long vaentetidsKodId;
    // @Transient
    // private List<VaentetidsKod> vaentetidsKodList;
    private Long vaardformId;
    @Transient
    private List<VaardformsKod> vaardformList = new ArrayList<VaardformsKod>();
    @Transient
    private Long aatgaerdsRiskKodId;
    @Transient
    private List<AatgaerdsRiskKod> aatgaerdsRiskKodList;
    @Transient
    private Long rangordningsKodId;
    @Transient
    private List<RangordningsKod> rangordningsKodList;
    @Transient
    private Map<String, Column> columns = new HashMap<String, Column>();
    private List<DiagnosKod> diagnoserList;

    private Prioriteringsobjekt skarpVersion;

    private PrioriteringsobjektForm unalteredVersion;

    // @Transient
    // @SuppressWarnings("serial")
    // ManyCodesRef<AatgaerdsKod> aatgaerdRef = new ManyCodesRef<AatgaerdsKod>() {
    // @Override
    // public Set<AatgaerdsKod> getCodes() {
    // return getAatgaerdskoder();
    // }
    //
    // @Override
    // public void setCodes(Set<AatgaerdsKod> codes) {
    // setAatgaerdskoder(codes);
    // }
    // };
    //
    // @Transient
    // @SuppressWarnings("serial")
    // private ManyCodesRef<DiagnosKod> diagnosRef = new ManyCodesRef<DiagnosKod>() {
    // @Override
    // public Set<DiagnosKod> getCodes() {
    // return getDiagnoser();
    // }
    //
    // @Override
    // public void setCodes(Set<DiagnosKod> codes) {
    // setDiagnoser(codes);
    // }
    // };

    // @Transient
    // @SuppressWarnings("serial")
    // private ManyCodesRef<VaardformsKod> vaardformskoderRef = new ManyCodesRef<VaardformsKod>() {
    // @Override
    // public List<VaardformsKod> getCodes() {
    // return getVaardformskoder();
    // }
    //
    // @Override
    // public void setCodes(List<VaardformsKod> codes) {
    // setVaardformskoder(codes);
    // }
    // };

    // @Transient
    // @SuppressWarnings("serial")
    // private ManyCodesRef<AtcKod> atcKoderRef = new ManyCodesRef<AtcKod>() {
    // @Override
    // public Set<AtcKod> getCodes() {
    // return getAtcKoder();
    // }
    //
    // @Override
    // public void setCodes(Set<AtcKod> codes) {
    // setAtcKoder(codes);
    // }
    // };

    /**
     * Copy all id's from their codes into their corresponding local attributes.
     * 
     * Example patientnyttaEffektAatgaerdsKodId getts it's value from the id in the patientnyttaEffektAatgaerdsKod
     * property.
     */
    public void putAllIdsFromCodesIfAnyIntoAttributeOnThisObject() {
        patientnyttaEffektAatgaerdsKodId = getIdFromeCodeIfAny(getPatientnyttaEffektAatgaerdsKod());
        sektorRaadId = getIdFromeCodeIfAny(getSektorRaad());
        patientnyttoEvidensKodId = getIdFromeCodeIfAny(getPatientnyttoEvidensKod());
        tillstaandetsSvaarighetsgradKodId = getIdFromeCodeIfAny(getTillstaandetsSvaarighetsgradKod());
        haelsonekonomiskEvidensKodId = getIdFromeCodeIfAny(getHaelsonekonomiskEvidensKod());
        vaardnivaaKodId = getIdFromeCodeIfAny(getVaardnivaaKod());
        vaentetidsKodId = getIdFromeCodeIfAny(getVaentetidBehandlingVeckor());
        aatgaerdsRiskKodId = getIdFromeCodeIfAny(getAatgaerdsRiskKod());
        rangordningsKodId = getIdFromeCodeIfAny(getRangordningsKod());
        vaentetidBehandlingVeckorId = getIdFromeCodeIfAny(getVaentetidBehandlingVeckor());
        vaentetidBesookVeckorId = getIdFromeCodeIfAny(getVaentetidBesookVeckor());
        vaardformId = getIdFromeCodeIfAny(getVaardform());
        kostnadLevnadsaarKodId = getIdFromeCodeIfAny(getKostnadLevnadsaarKod());
    }

    /**
     * Uses the id's in this object to look up correspoinding code-objects in the *List attributes and assigns
     * those to the attributes that where designed to hold them.
     * 
     * For instance: if the field vaardnivaaKodId = 4, then the value of the vaardnivaaKod property is set to a
     * VadrdnivaKod object with id = 4. That is if there are a List with that object present in the
     * vaardnivaaKodList property.
     * 
     * @throws NullPointerException
     *             if there are no List holding possible values to pick from.
     */
    public void asignCodesFromTheListsByCorrespondingIdAttributes() {
        setPatientnyttaEffektAatgaerdsKod(getKodByIdAndList(patientnyttaEffektAatgaerdsKodList,
                patientnyttaEffektAatgaerdsKodId));
        setSektorRaad(getNestedKodByIdAndList(sektorRaadList, sektorRaadId));
        setPatientnyttoEvidensKod(getKodByIdAndList(patientnyttoEvidensKodList, patientnyttoEvidensKodId));
        setTillstaandetsSvaarighetsgradKod(getKodByIdAndList(tillstaandetsSvaarighetsgradKodList,
                tillstaandetsSvaarighetsgradKodId));
        setHaelsonekonomiskEvidensKod(getKodByIdAndList(haelsonekonomiskEvidensKodList,
                haelsonekonomiskEvidensKodId));
        setVaardnivaaKod(getKodByIdAndList(vaardnivaaKodList, vaardnivaaKodId));
        setAatgaerdsRiskKod(getKodByIdAndList(aatgaerdsRiskKodList, aatgaerdsRiskKodId));
        setRangordningsKod(getKodByIdAndList(rangordningsKodList, rangordningsKodId));
        setVaentetidBehandlingVeckor(getKodByIdAndList(vaentetidBehandlingVeckorList, vaentetidBehandlingVeckorId));
        setVaentetidBesookVeckor(getKodByIdAndList(vaentetidBesookVeckorList, vaentetidBesookVeckorId));
        setVaardform(getKodByIdAndList(vaardformList, vaardformId));
        setKostnadLevnadsaarKod(getKodByIdAndList(getKostnadLevnadsaarKodList(), kostnadLevnadsaarKodId));
    }

    public Long getPatientnyttaEffektAatgaerdsKodId() {
        return patientnyttaEffektAatgaerdsKodId;
    }

    public void setPatientnyttaEffektAatgaerdsKodId(Long patientnyttaEffektAatgaerdsKodId) {
        this.patientnyttaEffektAatgaerdsKodId = patientnyttaEffektAatgaerdsKodId;
    }

    public List<PatientnyttaEffektAatgaerdsKod> getPatientnyttaEffektAatgaerdsKodList() {
        return patientnyttaEffektAatgaerdsKodList;
    }

    public void setPatientnyttaEffektAatgaerdsKodList(
            List<PatientnyttaEffektAatgaerdsKod> patientnyttaEffektAatgaerdsKodList) {
        this.patientnyttaEffektAatgaerdsKodList = patientnyttaEffektAatgaerdsKodList;
    }

    public Long getSektorRaadId() {
        return sektorRaadId;
    }

    public void setSektorRaadId(Long sektorRaadId) {
        this.sektorRaadId = sektorRaadId;
    }

    public List<SektorRaad> getSektorRaadList() {
        return sektorRaadList;
    }

    public void setSektorRaadList(List<SektorRaad> sektorRaadList) {
        this.sektorRaadList = sektorRaadList;
    }

    public Long getPatientnyttoEvidensKodId() {
        return patientnyttoEvidensKodId;
    }

    public void setPatientnyttoEvidensKodId(Long patientnyttoEvidensKodId) {
        this.patientnyttoEvidensKodId = patientnyttoEvidensKodId;
    }

    public List<PatientnyttoEvidensKod> getPatientnyttoEvidensKodList() {
        return patientnyttoEvidensKodList;
    }

    public void setPatientnyttoEvidensKodList(List<PatientnyttoEvidensKod> patientnyttoEvidensKodList) {
        this.patientnyttoEvidensKodList = patientnyttoEvidensKodList;
    }

    public Long getTillstaandetsSvaarighetsgradKodId() {
        return tillstaandetsSvaarighetsgradKodId;
    }

    public void setTillstaandetsSvaarighetsgradKodId(Long tillstaandetsSvaarighetsgradKodId) {
        this.tillstaandetsSvaarighetsgradKodId = tillstaandetsSvaarighetsgradKodId;
    }

    public List<TillstaandetsSvaarighetsgradKod> getTillstaandetsSvaarighetsgradKodList() {
        return tillstaandetsSvaarighetsgradKodList;
    }

    public void setTillstaandetsSvaarighetsgradKodList(
            List<TillstaandetsSvaarighetsgradKod> tillstaandetsSvaarighetsgradKodList) {
        this.tillstaandetsSvaarighetsgradKodList = tillstaandetsSvaarighetsgradKodList;
    }

    public Long getHaelsonekonomiskEvidensKodId() {
        return haelsonekonomiskEvidensKodId;
    }

    public void setHaelsonekonomiskEvidensKodId(Long haelsonekonomiskEvidensKodId) {
        this.haelsonekonomiskEvidensKodId = haelsonekonomiskEvidensKodId;
    }

    public List<HaelsonekonomiskEvidensKod> getHaelsonekonomiskEvidensKodList() {
        return haelsonekonomiskEvidensKodList;
    }

    public void setHaelsonekonomiskEvidensKodList(List<HaelsonekonomiskEvidensKod> haelsonekonomiskEvidensKodList) {
        this.haelsonekonomiskEvidensKodList = haelsonekonomiskEvidensKodList;
    }

    public Long getVaardnivaaKodId() {
        return vaardnivaaKodId;
    }

    public void setVaardnivaaKodId(Long vaardnivaaKodId) {
        this.vaardnivaaKodId = vaardnivaaKodId;
    }

    public List<VaardnivaaKod> getVaardnivaaKodList() {
        return vaardnivaaKodList;
    }

    public void setVaardnivaaKodList(List<VaardnivaaKod> vaardnivaaKodList) {
        this.vaardnivaaKodList = vaardnivaaKodList;
    }

    public Long getVaentetidsKodId() {
        return vaentetidsKodId;
    }

    public void setVaentetidsKodId(Long vaentetidsKodId) {
        this.vaentetidsKodId = vaentetidsKodId;
    }

    // public List<VaentetidsKod> getVaentetidsKodList() {
    // return vaentetidsKodList;
    // }
    //
    // public void setVaentetidsKodList(List<VaentetidsKod> vaentetidsKodList) {
    // this.vaentetidsKodList = vaentetidsKodList;
    // }

    public Long getAatgaerdsRiskKodId() {
        return aatgaerdsRiskKodId;
    }

    public void setAatgaerdsRiskKodId(Long aatgaerdsRiskKodId) {
        this.aatgaerdsRiskKodId = aatgaerdsRiskKodId;
    }

    public List<AatgaerdsRiskKod> getAatgaerdsRiskKodList() {
        return aatgaerdsRiskKodList;
    }

    public void setAatgaerdsRiskKodList(List<AatgaerdsRiskKod> aatgaerdsRiskKodList) {
        this.aatgaerdsRiskKodList = aatgaerdsRiskKodList;
    }

    public Long getRangordningsKodId() {
        return rangordningsKodId;
    }

    public void setRangordningsKodId(Long rangordningsKodId) {
        this.rangordningsKodId = rangordningsKodId;
    }

    public List<RangordningsKod> getRangordningsKodList() {
        return rangordningsKodList;
    }

    public void setRangordningsKodList(List<RangordningsKod> rangordningsKodList) {
        this.rangordningsKodList = rangordningsKodList;
    }

    public static void main(String[] args) throws Exception {
        Prioriteringsobjekt po = new Prioriteringsobjekt();
        BeanMap bm = new BeanMap(po);
        Set<String> keys = new HashSet<String>(bm.keySet());

        keys.remove("class");
        StringBuilder sb = new StringBuilder();

        for (String key : keys) {
            Class<?> type = bm.getType(key);

            if (!AbstractKod.class.isAssignableFrom(type)) {
                continue;
            }
            // sb.append("private Long " + key + "Id;\n");
            // sb.append("propertyKeyId = getIdFromeCodeIfAny(propertyMethod());\n".replace("propertyKey", key)
            // .replace("propertyMethod", bm.getReadMethod(key).getName()));

            String text = "setter(getKodByIdAndList(${key}List, ${key}Id));\n";
            text = text.replace("${key}", key);
            text = text.replace("setter", bm.getWriteMethod(key).getName());
            sb.append(text);

            // sb.append("private List<" + type.getSimpleName() + "> " + key + "List;\n");
            // sb.append("<tags:kod key=\"propertyKey\" label=\"propertyKey\" />\n".replace("propertyKey", key));
        }
        System.out.println(sb);
    }

    private Long getIdFromeCodeIfAny(AbstractKod abstractKod) {
        if (abstractKod == null) {
            return null;
        }
        return abstractKod.getId();
    }

    private <T extends AbstractKod> T getKodByIdAndList(List<T> possibleValues, Long id) {
        if (id == null) {
            return null;
        }

        for (AbstractKod ak : possibleValues) {
            if (id.equals(ak.getId())) {
                return (T) ak;
            }
        }
        return null;
    }

    private <T extends AbstractKod> T getNestedKodByIdAndList(List<T> possibleValues, Long id) {

        T kod = getKodByIdAndList(possibleValues, id);
        if (kod != null) {
            return kod;
        }
        List<AbstractHirarkiskKod> nestedOnes = (List<AbstractHirarkiskKod>) possibleValues;

        if (nestedOnes != null) {
            for (AbstractHirarkiskKod ak : nestedOnes) {
                T possibleResult = (T) getNestedKodByIdAndList(ak.getChildren(), id);
                if (possibleResult != null) {
                    return possibleResult;
                }
            }
        }

        return null;
    }

    // public ManyCodesRef<DiagnosKod> getDiagnosRef() {
    // return diagnosRef;
    // }
    //
    // public ManyCodesRef<AatgaerdsKod> getAatgaerdRef() {
    // return aatgaerdRef;
    // }
    //
    // // public ManyCodesRef<VaardformsKod> getVaardformskoderRef() {
    // // return vaardformskoderRef;
    // // }
    //
    // public ManyCodesRef<AtcKod> getAtcKoderRef() {
    // return atcKoderRef;
    // }

    public Map<String, Column> getColumns() {
        return columns;
    }

    public void setColumns(Map<String, Column> columns) {
        this.columns = columns;
    }

    public void setVaentetidBesookVeckorId(Long vaentetidBesookVeckorId) {
        this.vaentetidBesookVeckorId = vaentetidBesookVeckorId;
    }

    public Long getVaentetidBesookVeckorId() {
        return vaentetidBesookVeckorId;
    }

    public void setVaentetidBesookVeckorList(List<VaentetidsKod> vaentetidBesookVeckorList) {
        this.vaentetidBesookVeckorList = vaentetidBesookVeckorList;
    }

    public List<VaentetidsKod> getVaentetidBesookVeckorList() {
        return vaentetidBesookVeckorList;
    }

    public void setVaentetidBehandlingVeckorId(Long vaentetidBehandlingVeckorId) {
        this.vaentetidBehandlingVeckorId = vaentetidBehandlingVeckorId;
    }

    public Long getVaentetidBehandlingVeckorId() {
        return vaentetidBehandlingVeckorId;
    }

    public void setVaentetidBehandlingVeckorList(List<VaentetidsKod> vaentetidBehandlingVeckorList) {
        this.vaentetidBehandlingVeckorList = vaentetidBehandlingVeckorList;
    }

    public List<VaentetidsKod> getVaentetidBehandlingVeckorList() {
        return vaentetidBehandlingVeckorList;
    }

    public void setVaardformList(List<VaardformsKod> vaardformsKodList) {
        this.vaardformList = vaardformsKodList;
    }

    public List<VaardformsKod> getVaardformList() {
        return vaardformList;
    }

    public void setVaardformId(Long vaardformsKodId) {
        this.vaardformId = vaardformsKodId;
    }

    public Long getVaardformId() {
        return vaardformId;
    }

    public void setKostnadLevnadsaarKodId(Long kostnadLevnadsaarKodId) {
        this.kostnadLevnadsaarKodId = kostnadLevnadsaarKodId;
    }

    public Long getKostnadLevnadsaarKodId() {
        return kostnadLevnadsaarKodId;
    }

    public void setKostnadLevnadsaarKodList(List<KostnadLevnadsaarKod> kostnadLevnadsaarKodList) {
        this.kostnadLevnadsaarKodList = kostnadLevnadsaarKodList;
    }

    public List<KostnadLevnadsaarKod> getKostnadLevnadsaarKodList() {
        return kostnadLevnadsaarKodList;
    }

    public void setDiagnoserList(List<DiagnosKod> diagnoserList) {
        this.diagnoserList = diagnoserList;
    }

    public List<DiagnosKod> getDiagnoserList() {
        return diagnoserList;
    }

    public void setSkarpVersion(Prioriteringsobjekt skarpVersion) {
        this.skarpVersion = skarpVersion;
    }

    public Prioriteringsobjekt getSkarpVersion() {
        return skarpVersion;
    }

    public void setSektorRaad(String sektorRaad) {
        throw new UnsupportedOperationException("Is this called?");
    }

    public void setUnalteredVersion(PrioriteringsobjektForm unalteredVersion) {
        this.unalteredVersion = unalteredVersion;
    }

    public PrioriteringsobjektForm getUnalteredVersion() {
        return unalteredVersion;
    }

}
