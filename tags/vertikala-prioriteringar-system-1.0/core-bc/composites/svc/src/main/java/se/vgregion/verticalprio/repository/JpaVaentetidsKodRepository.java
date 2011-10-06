package se.vgregion.verticalprio.repository;

import org.springframework.stereotype.Repository;

import se.vgregion.dao.domain.patterns.repository.db.jpa.DefaultJpaRepository;
import se.vgregion.verticalprio.entity.VaentetidsKod;

@Repository
public class JpaVaentetidsKodRepository extends DefaultJpaRepository<VaentetidsKod> implements
        VaentetidsKodRepository {
}