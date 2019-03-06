package com.jsf.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the room database table.
 * 
 */
@Entity
@NamedQuery(name="Room.findAll", query="SELECT r FROM Room r")
public class Room implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(unique=true, nullable=false)
	private Integer idRoom;

	private boolean archive;

	@Lob
	private String description;

	private String floor;

	private String number;

	//bi-directional many-to-one association to Department
	@ManyToOne
	private Department department;

	//bi-directional many-to-one association to Roomperson
	@OneToMany(mappedBy="room")
	private List<Roomperson> roompersons;
	
	//bi-directional many-to-one association to Room
	@OneToMany(mappedBy="room")
	private List<Invobject> invObject;

	public Room() {
	}

	public Integer getIdRoom() {
		return this.idRoom;
	}

	public void setIdRoom(Integer idRoom) {
		this.idRoom = idRoom;
	}

	public boolean getArchive() {
		return this.archive;
	}

	public void setArchive(boolean archive) {
		this.archive = archive;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getFloor() {
		return this.floor;
	}

	public void setFloor(String floor) {
		this.floor = floor;
	}

	public String getNumber() {
		return this.number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public Department getDepartment() {
		return this.department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public List<Roomperson> getRoompersons() {
		return this.roompersons;
	}

	public void setRoompersons(List<Roomperson> roompersons) {
		this.roompersons = roompersons;
	}

	public Roomperson addRoomperson(Roomperson roomperson) {
		getRoompersons().add(roomperson);
		roomperson.setRoom(this);

		return roomperson;
	}

	public Roomperson removeRoomperson(Roomperson roomperson) {
		getRoompersons().remove(roomperson);
		roomperson.setRoom(null);

		return roomperson;
	}

	public List<Invobject> getInvObject() {
		return invObject;
	}

	public void setInvObject(List<Invobject> invObject) {
		this.invObject = invObject;
	}
	
	public Invobject addInvobject(Invobject invObject) {
		getInvObject().add(invObject);
		invObject.setRoom(this);

		return invObject;
	}

	public Invobject removeInvobject(Invobject invObject) {
		getInvObject().remove(invObject);
		invObject.setRoom(null);

		return invObject;
	}
	
	

}