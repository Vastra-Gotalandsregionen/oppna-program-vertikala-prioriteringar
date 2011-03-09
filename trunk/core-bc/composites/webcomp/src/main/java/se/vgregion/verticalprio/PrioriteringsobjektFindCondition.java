package se.vgregion.verticalprio;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Transient;

import org.apache.commons.beanutils.BeanMap;
import org.hibernate.LazyInitializationException;

import se.vgregion.verticalprio.controllers.ManyCodesRef;
import se.vgregion.verticalprio.controllers.PrioriteringsobjektForm;
import se.vgregion.verticalprio.entity.AatgaerdsKod;
import se.vgregion.verticalprio.entity.AbstractPrioriteringsobjekt;
import se.vgregion.verticalprio.entity.AtcKod;
import se.vgregion.verticalprio.entity.DiagnosKod;
import se.vgregion.verticalprio.entity.Prioriteringsobjekt;
import se.vgregion.verticalprio.entity.RangordningsKod;
import se.vgregion.verticalprio.entity.SektorRaad;
import se.vgregion.verticalprio.entity.TillstaandetsSvaarighetsgradKod;
import se.vgregion.verticalprio.entity.VaardformsKod;
import se.vgregion.verticalprio.entity.VaardnivaaKod;
import se.vgregion.verticalprio.repository.finding.HaveExplicitTypeToFind;
import se.vgregion.verticalprio.repository.finding.HaveNestedEntities;
import se.vgregion.verticalprio.repository.finding.HaveQuerySortOrder;
import se.vgregion.verticalprio.repository.finding.NestedHashSet;
import se.vgregion.verticalprio.repository.finding.NestedRangordningsKod;
import se.vgregion.verticalprio.repository.finding.NestedSektorRaad;
import se.vgregion.verticalprio.repository.finding.NestedTillstaandetsSvaarighetsgradKod;
import se.vgregion.verticalprio.repository.finding.NestedVaardformsKod;
import se.vgregion.verticalprio.repository.finding.NestedVaardnivaaKod;
import se.vgregion.verticalprio.repository.finding.SortingDiagnosKod;
import se.vgregion.verticalprio.repository.finding.SortingRangordningsKod;
import se.vgregion.verticalprio.repository.finding.SortingSektorRaad;
import se.vgregion.verticalprio.repository.finding.SortingTillstaandetsSvaarighetsgradKod;

/**
 * To be used as search argument with the <code>PrioRepository</code> implementation and its
 * <code>findByExample</code> method.
 * 
 * @author Claes Lundahl, vgrid=clalu4
 */
public class PrioriteringsobjektFindCondition extends PrioriteringsobjektForm implements HaveExplicitTypeToFind,
        HaveQuerySortOrder {

    private final NestedRangordningsKod rangordningsHolder = new NestedRangordningsKod();

    private final NestedVaardformsKod vaardformHolder = new NestedVaardformsKod();

    private final NestedVaardnivaaKod vaardnivaHolder = new NestedVaardnivaaKod();

    private final List<SortOrderField> sortOrder = new ArrayList<SortOrderField>();

    private Class<? extends AbstractPrioriteringsobjekt> typeToFind = Prioriteringsobjekt.class;

    public PrioriteringsobjektFindCondition() {
        super();
        setRangordningsKod(rangordningsHolder);
        super.setTillstaandetsSvaarighetsgradKod(svaarighetsgradHolder);
        setVaardform(vaardformHolder);

        NestedSektorRaad nsr = new NestedSektorRaad();
        SortingSektorRaad ssr = new SortingSektorRaad();

        // Create sort order field that is used in NestedSektorRaad. It indicates that sorting should be done on
        // the field kod in the sektor raad table.
        // Sorting: NestedSektorsRaad -> SortingSektorsRaad -> SortOrderField ("kod")
        SortOrderField sof = new SortOrderField();
        sof.setOrder(0);
        sof.setName("kod");
        ssr.listSortOrders().add(sof);
        nsr.content().add(ssr);
        super.setSektorRaad(nsr);

        super.setTillstaandetsSvaarighetsgradKod(new NestedTillstaandetsSvaarighetsgradKod());

        NestedHashSet<DiagnosKod> diagnoser = new NestedHashSet<DiagnosKod>();
        SortingDiagnosKod sdk = new SortingDiagnosKod();
        sof = new SortOrderField();
        sof.setOrder(1);
        sof.setName("kod");
        sdk.listSortOrders().add(sof);
        diagnoser.add(new SortingDiagnosKod());
        super.setDiagnoser(diagnoser);

        super.setAatgaerdskoder(new NestedHashSet<AatgaerdsKod>());
        super.setAtcKoder(new NestedHashSet<AtcKod>());
        // setGodkaend(new DateNullLogick(true));
    }

    /**
     * @inheritDoc
     */
    @Override
    public Class<?> type() {
        return typeToFind;
    }

    @Transient
    @SuppressWarnings("serial")
    ManyCodesRef<RangordningsKod> rangordningsRef = new ManyCodesRef<RangordningsKod>() {

        @Override
        public Set<RangordningsKod> getCodes() {
            return rangordningsHolder.content();
        }

        @Override
        public void setCodes(Set<RangordningsKod> codes) {
            rangordningsHolder.setNestedContent(codes);
        }

    };

    private NestedTillstaandetsSvaarighetsgradKod svaarighetsgradHolder = new NestedTillstaandetsSvaarighetsgradKod();

    @Transient
    @SuppressWarnings("serial")
    final ManyCodesRef<TillstaandetsSvaarighetsgradKod> tillstaandetsSvaarighetsgradRef = new ManyCodesRef<TillstaandetsSvaarighetsgradKod>() {

        @Override
        public Set<TillstaandetsSvaarighetsgradKod> getCodes() {
            return svaarighetsgradHolder.content();
        }

        @Override
        public void setCodes(Set<TillstaandetsSvaarighetsgradKod> codes) {
            svaarighetsgradHolder.setNestedContent(codes);
        }

    };

    @Transient
    @SuppressWarnings("serial")
    private ManyCodesRef<VaardformsKod> vaardformRef = new ManyCodesRef<VaardformsKod>() {

        @Override
        public Set<VaardformsKod> getCodes() {
            return vaardformHolder.content();
        }

        @Override
        public void setCodes(Set<VaardformsKod> codes) {
            vaardformHolder.setNestedContent(codes);
        }

    };

    /**
     * @return the rangordningsRef
     */
    public ManyCodesRef<RangordningsKod> getRangordningsRef() {
        return rangordningsRef;
    }

    /**
     * @return the tillstaandetsSvaarighetsgradRef
     */
    public ManyCodesRef<TillstaandetsSvaarighetsgradKod> getTillstaandetsSvaarighetsgradRef() {
        return tillstaandetsSvaarighetsgradRef;
    }

    public void setVaardformRef(ManyCodesRef<VaardformsKod> vaardformRef) {
        this.vaardformRef = vaardformRef;
    }

    public ManyCodesRef<VaardformsKod> getVaardformRef() {
        return vaardformRef;
    }

    /**
     * @inheritDoc
     */
    @Override
    public List<SortOrderField> listSortOrders() {
        return sortOrder;
    }

    /**
     * Clears (removes) all objects that implements the {@link HaveQuerySortOrder} interface in the object graph
     * underneath this object. It also removes all {@link SortOrderField} from itself.
     */
    public void clearSorting() {
        listSortOrders().clear();
        clearSorting(this, new HashSet<Object>());
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private void clearSorting(Object o, Set<Object> passed) {
        if (o == null || o instanceof ManyCodesRef || o.getClass().isPrimitive()) {
            return;
        }
        if (passed.contains(o)) {
            return;
        } else {
            passed.add(o);
        }
        if (o instanceof Collection) {
            Collection collection = (Collection) o;
            for (Object item : new ArrayList(collection)) {
                if (item instanceof HaveQuerySortOrder) {
                    collection.remove(item);
                } else {
                    try {
                        clearSorting(item, passed);
                    } catch (LazyInitializationException e) {
                        // ignore and continue. If this error occurs no
                        // sorting could be found anyway deeper down.
                    }
                }
            }
        }
        if (o instanceof HaveNestedEntities) {
            HaveNestedEntities hne = (HaveNestedEntities) o;
            clearSorting(hne.content(), passed);
        }

        BeanMap bm = new BeanMap(o);
        for (Object key : bm.keySet()) {
            Object value = bm.get(key);
            if (value instanceof HaveQuerySortOrder) {
                if (bm.getWriteMethod(key.toString()) != null) {
                    bm.put(key, null);
                }
            } else {
                clearSorting(value, passed);
            }
        }
    }

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
        NestedRangordningsKod nrk = (NestedRangordningsKod) getRangordningsKod();
        if (nrk == null) {
            setRangordningsKod(nrk = new NestedRangordningsKod());
        }
        SortingRangordningsKod srk = new SortingRangordningsKod();
        nrk.content().add(srk);
        srk.listSortOrders().add(mkSortOrderField("kod"));

        NestedSektorRaad nsr = getSektorRaad();
        SortingSektorRaad ssr = new SortingSektorRaad();
        ssr.listSortOrders().add(mkSortOrderField("kod"));
        nsr.content().add(ssr);

        SortingDiagnosKod sdk = new SortingDiagnosKod();
        sdk.listSortOrders().add(mkSortOrderField("kod"));
        getDiagnoser().add(sdk);
    }

    // Svårighetsgrad (sektorsråd, diagnoskod)
    public void sortByTillstaandetsSvaarighetsgradKod() {
        NestedTillstaandetsSvaarighetsgradKod ntsk = getTillstaandetsSvaarighetsgradKod();
        SortingTillstaandetsSvaarighetsgradKod stsk = new SortingTillstaandetsSvaarighetsgradKod();
        stsk.listSortOrders().add(mkSortOrderField("kod"));
        ntsk.content().add(stsk);

        NestedSektorRaad nsr = getSektorRaad();
        SortingSektorRaad ssr = new SortingSektorRaad();
        ssr.listSortOrders().add(mkSortOrderField("kod"));
        nsr.content().add(ssr);

        SortingDiagnosKod sdk = new SortingDiagnosKod();
        sdk.listSortOrders().add(mkSortOrderField("kod"));
        getDiagnoser().add(sdk);
    }

    // Diagnoskod (sektorsråd)
    public void sortByDiagnoser() {
        SortingDiagnosKod sdk = new SortingDiagnosKod();
        sdk.listSortOrders().add(mkSortOrderField("kod"));
        getDiagnoser().add(sdk);

        NestedSektorRaad nsr = getSektorRaad();
        SortingSektorRaad ssr = new SortingSektorRaad();
        ssr.listSortOrders().add(mkSortOrderField("kod"));
        nsr.content().add(ssr);
    }

    private int sortOrderCount = 0;

    private SortOrderField mkSortOrderField(String name) {
        SortOrderField sof = new SortOrderField();
        sof.setName("kod");
        sof.setOrder(sortOrderCount++);
        return sof;
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

    public void setTypeToFind(Class<? extends AbstractPrioriteringsobjekt> typeToFind) {
        this.typeToFind = typeToFind;
    }

    public Class<? extends AbstractPrioriteringsobjekt> getTypeToFind() {
        return typeToFind;
    }

}
