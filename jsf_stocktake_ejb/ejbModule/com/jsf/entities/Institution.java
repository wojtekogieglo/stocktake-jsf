package com.jsf.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the institution database table.
 * 
 */
@Entity
@NamedQuery(name="Institution.findAll", query="SELECT i FROM Institution i")
public class Institution implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(unique=true, nullable=false)
	private Integer idInstitution;

	private Boolean archive;

	private String nameInstitution;

	//bi-directional many-to-one association to Department
	@OneToMany(mappedBy="institution")
	private List<Department> departments;

	//bi-directional many-to-one association to User
	@OneToMany(mappedBy="institution")
	private List<User> users;

	public Institution() {
	}

	public Integer getIdInstitution() {
		return this.idInstitution;
	}

	public void setIdInstitution(Integer idInstitution) {
		this.idInstitution = idInstitution;
	}

	public Boolean getArchive() {
		return this.archive;
	}

	public void setArchive(Boolean archive) {
		this.archive = archive;
	}

	public String getNameInstitution() {
		return this.nameInstitution;
	}

	public void setNameInstitution(String nameInstitution) {
		this.nameInstitution = nameInstitution;
	}

	public List<Department> getDepartments() {
		return this.departments;
	}

	public void setDepartments(List<Department> departments) {
		this.departments = departments;
	}

	public Department addDepartment(Department department) {
		getDepartments().add(department);
		department.setInstitution(this);

		return department;
	}

	public Department removeDepartment(Department department) {
		getDepartments().remove(department);
		department.setInstitution(null);

		return department;
	}

	public List<User> getUsers() {
		return this.users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public User addUser(User user) {
		getUsers().add(user);
		user.setInstitution(this);

		return user;
	}

	public User removeUser(User user) {
		getUsers().remove(user);
		user.setInstitution(null);

		return user;
	}

}