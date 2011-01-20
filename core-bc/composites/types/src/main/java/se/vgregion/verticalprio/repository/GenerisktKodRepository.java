package se.vgregion.verticalprio.repository;

import java.util.List;

import se.vgregion.dao.domain.patterns.entity.AbstractEntity;
import se.vgregion.dao.domain.patterns.repository.Repository;

public interface GenerisktKodRepository<T extends AbstractEntity<Long>> extends Repository<T, Long> {

    /**
     * Uses the values of a bean to find matching values.
     * 
     * If a property of the bean is not transient (either marked with the 'transient' modifier or with the
     * annotation @Transient) then that value is used in a equals or like boolean expression to find results. If
     * the value is a String then an extra check is made to look for the '*' sign - if present it is removed in
     * favor for '%' and the operation becomes 'likes' instad of '='.
     * 
     * Null-values in the properties of the bean is ignored. They do no impact on the search expression.
     * 
     * @param bean
     * @return
     */
    List<T> findByExample(T bean, Integer maxResult);

}