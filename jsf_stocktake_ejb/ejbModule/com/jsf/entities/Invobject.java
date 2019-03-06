package com.jsf.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the invobject database table.
 * 
 */
@Entity
@NamedQuery(name="Invobject.findAll", query="SELECT i FROM Invobject i")
public class Invobject implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(unique=true, nullable=false)
	private Integer idInvObject;

	private boolean archive;

	@Lob
	private String description;

	private String inventoryNo;

	private String name;

	@Temporal(TemporalType.DATE)
	private Date purchaseDate;

	private int quantity;

	private String serialNo;

	@Temporal(TemporalType.DATE)
	private Date toDate;

	//bi-directional many-to-one association to Group
	@ManyToOne
	private Groups group;

	//bi-directional many-to-one association to Personresponsible
	@ManyToOne
	private Personresponsible personresponsible;
	
	//bi-directional many-to-one association to Room
	@ManyToOne
	private Room room;

	public Invobject() {
	}

	public Integer getIdInvObject() {
		return this.idInvObject;
	}

	public void setIdInvObject(Integer idInvObject) {
		this.idInvObject = idInvObject;
	}

	public boolean getArchive() {
		return this.archive;
	}

	public void setArchive(boolean archive) {
		this.archive = archive;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getInventoryNo() {
		return this.inventoryNo;
	}

	public void setInventoryNo(String inventoryNo) {
		this.inventoryNo = inventoryNo;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getPurchaseDate() {
		return this.purchaseDate;
	}

	public void setPurchaseDate(Date purchaseDate) {
		this.purchaseDate = purchaseDate;
	}

	public int getQuantity() {
		return this.quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getSerialNo() {
		return this.serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public Date getToDate() {
		return this.toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public Groups getGroups() {
		return this.group;
	}

	public void setGroups(Groups group) {
		this.group = group;
	}

	public Personresponsible getPersonresponsible() {
		return this.personresponsible;
	}

	public void setPersonresponsible(Personresponsible personresponsible) {
		this.personresponsible = personresponsible;
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}
	
	

}