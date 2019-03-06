package com.jsf.group;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.simplesecurity.RemoteClient;
import javax.servlet.http.HttpSession;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import com.jsf.dao.DepartmentDAO;
import com.jsf.dao.InvObjectDAO;
import com.jsf.dao.RoomDAO;
import com.jsf.entities.Department;
import com.jsf.entities.Groups;
import com.jsf.entities.Institution;
import com.jsf.entities.Invobject;
import com.jsf.entities.Room;
import com.jsf.entities.User;

@ManagedBean
@ViewScoped
public class GroupDetailsBB implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private static final String PAGE_STAY_AT_THE_SAME = null;
	
	private String nameInvObject;
	private String category;
	private String description;
	private boolean archive;
	

	public String getNameInvObject() {
		return nameInvObject;
	}

	public void setNameInvObject(String nameInvObject) {
		this.nameInvObject = nameInvObject;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public boolean isArchive() {
		return archive;
	}

	public void setArchive(boolean archive) {
		this.archive = archive;
	}

	@EJB
	InvObjectDAO invObjectDAO;
	@EJB
	RoomDAO roomDAO;
	@EJB
	DepartmentDAO departmentDAO;
	
	private Groups group = null;
	
	private LazyDataModel<Invobject> invObjectGroup;
	private Institution institution = null;
	private List<Department> listDepartment = null;
	private List<Room> listRoom = null;
	
	@PostConstruct
	public void postConstruct() {
		
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(true);
		RemoteClient<User> client = RemoteClient.load(session);
		institution = client.getDetails().getInstitution();
		listDepartment = departmentDAO.getFullList(institution);
		listRoom = roomDAO.getFullList(listDepartment);
		group = (Groups) session.getAttribute("group");
		
		if(group != null) {
			setCategory(group.getCategory());
			setDescription(group.getDescription());
		}
		
		invObjectGroup = new LazyDataModel<Invobject>() {
			private static final long serialVersionUID = 1L;
			
			@Override
			public List<Invobject> load(int first, int pageSize, String sortField, SortOrder sortOrder,  Map<String, Object> filters) {
				setRowCount(invObjectDAO.getRowsCountGroup(listRoom, group, nameInvObject, archive));
				
				return invObjectDAO.retrieveInvObjectGroup(first, pageSize, listRoom, group, nameInvObject, archive);
			}
		};
	}
	
	public LazyDataModel<Invobject> getInvObjectGroup(){
		return invObjectGroup;
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
