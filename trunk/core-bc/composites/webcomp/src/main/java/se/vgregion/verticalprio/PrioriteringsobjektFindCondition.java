package se.vgregion.verticalprio;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Transient;

import se.vgregion.verticalprio.controllers.ManyCodesRef;
import se.vgregion.verticalprio.controllers.PrioriteringsobjektForm;
import se.vgregion.verticalprio.entity.Prioriteringsobjekt;
import se.vgregion.verticalprio.entity.RangordningsKod;
import se.vgregion.verticalprio.entity.TillstaandetsSvaarighetsgradKod;
import se.vgregion.verticalprio.entity.VaardformsKod;
import se.vgregion.verticalprio.repository.HaveExplicitTypeToFind;
import se.vgregion.verticalprio.repository.HaveQuerySortOrder;
import se.vgregion.verticalprio.repository.NestedRangordningsKod;
import se.vgregion.verticalprio.repository.NestedTillstaandetsSvaarighetsgradKod;
import se.vgregion.verticalprio.repository.NestedVaardformsKod;

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
        public List<RangordningsKod> getCodes() {
            return rangordningsHolder.content();
        }

        @Override
        public void setCodes(List<RangordningsKod> codes) {
            rangordningsHolder.setNestedContent(codes);
        }

    };

    private NestedTillstaandetsSvaarighetsgradKod svaarighetsgradHolder = new NestedTillstaandetsSvaarighetsgradKod();

    @Transient
    @SuppressWarnings("serial")
    final ManyCodesRef<TillstaandetsSvaarighetsgradKod> tillstaandetsSvaarighetsgradRef = new ManyCodesRef<TillstaandetsSvaarighetsgradKod>() {

        @Override
        public List<TillstaandetsSvaarighetsgradKod> getCodes() {
            return svaarighetsgradHolder.content();
        }

        @Override
        public void setCodes(List<TillstaandetsSvaarighetsgradKod> codes) {
            svaarighetsgradHolder.setNestedContent(codes);
        }

    };

    @Transient
    @SuppressWarnings("serial")
    private ManyCodesRef<VaardformsKod> vaardformRef = new ManyCodesRef<VaardformsKod>() {

        @Override
        public List<VaardformsKod> getCodes() {
            return vaardformHolder.content();
        }

        @Override
        public void setCodes(List<VaardformsKod> codes) {
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
}
