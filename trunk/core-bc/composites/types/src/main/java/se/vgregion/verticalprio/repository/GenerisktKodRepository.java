package se.vgregion.verticalprio.repository;

import se.vgregion.dao.domain.patterns.repository.Repository;
import se.vgregion.verticalprio.entity.AbstractKod;

public interface GenerisktKodRepository<T extends AbstractKod> extends Repository<T, Long> {
}