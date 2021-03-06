package se.vgregion.verticalprio.repository.finding;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import org.apache.commons.beanutils.BeanMap;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import se.vgregion.dao.domain.patterns.entity.AbstractEntity;

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

	private String extraWhere;

	private Class getClassToSelect(Object bean) {
		if (bean instanceof HaveExplicitTypeToFind) {
			HaveExplicitTypeToFind hettf = (HaveExplicitTypeToFind) bean;
			return hettf.type();
		} else {
			return bean.getClass();
		}
	}

	private String getNameOfTypeToSelect(Object bean) {
		return getClassToSelect(bean).getSimpleName();
	}

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
		StringBuilder sb = new StringBuilder();
		JpqlResultParts result = mkFindByExampleJpqlParts(bean, values);
		sb.append(result.select);
		sb.append(result.fromJoin);
		sb.append(result.where);
		sb.append(result.orderBy);

		return sb.toString();
	}

	public JpqlResultParts mkFindByExampleJpqlParts(Object bean, List<Object> values) {

		final JpqlResultParts result = new JpqlResultParts();
		if (values == null) {
			throw new RuntimeException();
		}

		QueryParts qp = new QueryParts();
		qp.values = values;
		qp.selects.add("o0");
		qp.fromJoin.add(getNameOfTypeToSelect(bean) + " o0");
		mkFindByExampleJpql(bean, qp, 0);
		List<String> orderBy = new ArrayList<String>();

		if (bean instanceof HaveOrderByPaths) {
			HaveOrderByPaths havePaths = (HaveOrderByPaths) bean;
			if (!havePaths.paths().isEmpty()) {
				qp.selects.addAll(OrderByPath.toJpqlSelectParts(havePaths.paths(), "o0"));
				qp.fromJoin.addAll(OrderByPath.toJpqlJoinParts(havePaths.paths(), "o0"));
			}
		}

		StringBuilder sb = new StringBuilder();

		sb.append("select distinct ");
		sb.append(toString(qp.selects, ", "));
		result.select = sb.toString();

		sb = new StringBuilder();

		sb.append(" from ");
		sb.append(toString(qp.fromJoin, " left join "));
		sb.append(" ");
		sb.append(mkFetchJoinForMasterEntity(bean, "o0"));

		result.fromJoin = sb.toString();
		sb = new StringBuilder();

		if (extraWhere != null && !"".equals(extraWhere)) {
			qp.where.add(extraWhere);
		}

		if (!qp.where.isEmpty()) {
			String where = toString(qp.where, " and ");
			if (!where.trim().equals("")) {
				sb.append(" \nwhere ");
				sb.append(where);
			}
		}

		result.where = sb.toString();
		sb = new StringBuilder();

		if (bean instanceof HaveOrderByPaths) {
			HaveOrderByPaths havePaths = (HaveOrderByPaths) bean;
			if (!havePaths.paths().isEmpty()) {
				sb.append(" \norder by ");
				List<String> order = OrderByPath.toJpqlOrderByParts(havePaths.paths(), "o0");
				sb.append(toString(order, ", "));
			}
		}

		result.orderBy = sb.toString();
		return result;
	}

	private void mkFindByExampleJpql(Object bean, QueryParts qp, int aliasIndex) {

		// See to it that there will be no endless recursive calls.
		// if (qp.passedItems.contains(bean)) {
		// return;
		// } else {
		// qp.passedItems.add(bean);
		// }

		final List<String> where = qp.where;
		final List<Object> values = qp.values;

		String prefix = "o" + aliasIndex;
		// here is the main loop where we iterate over all properties inside a bean and build up the corresponding
		// JPQL query
		BeanMap bm = new BeanMap(bean);

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

			if (value instanceof HaveNullLogic) {
				HaveNullLogic hnl = (HaveNullLogic) value;
				String nullOrEmpty = "null";
				if (value instanceof Collection) {
					nullOrEmpty = "empty";
				}
				if (hnl.isNotNull()) {
					where.add(prefix + propertyName + " is not " + nullOrEmpty);
				} else {
					where.add(prefix + propertyName + " is " + nullOrEmpty);
				}
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
	 * This method forces sql to really make a "fetch" for every table in the query. This will create ONE complex
	 * sql query instead of many many small queries
	 * 
	 * @param bean
	 * @return
	 */
	protected String mkFetchJoinForMasterEntity(Object bean, String alias) {
		try {
			return mkFetchJoinForMasterEntity(getClassToSelect(bean), alias, new HashSet<String>());
		} catch (InstantiationException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	private String mkFetchJoinForMasterEntity(Class klass, String alias, Set<String> passedFields)
	        throws InstantiationException, IllegalAccessException {

		if (passedFields.contains(alias)) {
			return "";
		} else {
			passedFields.add(alias);
		}

		StringBuilder sb = new StringBuilder();

		for (Field field : klass.getDeclaredFields()) {
			if (isFetchOfTypeJoinPresent(field)) {
				String fieldKey = getNameOfTypeToSelect(klass.newInstance()) + "_" + field.getName();

				sb.append(" left outer join fetch " + alias + ".");
				sb.append(field.getName() + " " + fieldKey + " ");
				if (!passedFields.contains(fieldKey)) {
					sb.append(mkFetchJoinForMasterEntity(getPropertyClassOrGenericCollectionTypeing(field),
					        fieldKey, passedFields));
				}
			}
		}

		return sb.toString();
	}

	private Class getPropertyClassOrGenericCollectionTypeing(Field field) {
		Class clazz = field.getType();
		if (Collection.class.isAssignableFrom(clazz)) {
			ParameterizedType type = (ParameterizedType) field.getGenericType();
			clazz = (Class) type.getActualTypeArguments()[0];
		}
		return clazz;
	}

	boolean isFetchOfTypeJoinPresent(Field field) {
		Annotation annotation = field.getAnnotation(Fetch.class);
		if (annotation instanceof Fetch) {
			Fetch fetch = (Fetch) annotation;
			return FetchMode.JOIN == fetch.value();
		}
		return false;
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

		// See to it that there will be no endless recursive calls.
		if (qp.passedItems.contains(bean)) {
			return false;
		} else {
			qp.passedItems.add(bean);
		}

		QueryParts deepQp = new QueryParts();
		deepQp.passedItems.addAll(qp.passedItems);
		deepQp.fromJoin.add(prefix + parentPropertyName + " o" + aliasIndex);

		mkFindByExampleJpql(bean, deepQp, aliasIndex);
		qp.passedItems.addAll(deepQp.passedItems);
		// Check to see if the new jpql should be added. It is considered irrelevant if there is no 'atoms' -
		// strings and numbers in it to use for matching.
		// boolean result = valueCount < qp.values.size();
		if (!deepQp.values.isEmpty()) {
			qp.inc(deepQp);
			return true;
		}
		return false;
	}

	private Collection<AbstractEntity<Long>> getOnlyThoseWithId(Collection<AbstractEntity<Long>> items) {
		List<AbstractEntity<Long>> result = new ArrayList<AbstractEntity<Long>>();
		for (AbstractEntity<Long> item : items) {
			if (item.getId() != null) {
				result.add(item);
			}
		}
		return result;
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
		Set<AbstractEntity<Long>> items = hne.content();
		if (items.isEmpty()) {
			return aliasIndex;
		}
		List<String> allItemsWhere = new ArrayList<String>();

		Collection<AbstractEntity<Long>> havingId = getOnlyThoseWithId(items);

		if (!havingId.isEmpty()) {
			aliasIndex++;
			// Make a id in (all the ids) for the items with id.
			qp.fromJoin.add(prefix + parentPropertyName + " o" + aliasIndex);
			StringBuilder sb = new StringBuilder();
			for (AbstractEntity<Long> ent : havingId) {
				sb.append("?, ");
				qp.values.add(ent.getId());
			}
			sb.delete(sb.length() - 2, sb.length());
			allItemsWhere.add("o" + aliasIndex + ".id in (" + sb + ")");
			items = new HashSet<AbstractEntity<Long>>(items);
			items.removeAll(havingId);
		}

		for (AbstractEntity<Long> ent : items) {
			QueryParts iterationQp = new QueryParts(qp);
			iterationQp.where = new ArrayList<String>();

			// check if there are values to match inside the bean when building the query
			boolean hasValuesToMatch = handleSubBean(prefix, parentPropertyName, ent, iterationQp, aliasIndex + 1);

			if (hasValuesToMatch) {
				aliasIndex++;
				String oneIterationWhere = toString(iterationQp.where, " and ");
				if (oneIterationWhere != null && !"".equals(oneIterationWhere)) {
					oneIterationWhere = "(" + oneIterationWhere + ")";
				}
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

	public static String toString(List<String> list, String junctor) {
		StringBuilder sb = new StringBuilder();
		while (list.remove("") || list.remove("()")) {
		}
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

		if (!(field.isAnnotationPresent(Column.class) || field.isAnnotationPresent(Id.class)
		        || field.isAnnotationPresent(ManyToMany.class) || field.isAnnotationPresent(ManyToOne.class)
		        || field.isAnnotationPresent(OneToMany.class) || field.isAnnotationPresent(OneToOne.class))) {
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

	Field getField(@SuppressWarnings("rawtypes") Class klass, String name) {
		try {
			if (klass == null) {
				return null;
			}
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
		if (value instanceof Date) {
			return true;
		}
		return false;
	}

	private boolean isCollectionOfEntitys(Object value) {
		if (!(value instanceof Collection)) {
			return false;
		}
		Collection<?> collection = (Collection<?>) value;
		try {
			if (collection.isEmpty()) {
				return false;
			}
		} catch (Exception e) {
			// Error - for instance the LazyInitializationException from hibernate... then we should not bother
			// with this collection anyway.
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

	public void setExtraWhere(String extraWhere) {
		this.extraWhere = extraWhere;
	}

	public String getExtraWhere() {
		return extraWhere;
	}

	private class QueryParts {

		public QueryParts() {
		}

		QueryParts(QueryParts qp) {
			this.selects = qp.selects;
			this.fromJoin = qp.fromJoin;
			this.where = qp.where;
			this.values = qp.values;
		}

		public void inc(QueryParts deepQp) {
			fromJoin.addAll(deepQp.fromJoin);
			where.addAll(deepQp.where);
			values.addAll(deepQp.values);
			selects.addAll(deepQp.selects);
		}

		public List<String> selects = new ArrayList<String>();
		public List<String> fromJoin = new ArrayList<String>();
		public List<String> where = new ArrayList<String>();
		public List<Object> values = new ArrayList<Object>();
		public final Set<Object> passedItems = new HashSet<Object>();
	}

	public static class JpqlResultParts {
		public String fromJoin, where, select, orderBy;
	}

}
