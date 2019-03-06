package com.jsf.user;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.simplesecurity.RemoteClient;
import javax.servlet.http.HttpSession;

import com.jsf.entities.User;
import com.jsf.dao.UserDAO;

@ManagedBean
public class UserListBB {
	private static final String PAGE_USER_EDIT = "/pages/admin/users/userEdit?faces-redirect=true";
	private static final String PAGE_USER_DETAILS = "/pages/admin/users/userDetails?faces-redirect=true";
	private static final String PAGE_WORKER_DETAILS = "/pages/institution_admin/user/userDetails?faces-redirect=true";
	private static final String PAGE_STAY_AT_THE_SAME = null;
	
	private String surname;
	private ResourceBundle txtMain;

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}
	//Dependency injection
		// - no setter method needed in this case
	@EJB
	UserDAO userDAO;
	
	@PostConstruct
	public void initialization() {
		FacesContext context = FacesContext.getCurrentInstance();
		txtMain = ResourceBundle.getBundle("resources.textMain", context.getViewRoot().getLocale());
	}
		
	public List<User> getFullList(){
		return userDAO.getFullList();
	}
	
	public List<User> getList(){
		List<User> list = null;
		
		//1. Prepare search params
		Map<String,Object> searchParams = new HashMap<String, Object>();
		
		if (surname != null && surname.length() > 0){
			searchParams.put("surname", surname);
		}
		
		//2. Get list
		list = userDAO.getList(searchParams);
		
		return list;
	}
	
	public String newUser(){
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(true);
		User user = new User();
		session.setAttribute("user", user);
		return PAGE_USER_EDIT;
	}
	
	public String editUser(User user){
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(true);
		session.setAttribute("user", user);
		return PAGE_USER_EDIT;
	}
	
	public String detailsUser(User user){
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(true);
		session.setAttribute("user", user);
		
		RemoteClient client = RemoteClient.load(session);

		if(client.isInRole("ROLE_ADMIN")) {
			return PAGE_USER_DETAILS;
		}else {
			return PAGE_WORKER_DETAILS;
		}
	}
	
	public String inactivateUser(User user){
		userDAO.inactivateUser(user);
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, 
				txtMain.getString("typeMessage"), txtMain.getString("unarchiveRecord")));
		return PAGE_STAY_AT_THE_SAME;
	}
	
	public String activateUser(User user){
		userDAO.activateUser(user);
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, 
				txtMain.getString("typeMessage"), txtMain.getString("archiveRecord")));
		return PAGE_STAY_AT_THE_SAME;
	}
}
