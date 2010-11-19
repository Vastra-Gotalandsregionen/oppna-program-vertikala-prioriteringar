package se.vgregion.verticalprio.service;

import org.springframework.stereotype.Repository;

import se.vgregion.dao.domain.patterns.repository.db.jpa.DefaultJpaRepository;
import se.vgregion.verticalprio.model.Prio;

@Repository
public class JpaPrioRepository extends DefaultJpaRepository<Prio> implements PrioRepository {

}
