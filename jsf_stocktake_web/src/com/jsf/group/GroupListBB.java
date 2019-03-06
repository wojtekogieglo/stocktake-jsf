package com.jsf.group;

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
import javax.servlet.http.HttpSession;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import com.jsf.dao.GroupDAO;
import com.jsf.entities.Department;
import com.jsf.entities.Groups;

@ManagedBean
@ViewScoped
public class GroupListBB implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private static final String PAGE_GROUP_EDIT = "/pages/user/group/groupEdit?faces-redirect=true";
	private static final String PAGE_GROUP_DETAILS = "/pages/user/group/groupDetails?faces-redirect=true";
	private static final String PAGE_STAY_AT_THE_SAME = null;
	
	private String category;
	private boolean archive;
	private ResourceBundle txtMain;

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}
	
	public boolean isArchive() {
		return archive;
	}

	public void setArchive(boolean archive) {
		this.archive = archive;
	}

	@EJB
	GroupDAO groupDAO;
	
	private LazyDataModel<Groups> groups;
	
	@PostConstruct
	public void initialization() {
		FacesContext context = FacesContext.getCurrentInstance();
		txtMain = ResourceBundle.getBundle("resources.textMain", context.getViewRoot().getLocale());
        groups = new LazyDataModel<Groups>() {
            private static final long serialVersionUID = 1L;
           
            @Override
            public List<Groups> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
                setRowCount(groupDAO.getRowsCount(category, archive));
               
                return groupDAO.retrieveGroups(first, pageSize, category, archive);
            }
        };
    }
	
	public LazyDataModel<Groups> getGroups(){
		return groups;
	}
	
	public List<Groups> getFullList(){
		return groupDAO.getFullList();
	}
	
	public List<Groups> getList(){
		List<Groups> list = null;
		
		//1. Prepare search params
		Map<String,Object> searchParams = new HashMap<String, Object>();
		
		if (category != null && category.length() > 0){
			searchParams.put("category", category);
		}
		
		//2. Get list
		list = groupDAO.getList(searchParams);
		
		return list;
	}
	
	public String displayUnarchive() {
		setArchive(false);
		return PAGE_STAY_AT_THE_SAME;
	}
	
	public String displayArchive() {
		setArchive(true);
		return PAGE_STAY_AT_THE_SAME;
	}
	
	public String newGroup(){
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(true);
		Groups group = new Groups();
		session.setAttribute("group", group);
		return PAGE_GROUP_EDIT;
	}
	
	public String editGroup(Groups group){
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(true);
		session.setAttribute("group", group);
		return PAGE_GROUP_EDIT;
	}
	
	public String detailsGroup(Groups group){
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(true);
		session.setAttribute("group", group);
		return PAGE_GROUP_DETAILS;
	}

	public String archiveGroup(Groups group){
		groupDAO.archiveGroup(group);
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, 
				txtMain.getString("typeMessage"), txtMain.getString("archiveRecord")));
		return PAGE_STAY_AT_THE_SAME;
	}
	
	public String unarchiveGroup(Groups group){
		groupDAO.unarchiveGroup(group);
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, 
				txtMain.getString("typeMessage"), txtMain.getString("unarchiveRecord")));
		return PAGE_STAY_AT_THE_SAME;
	}
	
}
