package se.vgregion.verticalprio.repository;

import java.util.List;

import se.vgregion.dao.domain.patterns.repository.Repository;
import se.vgregion.verticalprio.entity.Sector;

public interface SectorRepository extends Repository<Sector, Long> {

	public List<Sector> getTreeRoots();

}
