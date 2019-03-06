package com.jsf.room;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

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

import com.jsf.dao.DepartmentDAO;
import com.jsf.dao.RoomDAO;
import com.jsf.entities.Department;
import com.jsf.entities.Institution;
import com.jsf.entities.Room;
import com.jsf.entities.User;

@ManagedBean
@ViewScoped
public class RoomListBB implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private static final String PAGE_ROOM_EDIT = "/pages/institution_admin/room/roomEdit?faces-redirect=true";
	private static final String PAGE_ROOM_DETAILS = "/pages/institution_admin/room/roomDetails?faces-redirect=true";
	private static final String PAGE_STAY_AT_THE_SAME = null;
	
	private String roomNumber;
	private boolean archive;
	private Institution institution;
	private List<Department> listDepartment;
	private ResourceBundle txtMain;

	public String getRoomNumber() {
		return roomNumber;
	}

	public void setRoomNumber(String roomNumber) {
		this.roomNumber = roomNumber;
	}
	
	public boolean isArchive() {
		return archive;
	}

	public void setArchive(boolean archive) {
		this.archive = archive;
	}

	public Institution getInstitution() {
		return institution;
	}

	public void setInstitution(Institution institution) {
		this.institution = institution;
	}

	public List<Department> getListDepartment() {
		return listDepartment;
	}

	public void setListDepartment(List<Department> listDepartment) {
		this.listDepartment = listDepartment;
	}


	@EJB
	RoomDAO roomDAO;
	@EJB
	DepartmentDAO departmentDAO;
	
	private LazyDataModel<Room> room;
	
	@PostConstruct
	public void initialization() {
		FacesContext context = FacesContext.getCurrentInstance();
		txtMain = ResourceBundle.getBundle("resources.textMain", context.getViewRoot().getLocale());
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(true);
		RemoteClient<User> client = RemoteClient.load(session);
		institution = client.getDetails().getInstitution();
		listDepartment = departmentDAO.getFullList(institution);
		
        room = new LazyDataModel<Room>() {
            private static final long serialVersionUID = 1L;
           
            @Override
            public List<Room> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
                setRowCount(roomDAO.getRowsCount(listDepartment, roomNumber, archive));
               
                return roomDAO.retrieveRooms(first, pageSize, listDepartment, roomNumber, archive);
            }
        };
    }
	
	public LazyDataModel<Room> getRoom(){
		return room;
	}
	
	public List<Room> getList(){
		List<Room> list = null;
		
		Map<String,Object> searchParams = new HashMap<String, Object>();
		
		if (roomNumber != null && roomNumber.length() > 0){
			searchParams.put("roomNumber", roomNumber);
		}
		
		list = roomDAO.getList(searchParams);
		
		return list;
	}
	
	public String newRoom(){
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(true);
		Room room = new Room();
		session.setAttribute("room", room);
		session.setAttribute("institution", institution);
		return PAGE_ROOM_EDIT;
	}
	
	public String editRoom(Room room){
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(true);
		session.setAttribute("room", room);
		session.setAttribute("institution", institution);
		return PAGE_ROOM_EDIT;
	}
	
	public String detailsRoom(Room room){
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(true);
		session.setAttribute("room", room);
		
		return PAGE_ROOM_DETAILS;
	}
	
	public String displayUnarchive() {
		setArchive(false);
		return PAGE_STAY_AT_THE_SAME;
	}
	
	public String displayArchive() {
		setArchive(true);
		return PAGE_STAY_AT_THE_SAME;
	}
	
	public String archiveRoom(Room room){
		roomDAO.archiveRoom(room);
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, 
				txtMain.getString("typeMessage"), txtMain.getString("archiveRecord")));
		return PAGE_STAY_AT_THE_SAME;
	}
	
	public String unarchiveRoom(Room room){
		roomDAO.unarchiveRoom(room);
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, 
				txtMain.getString("typeMessage"), txtMain.getString("unarchiveRecord")));
		return PAGE_STAY_AT_THE_SAME;
	}
}
