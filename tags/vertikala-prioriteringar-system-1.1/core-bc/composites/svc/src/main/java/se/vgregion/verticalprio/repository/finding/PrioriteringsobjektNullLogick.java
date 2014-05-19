package se.vgregion.verticalprio.repository.finding;

import se.vgregion.verticalprio.entity.Prioriteringsobjekt;

/**
 * @author Claes Lundahl, vgrid=clalu4
 * 
 */
public class PrioriteringsobjektNullLogick extends Prioriteringsobjekt implements HaveNullLogic {

    public PrioriteringsobjektNullLogick() {

    }

    public PrioriteringsobjektNullLogick(boolean notNull) {
        setNotNull(notNull);
    }

    private boolean notNull;

    /**
     * @inheritDoc
     */
    @Override
    public boolean isNotNull() {
        return notNull;
    }

    /**
     * @inheritDoc
     */
    @Override
    public void setNotNull(boolean notNull) {
        this.notNull = notNull;
    }

}
