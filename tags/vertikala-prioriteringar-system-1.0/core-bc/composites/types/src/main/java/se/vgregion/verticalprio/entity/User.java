package se.vgregion.verticalprio.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import se.vgregion.dao.domain.patterns.entity.AbstractEntity;

/**
 * @author Claes Lundahl, vgrid=clalu4
 * 
 */
@Entity
@Table(name = "vgr_user")
public class User extends AbstractEntity<Long> {

	public User() {
	}

	public User(Long id) {
		setId(id);
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "editor")
	private Boolean editor;

	@Column(name = "user_editor")
	private Boolean userEditor;

	@Column(name = "approver")
	private Boolean approver;

	@Column(name = "first_name")
	private String firstName;

	@Column(name = "last_name")
	private String lastName;

	@Column(name = "vgr_id", unique = true)
	private String vgrId;

	@Column(name = "password")
	private String password;

	@ManyToMany()
	@Fetch(FetchMode.JOIN)
	@JoinTable(name = "link_vgr_user_sektor_raad", joinColumns = { @JoinColumn(name = "user_id") }, inverseJoinColumns = { @JoinColumn(name = "sektor_raad_id") })
	private List<SektorRaad> sektorRaad = new ArrayList<SektorRaad>();

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

	public void setSektorRaad(List<SektorRaad> sektorRaad) {
		this.sektorRaad = sektorRaad;
	}

	public List<SektorRaad> getSektorRaad() {
		return sektorRaad;
	}

	public void setUserEditor(Boolean userEditor) {
		this.userEditor = userEditor;
	}

	public Boolean getUserEditor() {
		return userEditor;
	}

	public void setApprover(Boolean approver) {
		this.approver = approver;
	}

	public Boolean getApprover() {
		return approver;
	}

	public Boolean isApprover() {
		return approver;
	}

	public void setEditor(Boolean editor) {
		this.editor = editor;
	}

	public Boolean getEditor() {
		return editor;
	}

	public Boolean isEditor() {
		return editor;
	}

}
