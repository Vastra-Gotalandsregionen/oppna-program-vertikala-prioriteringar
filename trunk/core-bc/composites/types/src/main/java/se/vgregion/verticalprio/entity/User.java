package se.vgregion.verticalprio.entity;

import java.io.Serializable;

/**
 * @author Claes Lundahl, vgrid=clalu4
 * 
 */
public class User implements Serializable {

    private boolean editor;
    private String name;
    private String firstName;
    private String lastName;

    private String vgrId;

    public boolean isEditor() {
        return editor;
    }

    public void setEditor(boolean editor) {
        this.editor = editor;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setVgrId(String vgrId) {
        this.vgrId = vgrId;
    }

    public String getVgrId() {
        return vgrId;
    }

}
