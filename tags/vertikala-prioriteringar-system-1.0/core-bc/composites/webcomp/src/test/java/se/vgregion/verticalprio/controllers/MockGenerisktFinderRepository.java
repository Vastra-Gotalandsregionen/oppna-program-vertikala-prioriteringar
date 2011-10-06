package se.vgregion.verticalprio.controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import se.vgregion.dao.domain.patterns.entity.AbstractEntity;
import se.vgregion.verticalprio.repository.GenerisktFinderRepository;

/**
 * @author Claes Lundahl, vgrid=clalu4
 * 
 */
public class MockGenerisktFinderRepository<T extends AbstractEntity<Long>> implements GenerisktFinderRepository<T> {

    Class<T> clazz;

    /**
     * 
     */
    public MockGenerisktFinderRepository(Class<T> clazz) {
        this.clazz = clazz;
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
        List<T> result = new ArrayList<T>();
        return result;
    }

    /**
     * @inheritDoc
     */
    @Override
    public T find(Long id) {
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
        return false;
    }

    /**
     * @inheritDoc
     */
    @Override
    public T store(T entity) {
        return entity;
    }

    /**
     * @inheritDoc
     */
    @Override
    public List<T> findByExample(T bean, Integer maxResult) {
        List<T> result = new ArrayList<T>();
        result.add(bean);
        return result;
    }

    /**
     * @inheritDoc
     */
    @Override
    public List<String> getSortOrder() {
        return null;
    }

    /**
     * @inheritDoc
     */
    @Override
    public void setExtraWhere(String extraWhere) {
        throw new UnsupportedOperationException("TODO: Implement this method");
    }

    /**
     * @inheritDoc
     */
    @Override
    public String getExtraWhere() {
        throw new UnsupportedOperationException("TODO: Implement this method");
    }

    /**
     * @inheritDoc
     */
    @Override
    public List<T> query(String qt, Integer maxResult, Object... values) {
        throw new UnsupportedOperationException("TODO: Implement this method");
    }

}
