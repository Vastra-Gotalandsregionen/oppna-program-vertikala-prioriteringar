package se.vgregion.verticalprio.repository;

import java.util.Collection;
import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import se.vgregion.dao.domain.patterns.repository.db.jpa.DefaultJpaRepository;
import se.vgregion.verticalprio.entity.SektorRaad;

@Repository
public class JpaSektorRaadRepository extends DefaultJpaRepository<SektorRaad> implements SektorRaadRepository {

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<SektorRaad> getTreeRoots() {
        List<SektorRaad> result = query("select o from @ o where o.parent is null order by LOWER(o.code) DESC");
        populateChildren(result);
        return result;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    private void populateChildren(Collection<SektorRaad> parents) {
        for (SektorRaad child : parents) {
            populateChildren(child);
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    private void populateChildren(SektorRaad parent) {
        String qt = "select o from @ o where o.parent = ?1";
        parent.getChildren().addAll(query(qt, parent));
        populateChildren(parent.getChildren());
    }

    @SuppressWarnings("unchecked")
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    private List<SektorRaad> query(String qt, Object... values) {
        qt = qt.replace("@", SektorRaad.class.getSimpleName());
        Query query = entityManager.createQuery(qt);
        int i = 1;
        for (Object value : values) {
            query.setParameter(i++, value);
        }
        return query.getResultList();
    }

}
