package se.vgregion.verticalprio.repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import se.vgregion.verticalprio.entity.SektorRaad;

/**
 * @author Claes Lundahl, vgrid=clalu4
 * 
 */
@SuppressWarnings("serial")
public class NestedSektorRaad extends SektorRaad implements HaveNestedEntities<SektorRaad> {

    private List<SektorRaad> sektors = new ArrayList<SektorRaad>();

    public NestedSektorRaad() {
    }

    public NestedSektorRaad(Collection<SektorRaad> raad) {
        sektors.addAll(raad);
    }

    /**
     * @inheritDoc
     */
    @Override
    public List<SektorRaad> content() {
        return sektors;
    }

}
