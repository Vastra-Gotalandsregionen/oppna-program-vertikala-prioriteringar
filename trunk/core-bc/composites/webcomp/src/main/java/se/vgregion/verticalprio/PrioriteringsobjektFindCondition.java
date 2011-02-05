package se.vgregion.verticalprio;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.persistence.Transient;

import org.apache.commons.beanutils.BeanMap;

import se.vgregion.verticalprio.controllers.ManyCodesRef;
import se.vgregion.verticalprio.controllers.PrioriteringsobjektForm;
import se.vgregion.verticalprio.entity.Prioriteringsobjekt;
import se.vgregion.verticalprio.entity.RangordningsKod;
import se.vgregion.verticalprio.entity.TillstaandetsSvaarighetsgradKod;
import se.vgregion.verticalprio.entity.VaardformsKod;
import se.vgregion.verticalprio.repository.finding.HaveExplicitTypeToFind;
import se.vgregion.verticalprio.repository.finding.HaveNestedEntities;
import se.vgregion.verticalprio.repository.finding.HaveQuerySortOrder;
import se.vgregion.verticalprio.repository.finding.NestedRangordningsKod;
import se.vgregion.verticalprio.repository.finding.NestedTillstaandetsSvaarighetsgradKod;
import se.vgregion.verticalprio.repository.finding.NestedVaardformsKod;

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

    private final List<SortOrderField> sortOrder = new ArrayList<SortOrderField>();

    public PrioriteringsobjektFindCondition() {
        super();
        setRangordningsKod(rangordningsHolder);
        setTillstaandetsSvaarighetsgradKod(svaarighetsgradHolder);
        setVaardform(vaardformHolder);
    }

    /**
     * @inheritDoc
     */
    @Override
    public Class<?> type() {
        return Prioriteringsobjekt.class;
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
        clearSorting(this);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private void clearSorting(Object o) {
        if (o == null || o instanceof ManyCodesRef || o.getClass().isPrimitive()
                || o.getClass().getName().startsWith("java.")) {
            return;
        }
        if (o instanceof Collections) {
            Collection collection = (Collection) o;
            for (Object item : new ArrayList(collection)) {
                if (item instanceof HaveQuerySortOrder) {
                    collection.remove(item);
                } else {
                    clearSorting(item);
                }
            }
        }
        if (o instanceof HaveNestedEntities) {
            HaveNestedEntities hne = (HaveNestedEntities) o;
            clearSorting(hne.content());
        }

        BeanMap bm = new BeanMap(o);
        for (Object key : bm.keySet()) {
            Object value = bm.get(key);
            if (value instanceof HaveQuerySortOrder) {
                if (bm.getWriteMethod(key.toString()) != null) {
                    bm.put(key, null);
                }
            } else {
                clearSorting(value);
            }
        }
    }

}
