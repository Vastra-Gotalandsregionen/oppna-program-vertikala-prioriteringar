package se.vgregion.verticalprio.repository.finding;

/**
 * @author Claes Lundahl, vgrid=clalu4
 * 
 */
public class SetNullLogic implements HaveNullLogic {

    public SetNullLogic() {

    }

    public SetNullLogic(boolean notNull) {
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
