package se.vgregion.verticalprio.repository;

import java.util.Collection;
import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import se.vgregion.dao.domain.patterns.repository.db.jpa.DefaultJpaRepository;
import se.vgregion.verticalprio.entity.Sector;
import se.vgregion.verticalprio.repository.SectorRepository;

@Repository
public class JpaSectorRepository extends DefaultJpaRepository<Sector> implements SectorRepository {

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<Sector> getTreeRoots() {
        List<Sector> result = query("select o from @ o where o.parent is null");
        populateChildren(result);
        return result;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    private void populateChildren(Collection<Sector> parents) {
        for (Sector child : parents) {
            populateChildren(child);
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    private void populateChildren(Sector parent) {
        String qt = "select o from @ o where o.parent = ?1";
        parent.getChildren().addAll(query(qt, parent));
        populateChildren(parent.getChildren());
    }

    @SuppressWarnings("unchecked")
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    private List<Sector> query(String qt, Object... values) {
        qt = qt.replace("@", type.getSimpleName());
        Query query = entityManager.createQuery(qt);
        int i = 1;
        for (Object value : values) {
            query.setParameter(i++, value);
        }
        return query.getResultList();
    }

}
