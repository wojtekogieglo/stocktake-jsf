package com.jsf.worker;

import java.util.ArrayList;
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

import com.jsf.entities.Institution;
import com.jsf.entities.Roleuser;
import com.jsf.entities.User;
import com.jsf.dao.UserDAO;

@ManagedBean
public class WorkerListBB {
	private static final String PAGE_WORKER_EDIT = "/pages/institution_admin/user/userEdit?faces-redirect=true";
	private static final String PAGE_STAY_AT_THE_SAME = null;
	
	private String surname;
	private ResourceBundle txtMain;

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}
	
	@EJB
	UserDAO userDAO;
	
	@PostConstruct
	public void initialization() {
		FacesContext context = FacesContext.getCurrentInstance();
		txtMain = ResourceBundle.getBundle("resources.textMain", context.getViewRoot().getLocale());
	}
	
	public List<User> getList(Integer idUser, Institution institution){
		List<User> list = null;
		
		Map<String,Object> searchParams = new HashMap<String, Object>();
		
		if (surname != null && surname.length() > 0){
			searchParams.put("surname", surname);
		}
		searchParams.put("institution", institution);
		
		//2. Get list
		list = userDAO.getList(searchParams);
		
		List<User> userList = new ArrayList<User>();
		for(User user : list) {
			if(user.getIdUser() != idUser ) {
				userList.add(user);
			}
		}
		
		return userList;
	}
	
	public String newUser(){
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(true);
		User user = new User();
		session.setAttribute("user", user);
		return PAGE_WORKER_EDIT;
	}
	
	public String editUser(User user){
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(true);
		session.setAttribute("user", user);
		return PAGE_WORKER_EDIT;
	}
	
	public String roleUser(User user){
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(true);
		session.setAttribute("user", user);
		return PAGE_WORKER_EDIT;
	}

	public String inactivateUser(User user){
		userDAO.inactivateUser(user);
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, 
				txtMain.getString("typeMessage"), txtMain.getString("archiveRecord")));
		return PAGE_STAY_AT_THE_SAME;
	}
	
	public String activateUser(User user){
		userDAO.activateUser(user);
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, 
				txtMain.getString("typeMessage"), txtMain.getString("unarchiveRecord")));
		return PAGE_STAY_AT_THE_SAME;
	}
}
