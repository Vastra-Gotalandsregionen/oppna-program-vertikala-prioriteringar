package se.vgregion.verticalprio.entity;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

@SuppressWarnings("serial")
@Entity
@Table(name = "sektor_raad")
public class SektorRaad extends AbstractHirarkiskKod<SektorRaad> implements Cloneable {

	public SektorRaad() {
	}

	public SektorRaad(Long id) {
		setId(id);
	}

	@Transient
	private transient SektorRaad parent;

	@Transient
	private boolean able = true;

	@ManyToMany(mappedBy = "sektorRaad")
	private Set<User> users;

	@ManyToMany(mappedBy = "sektorRaad")
	private Set<Prioriteringsobjekt> prioriteringsobjekt;

	public void setAble(boolean able) {
		this.able = able;
	}

	public boolean isAble() {
		return able;
	}

	@Override
	public void setParent(SektorRaad parent) {
		this.parent = parent;
	}

	@Override
	public SektorRaad getParent() {
		return parent;
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public List<SektorRaad> getChildren() {
		List<SektorRaad> result = super.getChildren();

		if (result != null) {
			for (SektorRaad raad : result) {
				raad.setParent(this);
			}

			Collections.sort(result, new Comparator<SektorRaad>() {
				@Override
				public int compare(SektorRaad o1, SektorRaad o2) {
					String kod1 = o1.getKod();
					String kod2 = o2.getKod();
					if (kod1 == null) {
						kod1 = "";
					}
					if (kod2 == null) {
						kod2 = "";
					}
					return kod1.compareTo(kod2);
				}
			});
		}

		return result;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}

	public Set<User> getUsers() {
		return users;
	}

	public void setPrioriteringsobjekt(Set<Prioriteringsobjekt> prioriteringsobjekt) {
		this.prioriteringsobjekt = prioriteringsobjekt;
	}

	public Set<Prioriteringsobjekt> getPrioriteringsobjekt() {
		return prioriteringsobjekt;
	}

}
