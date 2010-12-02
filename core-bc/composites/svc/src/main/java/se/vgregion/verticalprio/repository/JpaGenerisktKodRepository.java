package se.vgregion.verticalprio.repository;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Query;
import javax.persistence.Transient;

import org.apache.commons.beanutils.BeanMap;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import se.vgregion.dao.domain.patterns.repository.db.jpa.DefaultJpaRepository;
import se.vgregion.verticalprio.entity.AbstractKod;

@Repository
public class JpaGenerisktKodRepository<T extends AbstractKod> extends DefaultJpaRepository<T> implements
        GenerisktKodRepository<T> {

    private Class klass;

    private String wildCard = "*";

    private String jpaWildCard = "%";

    /**
     * Initializing the repository with the intended type of bean to be handled.
     */
    public JpaGenerisktKodRepository(Class<T> klass) {
        super(klass);
        this.klass = klass;
    }

    /**
     * @inheritDoc
     */
    @Override
    public List<T> findByExample(T bean) {
        StringBuilder sb = new StringBuilder();
        sb.append("select o from @");
        StringBuilder where = new StringBuilder();
        Set<String> keys = new HashSet<String>();
        BeanMap bm = new BeanMap(bean);
        for (Object key : bm.keySet()) {
            Class<?> type = bm.getType(key.toString());
            if (useField(key.toString())) {
                klass.getAnnotation(type);
                keys.add(key.toString());
            }
        }
        List<Object> values = new ArrayList<Object>();
        int i = 1;
        for (String key : keys) {
            Object value = bm.get(key);
            if (value != null) {
                where.append(key);
                if (value instanceof String && ((String) value).contains(wildCard)) {
                    String wildValue = (String) value;
                    wildValue = wildValue.replace(wildCard, jpaWildCard);
                    value = wildValue;
                    where.append(" like ");
                } else {
                    where.append(" = ");
                }
                where.append("?" + i++);
                values.add(value);
                where.append(" and ");
            }
        }

        String jpql = sb.toString();

        if (where.length() > 0) {
            where.delete(where.length() - 4, 4);
            jpql += " where " + where;
        }

        return query(jpql, values);
    }

    private boolean useField(String name) {
        try {
            if ("class".equals(name)) {
                return false;
            }
            Field field = klass.getField(name);
            if (!field.isAnnotationPresent(Transient.class)) {
                return false;
            }
            if (Modifier.isTransient(field.getModifiers())) {
                return false;
            }
            if (field.getType().isPrimitive()) {
                return false;
            }

            return true;
        } catch (SecurityException e) {
            throw new RuntimeException(e);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }

    }

    @SuppressWarnings("unchecked")
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    protected List<T> query(String qt, Object... values) {
        qt = qt.replace("@", klass.getSimpleName());
        Query query = entityManager.createQuery(qt);
        int i = 1;
        for (Object value : values) {
            query.setParameter(i++, value);
        }
        return query.getResultList();
    }
}