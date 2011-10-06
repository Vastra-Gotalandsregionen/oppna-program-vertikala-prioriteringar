package se.vgregion.verticalprio.repository;

import java.util.List;

import se.vgregion.dao.domain.patterns.entity.AbstractEntity;

public interface GenerisktHierarkisktKodRepository<T extends AbstractEntity<Long>> extends
        GenerisktKodRepository<T> {

	public List<T> getTreeRoots();

}
