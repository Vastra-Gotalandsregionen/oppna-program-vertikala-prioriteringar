package se.vgregion.verticalprio.repository;

import java.util.ArrayList;
import java.util.List;

import se.vgregion.dao.domain.patterns.entity.AbstractEntity;

/**
 * If a collection of object (with entities inside) should generate an inclusive condition when used in conjunction
 * with <code>findByExample</code> in {@link GenerisktFinderRepository} then use this class.
 * 
 * @author Claes Lundahl, vgrid=clalu4
 */
public class NestedArrayList<T extends AbstractEntity<Long>> extends ArrayList implements HaveNestedEntities<T> {

    /**
     * @inheritDoc
     */
    @Override
    public List<T> content() {
        return this;
    }
}
