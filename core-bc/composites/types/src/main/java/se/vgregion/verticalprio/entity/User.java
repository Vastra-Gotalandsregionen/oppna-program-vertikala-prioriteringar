package se.vgregion.verticalprio.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import se.vgregion.dao.domain.patterns.entity.AbstractEntity;

/**
 * @author Claes Lundahl, vgrid=clalu4
 * 
 */
@Entity
@Table(name = "vgr_user")
public class User extends AbstractEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "editor")
    private boolean editor;

    private String name;

    // @Column(name = "first_name")
    private String firstName;

    // @Column(name = "last_name")
    private String lastName;

    @Column(name = "vgr_id")
    private String vgrId;

    @Column(name = "password")
    private String password;

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

    /**
     * @inheritDoc
     */
    @Override
    public Long getId() {
        return id;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

}
