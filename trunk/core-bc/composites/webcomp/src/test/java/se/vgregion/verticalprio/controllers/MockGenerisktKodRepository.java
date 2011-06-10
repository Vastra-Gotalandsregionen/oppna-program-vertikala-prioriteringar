package se.vgregion.verticalprio.controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.beanutils.BeanMap;

import se.vgregion.dao.domain.patterns.entity.AbstractEntity;
import se.vgregion.verticalprio.repository.GenerisktKodRepository;

/**
 * @author Claes Lundahl, vgrid=clalu4
 * 
 */
public class MockGenerisktKodRepository<T extends AbstractEntity<Long>> implements GenerisktKodRepository<T> {

	private Class clazz;

	private final Set<T> addedItems = new HashSet<T>();

	private final Set<T> deletedItems = new HashSet<T>();

	private final Set<Long> deletedItemsId = new HashSet<Long>();

	/**
     * 
     */
	public MockGenerisktKodRepository(Class clazz) {
		this.clazz = clazz;
	}

	protected List<T> someItems() {

		try {
			List<T> result = new ArrayList<T>();
			T p1;
			p1 = (T) clazz.newInstance();
			new BeanMap(p1).put("id", 1l);
			// p1.setId(1l);
			result.add(p1);
			initSomeDummyValues(p1);

			p1 = (T) clazz.newInstance();
			new BeanMap(p1).put("id", 2l);
			// p1.setId(2l);
			result.add(p1);
			initSomeDummyValues(p1);

			return result;
		} catch (InstantiationException e) {
			throw new RuntimeException("TODO: Handle this exception better", e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException("TODO: Handle this exception better", e);
		}

	}

	private void initSomeDummyValues(T p1) {
		BeanMap bm = new BeanMap(p1);

		for (Object key : bm.keySet()) {
			String name = (String) key;
			if (String.class.equals(bm.getType(name))) {
				if (bm.getWriteMethod(name) != null) {
					bm.put(name, name);
				}
			}
		}
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public List<T> findByExample(T bean, Integer maxResult) {
		return someItems();
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public T persist(T entity) {
		addedItems.add(entity);
		return entity;
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
		deletedItems.add(object);
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public void remove(Long id) {
		deletedItemsId.add(id);
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public Collection<T> findAll() {
		return someItems();
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public T find(Long id) {
		if (id == 1) {
			return someItems().get(0);
		} else if (id == 2) {
			return someItems().get(1);
		}
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
		return someItems().contains(entity);
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public T store(T entity) {
		addedItems.add(entity);
		return entity;
	}

	public Set<T> getAddedItems() {
		return addedItems;
	}

	public Set<Long> getDeletedItemsId() {
		return deletedItemsId;
	}

}
