package se.vgregion.verticalprio.repository;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.persistence.Transient;

import org.apache.commons.beanutils.BeanMap;

import se.vgregion.dao.domain.patterns.entity.AbstractEntity;
import se.vgregion.verticalprio.repository.HaveQuerySortOrder.SortOrderField;

/**
 * Help-logic to produce jpql that selects data from the db. It does "search by example". Uses values in a object
 * graph to produce conditions for retrieving data. The objects being returned is of the same type as is used in
 * the query.
 * 
 * Atomic values - strings and objects inheriting from the Number class are used for the conditions that limits the
 * search. That is under some conditions: If the field is not marked as transient (with key word or annotation).
 * And if it is not, in the number case, a primitive. See methods <code>skipFieldForBuildingCondition</code> and
 * <code>isAtomic</code> for logic differencing between fields to be used in query.
 * 
 * Entities (objects children of <code>AbstractEntity</code>) in member variables are joined with the query. Atomic
 * values inside these are included in the query. If not marked as transient. Several objects inside a collection
 * is joined with separately - making conditions that stipulates that a certain parent must have some specific
 * children: If (object) A have (obj) B and C in list 'children' and B.id = 1 and C.id = 2 - then the search
 * stipulates that results (of same class as A) must have at least one child B (with B.id=1) and at least one child
 * C (with C.id=2).
 * 
 * Entities that also implements <code>HaveNestedEnteties</code> is not used themselves. Rather their children or
 * return value of <code>List<> content()</code> is used. Those 'sub-entities' are joined into the query but their
 * conditions are joined by 'or':s rather than, the usual, 'and'. This makes us able to have mutually inclusive
 * search conditions. Referring to example in clause above this would mean that A must have any (or both) children
 * with matching id property.
 * 
 * Strings found in the object graph are looked into. If the wild card sign is present in the text the
 * <code>like</code> operator is used in the condition. Normally, both for strings and numbers, the equals '
 * <code>=</code>' is used for comparison.
 * 
 * All conditions are joined with the <code>and</code> word from jpql.
 * 
 * @author Claes Lundahl, vgrid=clalu4
 */
public class JpqlMatchBuilder {

    private String wildCard = "*";

    private String jpaWildCard = "%";

    // private final List<String> sortOrder = new ArrayList<String>();

    /**
     * Produces jpql code for selecting data that matches the provided example in values and class. See description
     * of this class for more logic behind query creation.
     * 
     * @param bean
     *            object containing values that should be used to match against fields in the db.
     * @param values
     *            Used to return values from this method, should therefore be empty at the start. the values that
     *            later should be used to when running the query. The values should be set in the order they are
     *            presented in the list.
     * @return jpql representing conditions and joins between objects in the graph.
     */
    public String mkFindByExampleJpql(Object bean, List<Object> values) {

        if (values == null) {
            throw new RuntimeException();
        }

        QueryParts qp = new QueryParts();
        qp.values = values;
        qp.selects.add("o0");

        // List<String> where = new ArrayList<String>();
        // List<String> fromJoin = new ArrayList<String>();

        if (bean instanceof HaveExplicitTypeToFind) {
            HaveExplicitTypeToFind hettf = (HaveExplicitTypeToFind) bean;
            qp.fromJoin.add(hettf.type().getSimpleName() + " o0");
        } else {
            qp.fromJoin.add(bean.getClass().getSimpleName() + " o0");
        }

        mkFindByExampleJpql(bean, qp, 0);

        List<String> orderBy = new ArrayList<String>();

        if (!qp.order.isEmpty()) {
            Collections.sort(qp.order);
            for (SortOrderField orderField : qp.order) {
                String fr = orderField.getAlias() + "." + orderField.getName();
                qp.selects.add(fr);
                if (!orderField.isAscending()) {
                    fr += " desc";
                }
                orderBy.add(fr);
            }
        }

        StringBuilder sb = new StringBuilder();
        // sb.append("select distinct o0 from ");
        sb.append("select distinct ");
        sb.append(toString(qp.selects, ", "));
        sb.append(" from ");
        sb.append(toString(qp.fromJoin, " left join "));
        if (!qp.where.isEmpty()) {
            sb.append(" \nwhere ");
            sb.append(toString(qp.where, " and "));
        }

        if (!orderBy.isEmpty()) {
            sb.append(" order by ");
            sb.append(toString(orderBy, ", "));
        }

        return sb.toString();
    }

    private void mkFindByExampleJpql(Object bean, QueryParts qp, int aliasIndex) {

        // final List<String> fromJoin = qp.fromJoin;
        final List<String> where = qp.where;
        final List<Object> values = qp.values;

        String prefix = "o" + aliasIndex;
        // here is the main loop where we iterate over all properties inside a bean and build up the corresponding
        // JPQL query
        BeanMap bm = new BeanMap(bean);

        if (bean instanceof HaveQuerySortOrder) {
            HaveQuerySortOrder hqso = (HaveQuerySortOrder) bean;
            for (SortOrderField item : hqso.listSortOrders()) {
                item.setAlias(prefix);
            }
            qp.order.addAll(hqso.listSortOrders());
        }

        prefix += ".";

        for (Object key : bm.keySet()) {
            String propertyName = (String) key;
            Object value = bm.get(propertyName);
            if (value == null || "".equals(value)) {
                continue;
            }
            if (skipFieldForBuildingCondition(bean, propertyName)) {
                continue;
            }

            if (value instanceof HaveNestedEntities<?>) {
                HaveNestedEntities<AbstractEntity<Long>> hne = (HaveNestedEntities<AbstractEntity<Long>>) value;
                aliasIndex = handleNestedEnteties(hne, prefix, propertyName, bean, qp, aliasIndex);
                continue;
            }

            if (isAtomic(value)) {
                if (value instanceof String) {
                    if (isLikeValue(value)) {
                        where.add(prefix + propertyName + " like ?");
                        value = value.toString().replace(wildCard, jpaWildCard);
                        values.add(value);
                        continue;
                    }
                }
                values.add(value);
                where.add(prefix + propertyName + " = ?");
                continue;
            }

            if (isCollectionOfEntitys(value)) {
                Collection<AbstractEntity<?>> collection = (Collection<AbstractEntity<?>>) value;
                for (AbstractEntity<?> entity : collection) {
                    boolean addedCondition = handleSubBean(prefix, propertyName, entity, qp, aliasIndex + 1);
                    if (addedCondition) {
                        aliasIndex++;
                    }
                }
                continue;
            }

            if (isEntity(value)) {
                boolean addedCondition = handleSubBean(prefix, propertyName, value, qp, aliasIndex + 1);
                if (addedCondition) {
                    aliasIndex++;
                }
            }
        }

    }

    /**
     * 
     * @param prefix
     *            the alias name of the object having the bean as a child.
     * @param parentPropertyName
     *            name of the property in the parent that holds/points to the bean argument, comming next in the
     *            arg list.
     * @param bean
     *            the child value to be processed.
     * @param fromJoin
     *            the list of Entity names and aliases of these.
     * @param where
     *            a list of conditions.
     * @param values
     *            atomic values that is to be used with the query.
     * @param aliasIndex
     *            the current index of aliases.
     * @return if there was any values inside the bean to be used as values in conditions. If not true the work
     *         inside this method is discarded.
     */
    private boolean handleSubBean(String prefix, String parentPropertyName, Object bean, QueryParts qp,
            int aliasIndex) {
        // int valueCount = qp.values.size();

        // List<String> deepWhere = new ArrayList<String>();
        // List<String> deepFromJoin = new ArrayList<String>();
        QueryParts deepQp = new QueryParts();

        deepQp.fromJoin.add(prefix + parentPropertyName + " o" + aliasIndex);

        mkFindByExampleJpql(bean, deepQp, aliasIndex);
        // Check to see if the new jpql should be added. It is considered irrelevant if there is no 'atoms' -
        // strings and numbers in it to use for matching.
        // boolean result = valueCount < qp.values.size();
        if (!deepQp.values.isEmpty()) {
            qp.inc(deepQp);
            return true;
        }
        return false;
    }

    /**
     * This method is used to build up a part of the query. It is used to iterate through a collection of entities
     * (that implements the <code>HaveNestedEntities</code>) and create an inclusive condition for every item in
     * the list.
     * 
     * @param hne
     * @param prefix
     * @param parentPropertyName
     * @param bean
     * @param fromJoin
     * @param where
     * @param values
     * @param aliasIndex
     *            used in join conditions as alias name e.g. 1 or 2 denoting a table named o1 or o2
     * @return
     */
    private int handleNestedEnteties(HaveNestedEntities<AbstractEntity<Long>> hne, String prefix,
            String parentPropertyName, Object bean, QueryParts qp, int aliasIndex) {

        List<String> allItemsWhere = new ArrayList<String>();
        for (AbstractEntity<Long> ent : hne.content()) {
            // List<String> iterationWhere = new ArrayList<String>();

            QueryParts iterationQp = new QueryParts(qp);
            iterationQp.where = new ArrayList<String>();

            // check if there are values to match inside the bean when building the query
            boolean hasValuesToMatch = handleSubBean(prefix, parentPropertyName, ent, iterationQp, aliasIndex + 1);

            if (hasValuesToMatch) {
                aliasIndex++;
                String oneIterationWhere = toString(iterationQp.where, " and ");
                oneIterationWhere = "(" + oneIterationWhere + ")";
                allItemsWhere.add(oneIterationWhere);
            }
        }

        if (allItemsWhere.size() > 1) {
            String resultWhere = toString(allItemsWhere, " or ");
            resultWhere = "(" + resultWhere + ")";
            qp.where.add(resultWhere);
            return aliasIndex;
        }

        if (!allItemsWhere.isEmpty()) {
            qp.where.addAll(allItemsWhere);
        }
        return aliasIndex;
    }

    private String toString(List<String> list, String junctor) {
        StringBuilder sb = new StringBuilder();
        for (String item : list) {
            sb.append(item);
            sb.append(junctor);
        }
        if (sb.length() > 0) {
            sb.delete(sb.length() - junctor.length(), sb.length());
        }
        return sb.toString();
    }

    private boolean isLikeValue(Object str) {
        return (str.toString().contains(wildCard));
    }

    private boolean skipFieldForBuildingCondition(Object parent, String propertyName) {
        Field field = getField(parent.getClass(), propertyName);

        if (field == null) {
            return true;
        }

        if (field.isAnnotationPresent(Transient.class)) {
            return true;
        }
        if (Modifier.isTransient(field.getModifiers())) {
            return true;
        }
        if (field.getType().isPrimitive()) {
            return true;
        }

        return false;
    }

    private Field getField(@SuppressWarnings("rawtypes") Class klass, String name) {
        try {
            Field field = klass.getDeclaredField(name);
            return field;
        } catch (NoSuchFieldException e) {
            if (klass.equals(Object.class)) {
                return null;
            } else {
                return getField(klass.getSuperclass(), name);
            }
        }
    }

    private boolean isEntity(Object value) {
        return value instanceof AbstractEntity<?>;
    }

    private boolean isAtomic(Object value) {
        if (value == null) {
            return false;
        }
        if (value instanceof Number) {
            return true;
        }
        if (value instanceof String) {
            return true;
        }
        return false;
    }

    private boolean isCollectionOfEntitys(Object value) {
        if (!(value instanceof Collection)) {
            return false;
        }
        Collection<?> collection = (Collection<?>) value;
        if (collection.isEmpty()) {
            return false;
        }
        // Check first item in list to see it it is of a type to be used in a query.
        for (Object o : collection) {
            if (isEntity(o)) {
                return true;
            }
            return false;
        }
        return false;
    }

    // public List<String> getSortOrder() {
    // return sortOrder;
    // }

    /*
     * private String mkOrderBy(Object exampleBean) { validateSortOrderReallyAmongObjectProperties(exampleBean); if
     * (sortOrder.isEmpty()) { return ""; } List<String> prefixedSortOrderColumns = new ArrayList<String>(); for
     * (String item : sortOrder) { prefixedSortOrderColumns.add("o0." + item); } return "\n order by " +
     * toString(prefixedSortOrderColumns, ", "); }
     * 
     * protected void validateSortOrderReallyAmongObjectProperties(Object exampleBean) { BeanMap bm = new
     * BeanMap(exampleBean); for (String key : sortOrder) { if (!bm.keySet().contains(key)) { throw new
     * RuntimeException("Property " + key + " used in order by is not present in the example object (" +
     * exampleBean.getClass().getName()); } } }
     */

    private class QueryParts {

        QueryParts() {
        }

        QueryParts(QueryParts qp) {
            this.selects = qp.selects;
            this.fromJoin = qp.fromJoin;
            this.where = qp.where;
            // this.orderBy = qp.orderBy;
            this.values = qp.values;
            this.order = qp.order;
        }

        public void inc(QueryParts deepQp) {
            fromJoin.addAll(deepQp.fromJoin);
            where.addAll(deepQp.where);
            values.addAll(deepQp.values);
            selects.addAll(deepQp.selects);
            order.addAll(deepQp.order);
            // orderBy.addAll(deepQp.orderBy);
        }

        public List<String> selects = new ArrayList<String>();
        public List<String> fromJoin = new ArrayList<String>();
        public List<String> where = new ArrayList<String>();
        // public List<String> orderBy = new ArrayList<String>();
        public List<Object> values = new ArrayList<Object>();

        public List<SortOrderField> order = new ArrayList<HaveQuerySortOrder.SortOrderField>();
    }

}
