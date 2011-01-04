package se.vgregion.verticalprio.repository;

import java.util.ArrayList;
import java.util.List;

import se.vgregion.verticalprio.entity.SektorRaad;

/**
 * @author Claes Lundahl, vgrid=clalu4
 * 
 */
@SuppressWarnings("serial")
public class NestedSektorRaad extends SektorRaad implements HaveNestedEnteties<SektorRaad> {

    private List<SektorRaad> sektors = new ArrayList<SektorRaad>();

    public NestedSektorRaad() {
    }

    public NestedSektorRaad(Long id) {
        setId(id);
    }

    /**
     * @inheritDoc
     */
    @Override
    public List<SektorRaad> content() {
        return sektors;
    }

}
