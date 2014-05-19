package se.vgregion.verticalprio.repository.finding;

import java.util.Set;

import se.vgregion.dao.domain.patterns.entity.AbstractEntity;

/**
 * Use this
 * 
 * @author Claes Lundahl, vgrid=clalu4
 */
public interface HaveNestedEntities<T extends AbstractEntity<Long>> {

    Set<T> content();

}
