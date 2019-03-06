package com.jsf.invobject;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import com.jsf.dao.InvObjectDAO;
import com.jsf.entities.Groups;
import com.jsf.entities.Invobject;
import com.jsf.entities.Personresponsible;
import com.jsf.entities.Room;

@ManagedBean
@ViewScoped
public class InvObjectDetailsBB implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private static final String PAGE_STAY_AT_THE_SAME = null;
	
	private String name;
	private String inventoryNo;
	private String serialNo;
	private Integer quantity;
	private Date purchaseDate;
	private Date toDate;
	private String description;
	private String idPersonResponsible;
	private String idGroup;
	private String idRoom;
	
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
	
	public String getIdPersonResponsible() {
		return idPersonResponsible;
	}
	
	public void setIdPersonResponsible(String idPersonResponsible) {
		this.idPersonResponsible = idPersonResponsible;
	}
	
	public String getIdGroup() {
		return idGroup;
	}

	public void setIdGroup(String idGroup) {
		this.idGroup = idGroup;
	}
	
	public String getIdRoom() {
		return idRoom;
	}
	
	public void setIdRoom(String idRoom) {
		this.idRoom = idRoom;
	}
	
	@EJB
	InvObjectDAO invObjectDAO;
	
	private Invobject invObject = null;
	
	@PostConstruct
	public void postConstruct() {
		// A. load person if passed through session
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(true);
		invObject = (Invobject) session.getAttribute("invObject");
		
		setName(invObject.getName());
		setInventoryNo(invObject.getInventoryNo());
		setSerialNo(invObject.getSerialNo());
		setQuantity(invObject.getQuantity());
		setPurchaseDate(invObject.getPurchaseDate());
		setToDate(invObject.getToDate());
		setIdGroup(invObject.getGroups().getCategory());
		setIdPersonResponsible(invObject.getPersonresponsible().getName() + " " + invObject.getPersonresponsible().getSurname());
		setIdRoom(invObject.getRoom().getNumber());
	}
	
	
}
