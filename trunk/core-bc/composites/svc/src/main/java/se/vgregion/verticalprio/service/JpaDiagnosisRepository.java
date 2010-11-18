package se.vgregion.verticalprio.service;

import se.vgregion.dao.domain.patterns.repository.db.jpa.DefaultJpaRepository;
import se.vgregion.verticalprio.model.Diagnosis;

public class JpaDiagnosisRepository extends DefaultJpaRepository<Diagnosis> implements DiagnosisRepository {

}
