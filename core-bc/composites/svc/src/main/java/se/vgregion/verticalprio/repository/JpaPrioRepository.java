package se.vgregion.verticalprio.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import se.vgregion.verticalprio.entity.AatgaerdsKod;
import se.vgregion.verticalprio.entity.AtcKod;
import se.vgregion.verticalprio.entity.DiagnosKod;
import se.vgregion.verticalprio.entity.LitePrioriteringsobjekt;
import se.vgregion.verticalprio.entity.Prioriteringsobjekt;

@Repository
public class JpaPrioRepository extends JpaGenerisktFinderRepository<Prioriteringsobjekt> implements PrioRepository {

    @Resource(name = "diagnosRepository")
    GenerisktFinderRepository<DiagnosKod> diagnosRepository;

    @Resource(name = "aatgaerdsKodRepository")
    GenerisktKodRepository<AatgaerdsKod> aatgaerdsKodRepository;

    @Resource(name = "atcKodRepository")
    GenerisktKodRepository<AtcKod> atcKodRepository;

    public JpaPrioRepository() {
        super(Prioriteringsobjekt.class);
    }

    /**
     * @inheritDoc
     */
    @Override
    public List<Prioriteringsobjekt> findLargeResult(Prioriteringsobjekt example) {
        List<Prioriteringsobjekt> prioriteringsobjekt = findByExample(example, null);

        Map<Long, Prioriteringsobjekt> idPrioMapping = new HashMap<Long, Prioriteringsobjekt>();
        List<Prioriteringsobjekt> result = new ArrayList<Prioriteringsobjekt>();

        for (Prioriteringsobjekt prio : prioriteringsobjekt) {
            PrioriteringsobjectForLargeResult item = new PrioriteringsobjectForLargeResult();
            copy(prio, item);
            idPrioMapping.put(prio.getId(), item);
            result.add(item);
        }

        // Diagnoser
        DiagnosKod diagnosCondition = new DiagnosKod();
        diagnosCondition.getPrioriteringsobjekt().add(example);

        List<DiagnosKod> diagnosesInResult = diagnosRepository.findByExample(diagnosCondition, null);
        for (DiagnosKod kod : diagnosesInResult) {
            for (LitePrioriteringsobjekt lp : kod.getLitePrioriteringsobjekt()) {
                Long id = lp.getId();
                Prioriteringsobjekt prio = idPrioMapping.get(id);
                if (prio != null) {
                    Set<DiagnosKod> diagnoser = prio.getDiagnoser();
                    diagnoser.add(kod);
                }
            }
        }

        // ATC-koder
        AtcKod atcCondition = new AtcKod();
        atcCondition.getPrioriteringsobjekt().add(example);

        List<AtcKod> atcInResult = atcKodRepository.findByExample(atcCondition, null);
        for (AtcKod kod : atcInResult) {
            for (LitePrioriteringsobjekt lp : kod.getLitePrioriteringsobjekt()) {
                Long id = lp.getId();
                Prioriteringsobjekt prio = idPrioMapping.get(id);
                if (prio != null) {
                    Set<AtcKod> atcKoder = prio.getAtcKoder();
                    atcKoder.add(kod);
                }
            }
        }

        // Aatgardskoder
        AatgaerdsKod aatgardsCondition = new AatgaerdsKod();
        aatgardsCondition.getPrioriteringsobjekt().add(example);
        List<AatgaerdsKod> aatgardInResult = aatgaerdsKodRepository.findByExample(aatgardsCondition, null);
        for (AatgaerdsKod kod : aatgardInResult) {
            for (LitePrioriteringsobjekt lp : kod.getLitePrioriteringsobjekt()) {
                Long id = lp.getId();
                Prioriteringsobjekt prio = idPrioMapping.get(id);
                if (prio != null) {
                    Set<AatgaerdsKod> aatgaerdsKoder = prio.getAatgaerdskoder();
                    aatgaerdsKoder.add(kod);
                }
            }
        }

        return result;
    }

    /**
     * 
     * @author Claes Lundahl, vgrid=clalu4
     * 
     */
    public static class PrioriteringsobjectForLargeResult extends Prioriteringsobjekt {

        private static final long serialVersionUID = 1L;

        private Set<DiagnosKod> diagnoser = new HashSet<DiagnosKod>();

        private Set<AatgaerdsKod> aatgaerdskoder = new HashSet<AatgaerdsKod>();

        private Set<AtcKod> atcKoder = new HashSet<AtcKod>();

        @Override
        public void setDiagnoser(Set<DiagnosKod> diagnoser) {
            this.diagnoser = diagnoser;
        }

        @Override
        public Set<DiagnosKod> getDiagnoser() {
            return diagnoser;
        }

        @Override
        public void setAtcKoder(Set<AtcKod> atcKoder) {
            this.atcKoder = atcKoder;
        }

        @Override
        public Set<AtcKod> getAtcKoder() {
            return atcKoder;
        }

        @Override
        public void setAatgaerdskoder(Set<AatgaerdsKod> aatgaerdskoder) {
            this.aatgaerdskoder = aatgaerdskoder;
        }

        @Override
        public Set<AatgaerdsKod> getAatgaerdskoder() {
            return aatgaerdskoder;
        }
    }

    void copy(Prioriteringsobjekt source, Prioriteringsobjekt target) {
        // target.setDiagnoser(source.getDiagnoser());
        target.setId(source.getId());
        target.setPatientnyttoEvidensKod(source.getPatientnyttoEvidensKod());
        target.setKostnad(source.getKostnad());

        target.setChildren(source.getChildren());
        for (Prioriteringsobjekt child : source.getChildren()) {
            PrioriteringsobjectForLargeResult childForLargeResult = new PrioriteringsobjectForLargeResult();
            copy(child, childForLargeResult);
            target.getChildren().add(childForLargeResult);
        }

        target.setKostnadLevnadsaarKod(source.getKostnadLevnadsaarKod());
        target.setVaardform(source.getVaardform());
        target.setGodkaend(source.getGodkaend());
        target.setParentId(source.getParentId());
        target.setVaardnivaaKod(source.getVaardnivaaKod());
        // target.setAatgaerdskoder(source.getAatgaerdskoder());
        target.setVaentetidBesookVeckor(source.getVaentetidBesookVeckor());
        target.setKommentar(source.getKommentar());
        // target.setAtcKoder(source.getAtcKoder());
        target.setSenastUppdaterad(source.getSenastUppdaterad());
        target.setPatientnyttaEffektAatgaerdsKod(source.getPatientnyttaEffektAatgaerdsKod());
        target.setSektorRaad(source.getSektorRaad());
        target.setTillstaandetsSvaarighetsgradKod(source.getTillstaandetsSvaarighetsgradKod());
        target.setVolym(source.getVolym());
        target.setHaelsonekonomiskEvidensKod(source.getHaelsonekonomiskEvidensKod());
        target.setIndikationGaf(source.getIndikationGaf());
        target.setVaentetidBehandlingVeckor(source.getVaentetidBehandlingVeckor());
        target.setAatgaerdsRiskKod(source.getAatgaerdsRiskKod());
        target.setRangordningsKod(source.getRangordningsKod());

        // Use this main method to generate the copying.
        // public static void main(String[] args) {
        // BeanMap bm = new BeanMap(new PrioriteringsobjectForLargeResult());
        // for (Object key : bm.keySet()) {
        // String name = (String) key;
        // if (bm.getWriteMethod(name) != null && bm.getReadMethod(name) != null) {
        // String setting = " target." + bm.getWriteMethod(name).getName();
        // setting += "(source." + bm.getReadMethod(name).getName() + "());";
        // System.out.println(setting);
        // }
        // }
        // }

    }

}
