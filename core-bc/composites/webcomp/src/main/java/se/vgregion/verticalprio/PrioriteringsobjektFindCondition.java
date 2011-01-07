package se.vgregion.verticalprio;

import java.util.List;

import javax.persistence.Transient;

import se.vgregion.verticalprio.controllers.ManyCodesRef;
import se.vgregion.verticalprio.controllers.PrioriteringsobjektForm;
import se.vgregion.verticalprio.entity.Prioriteringsobjekt;
import se.vgregion.verticalprio.entity.RangordningsKod;
import se.vgregion.verticalprio.entity.TillstaandetsSvaarighetsgradKod;
import se.vgregion.verticalprio.repository.HaveExplicitTypeToFind;
import se.vgregion.verticalprio.repository.NestedRangordningsKod;
import se.vgregion.verticalprio.repository.NestedTillstaandetsSvaarighetsgradKod;

/**
 * To be used as search argument with the <code>PrioRepository</code> implementation and its
 * <code>findByExample</code> method.
 * 
 * @author Claes Lundahl, vgrid=clalu4
 */
public class PrioriteringsobjektFindCondition extends PrioriteringsobjektForm implements HaveExplicitTypeToFind {

    final private NestedRangordningsKod rangordningsHolder = new NestedRangordningsKod();

    public PrioriteringsobjektFindCondition() {
        super();
        setRangordningsKod(rangordningsHolder);
        setTillstaandetsSvaarighetsgradKod(svaarighetsgradHolder);
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
}
