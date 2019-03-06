package com.jsf.invobject;

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
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.simplesecurity.RemoteClient;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.jsf.dao.DepartmentDAO;
import com.jsf.dao.GroupDAO;
import com.jsf.dao.InvObjectDAO;
import com.jsf.dao.PersonResponsibleDAO;
import com.jsf.dao.RoomDAO;
import com.jsf.entities.Department;
import com.jsf.entities.Groups;
import com.jsf.entities.Institution;
import com.jsf.entities.Invobject;
import com.jsf.entities.Personresponsible;
import com.jsf.entities.Room;
import com.jsf.entities.User;

@ManagedBean
@ViewScoped
public class InvObjectEditBB implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private static final String PAGE_INV_OBJECT_LIST = "/pages/user/invObject/invObjectList?faces-redirect=true";
	private static final String PAGE_STAY_AT_THE_SAME = null;

	@EJB
	InvObjectDAO invObjectDAO;
	@EJB
	PersonResponsibleDAO personResponsibleDAO;
	@EJB
	GroupDAO groupDAO;
	@EJB
	RoomDAO roomDAO;
	@EJB
	DepartmentDAO departmentDAO;
	
	private ResourceBundle txtMain;
	private String name;
	private String inventoryNo;
	private String serialNo;
	private Integer quantity;
	private Date purchaseDate;
	private Date toDate;
	private String description;
	private Integer idPersonResponsible;
	private Integer idGroup;
	private Integer idRoom;
	private List<Personresponsible> personResponsibleNames;
	private List<Groups> groupCategoryNames;
	private List<Room> roomNumbers;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getInventoryNo() {
		return inventoryNo;
	}
	
	public void setInventoryNo(String inventoryNo) {
		this.inventoryNo = inventoryNo;
	}
	
	public String getSerialNo() {
		return serialNo;
	}
	
	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}
	
	public Integer getQuantity() {
		return quantity;
	}
	
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	
	public Date getPurchaseDate() {
		return purchaseDate;
	}
	
	public void setPurchaseDate(Date purchaseDate) {
		this.purchaseDate = purchaseDate;
	}
	
	public Date getToDate() {
		return toDate;
	}
	
	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public Integer getIdPersonResponsible() {
		return idPersonResponsible;
	}
	
	public void setIdPersonResponsible(Integer idPersonResponsible) {
		this.idPersonResponsible = idPersonResponsible;
	}
	
	public Integer getIdGroup() {
		return idGroup;
	}

	public void setIdGroup(Integer idGroup) {
		this.idGroup = idGroup;
	}
	
	public Integer getIdRoom() {
		return idRoom;
	}
	
	public void setIdRoom(Integer idRoom) {
		this.idRoom = idRoom;
	}
	
	public List<Personresponsible> getPersonResponsibleNames() {
		return personResponsibleNames;
	}

	public void setPersonResponsibleNames(List<Personresponsible> personResponsibleNames) {
		this.personResponsibleNames = personResponsibleNames;
	}

	public List<Groups> getGroupCategoryNames() {
		return groupCategoryNames;
	}

	public void setGroupCategoryNames(List<Groups> groupCategoryNames) {
		this.groupCategoryNames = groupCategoryNames;
	}

	public List<Room> getRoomNumbers() {
		return roomNumbers;
	}

	public void setRoomNumbers(List<Room> roomNumbers) {
		this.roomNumbers = roomNumbers;
	}

	private Invobject invObject = null;
	private List<Department> departmentList = null;
	private Institution institution = null;
	
	@PostConstruct
	public void postConstruct() {
		FacesContext context = FacesContext.getCurrentInstance();
		txtMain = ResourceBundle.getBundle("resources.textMain", context.getViewRoot().getLocale());
		// A. load person if passed through session
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(true);
		RemoteClient<User> client = RemoteClient.load(session);
		institution = client.getDetails().getInstitution();
		invObject = (Invobject) session.getAttribute("invObject");
		departmentList = departmentDAO.getFullList(institution);
		groupCategoryNames = groupDAO.getFullList();
		roomNumbers = roomDAO.getFullList(departmentList);
		
		if(!roomNumbers.isEmpty()) {
			Department department = roomDAO.find(roomNumbers.get(0).getIdRoom()).getDepartment();
			personResponsibleNames = personResponsibleDAO.getListByDepartment(department);
		}

		if (invObject != null && invObject.getIdInvObject() != null) {
			setName(invObject.getName());
			setInventoryNo(invObject.getInventoryNo());
			setSerialNo(invObject.getSerialNo());
			setQuantity(invObject.getQuantity());
			setPurchaseDate(invObject.getPurchaseDate());
			setToDate(invObject.getToDate());
			setIdPersonResponsible(invObject.getPersonresponsible().getIdPersonResponsible());
			setIdGroup(invObject.getGroups().getIdGroup());
			setIdRoom(invObject.getRoom().getIdRoom());
			setDescription(invObject.getDescription());
		}
	}
	
	public void changeRoom(AjaxBehaviorEvent event) {
		Department department = roomDAO.find(idRoom).getDepartment();
		personResponsibleNames = personResponsibleDAO.getListByDepartment(department);
	}
	
	private boolean validate() {
		FacesContext ctx = FacesContext.getCurrentInstance();
		boolean result = false;

		if (name == null || name.trim().length() == 0) {
			ctx.addMessage(null, new FacesMessage("Wpisz nazwê"));
		}
		
		if (inventoryNo == null || inventoryNo.trim().length() == 0) {
			ctx.addMessage(null, new FacesMessage("Wpisz numer inwentaryzacyjny"));
		}
		
		if (serialNo == null || serialNo.trim().length() == 0) {
			ctx.addMessage(null, new FacesMessage("Wpisz numer seryjny"));
		}
		
		if (quantity == null) {
			ctx.addMessage(null, new FacesMessage("Wpisz iloœæ"));
		}
		
		if (purchaseDate == null) {
			ctx.addMessage(null, new FacesMessage("Wpisz datê zakupu"));
		}
		
		if (toDate == null) {
			ctx.addMessage(null, new FacesMessage("Wpisz datê zakoñczenia"));
		}
		
		if (ctx.getMessageList().isEmpty()) {
			invObject.setName(name);
			invObject.setInventoryNo(inventoryNo);
			invObject.setSerialNo(serialNo);
			invObject.setQuantity(quantity);
			invObject.setPurchaseDate(purchaseDate);
			invObject.setToDate(toDate);
			invObject.setPersonresponsible(personResponsibleDAO.find(idPersonResponsible));
			invObject.setGroups(groupDAO.find(idGroup));
			invObject.setRoom(roomDAO.find(idRoom));
			invObject.setDescription(description);
			
			result = true;
		}

		return result;
	}
	
	public String saveData() {
		FacesContext context = FacesContext.getCurrentInstance();

		if (invObject == null) {
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, 
					txtMain.getString("typeMessageError"), txtMain.getString("errorSystem")));
			return PAGE_STAY_AT_THE_SAME;
		}

		if (!validate()) {
			return PAGE_STAY_AT_THE_SAME;
		}

		try {
			if (invObject.getIdInvObject() == null) {
				invObjectDAO.create(invObject);
				context.addMessage(null, new FacesMessage(txtMain.getString("typeMessage"), txtMain.getString("addRecord")));
				context.getExternalContext().getFlash().setKeepMessages(true);
			} else {
				invObjectDAO.merge(invObject);
				context.addMessage(null, new FacesMessage(txtMain.getString("typeMessage"), txtMain.getString("editRecord")));
				context.getExternalContext().getFlash().setKeepMessages(true);
			}
		} catch (Exception e) {
			e.printStackTrace();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, 
					txtMain.getString("typeMessageError"), txtMain.getString("errorSave")));
			return PAGE_STAY_AT_THE_SAME;
		}

		return PAGE_INV_OBJECT_LIST;
	}
	
}
