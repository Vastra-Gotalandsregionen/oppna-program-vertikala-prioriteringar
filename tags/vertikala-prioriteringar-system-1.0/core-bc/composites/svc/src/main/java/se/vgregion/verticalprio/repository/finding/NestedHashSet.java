package se.vgregion.verticalprio.repository.finding;

import java.util.HashSet;
import java.util.Set;

import se.vgregion.dao.domain.patterns.entity.AbstractEntity;
import se.vgregion.verticalprio.repository.GenerisktFinderRepository;

/**
 * If a collection of object (with entities inside) should generate an inclusive condition when used in conjunction
 * with <code>findByExample</code> in {@link GenerisktFinderRepository} then use this class.
 * 
 * @author Claes Lundahl, vgrid=clalu4
 */
public class NestedHashSet<T extends AbstractEntity<Long>> extends HashSet<T> implements HaveNestedEntities<T> {

    /**
     * @inheritDoc
     */
    @Override
    public Set<T> content() {
        return this;
    }
}
