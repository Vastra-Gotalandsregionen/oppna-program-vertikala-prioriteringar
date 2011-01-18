package se.vgregion.verticalprio.entity;

/**
 * @author Claes Lundahl, vgrid=clalu4
 * 
 */
@SuppressWarnings("serial")
public class AbstractShortLabelKod extends AbstractKod {

    /**
     * @inheritDoc
     */
    @Override
    public String getLabel() {
        return getKod();
    }

}
