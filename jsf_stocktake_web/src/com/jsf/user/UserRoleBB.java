package com.jsf.user;

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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.jsf.entities.Institution;
import com.jsf.entities.Role;
import com.jsf.entities.Roleuser;
import com.jsf.entities.User;
import com.jsf.dao.InstitutionDAO;
import com.jsf.dao.RoleDAO;
import com.jsf.dao.RoleUserDAO;
import com.jsf.dao.UserDAO;

@ManagedBean
@ViewScoped
public class UserRoleBB implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private static final String PAGE_ROLE_EDIT = "/pages/admin/users/roleUserEdit?faces-redirect=true";
	private static final String PAGE_ROLE_WORKER_EDIT = "/pages/institution_admin/user/roleUserEdit?faces-redirect=true";
	private static final String PAGE_STAY_AT_THE_SAME = null;

	@EJB
	RoleUserDAO roleUserDAO;
	
	private ResourceBundle txtMain;
	private User user = null;
	
	@PostConstruct
	public void postConstruct() {
		FacesContext context = FacesContext.getCurrentInstance();
		txtMain = ResourceBundle.getBundle("resources.textMain", context.getViewRoot().getLocale());
		// A. load person if passed through session
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(true);
		user = (User) session.getAttribute("user");
	}
	
	public List<Roleuser> getFullList(){
		return roleUserDAO.getFullList(user);
	}
	
	public String newUserRole(){
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(true);
		Roleuser roleuser = new Roleuser();
		session.setAttribute("roleuser", roleuser);
		session.setAttribute("user", user);
		RemoteClient client = RemoteClient.load(session);
		
		if(client.isInRole("ROLE_ADMIN")) {
			return PAGE_ROLE_EDIT;
		}else {
			return PAGE_ROLE_WORKER_EDIT;
		}
	} 
	
	public String editRoleUser(Roleuser roleuser){
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(true);
		session.setAttribute("roleuser", roleuser);
		
		RemoteClient client = RemoteClient.load(session);
		
		if(client.isInRole("ROLE_ADMIN")) {
			return PAGE_ROLE_EDIT;
		}else {
			return PAGE_ROLE_WORKER_EDIT;
		}
	}
	
	public String deleteRoleUser(Roleuser roleuser){
		roleUserDAO.remove(roleuser);
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(txtMain.getString("typeMessage"), txtMain.getString("deleteRecord")));
		return PAGE_STAY_AT_THE_SAME;
	}

}
