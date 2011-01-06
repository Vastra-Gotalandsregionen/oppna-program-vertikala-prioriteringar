package se.vgregion.verticalprio;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.beanutils.BeanMap;
import org.springframework.stereotype.Component;

import se.vgregion.verticalprio.controllers.PrioriteringsobjektForm;
import se.vgregion.verticalprio.entity.AatgaerdsKod;
import se.vgregion.verticalprio.entity.AatgaerdsRiskKod;
import se.vgregion.verticalprio.entity.DiagnosKod;
import se.vgregion.verticalprio.entity.HaelsonekonomiskEvidensKod;
import se.vgregion.verticalprio.entity.KostnadLevnadsaarKod;
import se.vgregion.verticalprio.entity.PatientnyttaEffektAatgaerdsKod;
import se.vgregion.verticalprio.entity.PatientnyttoEvidensKod;
import se.vgregion.verticalprio.entity.RangordningsKod;
import se.vgregion.verticalprio.entity.SektorRaad;
import se.vgregion.verticalprio.entity.TillstaandetsSvaarighetsgradKod;
import se.vgregion.verticalprio.entity.VaardformsKod;
import se.vgregion.verticalprio.entity.VaardnivaaKod;
import se.vgregion.verticalprio.entity.VaentetidsKod;
import se.vgregion.verticalprio.repository.GenerisktHierarkisktKodRepository;
import se.vgregion.verticalprio.repository.GenerisktKodRepository;

/**
 * Class to hold application level data available to all sessions.
 * 
 * All getter methods have the same pattern - if the member variable is null then use a repository to acquire all
 * the data from the db. If not return the beans (as a list).
 * 
 * @author Claes Lundahl, vgrid=clalu4
 * 
 */
@Component
public class ApplicationData implements Serializable {

    private static final long serialVersionUID = 1L;

    @Resource(name = "aatgaerdsKodRepository")
    GenerisktKodRepository<AatgaerdsKod> aatgaerdsKodRepository;
    List<AatgaerdsKod> everyAatgaerdsKod;

    public List<AatgaerdsKod> getAatgaerdsKodList() {
        if (everyAatgaerdsKod == null) {
            everyAatgaerdsKod = new ArrayList<AatgaerdsKod>(aatgaerdsKodRepository.findAll());
        }
        return everyAatgaerdsKod;
    }

    @Resource(name = "aatgaerdsRiskKodRepository")
    GenerisktKodRepository<AatgaerdsRiskKod> aatgaerdsRiskKodRepository;
    List<AatgaerdsRiskKod> everyAatgaerdsRiskKod;

    public List<AatgaerdsRiskKod> getAatgaerdsRiskKodList() {
        if (everyAatgaerdsRiskKod == null) {
            everyAatgaerdsRiskKod = new ArrayList<AatgaerdsRiskKod>(aatgaerdsRiskKodRepository.findAll());
        }
        return everyAatgaerdsRiskKod;
    }

    @Resource(name = "diagnosKodRepository")
    GenerisktHierarkisktKodRepository<DiagnosKod> diagnosKodRepository;
    List<DiagnosKod> everyDiagnosKod;

    public List<DiagnosKod> getDiagnosKodList() {
        if (everyDiagnosKod == null) {
            everyDiagnosKod = new ArrayList<DiagnosKod>(diagnosKodRepository.findAll());
        }
        return everyDiagnosKod;
    }

    @Resource(name = "haelsonekonomiskEvidensKodRepository")
    GenerisktKodRepository<HaelsonekonomiskEvidensKod> haelsonekonomiskEvidensKodRepository;
    List<HaelsonekonomiskEvidensKod> everyHaelsonekonomiskEvidensKod;

    public List<HaelsonekonomiskEvidensKod> getHaelsonekonomiskEvidensKodList() {
        if (everyHaelsonekonomiskEvidensKod == null) {
            everyHaelsonekonomiskEvidensKod = new ArrayList<HaelsonekonomiskEvidensKod>(
                    haelsonekonomiskEvidensKodRepository.findAll());
        }
        return everyHaelsonekonomiskEvidensKod;
    }

    @Resource(name = "kostnadLevnadsaarKodRepository")
    GenerisktKodRepository<KostnadLevnadsaarKod> kostnadLevnadsaarKodRepository;
    List<KostnadLevnadsaarKod> everyKostnadLevnadsaarKod;

    public List<KostnadLevnadsaarKod> getKostnadLevnadsaarKodList() {
        if (everyKostnadLevnadsaarKod == null) {
            everyKostnadLevnadsaarKod = new ArrayList<KostnadLevnadsaarKod>(
                    kostnadLevnadsaarKodRepository.findAll());
        }
        return everyKostnadLevnadsaarKod;
    }

    @Resource(name = "patientnyttaEffektAatgaerdsKodRepository")
    GenerisktKodRepository<PatientnyttaEffektAatgaerdsKod> patientnyttaEffektAatgaerdsKodRepository;
    List<PatientnyttaEffektAatgaerdsKod> everyPatientnyttaEffektAatgaerdsKod;

    public List<PatientnyttaEffektAatgaerdsKod> getPatientnyttaEffektAatgaerdsKodList() {
        if (everyPatientnyttaEffektAatgaerdsKod == null) {
            everyPatientnyttaEffektAatgaerdsKod = new ArrayList<PatientnyttaEffektAatgaerdsKod>(
                    patientnyttaEffektAatgaerdsKodRepository.findAll());
        }
        return everyPatientnyttaEffektAatgaerdsKod;
    }

    @Resource(name = "patientnyttoEvidensKodRepository")
    GenerisktKodRepository<PatientnyttoEvidensKod> patientnyttoEvidensKodRepository;
    List<PatientnyttoEvidensKod> everyPatientnyttoEvidensKod;

    public List<PatientnyttoEvidensKod> getPatientnyttoEvidensKodList() {
        if (everyPatientnyttoEvidensKod == null) {
            everyPatientnyttoEvidensKod = new ArrayList<PatientnyttoEvidensKod>(
                    patientnyttoEvidensKodRepository.findAll());
        }
        return everyPatientnyttoEvidensKod;
    }

    @Resource(name = "rangordningsKodRepository")
    GenerisktKodRepository<RangordningsKod> rangordningsKodRepository;
    List<RangordningsKod> everyRangordningsKod;

    public List<RangordningsKod> getRangordningsKodList() {
        if (everyRangordningsKod == null) {
            everyRangordningsKod = new ArrayList<RangordningsKod>(rangordningsKodRepository.findAll());
        }
        return everyRangordningsKod;
    }

    @Resource(name = "tillstaandetsSvaarighetsgradKodRepository")
    GenerisktKodRepository<TillstaandetsSvaarighetsgradKod> tillstaandetsSvaarighetsgradKodRepository;
    List<TillstaandetsSvaarighetsgradKod> everyTillstaandetsSvaarighetsgradKod;

    public List<TillstaandetsSvaarighetsgradKod> getTillstaandetsSvaarighetsgradKodList() {
        if (everyTillstaandetsSvaarighetsgradKod == null) {
            everyTillstaandetsSvaarighetsgradKod = new ArrayList<TillstaandetsSvaarighetsgradKod>(
                    tillstaandetsSvaarighetsgradKodRepository.findAll());
        }
        return everyTillstaandetsSvaarighetsgradKod;
    }

    @Resource(name = "vaardformsKodRepository")
    GenerisktKodRepository<VaardformsKod> vaardformsKodRepository;
    List<VaardformsKod> everyVaardformsKod;

    public List<VaardformsKod> getVaardformsKodList() {
        if (everyVaardformsKod == null) {
            everyVaardformsKod = new ArrayList<VaardformsKod>(vaardformsKodRepository.findAll());
        }
        return everyVaardformsKod;
    }

    @Resource(name = "vaardnivaaKodRepository")
    GenerisktKodRepository<VaardnivaaKod> vaardnivaaKodRepository;
    List<VaardnivaaKod> everyVaardnivaaKod;

    public List<VaardnivaaKod> getVaardnivaaKodList() {
        if (everyVaardnivaaKod == null) {
            everyVaardnivaaKod = new ArrayList<VaardnivaaKod>(vaardnivaaKodRepository.findAll());
        }
        return everyVaardnivaaKod;
    }

    @Resource(name = "vaentetidsKodRepository")
    GenerisktKodRepository<VaentetidsKod> vaentetidsKodRepository;
    List<VaentetidsKod> everyVaentetidsKod;

    public List<VaentetidsKod> getVaentetidsKodList() {
        if (everyVaentetidsKod == null) {
            everyVaentetidsKod = new ArrayList<VaentetidsKod>(vaentetidsKodRepository.findAll());
        }
        return everyVaentetidsKod;
    }

    @Resource(name = "sektorRaadRepository")
    GenerisktHierarkisktKodRepository<SektorRaad> sektorRaadRepository;
    List<SektorRaad> everySektorRaad;

    public List<SektorRaad> getSektorRaadList() {
        if (everySektorRaad == null) {
            everySektorRaad = sektorRaadRepository.getTreeRoots();
        }
        return everySektorRaad;
    }

    public void initKodLists(PrioriteringsobjektForm pf) {
        BeanMap pfMap = new BeanMap(pf);
        BeanMap adMap = new BeanMap(this);
        pfMap.putAllWriteable(adMap);
    }

}
