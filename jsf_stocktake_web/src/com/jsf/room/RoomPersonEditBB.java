package com.jsf.room;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.simplesecurity.RemoteClient;
import javax.servlet.http.HttpSession;

import com.jsf.dao.DepartmentDAO;
import com.jsf.dao.PersonResponsibleDAO;
import com.jsf.dao.RoomDAO;
import com.jsf.dao.RoomPersonDAO;
import com.jsf.entities.Department;
import com.jsf.entities.Institution;
import com.jsf.entities.Personresponsible;
import com.jsf.entities.Room;
import com.jsf.entities.Roomperson;
import com.jsf.entities.RoompersonPK;
import com.jsf.entities.User;

@ManagedBean
@ViewScoped
public class RoomPersonEditBB implements Serializable{
	
	private static final String PAGE_ROOM_PERSON_LIST = "/pages/institution_admin/room/roomDetails?faces-redirect=true";
	private static final String PAGE_STAY_AT_THE_SAME = null;
	
	@EJB
	PersonResponsibleDAO personResponsibleDAO;
	@EJB
	RoomPersonDAO roomPersonDAO;
	@EJB
	RoomDAO roomDAO;
	@EJB
	DepartmentDAO departmentDAO;
	
	private Integer idPersonResponsible;
	private Date fromDate;
	private Date toDate;
	private List<Personresponsible> personResponsibleNames;
	private ResourceBundle txtMain;
	
	public Integer getIdPersonResponsible() {
		return idPersonResponsible;
	}
	
	public void setIdPersonResponsible(Integer idPersonResponsible) {
		this.idPersonResponsible = idPersonResponsible;
	}
	
	public Date getFromDate() {
		return fromDate;
	}
	
	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}
	
	public Date getToDate() {
		return toDate;
	}
	
	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}
	
	public List<Personresponsible> getPersonResponsibleNames() {
		return personResponsibleNames;
	}
	
	public void setPersonResponsibleNames(List<Personresponsible> personResponsibleNames) {
		this.personResponsibleNames = personResponsibleNames;
	}
	
	private Room room = null;
	private Roomperson roomPerson = null;
	private Institution institution = null;
	private List<Department> listDepartment;
	
	@PostConstruct
	public void postConstruct() {
		FacesContext context = FacesContext.getCurrentInstance();
		txtMain = ResourceBundle.getBundle("resources.textMain", context.getViewRoot().getLocale());
		// A. load person if passed through session
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(true);
		RemoteClient<User> client = RemoteClient.load(session);
		institution = client.getDetails().getInstitution();
		room = (Room) session.getAttribute("room");
		roomPerson = (Roomperson) session.getAttribute("roomPerson");
		listDepartment = departmentDAO.getFullList(institution);
		
		personResponsibleNames = personResponsibleDAO.getFullList(room.getDepartment());
		
		if (roomPerson != null) {
			session.removeAttribute("roomPerson");
		}
		
		if(roomPerson != null && roomPerson.getId() != null) {
			setIdPersonResponsible(roomPerson.getPersonresponsible().getIdPersonResponsible());
			setFromDate(roomPerson.getFromDate());
			setToDate(roomPerson.getToDate());
		}
	}
	
	private boolean validate() {
		FacesContext ctx = FacesContext.getCurrentInstance();
		boolean result = false;

		if (fromDate == null) {
			ctx.addMessage(null, new FacesMessage("Wpisz datê przydzielenia"));
		}

		if (ctx.getMessageList().isEmpty()) {
			roomPerson.setPersonresponsible(personResponsibleDAO.find(idPersonResponsible));
			roomPerson.setRoom(room);
			roomPerson.setFromDate(fromDate);
			
			if(toDate != null) {
				roomPerson.setToDate(toDate);
			}else {
				roomPerson.setToDate(null);
			}
			
			result = true;
		}

		return result;
	}
	
	public String saveData() {
		FacesContext context = FacesContext.getCurrentInstance();

		// no Person object passed
		if (roomPerson == null) {
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, 
					txtMain.getString("typeMessageError"), txtMain.getString("errorSystem")));
			return PAGE_STAY_AT_THE_SAME;
		}

		if (!validate()) {
			return PAGE_STAY_AT_THE_SAME;
		}

		try {
			if (roomPerson.getId() == null) {
				RoompersonPK rpPK = new RoompersonPK();
				rpPK.setPersonResponsible_idPersonResponsible(idPersonResponsible);
				rpPK.setRoom_idRoom(room.getIdRoom());
				
				roomPerson.setId(rpPK);
				
				roomPersonDAO.create(roomPerson);
				context.addMessage(null, new FacesMessage(txtMain.getString("typeMessage"), txtMain.getString("addRecord")));
				context.getExternalContext().getFlash().setKeepMessages(true);
			} else {
				// existing record
				roomPersonDAO.merge(roomPerson);
				context.addMessage(null, new FacesMessage(txtMain.getString("typeMessage"), txtMain.getString("editRecord")));
				context.getExternalContext().getFlash().setKeepMessages(true);
			}
		} catch (Exception e) {
			e.printStackTrace();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, 
					txtMain.getString("typeMessageError"), txtMain.getString("errorSave")));
			return PAGE_STAY_AT_THE_SAME;
		}
		return PAGE_ROOM_PERSON_LIST;
	}
	
	
}
