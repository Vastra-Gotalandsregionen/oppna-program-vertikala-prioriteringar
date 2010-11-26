package se.vgregion.verticalprio;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import se.vgregion.verticalprio.entity.AatgaerdsKod;
import se.vgregion.verticalprio.entity.AatgaerdsRiskKod;
import se.vgregion.verticalprio.entity.DiagnosKod;
import se.vgregion.verticalprio.entity.HaelsonekonomiskEvidensKod;
import se.vgregion.verticalprio.entity.KostnadLevnadsaarKod;
import se.vgregion.verticalprio.entity.PatientnyttaEffektAatgaerdsKod;
import se.vgregion.verticalprio.entity.PatientnyttoEvidensKod;
import se.vgregion.verticalprio.entity.RangordningsKod;
import se.vgregion.verticalprio.entity.TillstaandetsSvaarighetsgradKod;
import se.vgregion.verticalprio.entity.VaardformsKod;
import se.vgregion.verticalprio.entity.VaardnivaaKod;
import se.vgregion.verticalprio.entity.VaentetidsKod;
import se.vgregion.verticalprio.repository.AatgaerdsKodRepository;
import se.vgregion.verticalprio.repository.AatgaerdsRiskKodRepository;
import se.vgregion.verticalprio.repository.DiagnosKodRepository;
import se.vgregion.verticalprio.repository.HaelsonekonomiskEvidensKodRepository;
import se.vgregion.verticalprio.repository.KostnadLevnadsaarKodRepository;
import se.vgregion.verticalprio.repository.PatientnyttaEffektAatgaerdsKodRepository;
import se.vgregion.verticalprio.repository.PatientnyttoEvidensKodRepository;
import se.vgregion.verticalprio.repository.RangordningsKodRepository;
import se.vgregion.verticalprio.repository.TillstaandetsSvaarighetsgradKodRepository;
import se.vgregion.verticalprio.repository.VaardformsKodRepository;
import se.vgregion.verticalprio.repository.VaardnivaaKodRepository;
import se.vgregion.verticalprio.repository.VaentetidsKodRepository;

/**
 * Class to hold application level data available to all sessions.
 * 
 * @author Claes Lundahl, vgrid=clalu4
 * 
 */
public class ApplicationData implements Serializable {

    private static final long serialVersionUID = 1L;

    @Resource(name = "aatgaerdsKodRepository")
    AatgaerdsKodRepository aatgaerdsKodRepository;
    List<AatgaerdsKod> everyAatgaerdsKod;

    public List<AatgaerdsKod> getEveryAatgaerdsKod() {
        if (everyAatgaerdsKod == null) {
            everyAatgaerdsKod = new ArrayList<AatgaerdsKod>(aatgaerdsKodRepository.findAll());
        }
        return everyAatgaerdsKod;
    }

    @Resource(name = "aatgaerdsRiskKodRepository")
    AatgaerdsRiskKodRepository aatgaerdsRiskKodRepository;
    List<AatgaerdsRiskKod> everyAatgaerdsRiskKod;

    public List<AatgaerdsRiskKod> getEveryAatgaerdsRiskKod() {
        if (everyAatgaerdsRiskKod == null) {
            everyAatgaerdsRiskKod = new ArrayList<AatgaerdsRiskKod>(aatgaerdsRiskKodRepository.findAll());
        }
        return everyAatgaerdsRiskKod;
    }

    @Resource(name = "diagnosKodRepository")
    DiagnosKodRepository diagnosKodRepository;
    List<DiagnosKod> everyDiagnosKod;

    public List<DiagnosKod> getEveryDiagnosKod() {
        if (everyDiagnosKod == null) {
            everyDiagnosKod = new ArrayList<DiagnosKod>(diagnosKodRepository.findAll());
        }
        return everyDiagnosKod;
    }

    @Resource(name = "haelsonekonomiskEvidensKodRepository")
    HaelsonekonomiskEvidensKodRepository haelsonekonomiskEvidensKodRepository;
    List<HaelsonekonomiskEvidensKod> everyHaelsonekonomiskEvidensKod;

    public List<HaelsonekonomiskEvidensKod> getEveryHaelsonekonomiskEvidensKod() {
        if (everyHaelsonekonomiskEvidensKod == null) {
            everyHaelsonekonomiskEvidensKod = new ArrayList<HaelsonekonomiskEvidensKod>(
                    haelsonekonomiskEvidensKodRepository.findAll());
        }
        return everyHaelsonekonomiskEvidensKod;
    }

    @Resource(name = "kostnadLevnadsaarKodRepository")
    KostnadLevnadsaarKodRepository kostnadLevnadsaarKodRepository;
    List<KostnadLevnadsaarKod> everyKostnadLevnadsaarKod;

    public List<KostnadLevnadsaarKod> getEveryKostnadLevnadsaarKod() {
        if (everyKostnadLevnadsaarKod == null) {
            everyKostnadLevnadsaarKod = new ArrayList<KostnadLevnadsaarKod>(
                    kostnadLevnadsaarKodRepository.findAll());
        }
        return everyKostnadLevnadsaarKod;
    }

    @Resource(name = "patientnyttaEffektAatgaerdsKodRepository")
    PatientnyttaEffektAatgaerdsKodRepository patientnyttaEffektAatgaerdsKodRepository;
    List<PatientnyttaEffektAatgaerdsKod> everyPatientnyttaEffektAatgaerdsKod;

    public List<PatientnyttaEffektAatgaerdsKod> getEveryPatientnyttaEffektAatgaerdsKod() {
        if (everyPatientnyttaEffektAatgaerdsKod == null) {
            everyPatientnyttaEffektAatgaerdsKod = new ArrayList<PatientnyttaEffektAatgaerdsKod>(
                    patientnyttaEffektAatgaerdsKodRepository.findAll());
        }
        return everyPatientnyttaEffektAatgaerdsKod;
    }

    @Resource(name = "patientnyttoEvidensKodRepository")
    PatientnyttoEvidensKodRepository patientnyttoEvidensKodRepository;
    List<PatientnyttoEvidensKod> everyPatientnyttoEvidensKod;

    public List<PatientnyttoEvidensKod> getEveryPatientnyttoEvidensKod() {
        if (everyPatientnyttoEvidensKod == null) {
            everyPatientnyttoEvidensKod = new ArrayList<PatientnyttoEvidensKod>(
                    patientnyttoEvidensKodRepository.findAll());
        }
        return everyPatientnyttoEvidensKod;
    }

    @Resource(name = "rangordningsKodRepository")
    RangordningsKodRepository rangordningsKodRepository;
    List<RangordningsKod> everyRangordningsKod;

    public List<RangordningsKod> getEveryRangordningsKod() {
        if (everyRangordningsKod == null) {
            everyRangordningsKod = new ArrayList<RangordningsKod>(rangordningsKodRepository.findAll());
        }
        return everyRangordningsKod;
    }

    @Resource(name = "tillstaandetsSvaarighetsgradKodRepository")
    TillstaandetsSvaarighetsgradKodRepository tillstaandetsSvaarighetsgradKodRepository;
    List<TillstaandetsSvaarighetsgradKod> everyTillstaandetsSvaarighetsgradKod;

    public List<TillstaandetsSvaarighetsgradKod> getEveryTillstaandetsSvaarighetsgradKod() {
        if (everyTillstaandetsSvaarighetsgradKod == null) {
            everyTillstaandetsSvaarighetsgradKod = new ArrayList<TillstaandetsSvaarighetsgradKod>(
                    tillstaandetsSvaarighetsgradKodRepository.findAll());
        }
        return everyTillstaandetsSvaarighetsgradKod;
    }

    @Resource(name = "vaardformsKodRepository")
    VaardformsKodRepository vaardformsKodRepository;
    List<VaardformsKod> everyVaardformsKod;

    public List<VaardformsKod> getEveryVaardformsKod() {
        if (everyVaardformsKod == null) {
            everyVaardformsKod = new ArrayList<VaardformsKod>(vaardformsKodRepository.findAll());
        }
        return everyVaardformsKod;
    }

    @Resource(name = "vaardnivaaKodRepository")
    VaardnivaaKodRepository vaardnivaaKodRepository;
    List<VaardnivaaKod> everyVaardnivaaKod;

    public List<VaardnivaaKod> getEveryVaardnivaaKod() {
        if (everyVaardnivaaKod == null) {
            everyVaardnivaaKod = new ArrayList<VaardnivaaKod>(vaardnivaaKodRepository.findAll());
        }
        return everyVaardnivaaKod;
    }

    @Resource(name = "vaentetidsKodRepository")
    VaentetidsKodRepository vaentetidsKodRepository;
    List<VaentetidsKod> everyVaentetidsKod;

    public List<VaentetidsKod> getEveryVaentetidsKod() {
        if (everyVaentetidsKod == null) {
            everyVaentetidsKod = new ArrayList<VaentetidsKod>(vaentetidsKodRepository.findAll());
        }
        return everyVaentetidsKod;
    }

}
