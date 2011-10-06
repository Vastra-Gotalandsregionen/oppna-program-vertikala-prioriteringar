package se.vgregion.verticalprio.repository;

import org.springframework.stereotype.Repository;

import se.vgregion.dao.domain.patterns.repository.db.jpa.DefaultJpaRepository;
import se.vgregion.verticalprio.entity.TillstaandetsSvaarighetsgradKod;

@Repository
public class JpaTillstaandetsSvaarighetsgradKodRepository extends
        DefaultJpaRepository<TillstaandetsSvaarighetsgradKod> implements TillstaandetsSvaarighetsgradKodRepository {
}