package se.vgregion.verticalprio.service;

import java.util.List;

import se.vgregion.dao.domain.patterns.repository.db.jpa.JpaRepository;
import se.vgregion.verticalprio.model.Sector;

public interface SectorRepository extends JpaRepository<Sector, Long, Long> {

    public List<Sector> getTreeRoots();

}
