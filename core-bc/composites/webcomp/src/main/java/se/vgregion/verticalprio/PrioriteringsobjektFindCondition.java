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
        // SortingSektorRaad ssr = new SortingSektorRaad();

        // Create sort order field that is used in NestedSektorRaad. It indicates that sorting should be done on
        // the field kod in the sektor raad table.
        // Sorting: NestedSektorsRaad -> SortingSektorsRaad -> SortOrderField ("kod")
        // SortOrderField sof = new SortOrderField();
        // sof.setOrder(0);
        // sof.setName("kod");
        // ssr.listSortOrders().add(sof);
        // nsr.content().add(ssr);
        super.setSektorRaad(nsr);

        OrderByPath sektorOrder = new OrderByPath("sektorRaad/kod");
        orderByPaths.add(sektorOrder);

        // The equivalent for adding sorting on diagnoses.
        NestedHashSet<DiagnosKod> diagnoser = new NestedHashSet<DiagnosKod>();
        // SortingDiagnosKod sdk = new SortingDiagnosKod();
        // sof = new SortOrderField();
        // sof.setOrder(1);
        // sof.setName("kod");
        // sdk.listSortOrders().add(sof);
        // diagnoser.add(sdk);
        super.setDiagnoser(diagnoser);

        OrderByPath diagnosOrder = new OrderByPath("diagnoser/kod");
        orderByPaths.add(diagnosOrder);

        // super.setTillstaandetsSvaarighetsgradKod(new NestedTillstaandetsSvaarighetsgradKod());

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

    // @Transient
    // @SuppressWarnings("serial")
    // ManyCodesRef<RangordningsKod> rangordningsRef = new ManyCodesRef<RangordningsKod>() {
    //
    // @Override
    // public Set<RangordningsKod> getCodes() {
    // return rangordningsHolder.content();
    // }
    //
    // @Override
    // public void setCodes(Set<RangordningsKod> codes) {
    // rangordningsHolder.setNestedContent(codes);
    // }
    //
    // };

    private NestedTillstaandetsSvaarighetsgradKod svaarighetsgradHolder = new NestedTillstaandetsSvaarighetsgradKod();

    // @Transient
    // @SuppressWarnings("serial")
    // final ManyCodesRef<TillstaandetsSvaarighetsgradKod> tillstaandetsSvaarighetsgradRef = new
    // ManyCodesRef<TillstaandetsSvaarighetsgradKod>() {
    //
    // @Override
    // public Set<TillstaandetsSvaarighetsgradKod> getCodes() {
    // return svaarighetsgradHolder.content();
    // }
    //
    // @Override
    // public void setCodes(Set<TillstaandetsSvaarighetsgradKod> codes) {
    // svaarighetsgradHolder.setNestedContent(codes);
    // }
    //
    // };
    //
    // @Transient
    // @SuppressWarnings("serial")
    // private ManyCodesRef<VaardformsKod> vaardformRef = new ManyCodesRef<VaardformsKod>() {
    //
    // @Override
    // public Set<VaardformsKod> getCodes() {
    // return vaardformHolder.content();
    // }
    //
    // @Override
    // public void setCodes(Set<VaardformsKod> codes) {
    // vaardformHolder.setNestedContent(codes);
    // }
    //
    // };
    //
    // /**
    // * @return the rangordningsRef
    // */
    // public ManyCodesRef<RangordningsKod> getRangordningsRef() {
    // return rangordningsRef;
    // }
    //
    // /**
    // * @return the tillstaandetsSvaarighetsgradRef
    // */
    // public ManyCodesRef<TillstaandetsSvaarighetsgradKod> getTillstaandetsSvaarighetsgradRef() {
    // return tillstaandetsSvaarighetsgradRef;
    // }
    //
    // public void setVaardformRef(ManyCodesRef<VaardformsKod> vaardformRef) {
    // this.vaardformRef = vaardformRef;
    // }
    //
    // public ManyCodesRef<VaardformsKod> getVaardformRef() {
    // return vaardformRef;
    // }

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

    public void sortByRangordningsKod() {
        // NestedRangordningsKod nrk = (NestedRangordningsKod) getRangordningsKod();
        // if (nrk == null) {
        // setRangordningsKod(nrk = new NestedRangordningsKod());
        // }
        // SortingRangordningsKod srk = new SortingRangordningsKod();
        // nrk.content().add(srk);
        // srk.listSortOrders().add(mkSortOrderField("kod"));
        //
        // NestedSektorRaad nsr = getSektorRaad();
        // SortingSektorRaad ssr = new SortingSektorRaad();
        // ssr.listSortOrders().add(mkSortOrderField("kod"));
        // nsr.content().add(ssr);
        //
        // SortingDiagnosKod sdk = new SortingDiagnosKod();
        // sdk.listSortOrders().add(mkSortOrderField("kod"));
        // getDiagnoser().add(sdk);

        paths().clear();
        paths().add(new OrderByPath("rangordningsKod/kod"));
        paths().add(new OrderByPath("sektorRaad/kod"));
        paths().add(new OrderByPath("diagnoser/kod"));
    }

    public void sortByTillstaandetsSvaarighetsgradKod() {
        // NestedTillstaandetsSvaarighetsgradKod ntsk = getTillstaandetsSvaarighetsgradKod();
        // SortingTillstaandetsSvaarighetsgradKod stsk = new SortingTillstaandetsSvaarighetsgradKod();
        // stsk.listSortOrders().add(mkSortOrderField("kod"));
        // ntsk.content().add(stsk);
        //
        // NestedSektorRaad nsr = getSektorRaad();
        // SortingSektorRaad ssr = new SortingSektorRaad();
        // ssr.listSortOrders().add(mkSortOrderField("kod"));
        // nsr.content().add(ssr);
        //
        // SortingDiagnosKod sdk = new SortingDiagnosKod();
        // sdk.listSortOrders().add(mkSortOrderField("kod"));
        // getDiagnoser().add(sdk);

        paths().clear();
        paths().add(new OrderByPath("tillstaandetsSvaarighetsgradKod/kod"));
        paths().add(new OrderByPath("sektorRaad/kod"));
        paths().add(new OrderByPath("diagnoser/kod"));
    }

    public void sortByDiagnoser() {
        paths().clear();
        paths().add(new OrderByPath("sektorRaad/kod"));
        paths().add(new OrderByPath("diagnoser/kod"));
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
