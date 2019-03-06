package com.jsf.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the roleuser database table.
 * 
 */
@Entity
@NamedQuery(name="Roleuser.findAll", query="SELECT r FROM Roleuser r")
public class Roleuser implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private RoleuserPK id;

	@Temporal(TemporalType.DATE)
	private Date expirationDate;

	@Temporal(TemporalType.DATE)
	private Date grantDate;

	//bi-directional many-to-one association to Role
	@ManyToOne
	@JoinColumn(name = "role_idRole", nullable = false, insertable = false, updatable = false)
	private Role role;

	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name = "user_idUser", nullable = false, insertable = false, updatable = false)
	private User user;

	public Roleuser() {
	}

	public RoleuserPK getId() {
		return this.id;
	}

	public void setId(RoleuserPK id) {
		this.id = id;
	}

	public Date getExpirationDate() {
		return this.expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	public Date getGrantDate() {
		return this.grantDate;
	}

	public void setGrantDate(Date grantDate) {
		this.grantDate = grantDate;
	}
	
	public Role getRole() {
		return this.role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}