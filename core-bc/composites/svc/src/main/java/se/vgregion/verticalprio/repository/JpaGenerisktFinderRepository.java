package se.vgregion.verticalprio.repository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Query;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import se.vgregion.dao.domain.patterns.entity.AbstractEntity;
import se.vgregion.dao.domain.patterns.repository.db.jpa.DefaultJpaRepository;
import se.vgregion.verticalprio.entity.AbstractKod;
import se.vgregion.verticalprio.repository.finding.JpqlMatchBuilder;

/**
 * @author Claes Lundahl, vgrid=clalu4
 * 
 */
public class JpaGenerisktFinderRepository<T extends AbstractEntity<Long>> extends DefaultJpaRepository<T>
        implements GenerisktFinderRepository<T> {

    private Class klass;

    private String extraWhere;

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
        if (extraWhere != null && !"".equals(extraWhere)) {
            builder.setExtraWhere(extraWhere);
        }
        String jpql = builder.mkFindByExampleJpql(bean, values);
        // System.out.println(jpql);
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
        List<?> rawResult = query.getResultList();

        List<T> result = new ArrayList<T>();

        // Sometimes the result is an array.
        if (!rawResult.isEmpty() && rawResult.get(0).getClass().isArray()) {
            Object[] firstRow = (Object[]) rawResult.get(0);
            int index = 0;
            for (int j = 0; j < firstRow.length; j++) {
                if (firstRow[j] instanceof AbstractEntity) {
                    index = j;
                    break;
                }
            }
            for (Object raw : rawResult) {
                Object[] row = (Object[]) raw;
                result.add((T) row[index]);
            }
        } else {
            result = (List<T>) rawResult;
        }

        Set<Long> ids = new HashSet<Long>();
        for (T item : new ArrayList<T>(result)) {
            if (ids.contains(item.getId())) {
                result.remove(item);
            } else {
                ids.add(item.getId());
            }
        }

        return result;
    }

    @Override
    public List<String> getSortOrder() {
        return sortOrder;
    }

    /**
     * @inheritDoc
     */
    @Override
    public void setExtraWhere(String extraWhere) {
        this.extraWhere = extraWhere;
    }

    /**
     * @inheritDoc
     */
    @Override
    public String getExtraWhere() {
        return extraWhere;
    }

}
