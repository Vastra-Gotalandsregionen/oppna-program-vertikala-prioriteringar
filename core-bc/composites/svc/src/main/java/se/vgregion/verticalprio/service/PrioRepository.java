package se.vgregion.verticalprio.service;

import se.vgregion.dao.domain.patterns.repository.db.jpa.JpaRepository;
import se.vgregion.verticalprio.model.Prio;

public interface PrioRepository extends JpaRepository<Prio, Long, Long> {

}
