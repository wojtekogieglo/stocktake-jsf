package com.jsf.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the group database table.
 * 
 */
@Entity
@NamedQuery(name="Groups.findAll", query="SELECT g FROM Groups g")
public class Groups implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(unique=true, nullable=false)
	private Integer idGroup;

	private boolean archive;

	private String category;

	@Lob
	private String description;

	//bi-directional many-to-one association to Invobject
	@OneToMany(mappedBy="group")
	private List<Invobject> invobjects;

	public Groups() {
	}

	public Integer getIdGroup() {
		return this.idGroup;
	}

	public void setIdGroup(Integer idGroup) {
		this.idGroup = idGroup;
	}

	public boolean getArchive() {
		return this.archive;
	}

	public void setArchive(boolean archive) {
		this.archive = archive;
	}

	public String getCategory() {
		return this.category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Invobject> getInvobjects() {
		return this.invobjects;
	}

	public void setInvobjects(List<Invobject> invobjects) {
		this.invobjects = invobjects;
	}

	public Invobject addInvobject(Invobject invobject) {
		getInvobjects().add(invobject);
		invobject.setGroups(this);

		return invobject;
	}

	public Invobject removeInvobject(Invobject invobject) {
		getInvobjects().remove(invobject);
		invobject.setGroups(null);

		return invobject;
	}

}