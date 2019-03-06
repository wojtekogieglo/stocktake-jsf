package com.jsf.entities;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the roleuser database table.
 * 
 */
@Embeddable
public class RoleuserPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(insertable=false, updatable=false)
	private int role_idRole;

	@Column(insertable=false, updatable=false)
	private int user_idUser;

	public RoleuserPK() {
	}
	public int getRole_idRole() {
		return this.role_idRole;
	}
	public void setRole_idRole(int role_idRole) {
		this.role_idRole = role_idRole;
	}
	public int getUser_idUser() {
		return this.user_idUser;
	}
	public void setUser_idUser(int user_idUser) {
		this.user_idUser = user_idUser;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof RoleuserPK)) {
			return false;
		}
		RoleuserPK castOther = (RoleuserPK)other;
		return 
			(this.role_idRole == castOther.role_idRole)
			&& (this.user_idUser == castOther.user_idUser);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.role_idRole;
		hash = hash * prime + this.user_idUser;
		
		return hash;
	}
}