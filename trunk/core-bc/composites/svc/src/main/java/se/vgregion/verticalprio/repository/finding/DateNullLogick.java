package se.vgregion.verticalprio.repository.finding;

import java.util.Date;

/**
 * @author Claes Lundahl, vgrid=clalu4
 * 
 */
public class DateNullLogick extends Date implements HaveNullLogick {

    public DateNullLogick() {

    }

    public DateNullLogick(boolean notNull) {
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
