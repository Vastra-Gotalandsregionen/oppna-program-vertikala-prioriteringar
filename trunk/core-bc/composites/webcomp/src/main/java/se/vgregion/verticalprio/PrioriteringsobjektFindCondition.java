package se.vgregion.verticalprio;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import se.vgregion.verticalprio.controllers.PrioriteringsobjektForm;
import se.vgregion.verticalprio.entity.AatgaerdsKod;
import se.vgregion.verticalprio.entity.AtcKod;
import se.vgregion.verticalprio.entity.DiagnosKod;
import se.vgregion.verticalprio.entity.Prioriteringsobjekt;
import se.vgregion.verticalprio.entity.SektorRaad;
import se.vgregion.verticalprio.entity.TillstaandetsSvaarighetsgradKod;
import se.vgregion.verticalprio.entity.VaardnivaaKod;
import se.vgregion.verticalprio.repository.finding.CoalesceingOrderByPath;
import se.vgregion.verticalprio.repository.finding.DateNullLogic;
import se.vgregion.verticalprio.repository.finding.HaveExplicitTypeToFind;
import se.vgregion.verticalprio.repository.finding.HaveOrderByPaths;
import se.vgregion.verticalprio.repository.finding.NestedHashSet;
import se.vgregion.verticalprio.repository.finding.NestedRangordningsKod;
import se.vgregion.verticalprio.repository.finding.NestedSektorRaad;
import se.vgregion.verticalprio.repository.finding.NestedTillstaandetsSvaarighetsgradKod;
import se.vgregion.verticalprio.repository.finding.NestedVaardformsKod;
import se.vgregion.verticalprio.repository.finding.NestedVaardnivaaKod;
import se.vgregion.verticalprio.repository.finding.OrderByPath;

/**
 * To be used as search argument with the <code>PrioRepository</code> implementation and its
 * <code>findByExample</code> method.
 * 
 * @author Claes Lundahl, vgrid=clalu4
 */
public class PrioriteringsobjektFindCondition extends PrioriteringsobjektForm implements HaveExplicitTypeToFind,
        HaveOrderByPaths {

    private final NestedRangordningsKod rangordningsHolder = new NestedRangordningsKod();

    private final NestedVaardformsKod vaardformHolder = new NestedVaardformsKod();

    private final NestedVaardnivaaKod vaardnivaHolder = new NestedVaardnivaaKod();

    private Class<? extends Prioriteringsobjekt> typeToFind = Prioriteringsobjekt.class;

    private final List<OrderByPath> orderByPaths = new ArrayList<OrderByPath>();

    public PrioriteringsobjektFindCondition() {
        super();
        setRangordningsKod(rangordningsHolder);
        super.setTillstaandetsSvaarighetsgradKod(svaarighetsgradHolder);
        setVaardform(vaardformHolder);

        NestedSektorRaad nsr = new NestedSektorRaad();
        super.setSektorRaad(nsr);

        sortBySektorsRaad();

        NestedHashSet<DiagnosKod> diagnoser = new NestedHashSet<DiagnosKod>();
        super.setDiagnoser(diagnoser);

        super.setAatgaerdskoder(new NestedHashSet<AatgaerdsKod>());
        super.setAtcKoder(new NestedHashSet<AtcKod>());
        setGodkaend(new DateNullLogic(true));
    }

    /**
     * @inheritDoc
     */
    @Override
    public Class<?> type() {
        return typeToFind;
    }

    private NestedTillstaandetsSvaarighetsgradKod svaarighetsgradHolder = new NestedTillstaandetsSvaarighetsgradKod();

    /**
     * @inheritDoc
     */
    @Override
    public NestedSektorRaad getSektorRaad() {
        return (NestedSektorRaad) super.getSektorRaad();
    }

    /**
     * @inheritDoc
     */
    @Override
    public void setSektorRaad(SektorRaad sektorRaad) {
        throw new UnsupportedOperationException("Dont use this setter");
    }

    public void sortBySektorsRaad() {
        paths().clear();
        addSortBySektorRaad();
        OrderByPath diagnosOrder = new OrderByPath("diagnoser/kod");
        paths().add(diagnosOrder);
    }

    public void sortByRangordningsKod() {
        paths().clear();
        paths().add(new OrderByPath("rangordningsKod/kod"));
        addSortBySektorRaad();
        paths().add(new OrderByPath("diagnoser/kod"));
    }

    public void sortByTillstaandetsSvaarighetsgradKod() {
        paths().clear();
        paths().add(new OrderByPath("tillstaandetsSvaarighetsgradKod/kod"));
        addSortBySektorRaad();
        paths().add(new OrderByPath("diagnoser/kod"));
    }

    public void sortByDiagnoser() {
        paths().clear();
        addSortBySektorRaad();
        paths().add(new OrderByPath("diagnoser/kod"));
    }

    private void addSortBySektorRaad() {
        CoalesceingOrderByPath coalesor = new CoalesceingOrderByPath(new OrderByPath("sektorRaad/parent/kod"),
                new OrderByPath("sektorRaad/kod"));
        orderByPaths.add(coalesor);
    }

    /**
     * @inheritDoc
     */
    @Override
    public NestedTillstaandetsSvaarighetsgradKod getTillstaandetsSvaarighetsgradKod() {
        return (NestedTillstaandetsSvaarighetsgradKod) super.getTillstaandetsSvaarighetsgradKod();
    }

    /**
     * @inheritDoc
     */
    @Override
    public void setTillstaandetsSvaarighetsgradKod(TillstaandetsSvaarighetsgradKod tillstaandetsSvaarighetsgrad) {
        throw new UnsupportedOperationException("Dont use this setter");
    }

    /**
     * @inheritDoc
     */
    @Override
    public void setDiagnoser(Set<DiagnosKod> diagnoser) {
        throw new UnsupportedOperationException("Dont use this setter");
    }

    /**
     * @inheritDoc
     */
    @Override
    public void setAatgaerdskoder(Set<AatgaerdsKod> aatgaerdskoder) {
        throw new UnsupportedOperationException("Dont use this setter");
    }

    /**
     * @inheritDoc
     */
    @Override
    public void setAtcKoder(Set<AtcKod> atcKoder) {
        throw new UnsupportedOperationException("Dont use this setter");
    }

    /**
     * @inheritDoc
     */
    @Override
    public void setVaardnivaaKod(VaardnivaaKod vaardnivaaKod) {
        throw new UnsupportedOperationException("Dont use this setter");
    }

    /**
     * @inheritDoc
     */
    @Override
    public NestedVaardnivaaKod getVaardnivaaKod() {
        return vaardnivaHolder;
    }

    public void setTypeToFind(Class<? extends Prioriteringsobjekt> typeToFind) {
        this.typeToFind = typeToFind;
    }

    public Class<? extends Prioriteringsobjekt> getTypeToFind() {
        return typeToFind;
    }

    /**
     * @inheritDoc
     */
    @Override
    public List<OrderByPath> paths() {
        return orderByPaths;
    }

}
