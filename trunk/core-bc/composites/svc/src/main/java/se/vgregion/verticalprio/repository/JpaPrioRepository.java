package se.vgregion.verticalprio.repository;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import se.vgregion.verticalprio.entity.Prioriteringsobjekt;

@Repository
public class JpaPrioRepository extends JpaGenerisktFinderRepository<Prioriteringsobjekt> implements PrioRepository {

    public JpaPrioRepository() {
        super(Prioriteringsobjekt.class);
    }

    @Override
    @SuppressWarnings("unchecked")
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    protected List<Prioriteringsobjekt> query(String qt, Integer maxResult, Object... values) {
        qt = qt.replace("@", Prioriteringsobjekt.class.getSimpleName());
        Query query = entityManager.createQuery(qt);
        if (maxResult != null) {
            query.setMaxResults(maxResult);
        }
        int i = 1;
        for (Object value : values) {
            query.setParameter(i++, value);
        }
        try {
            List<Prioriteringsobjekt> result = query.getResultList();
            return result;
        } catch (Throwable e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }

}
