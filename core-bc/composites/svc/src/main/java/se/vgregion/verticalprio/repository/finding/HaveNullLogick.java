package se.vgregion.verticalprio.repository.finding;

/**
 * Use this interface to create values that signals not therir actual value but that the value should, or should
 * not, be null.
 * 
 * @author Claes Lundahl, vgrid=clalu4
 */
public interface HaveNullLogick {

    /**
     * If returns true the field referenced with this is checked for not null - 'foo is not null'. If untrue the
     * opposite applies - 'foo is null'.
     * 
     * @return
     */
    boolean isNotNull();

    /**
     * Sets the field.
     * 
     * @param notNull
     */
    void setNotNull(boolean notNull);

}
