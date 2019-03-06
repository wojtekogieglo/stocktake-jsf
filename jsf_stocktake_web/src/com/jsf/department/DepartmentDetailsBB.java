package com.jsf.department;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import com.jsf.dao.PersonResponsibleDAO;
import com.jsf.dao.RoomDAO;
import com.jsf.entities.Department;
import com.jsf.entities.Personresponsible;
import com.jsf.entities.Room;

@ManagedBean
@ViewScoped
public class DepartmentDetailsBB implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private static final String PAGE_STAY_AT_THE_SAME = null;
	
	private String nameDepartment;
	private String institutionNameDepartment;
	private String roomNumber;
	private String surname;
	private boolean archive;

	public String getNameDepartment() {
		return nameDepartment;
	}

	public void setNameDepartment(String nameDepartment) {
		this.nameDepartment = nameDepartment;
	}
	
	public String getInstitutionNameDepartment() {
		return institutionNameDepartment;
	}

	public void setInstitutionNameDepartment(String institutionNameDepartment) {
		this.institutionNameDepartment = institutionNameDepartment;
	}

	public String getRoomNumber() {
		return roomNumber;
	}

	public void setRoomNumber(String roomNumber) {
		this.roomNumber = roomNumber;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}
	
	public boolean isArchive() {
		return archive;
	}

	public void setArchive(boolean archive) {
		this.archive = archive;
	}

	@EJB
	RoomDAO roomDAO;
	@EJB
	PersonResponsibleDAO personResponsibleDAO;

	private Department department = null;
	
	private LazyDataModel<Room> roomDepartment;
	private LazyDataModel<Personresponsible> personResponsible;
	
	@PostConstruct
	public void postConstruct() {
		// A. load person if passed through session
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(true);
		department = (Department) session.getAttribute("department");
		
		if(department != null) {
			setNameDepartment(department.getNameDepartment());
			setInstitutionNameDepartment(department.getInstitution().getNameInstitution());
		}
		
		roomDepartment = new LazyDataModel<Room>() {
			private static final long serialVersionUID = 1L;
			
			@Override
			public List<Room> load(int first, int pageSize, String sortField, SortOrder sortOrder,  Map<String, Object> filters) {
				setRowCount(roomDAO.getRowsCountDepartment(department, roomNumber, archive));
				
				return roomDAO.retrieveRoomsDepartment(first, pageSize, department, roomNumber, archive);
			}
		};
		
		personResponsible = new LazyDataModel<Personresponsible>() {
			private static final long serialVersionUID = 1L;
			
			@Override
			public List<Personresponsible> load(int first, int pageSize, String sortField, SortOrder sortOrder,  Map<String, Object> filters) {
				setRowCount(personResponsibleDAO.getRowsCountDepartment(department, surname, archive));
				
				return personResponsibleDAO.retrievePersonDepartment(first, pageSize, department, surname, archive);
			}
		};
	}
	
	public LazyDataModel<Room> getRoomsDepartment(){
		return roomDepartment;
	}
	
	public LazyDataModel<Personresponsible> getPersonDepartment(){
		return personResponsible;
	}
	
	public String displayUnarchive() {
		setArchive(false);
		return PAGE_STAY_AT_THE_SAME;
	}
	
	public String displayArchive() {
		setArchive(true);
		return PAGE_STAY_AT_THE_SAME;
	}
	
}
