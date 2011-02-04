package se.vgregion.verticalprio.repository.finding;

import java.util.List;

import se.vgregion.dao.domain.patterns.entity.AbstractEntity;

/**
 * Use this
 * 
 * @author Claes Lundahl, vgrid=clalu4
 */
public interface HaveNestedEntities<T extends AbstractEntity<Long>> {

    List<T> content();

}
