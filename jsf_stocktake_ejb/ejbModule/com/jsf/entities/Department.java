package com.jsf.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the department database table.
 * 
 */
@Entity
@NamedQuery(name="Department.findAll", query="SELECT d FROM Department d")
public class Department implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(unique=true, nullable=false)
	private Integer idDepartment;

	private boolean archive;

	private String nameDepartment;

	//bi-directional many-to-one association to Institution
	@ManyToOne
	private Institution institution;
	
	//bi-directional many-to-one association to Room
	@OneToMany(mappedBy="department")
	private List<Personresponsible> personResponsible;

	//bi-directional many-to-one association to Room
	@OneToMany(mappedBy="department")
	private List<Room> rooms;

	public Department() {
	}

	public Integer getIdDepartment() {
		return this.idDepartment;
	}

	public void setIdDepartment(Integer idDepartment) {
		this.idDepartment = idDepartment;
	}

	public boolean getArchive() {
		return this.archive;
	}

	public void setArchive(boolean archive) {
		this.archive = archive;
	}

	public String getNameDepartment() {
		return this.nameDepartment;
	}

	public void setNameDepartment(String nameDepartment) {
		this.nameDepartment = nameDepartment;
	}

	public Institution getInstitution() {
		return this.institution;
	}

	public void setInstitution(Institution institution) {
		this.institution = institution;
	}

	public List<Room> getRooms() {
		return this.rooms;
	}

	public void setRooms(List<Room> rooms) {
		this.rooms = rooms;
	}

	public Room addRoom(Room room) {
		getRooms().add(room);
		room.setDepartment(this);

		return room;
	}

	public Room removeRoom(Room room) {
		getRooms().remove(room);
		room.setDepartment(null);

		return room;
	}

	public List<Personresponsible> getPersonResponsible() {
		return personResponsible;
	}

	public void setPersonResponsible(List<Personresponsible> personResponsible) {
		this.personResponsible = personResponsible;
	}
	
	

}