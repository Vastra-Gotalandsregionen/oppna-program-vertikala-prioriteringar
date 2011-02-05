package se.vgregion.verticalprio.repository.finding;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import se.vgregion.verticalprio.entity.DiagnosKod;

/**
 * @author Claes Lundahl, vgrid=clalu4
 * 
 */
@SuppressWarnings("serial")
public class NestedDiagnosKod extends DiagnosKod implements HaveNestedEntities<DiagnosKod> {

    private Set<DiagnosKod> diagnoses = new HashSet<DiagnosKod>();

    public NestedDiagnosKod() {
    }

    public NestedDiagnosKod(Collection<DiagnosKod> raad) {
        diagnoses.addAll(raad);
    }

    /**
     * @inheritDoc
     */
    @Override
    public Set<DiagnosKod> content() {
        return diagnoses;
    }

    /**
     * @inheritDoc
     */
    @Override
    public String toString() {
        return NestedUtil.toString(this);
    }

}
