package se.vgregion.verticalprio.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import se.vgregion.dao.domain.patterns.entity.AbstractEntity;

@Entity
@Table(name = "sektor_raad")
public class SektorRaad extends AbstractEntity<SektorRaad, Long> {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Transient
	private boolean selected;

	@Transient
	private List<SektorRaad> children = new ArrayList<SektorRaad>();

	@ManyToOne
	private SektorRaad parent;

	public SektorRaad() {
	}

	@Override
	public Long getId() {
		return id;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setParent(SektorRaad parent) {
		this.parent = parent;
	}

	public SektorRaad getParent() {
		return parent;
	}

	public List<SektorRaad> getChildren() {
		return children;
	}

}
