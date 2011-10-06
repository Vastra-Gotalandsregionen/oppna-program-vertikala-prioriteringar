package se.vgregion.verticalprio.repository.finding;

/**
 * Can be used on a example objects class when using it as argument to the <code>findByExample</code> in
 * <code>GenericFinderRepository</code>. It is used to specify what type to retriew by the jpa call.
 * 
 * @author Claes Lundahl, vgrid=clalu4
 */
public interface HaveExplicitTypeToFind {

    /**
     * 
     * @return type to select when doing a "find by example".
     */
    Class<?> type();

}
