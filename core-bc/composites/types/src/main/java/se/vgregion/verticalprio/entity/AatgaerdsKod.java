package se.vgregion.verticalprio.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "aatgaerds_kod")
public class AatgaerdsKod extends AbstractKod {

    /**
     * @inheritDoc
     */
    @Override
    public String getKortBeskrivning() {
        return getBeskrivning();
    }

}
