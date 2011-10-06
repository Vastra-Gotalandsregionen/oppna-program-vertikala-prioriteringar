package se.vgregion.verticalprio.repository;

import org.springframework.stereotype.Repository;

import se.vgregion.dao.domain.patterns.entity.AbstractEntity;
import se.vgregion.verticalprio.entity.AbstractKod;

@Repository
public class JpaGenerisktKodRepository<T extends AbstractEntity<Long>> extends JpaGenerisktFinderRepository<T>
        implements GenerisktKodRepository<T> {

	private Class klass;

	private String wildCard = "*";

	private String jpaWildCard = "%";

	public JpaGenerisktKodRepository() {
		this((Class<T>) AbstractKod.class);
	}

	/**
	 * Initializing the repository with the intended type of bean to be handled.
	 */
	public JpaGenerisktKodRepository(Class<T> klass) {
		super(klass);
		this.klass = klass;
	}

	@Override
	public void clear() {
		entityManager.clear();
	}

}