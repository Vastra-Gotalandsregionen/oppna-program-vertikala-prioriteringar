package se.vgregion.verticalprio.entity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "diagnos_kod")
public class DiagnosKod extends AbstractKod<DiagnosKod> {

	@ManyToOne
	private DiagnosKod parent;

	public DiagnosKod getParent() {
		return parent;
	}

	public void setParent(DiagnosKod parent) {
		this.parent = parent;
	}

}
