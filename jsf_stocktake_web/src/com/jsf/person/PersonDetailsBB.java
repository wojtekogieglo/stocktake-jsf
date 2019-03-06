package com.jsf.person;

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

import com.jsf.dao.InvObjectDAO;
import com.jsf.dao.RoomDAO;
import com.jsf.entities.Invobject;
import com.jsf.entities.Personresponsible;
import com.jsf.entities.Room;
import com.jsf.entities.Roomperson;

@ManagedBean
@ViewScoped
public class PersonDetailsBB implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private static final String PAGE_STAY_AT_THE_SAME = null;
	
	private String name;
	private String surname;
	private String description;
	private String roomNumber;
	private String nameInvObject;
	private boolean archive;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getSurname() {
		return surname;
	}
	
	public void setSurname(String surname) {
		this.surname = surname;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}

	public String getRoomNumber() {
		return roomNumber;
	}

	public void setRoomNumber(String roomNumber) {
		this.roomNumber = roomNumber;
	}

	public String getNameInvObject() {
		return nameInvObject;
	}

	public void setNameInvObject(String nameInvObject) {
		this.nameInvObject = nameInvObject;
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
	InvObjectDAO invObjectDAO;
	
	private Personresponsible personResponsible = null;
	
	private LazyDataModel<Invobject> invObjectPersonResponsible;
	
	@PostConstruct
	public void postConstruct() {
		// A. load person if passed through session
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(true);
		personResponsible = (Personresponsible) session.getAttribute("personResponsible");
		
		if(personResponsible != null) {
			setName(personResponsible.getName());
			setSurname(personResponsible.getSurname());
			setDescription(personResponsible.getDescription());
		}
		
		invObjectPersonResponsible = new LazyDataModel<Invobject>() {
			private static final long serialVersionUID = 1L;
			
			@Override
			public List<Invobject> load(int first, int pageSize, String sortField, SortOrder sortOrder,  Map<String, Object> filters) {
				setRowCount(invObjectDAO.getRowsCountPersonResponsible(personResponsible, nameInvObject, archive));
				
				return invObjectDAO.retrieveInvObjectPersonResponsible(first, pageSize, personResponsible, nameInvObject, archive);
			}
		};
	}
	
	public LazyDataModel<Invobject> getInvObjectPersonResponsible(){
		return invObjectPersonResponsible;
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
