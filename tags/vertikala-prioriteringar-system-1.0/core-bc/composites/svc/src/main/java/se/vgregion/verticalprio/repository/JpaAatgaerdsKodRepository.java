package se.vgregion.verticalprio.repository;

import org.springframework.stereotype.Repository;

import se.vgregion.dao.domain.patterns.repository.db.jpa.DefaultJpaRepository;
import se.vgregion.verticalprio.entity.AatgaerdsKod;

@Repository
public class JpaAatgaerdsKodRepository extends DefaultJpaRepository<AatgaerdsKod> implements
        AatgaerdsKodRepository {
}