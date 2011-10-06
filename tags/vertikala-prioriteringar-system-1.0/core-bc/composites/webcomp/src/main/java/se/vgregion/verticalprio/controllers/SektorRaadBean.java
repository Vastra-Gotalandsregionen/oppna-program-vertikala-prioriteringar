package se.vgregion.verticalprio.controllers;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanMap;
import org.springframework.transaction.annotation.Transactional;

import se.vgregion.verticalprio.entity.SektorRaad;

public class SektorRaadBean extends SektorRaad {

	public SektorRaadBean() {
	}

	public SektorRaadBean(Long id) {
		super(id);
	}

	private static final long serialVersionUID = 1L;

	private List<SektorRaadBean> beanChildren;

	private boolean markedAsDeleted;

	private boolean locked;

	private int prioCount;

	@Transactional
	public static SektorRaadBean toSektorRaadBean(SektorRaad sr) {
		SektorRaadBean newSektorRaadBean = new SektorRaadBean();
		if (sr.getPrioriteringsobjekt() != null) {
			newSektorRaadBean.setPrioCount(sr.getPrioriteringsobjekt().size());
		}
		new BeanMap(newSektorRaadBean).putAllWriteable(new BeanMap(sr));
		newSektorRaadBean.setBeanChildren(toSektorRaadBeans(sr.getChildren()));
		newSektorRaadBean.setId(sr.getId());
		newSektorRaadBean.setParentId(sr.getParentId());
		return newSektorRaadBean;
	}

	@Transactional
	public static List<SektorRaadBean> toSektorRaadBeans(List<SektorRaad> srs) {
		List<SektorRaadBean> result = new ArrayList<SektorRaadBean>();
		if (srs != null) {
			for (SektorRaad sr : srs) {
				result.add(toSektorRaadBean(sr));
			}
		}
		return result;
	}

	public static SektorRaad toSektorRaad(SektorRaadBean sr) {
		SektorRaad newSektorRaad = new SektorRaad();
		new BeanMap(newSektorRaad).putAllWriteable(new BeanMap(sr));
		newSektorRaad.setChildren(toSektorRaads(sr.getBeanChildren()));
		newSektorRaad.setId(sr.getId());
		newSektorRaad.setParentId(sr.getParentId());
		return newSektorRaad;
	}

	public static List<SektorRaad> toSektorRaads(List<SektorRaadBean> srs) {
		List<SektorRaad> result = new ArrayList<SektorRaad>();
		for (SektorRaadBean sr : srs) {
			result.add(toSektorRaad(sr));
		}
		return result;
	}

	public void setBeanChildren(List<SektorRaadBean> beanChildren) {
		this.beanChildren = beanChildren;
	}

	public List<SektorRaadBean> getBeanChildren() {
		return beanChildren;
	}

	public void setMarkedAsDeleted(boolean markedAsDeleted) {
		this.markedAsDeleted = markedAsDeleted;

		if (getBeanChildren() != null) {
			for (SektorRaadBean srb : getBeanChildren()) {
				srb.setMarkedAsDeleted(markedAsDeleted);
			}
		}
	}

	public boolean isMarkedAsDeleted() {
		return markedAsDeleted;
	}

	public void setLocked(boolean locked) {
		this.locked = locked;
	}

	public boolean isLocked() {
		return locked;
	}

	public boolean isDeleteAble() {
		return (getId() == null || (!isLocked() && getPrioCount() == 0)) && isDeplyDeletAble();
	}

	private boolean isDeplyDeletAble() {
		if (getBeanChildren() != null) {
			for (SektorRaadBean sr : getBeanChildren()) {
				if (!sr.isDeleteAble() || !sr.isDeplyDeletAble()) {
					return false;
				}
			}
		}
		return true;
	}

	public void setPrioCount(int prioCount) {
		this.prioCount = prioCount;
	}

	public int getPrioCount() {
		return prioCount;
	}

	// public void setParentMarkedAsDeleted(boolean parentMarkedAsDeleted) {
	// for (SektorRaadBean srb : getBeanChildren()) {
	// srb.setParentMarkedAsDeleted(markedAsDeleted);
	// }
	// }
	//
	// public boolean isParentMarkedAsDeleted() {
	// return parentMarkedAsDeleted;
	// }

}
