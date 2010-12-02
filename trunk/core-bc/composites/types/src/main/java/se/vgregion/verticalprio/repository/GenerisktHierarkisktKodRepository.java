package se.vgregion.verticalprio.repository;

import java.util.List;

import se.vgregion.verticalprio.entity.AbstractKod;

public interface GenerisktHierarkisktKodRepository<T extends AbstractKod> extends GenerisktKodRepository<T> {

    public List<T> getTreeRoots();

}
