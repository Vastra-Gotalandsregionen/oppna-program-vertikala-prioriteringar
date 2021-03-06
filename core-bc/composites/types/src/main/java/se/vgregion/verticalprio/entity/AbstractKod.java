package se.vgregion.verticalprio.entity;

import java.io.Serializable;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;

import org.apache.commons.collections.BeanMap;

import se.vgregion.dao.domain.patterns.entity.AbstractEntity;

@SuppressWarnings("serial")
@MappedSuperclass
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Cacheable(value = true)
public abstract class AbstractKod extends AbstractEntity<Long> implements Serializable, Comparable<AbstractKod>,
        Cloneable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "kod")
	private String kod;

	@Column(name = "beskrivning", length = 1000)
	private String beskrivning;

	@Column(name = "kort_beskrivning", length = 1000)
	private String kortBeskrivning;

	@Override
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getKod() {
		return kod;
	}

	public void setKod(String kod) {
		this.kod = kod;
	}

	public String getBeskrivning() {
		return beskrivning;
	}

	public void setBeskrivning(String beskrivning) {
		this.beskrivning = beskrivning;
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public String toString() {
		return getLabel();
	}

	public String getLabel() {
		if (kortBeskrivning != null) {
			return kortBeskrivning;
		}
		return (nullAsBlank(kod) + " " + nullAsBlank(beskrivning)).trim();
	}

	private String nullAsBlank(String s) {
		if (s == null) {
			return "";
		}
		return s;
	}

	public void setKortBeskrivning(String kortBeskrivning) {
		this.kortBeskrivning = kortBeskrivning;
	}

	public String getKortBeskrivning() {
		return kortBeskrivning;
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public int compareTo(AbstractKod o) {
        if (o == null) {
            return -1;
        }

        String thisKod = kod;
        String thatKod = o.kod;

        if (thisKod == null && thatKod == null) {
            return this.hashCode() - thatKod.hashCode();
        } else if (thatKod == null) {
            return -1;
        } else if (thisKod == null) {
            return 1;
        } else {
            return thisKod.compareTo(thatKod);
        }
    }

	public String getKodPlusBeskrivning() {
		return (format(getKod()) + " " + format(getBeskrivning())).trim();
	}

	private String format(String s) {
		if (s == null) {
			return "";
		}
		return s.trim();
	}

	@Override
	public AbstractKod clone() {
		try {
			AbstractKod result = getClass().newInstance();
			BeanMap thisMap = new BeanMap(this);
			BeanMap resultMap = new BeanMap(result);
			resultMap.putAllWriteable(thisMap);
			result.setId(getId());
			return result;
		} catch (InstantiationException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}

	}

}
