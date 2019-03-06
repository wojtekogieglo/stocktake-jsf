package com.jsf.role;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import com.jsf.entities.Role;
import com.jsf.dao.RoleDAO;

@ManagedBean
public class RoleListBB {
	private static final String PAGE_ROLE_EDIT = "/pages/admin/roles/roleEdit?faces-redirect=true";
	private static final String PAGE_STAY_AT_THE_SAME = null;

	private String roleName;
	private ResourceBundle txtMain;

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	
	@EJB
	RoleDAO roleDAO;
	
	@PostConstruct
	public void initialization() {
		FacesContext context = FacesContext.getCurrentInstance();
		txtMain = ResourceBundle.getBundle("resources.textMain", context.getViewRoot().getLocale());
	}
	
	public List<Role> getFullList(){
		return roleDAO.getFullList();
	}
	
	public List<Role> getList(){
		List<Role> list = null;
		
		//1. Prepare search params
		Map<String,Object> searchParams = new HashMap<String, Object>();
		
		if (roleName != null && roleName.length() > 0){
			searchParams.put("roleName", roleName);
		}
		
		//2. Get list
		list = roleDAO.getList(searchParams);
		
		return list;
	}
	
	public String newRole(){
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(true);
		Role role = new Role();
		session.setAttribute("role", role);
		return PAGE_ROLE_EDIT;
	}
	
	public String editRole(Role role){
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(true);
		session.setAttribute("role", role);
		return PAGE_ROLE_EDIT;
	}
	
	public String deleteRole(Role role){
		roleDAO.remove(role);
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, 
				txtMain.getString("typeMessage"), txtMain.getString("deleteRecord")));
		return PAGE_STAY_AT_THE_SAME;
	}
}
