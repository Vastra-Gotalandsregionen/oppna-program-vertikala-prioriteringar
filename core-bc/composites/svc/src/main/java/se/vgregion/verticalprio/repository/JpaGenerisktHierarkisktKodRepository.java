package se.vgregion.verticalprio.repository;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import se.vgregion.dao.domain.patterns.entity.AbstractEntity;
import se.vgregion.verticalprio.entity.AbstractHirarkiskKod;

@Repository
public class JpaGenerisktHierarkisktKodRepository<T extends AbstractEntity<Long>> extends
        JpaGenerisktKodRepository<T> implements GenerisktHierarkisktKodRepository<T> {

	public JpaGenerisktHierarkisktKodRepository() {
		super((Class) AbstractHirarkiskKod.class);
	}

	public JpaGenerisktHierarkisktKodRepository(Class<T> klass) {
		super(klass);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<T> getTreeRoots() {
		List<T> result = query("select o from @ o where o.parentId is null order by LOWER(o.kod)", null);
		initChildren(result);
		return result;
	}

	@SuppressWarnings("unchecked")
	private void initChildren(List<T> items) {
		for (T item : items) {
			initChildren(((AbstractHirarkiskKod)item).getChildren());
		}
	}

	/* @Transactional(propagation = Propagation.SUPPORTS, readOnly = true) private void
	 * populateChildren(Collection<T> parents) { for (T child : parents) { populateChildren(child); } }
	 * 
	 * @Transactional(propagation = Propagation.SUPPORTS, readOnly = true) private void populateChildren(T parent)
	 * { String qt = "select o from @ o where o.parent = ?1"; parent.getChildren().addAll(query(qt, parent));
	 * populateChildren(parent.getChildren()); } */

}
