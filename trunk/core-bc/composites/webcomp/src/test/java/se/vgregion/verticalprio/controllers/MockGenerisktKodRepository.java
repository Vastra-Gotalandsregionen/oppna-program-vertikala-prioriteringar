package se.vgregion.verticalprio.controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.beanutils.BeanMap;

import se.vgregion.verticalprio.entity.AbstractKod;
import se.vgregion.verticalprio.repository.GenerisktKodRepository;

/**
 * @author Claes Lundahl, vgrid=clalu4
 * 
 */
public class MockGenerisktKodRepository<T extends AbstractKod> implements GenerisktKodRepository<T> {

    private Class clazz;

    /**
     * 
     */
    public MockGenerisktKodRepository(Class clazz) {
        this.clazz = clazz;
    }

    protected List<T> someItems() {

        try {
            List<T> result = new ArrayList<T>();
            T p1;
            p1 = (T) clazz.newInstance();
            p1.setId(1l);
            result.add(p1);
            initSomeDummyValues(p1);

            p1 = (T) clazz.newInstance();
            p1.setId(2l);
            result.add(p1);
            initSomeDummyValues(p1);

            return result;
        } catch (InstantiationException e) {
            throw new RuntimeException("TODO: Handle this exception better", e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("TODO: Handle this exception better", e);
        }

    }

    private void initSomeDummyValues(T p1) {
        BeanMap bm = new BeanMap(p1);

        for (Object key : bm.keySet()) {
            String name = (String) key;
            if (String.class.equals(bm.getType(name))) {
                if (bm.getWriteMethod(name) != null) {
                    bm.put(name, name);
                }
            }
        }
    }

    /**
     * @inheritDoc
     */
    @Override
    public List<T> findByExample(T bean, Integer maxResult) {
        return someItems();
    }

    /**
     * @inheritDoc
     */
    @Override
    public T persist(T object) {
        return object;
    }

    /**
     * @inheritDoc
     */
    @Override
    public void flush() {

    }

    /**
     * @inheritDoc
     */
    @Override
    public void remove(T object) {

    }

    /**
     * @inheritDoc
     */
    @Override
    public void remove(Long id) {

    }

    /**
     * @inheritDoc
     */
    @Override
    public Collection<T> findAll() {
        return someItems();
    }

    /**
     * @inheritDoc
     */
    @Override
    public T find(Long id) {
        if (id == 1) {
            return someItems().get(0);
        } else if (id == 2) {
            return someItems().get(1);
        }
        return null;
    }

    /**
     * @inheritDoc
     */
    @Override
    public T merge(T object) {
        return object;
    }

    /**
     * @inheritDoc
     */
    @Override
    public void refresh(T object) {

    }

    /**
     * @inheritDoc
     */
    @Override
    public boolean contains(T entity) {
        return someItems().contains(entity);
    }

    /**
     * @inheritDoc
     */
    @Override
    public T store(T entity) {
        return entity;
    }

}
