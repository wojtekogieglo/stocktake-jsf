package com.jsf.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the roomperson database table.
 * 
 */
@Entity
@NamedQuery(name="Roomperson.findAll", query="SELECT r FROM Roomperson r")
public class Roomperson implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private RoompersonPK id;

	@Temporal(TemporalType.DATE)
	private Date fromDate;

	@Temporal(TemporalType.DATE)
	private Date toDate;

	//bi-directional many-to-one association to Personresponsible
	@ManyToOne
	@JoinColumn(name = "PersonResponsible_idPersonResponsible", nullable = false, insertable = false, updatable = false)
	private Personresponsible personresponsible;

	//bi-directional many-to-one association to Room
	@ManyToOne
	@JoinColumn(name = "Room_idRoom", nullable = false, insertable = false, updatable = false)
	private Room room;

	public Roomperson() {
	}

	public RoompersonPK getId() {
		return this.id;
	}

	public void setId(RoompersonPK id) {
		this.id = id;
	}

	public Date getFromDate() {
		return this.fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return this.toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	@JoinColumn(name = "PersonResponsible_idPersonResponsible", nullable = false, insertable = false, updatable = false)
	public Personresponsible getPersonresponsible() {
		return this.personresponsible;
	}

	public void setPersonresponsible(Personresponsible personresponsible) {
		this.personresponsible = personresponsible;
	}
	
	@JoinColumn(name = "Room_idRoom", nullable = false, insertable = false, updatable = false)
	public Room getRoom() {
		return this.room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

}