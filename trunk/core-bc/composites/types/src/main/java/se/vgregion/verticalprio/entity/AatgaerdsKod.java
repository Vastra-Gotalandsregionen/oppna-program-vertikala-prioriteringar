package se.vgregion.verticalprio.entity;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "aatgaerds_kod")
@Cacheable(value = true)
public class AatgaerdsKod extends AbstractKod {

}
