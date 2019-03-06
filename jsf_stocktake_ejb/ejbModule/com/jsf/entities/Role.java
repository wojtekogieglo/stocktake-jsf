package com.jsf.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the role database table.
 * 
 */
@Entity
@NamedQuery(name="Role.findAll", query="SELECT r FROM Role r")
public class Role implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(unique=true, nullable=false)
	private Integer idRole;

	@Lob
	private String description;

	private String roleName;

	//bi-directional many-to-one association to Roleuser
	@OneToMany(mappedBy="role")
	private List<Roleuser> roleusers;

	public Role() {
	}

	public Integer getIdRole() {
		return this.idRole;
	}

	public void setIdRole(Integer idRole) {
		this.idRole = idRole;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getRoleName() {
		return this.roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public List<Roleuser> getRoleusers() {
		return this.roleusers;
	}

	public void setRoleusers(List<Roleuser> roleusers) {
		this.roleusers = roleusers;
	}

	public Roleuser addRoleuser(Roleuser roleuser) {
		getRoleusers().add(roleuser);
		roleuser.setRole(this);

		return roleuser;
	}

	public Roleuser removeRoleuser(Roleuser roleuser) {
		getRoleusers().remove(roleuser);
		roleuser.setRole(null);

		return roleuser;
	}

}