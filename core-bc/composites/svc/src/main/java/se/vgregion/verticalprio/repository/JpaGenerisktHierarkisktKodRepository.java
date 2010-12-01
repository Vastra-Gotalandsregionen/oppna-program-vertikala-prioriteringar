package se.vgregion.verticalprio.repository;

import java.util.Collection;
import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import se.vgregion.dao.domain.patterns.repository.db.jpa.DefaultJpaRepository;
import se.vgregion.verticalprio.entity.AbstractHirarkiskKod;

@Repository
public class JpaGenerisktHierarkisktKodRepository<T extends AbstractHirarkiskKod> extends DefaultJpaRepository<T>
        implements GenerisktHierarkisktKodRepository<T> {

    private Class<T> klass;

    public JpaGenerisktHierarkisktKodRepository(Class<T> klass) {
        super(klass);
        this.klass = klass;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<T> getTreeRoots() {
        List<T> result = query("select o from @ o where o.parent is null order by LOWER(o.code) DESC");
        populateChildren(result);
        return result;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    private void populateChildren(Collection<T> parents) {
        for (T child : parents) {
            populateChildren(child);
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    private void populateChildren(T parent) {
        String qt = "select o from @ o where o.parent = ?1";
        parent.getChildren().addAll(query(qt, parent));
        populateChildren(parent.getChildren());
    }

    @SuppressWarnings("unchecked")
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    private List<T> query(String qt, Object... values) {
        qt = qt.replace("@", klass.getSimpleName());
        Query query = entityManager.createQuery(qt);
        int i = 1;
        for (Object value : values) {
            query.setParameter(i++, value);
        }
        return query.getResultList();
    }

}
