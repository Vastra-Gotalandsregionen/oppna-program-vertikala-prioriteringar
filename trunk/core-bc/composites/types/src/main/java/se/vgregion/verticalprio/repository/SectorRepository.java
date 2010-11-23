package se.vgregion.verticalprio.repository;

import java.util.List;

import se.vgregion.dao.domain.patterns.repository.Repository;
import se.vgregion.verticalprio.entity.SektorRaad;

public interface SectorRepository extends Repository<SektorRaad, Long> {

	public List<SektorRaad> getTreeRoots();

}
