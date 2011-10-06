package se.vgregion.verticalprio.controllers;

import java.util.List;

import se.vgregion.verticalprio.entity.AbstractHirarkiskKod;
import se.vgregion.verticalprio.repository.GenerisktHierarkisktKodRepository;

/**
 * @author Claes Lundahl, vgrid=clalu4
 * 
 */
public class MockGenerisktHierarkisktKodRepository<T extends AbstractHirarkiskKod> extends
        MockGenerisktKodRepository<T> implements GenerisktHierarkisktKodRepository<T> {

    private Class<?> clazz;

    public MockGenerisktHierarkisktKodRepository(Class<?> clazz) {
        super(clazz);
    }

    /**
     * @inheritDoc
     */
    @Override
    public List<T> getTreeRoots() {
        return someItems();
    }

}
