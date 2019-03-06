package com.jsf.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the personresponsible database table.
 * 
 */
@Entity
@NamedQuery(name="Personresponsible.findAll", query="SELECT p FROM Personresponsible p")
public class Personresponsible implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(unique=true, nullable=false)
	private Integer idPersonResponsible;

	private boolean archive;

	@Lob
	private String description;

	private String name;

	private String surname;
	
	//bi-directional many-to-one association to Department
	@ManyToOne
	private Department department;

	//bi-directional many-to-one association to Invobject
	@OneToMany(mappedBy="personresponsible")
	private List<Invobject> invobjects;

	//bi-directional many-to-one association to Roomperson
	@OneToMany(mappedBy="personresponsible")
	private List<Roomperson> roompersons;

	public Personresponsible() {
	}

	public Integer getIdPersonResponsible() {
		return this.idPersonResponsible;
	}

	public void setIdPersonResponsible(Integer idPersonResponsible) {
		this.idPersonResponsible = idPersonResponsible;
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

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return this.surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public List<Invobject> getInvobjects() {
		return this.invobjects;
	}

	public void setInvobjects(List<Invobject> invobjects) {
		this.invobjects = invobjects;
	}

	public Invobject addInvobject(Invobject invobject) {
		getInvobjects().add(invobject);
		invobject.setPersonresponsible(this);

		return invobject;
	}

	public Invobject removeInvobject(Invobject invobject) {
		getInvobjects().remove(invobject);
		invobject.setPersonresponsible(null);

		return invobject;
	}

	public List<Roomperson> getRoompersons() {
		return this.roompersons;
	}

	public void setRoompersons(List<Roomperson> roompersons) {
		this.roompersons = roompersons;
	}

	public Roomperson addRoomperson(Roomperson roomperson) {
		getRoompersons().add(roomperson);
		roomperson.setPersonresponsible(this);

		return roomperson;
	}

	public Roomperson removeRoomperson(Roomperson roomperson) {
		getRoompersons().remove(roomperson);
		roomperson.setPersonresponsible(null);

		return roomperson;
	}

}