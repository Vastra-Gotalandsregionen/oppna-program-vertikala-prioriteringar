package se.vgregion.verticalprio.repository;

import org.springframework.stereotype.Repository;

import se.vgregion.verticalprio.entity.Prioriteringsobjekt;

@Repository
public class JpaPrioRepository extends JpaGenerisktFinderRepository<Prioriteringsobjekt> implements PrioRepository {

    public JpaPrioRepository() {
        super(Prioriteringsobjekt.class);
    }

}
