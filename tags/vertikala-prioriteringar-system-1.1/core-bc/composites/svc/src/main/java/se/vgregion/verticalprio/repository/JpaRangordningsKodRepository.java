package se.vgregion.verticalprio.repository;

import org.springframework.stereotype.Repository;

import se.vgregion.dao.domain.patterns.repository.db.jpa.DefaultJpaRepository;
import se.vgregion.verticalprio.entity.RangordningsKod;

@Repository
public class JpaRangordningsKodRepository extends DefaultJpaRepository<RangordningsKod> implements
        RangordningsKodRepository {
}