package se.vgregion.verticalprio.repository;

import java.util.List;

import se.vgregion.dao.domain.patterns.repository.Repository;
import se.vgregion.verticalprio.entity.AbstractKod;

public interface GenerisktHierarkisktKodRepository<T extends AbstractKod> extends Repository<T, Long> {

    public List<T> getTreeRoots();

}
