package com.jsf.room;

import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.jsf.dao.DepartmentDAO;
import com.jsf.dao.RoomDAO;
import com.jsf.entities.Department;
import com.jsf.entities.Institution;
import com.jsf.entities.Room;

@ManagedBean
@ViewScoped
public class RoomEditBB implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private static final String PAGE_ROOM_LIST = "/pages/institution_admin/room/roomList?faces-redirect=true";
	private static final String PAGE_STAY_AT_THE_SAME = null;

	@EJB
	RoomDAO roomDAO;
	@EJB
	DepartmentDAO departmentDAO;
	
	private ResourceBundle txtMain;
	private String roomNumber;
	private String floor;
	private String description;
	private Integer idDepartment;
	private List<Department> departmentNames;

	
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
	
	public Integer getIdDepartment() {
		return idDepartment;
	}

	public void setIdDepartment(Integer idDepartment) {
		this.idDepartment = idDepartment;
	}

	public List<Department> getDepartmentNames() {
		return departmentNames;
	}

	public void setDepartmentNames(List<Department> departmentNames) {
		this.departmentNames = departmentNames;
	}

	Room room = new Room();
	Institution institution = new Institution();
	
	@PostConstruct
	public void postConstruct() {
		FacesContext context = FacesContext.getCurrentInstance();
		txtMain = ResourceBundle.getBundle("resources.textMain", context.getViewRoot().getLocale());
		// A. load person if passed through session
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(true);
		room = (Room) session.getAttribute("room");
		institution = (Institution) session.getAttribute("institution");
		departmentNames = departmentDAO.getFullList(institution);

		// cleaning: attribute received => delete it from session
		if (room!= null) {
			session.removeAttribute("room");
		}

		if (room != null && room.getIdRoom() != null) {
			setRoomNumber(room.getNumber());
			setFloor(room.getFloor());
			setDescription(room.getDescription());
			setIdDepartment(room.getDepartment().getIdDepartment());
		}
	}
	
	private boolean validate() {
		FacesContext ctx = FacesContext.getCurrentInstance();
		boolean result = false;

		if (roomNumber == null) {
			ctx.addMessage(null, new FacesMessage("Wpisz numer pokoju"));
		}
		
		if (floor == null) {
			ctx.addMessage(null, new FacesMessage("Wpisz piêtro"));
		}
		
		if (floor == null) {
			ctx.addMessage(null, new FacesMessage("Wpisz piêtro"));
		}
		
		if (ctx.getMessageList().isEmpty()) {
			room.setNumber(roomNumber);
			room.setFloor(floor);
			room.setDescription(description);
			room.setDepartment(departmentDAO.find(idDepartment));
			result = true;
		}

		return result;
	}
	
	public String saveData() {
		FacesContext context = FacesContext.getCurrentInstance();

		if (room == null) {
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, 
					txtMain.getString("typeMessageError"), txtMain.getString("errorSystem")));
			return PAGE_STAY_AT_THE_SAME;
		}

		if (!validate()) {
			return PAGE_STAY_AT_THE_SAME;
		}

		try {
			if (room.getIdRoom() == null) {
				roomDAO.create(room);
				context.addMessage(null, new FacesMessage(txtMain.getString("typeMessage"), txtMain.getString("addRecord")));
				context.getExternalContext().getFlash().setKeepMessages(true);
			} else {
				roomDAO.merge(room);
				context.addMessage(null, new FacesMessage(txtMain.getString("typeMessage"), txtMain.getString("editRecord")));
				context.getExternalContext().getFlash().setKeepMessages(true);
			}
		} catch (Exception e) {
			e.printStackTrace();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, 
					txtMain.getString("typeMessageError"), txtMain.getString("errorSave")));
			return PAGE_STAY_AT_THE_SAME;
		}

		return PAGE_ROOM_LIST;
	}

}
