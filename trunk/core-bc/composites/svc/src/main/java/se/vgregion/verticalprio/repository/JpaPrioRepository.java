package se.vgregion.verticalprio.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.persistence.Query;

import org.apache.commons.beanutils.BeanMap;
import org.springframework.stereotype.Repository;

import se.vgregion.verticalprio.entity.AatgaerdsKod;
import se.vgregion.verticalprio.entity.AbstractKod;
import se.vgregion.verticalprio.entity.AtcKod;
import se.vgregion.verticalprio.entity.DiagnosKod;
import se.vgregion.verticalprio.entity.LinkPrioriteringsobjektAatgaerdsKod;
import se.vgregion.verticalprio.entity.LinkPrioriteringsobjektAtcKod;
import se.vgregion.verticalprio.entity.LinkPrioriteringsobjektDiagnosKod;
import se.vgregion.verticalprio.entity.Prioriteringsobjekt;
import se.vgregion.verticalprio.repository.finding.JpqlMatchBuilder;
import se.vgregion.verticalprio.repository.finding.JpqlMatchBuilder.JpqlResultParts;

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

        // Add all of the Prioriteringsobjekt's to a map with the id as key
        for (Prioriteringsobjekt prio : prioriteringsobjekt) {
            PrioriteringsobjectForLargeResult item = new PrioriteringsobjectForLargeResult();
            copy(prio, item);
            idPrioMapping.put(prio.getId(), item);
            if (!item.getChildren().isEmpty()) {
                for (Prioriteringsobjekt child : item.getChildren()) {
                    idPrioMapping.put(child.getId(), child);
                }
            }
            result.add(item);
        }
        // Add DiagnosKod-items to the main query result.
        addLinkedObjects(example, LinkPrioriteringsobjektDiagnosKod.class, DiagnosKod.class, idPrioMapping,
                new KodSetting<DiagnosKod>() {

                    @Override
                    public void set(Prioriteringsobjekt p, DiagnosKod kod) {
                        p.getDiagnoser().add(kod);
                    }

                });

        // Add AatgaerdsKod-items to the main query result.
        addLinkedObjects(example, LinkPrioriteringsobjektAatgaerdsKod.class, AatgaerdsKod.class, idPrioMapping,
                new KodSetting<AatgaerdsKod>() {

                    @Override
                    public void set(Prioriteringsobjekt p, AatgaerdsKod kod) {
                        p.getAatgaerdskoder().add(kod);
                    }

                });

        // Add AtcKod-items to the main query result.
        addLinkedObjects(example, LinkPrioriteringsobjektAtcKod.class, AtcKod.class, idPrioMapping,
                new KodSetting<AtcKod>() {

                    @Override
                    public void set(Prioriteringsobjekt p, AtcKod kod) {
                        p.getAtcKoder().add(kod);
                    }

                });

        return result;
    }

    private interface KodSetting<T extends AbstractKod> {
        void set(Prioriteringsobjekt p, T kod);
    }

    private void addLinkedObjects(Prioriteringsobjekt example, Class<?> linkType, Class<?> codeType,
            Map<Long, Prioriteringsobjekt> prios, KodSetting kodSetting) {

        final JpqlMatchBuilder builder = new JpqlMatchBuilder() {
            /**
             * @inheritDoc
             */
            @Override
            protected String mkFetchJoinForMasterEntity(Object bean, String alias) {
                return "";
            }
        };

        Prioriteringsobjekt plainPrio = new Prioriteringsobjekt();
        new BeanMap(plainPrio).putAllWriteable(new BeanMap(example));

        List<Object> values = new ArrayList<Object>();
        JpqlResultParts jpqlParts = builder.mkFindByExampleJpqlParts(plainPrio, values);

        String jpql = "select distinct dl.id.prioId, d ";

        jpql += " from " + linkType.getSimpleName() + " dl, " + codeType.getSimpleName() + " d, "
                + jpqlParts.fromJoin.substring(5) + " left outer join o0.children cx";

        // Condition to hit both the template or / and also the approved versions.
        String joiningCondition = " ((o0.id = dl.id.prioId or cx.id = dl.id.prioId) and dl.id.kodId = d.id)";

        jpql += jpqlParts.where;
        if (jpqlParts.where.trim().length() > 0) {
            jpql += " and " + joiningCondition;
        } else {
            jpql += " where " + joiningCondition;
        }

        int i = 1;
        Query q = entityManager.createQuery(jpql);
        for (Object value : values) {
            q.setParameter(i++, value);
        }
        for (Object o : q.getResultList()) {
            Object[] idAndDiagnos = (Object[]) o;

            Prioriteringsobjekt prio = prios.get(idAndDiagnos[0]);
            if (prio != null) {
                kodSetting.set(prio, (AbstractKod) idAndDiagnos[1]);

            }
        }

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

        private Set<Prioriteringsobjekt> childrenWithNoAnnotation = new HashSet<Prioriteringsobjekt>();

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

        @Override
        public void setChildren(Set<Prioriteringsobjekt> children) {
            this.childrenWithNoAnnotation = children;
        }

        @Override
        public Set<Prioriteringsobjekt> getChildren() {
            return childrenWithNoAnnotation;
        }

        /**
         * @inheritDoc
         */
        @Override
        public Prioriteringsobjekt getChild() {
            if (getChildren().isEmpty()) {
                return null;
            }
            return getChildren().iterator().next();
        }
    }

    void copy(Prioriteringsobjekt source, Prioriteringsobjekt target) {
        // target.setDiagnoser(source.getDiagnoser());
        target.setId(source.getId());
        target.setPatientnyttoEvidensKod(source.getPatientnyttoEvidensKod());
        target.setKostnad(source.getKostnad());

        target.setChildren(new HashSet<Prioriteringsobjekt>());
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
