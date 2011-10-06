package se.vgregion.verticalprio;

import java.util.ArrayList;
import java.util.List;

import se.vgregion.verticalprio.controllers.PrioriteringsobjektForm;
import se.vgregion.verticalprio.entity.DiagnosKod;

/**
 * @author Claes Lundahl, vgrid=clalu4
 * 
 */
public class FindSelectDiagnosesForm extends PrioriteringsobjektForm {

    private String findPattern;

    private List<DiagnosKod> findings = new ArrayList<DiagnosKod>();

    private List<DiagnosKod> selectedDiagnoses = new ArrayList<DiagnosKod>();

    private List<Long> diagnoserId = new ArrayList<Long>();

    private String command;

    /**
     * @return the findPattern
     */
    public String getFindPattern() {
        return findPattern;
    }

    /**
     * @param findPattern
     *            the findPattern to set
     */
    public void setFindPattern(String findPattern) {
        this.findPattern = findPattern;
    }

    /**
     * @return the findings
     */
    public List<DiagnosKod> getFindings() {
        return findings;
    }

    /**
     * @return the diagnoserId
     */
    public List<Long> getDiagnoserId() {
        return diagnoserId;
    }

    /**
     * @param diagnoserId
     *            the diagnoserId to set
     */
    public void setDiagnoserId(List<Long> diagnoserId) {
        this.diagnoserId = diagnoserId;
    }

    /**
     * @return the command
     */
    public String getCommand() {
        return command;
    }

    /**
     * @param command
     *            the command to set
     */
    public void setCommand(String command) {
        this.command = command;
    }

    /**
     * @return the selectedDiagnoses
     */
    public List<DiagnosKod> getSelectedDiagnoses() {
        return selectedDiagnoses;
    }

    /**
     * @param selectedDiagnoses
     *            the selectedDiagnoses to set
     */
    public void setSelectedDiagnoses(List<DiagnosKod> selectedDiagnoses) {
        this.selectedDiagnoses = selectedDiagnoses;
    }
}
