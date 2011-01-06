package se.vgregion.verticalprio.repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import se.vgregion.verticalprio.entity.DiagnosKod;

/**
 * @author Claes Lundahl, vgrid=clalu4
 * 
 */
@SuppressWarnings("serial")
public class NestedDiagnosKod extends DiagnosKod implements HaveNestedEntities<DiagnosKod> {

    private List<DiagnosKod> diagnoses = new ArrayList<DiagnosKod>();

    public NestedDiagnosKod() {
    }

    public NestedDiagnosKod(Collection<DiagnosKod> raad) {
        diagnoses.addAll(raad);
    }

    /**
     * @inheritDoc
     */
    @Override
    public List<DiagnosKod> content() {
        return diagnoses;
    }

}
