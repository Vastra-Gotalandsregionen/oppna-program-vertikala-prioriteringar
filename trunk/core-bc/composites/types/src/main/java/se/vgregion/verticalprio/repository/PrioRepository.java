package se.vgregion.verticalprio.repository;

import java.util.List;

import se.vgregion.verticalprio.entity.Prioriteringsobjekt;

public interface PrioRepository extends GenerisktFinderRepository<Prioriteringsobjekt> {

    /**
     * Gets a larger result from the db.
     * 
     * @return
     */
    List<Prioriteringsobjekt> findLargeResult(Prioriteringsobjekt example);

}
