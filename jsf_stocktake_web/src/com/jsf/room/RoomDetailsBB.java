package com.jsf.room;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.simplesecurity.RemoteClient;
import javax.servlet.http.HttpSession;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import com.jsf.dao.InvObjectDAO;
import com.jsf.dao.RoomPersonDAO;

import com.jsf.entities.Department;
import com.jsf.entities.Invobject;
import com.jsf.entities.Roleuser;
import com.jsf.entities.Room;
import com.jsf.entities.Roomperson;

@ManagedBean
@ViewScoped
public class RoomDetailsBB implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private static final String PAGE_ROOM_PERSON_EDIT = "/pages/institution_admin/room/roomPersonEdit?faces-redirect=true";
	private static final String PAGE_STAY_AT_THE_SAME = null;
	
	private String nameInvObject;
	private String roomNumber;
	private String floor;
	private String description;
	private Department department;
	private boolean archive;
	
	public String getNameInvObject() {
		return nameInvObject;
	}

	public void setNameInvObject(String nameInvObject) {
		this.nameInvObject = nameInvObject;
	}

	public String getRoomNumber() {
		return roomNumber;
	}

	public void setRoomNumber(String roomNumber) {
		this.roomNumber = roomNumber;
	}

	public String getFloor() {
		return floor;
	}

	public void setFloor(String floor) {
		this.floor = floor;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public boolean isArchive() {
		return archive;
	}

	public void setArchive(boolean archive) {
		this.archive = archive;
	}

	@EJB
	RoomPersonDAO roomPersonDAO;
	@EJB
	InvObjectDAO invObjectDAO;
	
	private Room room = null;
	
	private LazyDataModel<Roomperson> roomPerson;
	private LazyDataModel<Invobject> invObjectsRoom;
	
	@PostConstruct
	public void postConstruct() {
		// A. load person if passed through session
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(true);
		room = (Room) session.getAttribute("room");
		
		if(room != null) {
			setRoomNumber(room.getNumber());
			setFloor(room.getFloor());
			setDepartment(room.getDepartment());
			setDescription(room.getDescription());
		}
		
		roomPerson = new LazyDataModel<Roomperson>() {
			private static final long serialVersionUID = 1L;
	           
            @Override
            public List<Roomperson> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
                setRowCount(roomPersonDAO.getRowsCount(room));
               
                return roomPersonDAO.retrieveRoomPerson(first, pageSize, room);
            }

		};
		
		invObjectsRoom = new LazyDataModel<Invobject>() {
			private static final long serialVersionUID = 1L;
			
			@Override
			public List<Invobject> load(int first, int pageSize, String sortField, SortOrder sortOrder,  Map<String, Object> filters) {
				setRowCount(invObjectDAO.getRowsCountRoom(room, nameInvObject, archive));
				
				return invObjectDAO.retrieveInvObjectRoom(first, pageSize, room, nameInvObject, archive);
			}
		};
	}

	public LazyDataModel<Roomperson> getRoomPerson(){
		return roomPerson;
	}
	
	public LazyDataModel<Invobject> getInvObjectRoom(){
		return invObjectsRoom;
	}
	
	public String displayUnarchive() {
		setArchive(false);
		return PAGE_STAY_AT_THE_SAME;
	}
	
	public String displayArchive() {
		setArchive(true);
		return PAGE_STAY_AT_THE_SAME;
	}
	
	public List<Roomperson> getFullList(){
		return roomPersonDAO.getFullList(room);
	}
	
	public String newRoomPerson(){
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(true);
		Roomperson roomPerson = new Roomperson();
		session.setAttribute("roomPerson", roomPerson);
		session.setAttribute("room", room);
		
		return PAGE_ROOM_PERSON_EDIT;
	} 
	
	public String editRoomPerson(Roomperson roomPerson){
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(true);
		session.setAttribute("roomPerson", roomPerson);
		session.setAttribute("room", room);
		
		return PAGE_ROOM_PERSON_EDIT;
	} 
	
	public String deleteRoomPerson(Roomperson roomPerson){
		roomPersonDAO.remove(roomPerson);
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sukces!", "Rekord zosta³ usuniêty"));
		return PAGE_STAY_AT_THE_SAME;
	}
}
