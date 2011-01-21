package se.vgregion.verticalprio.repository;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import se.vgregion.dao.domain.patterns.entity.AbstractEntity;
import se.vgregion.dao.domain.patterns.repository.db.jpa.DefaultJpaRepository;
import se.vgregion.verticalprio.entity.AbstractKod;

/**
 * @author Claes Lundahl, vgrid=clalu4
 * 
 */
public class JpaGenerisktFinderRepository<T extends AbstractEntity<Long>> extends DefaultJpaRepository<T>
        implements GenerisktFinderRepository<T> {

    private Class klass;

    public JpaGenerisktFinderRepository() {
        this((Class<T>) AbstractKod.class);
    }

    private final List<String> sortOrder = new ArrayList<String>();

    /**
     * Initializing the repository with the intended type of bean to be handled.
     */
    public JpaGenerisktFinderRepository(Class<T> klass) {
        super(klass);
        this.klass = klass;
    }

    /**
     * @inheritDoc
     */
    @Override
    public List<T> findByExample(T bean, Integer maxResult) {
        List<Object> values = new ArrayList<Object>();
        JpqlMatchBuilder builder = new JpqlMatchBuilder();
        builder.getSortOrder().addAll(getSortOrder());
        String jpql = builder.mkFindByExampleJpql(bean, values);
        System.out.println(jpql);
        return query(jpql, maxResult, values.toArray());
    }

    @SuppressWarnings("unchecked")
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    protected List<T> query(String qt, Integer maxResult, Object... values) {
        qt = qt.replace("@", klass.getSimpleName());
        Query query = entityManager.createQuery(qt);
        if (maxResult != null) {
            query.setMaxResults(maxResult);
        }
        int i = 1;
        for (Object value : values) {
            query.setParameter(i++, value);
        }
        List<T> result = query.getResultList();
        return result;
    }

    @Override
    public List<String> getSortOrder() {
        return sortOrder;
    }

}
