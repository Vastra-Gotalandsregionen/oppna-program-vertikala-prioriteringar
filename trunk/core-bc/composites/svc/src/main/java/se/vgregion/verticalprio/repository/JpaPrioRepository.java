package se.vgregion.verticalprio.repository;

import org.springframework.stereotype.Repository;

import se.vgregion.dao.domain.patterns.repository.db.jpa.DefaultJpaRepository;
import se.vgregion.verticalprio.entity.Prioriteringsobjekt;

@Repository
public class JpaPrioRepository extends DefaultJpaRepository<Prioriteringsobjekt> implements PrioRepository {

}
