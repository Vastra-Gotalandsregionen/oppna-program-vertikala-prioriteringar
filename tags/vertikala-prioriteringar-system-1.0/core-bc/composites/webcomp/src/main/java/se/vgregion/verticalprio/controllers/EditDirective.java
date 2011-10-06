package se.vgregion.verticalprio.controllers;

import java.io.Serializable;

/**
 * @author Claes Lundahl, vgrid=clalu4
 * 
 */
@SuppressWarnings("serial")
public class EditDirective implements Serializable {

    private Boolean override;

    private Boolean editable;

    /**
     * 
     */
    public EditDirective() {
    }

    /**
     * 
     */
    public EditDirective(Boolean editable, Boolean override) {
        setEditable(editable);
        setOverride(override);
    }

    public Boolean getOverride() {
        return override;
    }

    public void setOverride(Boolean override) {
        this.override = override;
    }

    public Boolean getEditable() {
        return editable;
    }

    public void setEditable(Boolean editable) {
        this.editable = editable;
    }

}
