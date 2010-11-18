package se.vgregion.verticalprio.service;

import se.vgregion.dao.domain.patterns.repository.db.jpa.JpaRepository;
import se.vgregion.verticalprio.model.Diagnosis;

public interface DiagnosisRepository extends JpaRepository<Diagnosis, Long, Long> {

}
