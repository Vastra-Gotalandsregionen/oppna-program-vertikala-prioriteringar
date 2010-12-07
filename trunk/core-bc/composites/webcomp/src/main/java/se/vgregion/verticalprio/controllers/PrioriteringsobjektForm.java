package se.vgregion.verticalprio.controllers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.beanutils.BeanMap;

import se.vgregion.verticalprio.entity.AatgaerdsRiskKod;
import se.vgregion.verticalprio.entity.AbstractKod;
import se.vgregion.verticalprio.entity.HaelsonekonomiskEvidensKod;
import se.vgregion.verticalprio.entity.PatientnyttaEffektAatgaerdsKod;
import se.vgregion.verticalprio.entity.PatientnyttoEvidensKod;
import se.vgregion.verticalprio.entity.Prioriteringsobjekt;
import se.vgregion.verticalprio.entity.RangordningsKod;
import se.vgregion.verticalprio.entity.SektorRaad;
import se.vgregion.verticalprio.entity.TillstaandetsSvaarighetsgradKod;
import se.vgregion.verticalprio.entity.VaardnivaaKod;
import se.vgregion.verticalprio.entity.VaentetidsKod;

/**
 * @author Claes Lundahl, vgrid=clalu4
 * 
 */
public class PrioriteringsobjektForm extends Prioriteringsobjekt {

    private Long patientnyttaEffektAatgaerdsKodId;
    private List<PatientnyttaEffektAatgaerdsKod> patientnyttaEffektAatgaerdsKodList;
    private Long sektorRaadId;
    private List<SektorRaad> sektorRaadList;
    private Long patientnyttoEvidensKodId;
    private List<PatientnyttoEvidensKod> patientnyttoEvidensKodList;
    private Long tillstaandetsSvaarighetsgradKodId;
    private List<TillstaandetsSvaarighetsgradKod> tillstaandetsSvaarighetsgradKodList;
    private Long haelsonekonomiskEvidensKodId;
    private List<HaelsonekonomiskEvidensKod> haelsonekonomiskEvidensKodList;
    private Long vaardnivaaKodId;
    private List<VaardnivaaKod> vaardnivaaKodList;
    private Long vaentetidsKodId;
    private List<VaentetidsKod> vaentetidsKodList;
    private Long aatgaerdsRiskKodId;
    private List<AatgaerdsRiskKod> aatgaerdsRiskKodList;
    private Long rangordningsKodId;
    private List<RangordningsKod> rangordningsKodList;

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

    public List<VaentetidsKod> getVaentetidsKodList() {
        return vaentetidsKodList;
    }

    public void setVaentetidsKodList(List<VaentetidsKod> vaentetidsKodList) {
        this.vaentetidsKodList = vaentetidsKodList;
    }

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
            // sb.append("private List<" + type.getSimpleName() + "> " + key + "List;\n");
            sb.append("<tags:kod key=\"propertyKey\" label=\"propertyKey\" />\n".replace("propertyKey", key));
        }
        System.out.println(sb);
    }

}
