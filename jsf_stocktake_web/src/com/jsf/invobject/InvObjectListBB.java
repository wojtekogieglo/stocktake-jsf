package com.jsf.invobject;

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
import com.jsf.dao.InvObjectDAO;
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
public class InvObjectListBB implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private static final String PAGE_INV_OBJECT_EDIT = "/pages/user/invObject/invObjectEdit?faces-redirect=true";
	private static final String PAGE_INV_OBJECT_DETAILS = "/pages/user/invObject/invObjectDetails?faces-redirect=true";
	private static final String PAGE_STAY_AT_THE_SAME = null;
	
	private String invObjectName;
	private boolean archive;
	private ResourceBundle txtMain;

	public String getInvObjectName() {
		return invObjectName;
	}

	public void setInvObjectName(String invObjectName) {
		this.invObjectName = invObjectName;
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
	
	private LazyDataModel<Invobject> invObjects;
	private Institution institution = null;
	private List<Department> listDepartment = null;
	private List<Room> listRoom = null;
	
	@PostConstruct
	public void initialization() {
		FacesContext context = FacesContext.getCurrentInstance();
		txtMain = ResourceBundle.getBundle("resources.textMain", context.getViewRoot().getLocale());
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(true);
		RemoteClient<User> client = RemoteClient.load(session);
		institution = client.getDetails().getInstitution();
		listDepartment = departmentDAO.getFullList(institution);
		listRoom = roomDAO.getFullList(listDepartment);
		
        invObjects = new LazyDataModel<Invobject>() {
            private static final long serialVersionUID = 1L;
           
            @Override
            public List<Invobject> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
                setRowCount(invObjectDAO.getRowsCount(listRoom, invObjectName, archive));
               
                return invObjectDAO.retrieveInvObjects(first, pageSize, listRoom, invObjectName, archive);
            }
        };
    }
	
	public LazyDataModel<Invobject> getInvObjects(){
		return invObjects;
	}
	
	public String displayUnarchive() {
		setArchive(false);
		return PAGE_STAY_AT_THE_SAME;
	}
	
	public String displayArchive() {
		setArchive(true);
		return PAGE_STAY_AT_THE_SAME;
	}
	
	public List<Invobject> getFullList(){
		return invObjectDAO.getFullList();
	}
	
	public List<Invobject> getList(){
		List<Invobject> list = null;
		
		Map<String,Object> searchParams = new HashMap<String, Object>();
		
		if (invObjectName != null && invObjectName.length() > 0){
			searchParams.put("invObjectName", invObjectName);
		}
		
		list = invObjectDAO.getList(searchParams);
		
		return list;
	}
	
	public String newInvObject(){
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(true);
		Invobject invObject = new Invobject();
		session.setAttribute("invObject", invObject);
		return PAGE_INV_OBJECT_EDIT;
	}
	
	public String editInvObject(Invobject invObject){
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(true);
		session.setAttribute("invObject", invObject);
		return PAGE_INV_OBJECT_EDIT;
	}
	
	public String detailsInvObject(Invobject invObject){
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(true);
		session.setAttribute("invObject", invObject);
		
		return PAGE_INV_OBJECT_DETAILS;
	}
	
	public String archiveInvObject(Invobject invObject){
		invObjectDAO.archiveInvObject(invObject);
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, 
				txtMain.getString("typeMessage"), txtMain.getString("archiveRecord")));
		return PAGE_STAY_AT_THE_SAME;
	}
	
	public String unarchiveInvObject(Invobject invObject){
		invObjectDAO.unarchiveInvObject(invObject);
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, 
				txtMain.getString("typeMessage"), txtMain.getString("unarchiveRecord")));
		return PAGE_STAY_AT_THE_SAME;
	}
	
}
