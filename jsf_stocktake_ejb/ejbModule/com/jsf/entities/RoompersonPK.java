package com.jsf.entities;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the roomperson database table.
 * 
 */
@Embeddable
public class RoompersonPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(insertable=false, updatable=false)
	private int room_idRoom;

	@Column(insertable=false, updatable=false)
	private int personResponsible_idPersonResponsible;

	public RoompersonPK() {
	}
	public int getRoom_idRoom() {
		return this.room_idRoom;
	}
	public void setRoom_idRoom(int room_idRoom) {
		this.room_idRoom = room_idRoom;
	}
	public int getPersonResponsible_idPersonResponsible() {
		return this.personResponsible_idPersonResponsible;
	}
	public void setPersonResponsible_idPersonResponsible(int personResponsible_idPersonResponsible) {
		this.personResponsible_idPersonResponsible = personResponsible_idPersonResponsible;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof RoompersonPK)) {
			return false;
		}
		RoompersonPK castOther = (RoompersonPK)other;
		return 
			(this.room_idRoom == castOther.room_idRoom)
			&& (this.personResponsible_idPersonResponsible == castOther.personResponsible_idPersonResponsible);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.room_idRoom;
		hash = hash * prime + this.personResponsible_idPersonResponsible;
		
		return hash;
	}
}