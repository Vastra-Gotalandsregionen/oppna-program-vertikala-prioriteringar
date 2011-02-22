package se.vgregion.verticalprio.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "vaentetids_kod")
public class VaentetidsKod extends AbstractKod {

    /**
     * @inheritDoc
     */
    @Override
    public String getLabel() {
        return getKod();
    }

}
