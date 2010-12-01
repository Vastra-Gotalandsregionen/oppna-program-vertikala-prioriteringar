package se.vgregion.verticalprio.repository;

import org.springframework.stereotype.Repository;

import se.vgregion.dao.domain.patterns.repository.db.jpa.DefaultJpaRepository;
import se.vgregion.verticalprio.entity.AbstractKod;

@Repository
public class JpaGenerisktKodRepository<T extends AbstractKod> extends DefaultJpaRepository<T> implements GenerisktKodRepository<T> {

    /**
     * Initializing the repository with the intended type of bean to be handled.
     */
    public JpaGenerisktKodRepository(Class<T> klass) {
        super(klass);
    }

}