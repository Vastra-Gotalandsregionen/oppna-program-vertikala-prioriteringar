package se.vgregion.verticalprio.repository;

import se.vgregion.dao.domain.patterns.repository.db.jpa.DefaultJpaRepository;
import se.vgregion.verticalprio.entity.Diagnosis;
import se.vgregion.verticalprio.repository.DiagnosisRepository;

public class JpaDiagnosisRepository extends DefaultJpaRepository<Diagnosis> implements DiagnosisRepository {

}
