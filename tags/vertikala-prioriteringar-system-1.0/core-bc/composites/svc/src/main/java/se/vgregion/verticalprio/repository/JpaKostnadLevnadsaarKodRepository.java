package se.vgregion.verticalprio.repository;

import org.springframework.stereotype.Repository;

import se.vgregion.dao.domain.patterns.repository.db.jpa.DefaultJpaRepository;
import se.vgregion.verticalprio.entity.KostnadLevnadsaarKod;

@Repository
public class JpaKostnadLevnadsaarKodRepository extends DefaultJpaRepository<KostnadLevnadsaarKod> implements
        KostnadLevnadsaarKodRepository {
}