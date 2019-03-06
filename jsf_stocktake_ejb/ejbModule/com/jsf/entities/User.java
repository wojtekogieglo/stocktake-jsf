package com.jsf.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the user database table.
 * 
 */
@Entity
@NamedQuery(name="User.findAll", query="SELECT u FROM User u")
public class User implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(unique=true, nullable=false)
	private Integer idUser;

	private boolean active;

	private String email;

	private String name;

	private String password;

	private String surname;

	//bi-directional many-to-one association to Roleuser
	@OneToMany(mappedBy="user")
	private List<Roleuser> roleusers;

	//bi-directional many-to-one association to Institution
	@ManyToOne
	private Institution institution;

	public User() {
	}

	public Integer getIdUser() {
		return this.idUser;
	}

	public void setIdUser(Integer idUser) {
		this.idUser = idUser;
	}

	public boolean getActive() {
		return this.active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSurname() {
		return this.surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public List<Roleuser> getRoleusers() {
		return this.roleusers;
	}

	public void setRoleusers(List<Roleuser> roleusers) {
		this.roleusers = roleusers;
	}

	public Roleuser addRoleuser(Roleuser roleuser) {
		getRoleusers().add(roleuser);
		roleuser.setUser(this);

		return roleuser;
	}

	public Roleuser removeRoleuser(Roleuser roleuser) {
		getRoleusers().remove(roleuser);
		roleuser.setUser(null);

		return roleuser;
	}

	public Institution getInstitution() {
		return this.institution;
	}

	public void setInstitution(Institution institution) {
		this.institution = institution;
	}

}