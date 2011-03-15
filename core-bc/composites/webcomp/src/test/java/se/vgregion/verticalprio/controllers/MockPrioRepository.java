package se.vgregion.verticalprio.controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import se.vgregion.verticalprio.entity.Prioriteringsobjekt;
import se.vgregion.verticalprio.repository.PrioRepository;

/**
 * @author Claes Lundahl, vgrid=clalu4
 * 
 */
public class MockPrioRepository implements PrioRepository {

    private String extraWhere;

    /**
     * @inheritDoc
     */
    @Override
    public Prioriteringsobjekt persist(Prioriteringsobjekt object) {
        throw new UnsupportedOperationException("TODO: Implement this method");
    }

    /**
     * @inheritDoc
     */
    @Override
    public void flush() {
        throw new UnsupportedOperationException("TODO: Implement this method");
    }

    /**
     * @inheritDoc
     */
    @Override
    public void remove(Prioriteringsobjekt object) {
        throw new UnsupportedOperationException("TODO: Implement this method");
    }

    /**
     * @inheritDoc
     */
    @Override
    public void remove(Long id) {
        throw new UnsupportedOperationException("TODO: Implement this method");
    }

    /**
     * @inheritDoc
     */
    @Override
    public Collection<Prioriteringsobjekt> findAll() {
        List<Prioriteringsobjekt> result = new ArrayList<Prioriteringsobjekt>();
        for (long i = 0; i < 100; i++) {
            result.add(find(i));
        }
        return result;
    }

    /**
     * @inheritDoc
     */
    @Override
    public Prioriteringsobjekt find(Long id) {
        Prioriteringsobjekt result = new Prioriteringsobjekt();
        result.setId(id);
        return result;
    }

    /**
     * @inheritDoc
     */
    @Override
    public Prioriteringsobjekt merge(Prioriteringsobjekt object) {
        throw new UnsupportedOperationException("TODO: Implement this method");
    }

    /**
     * @inheritDoc
     */
    @Override
    public void refresh(Prioriteringsobjekt object) {
        throw new UnsupportedOperationException("TODO: Implement this method");
    }

    /**
     * @inheritDoc
     */
    @Override
    public boolean contains(Prioriteringsobjekt entity) {
        throw new UnsupportedOperationException("TODO: Implement this method");
    }

    /**
     * @inheritDoc
     */
    @Override
    public Prioriteringsobjekt store(Prioriteringsobjekt entity) {
        return entity;
    }

    /**
     * @inheritDoc
     */
    @Override
    public List<Prioriteringsobjekt> findByExample(Prioriteringsobjekt bean, Integer maxResult) {
        return new ArrayList<Prioriteringsobjekt>(findAll());
    }

    /**
     * @inheritDoc
     */
    @Override
    public List<String> getSortOrder() {
        throw new UnsupportedOperationException("TODO: Implement this method");
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
