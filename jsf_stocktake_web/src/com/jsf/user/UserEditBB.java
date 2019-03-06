package com.jsf.user;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.jsf.entities.User;
import com.jsf.entities.Institution;
import com.jsf.entities.Role;
import com.jsf.entities.Roleuser;
import com.jsf.dao.UserDAO;
import com.jsf.dao.InstitutionDAO;
import com.jsf.dao.RoleDAO;
import com.jsf.dao.RoleUserDAO;

@ManagedBean
@ViewScoped
public class UserEditBB implements Serializable{
	private static final long serialVersionUID = 1L;

	private static final String PAGE_USER_LIST = "/pages/admin/users/userList?faces-redirect=true";
	private static final String PAGE_STAY_AT_THE_SAME = null;
	
	@EJB
	UserDAO userDAO;
	@EJB
	InstitutionDAO institutionDAO;
	@EJB
	RoleDAO roleDAO;
	
	private ResourceBundle txtMain;
	private String idUser;
	private String name;
	private String surname;
	private String email;
	private Boolean active;
	private Integer idInstitution;
	private List<Institution> institutionNames;

	private boolean isEdited = false;


	public String getIdUser() {
		return idUser;
	}

	public void setIdUser(String idUser) {
		this.idUser = idUser;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}
	
	public List<Institution> getInstitutionNames() {
		return institutionNames;
	}

	public void setInstitutionNames(List<Institution> institutionNames) {
		this.institutionNames = institutionNames;
	}
	
	public Integer getIdInstitution() {
		return idInstitution;
	}

	public void setIdInstitution(Integer idInstitution) {
		this.idInstitution = idInstitution;
	}
	
	private User user = null;
	
	@PostConstruct
	public void postConstruct() {
		FacesContext context = FacesContext.getCurrentInstance();
		txtMain = ResourceBundle.getBundle("resources.textMain", context.getViewRoot().getLocale());
		// A. load person if passed through session
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(true);
		user = (User) session.getAttribute("user");
		institutionNames = institutionDAO.getFullList();
		
		// cleaning: attribute received => delete it from session
		if (user != null) {
			session.removeAttribute("user");
		}

		// if loaded record is to be edited then copy data to properties
		if (user != null && user.getIdUser() != null) {
			setName(user.getName());
			setSurname(user.getSurname());
			setEmail(user.getEmail());
			setIdInstitution(user.getInstitution().getIdInstitution());
			isEdited = true;
		}	
	}
	
	private boolean validate() {
		FacesContext ctx = FacesContext.getCurrentInstance();
		boolean result = false;

		if (name == null || name.trim().length() == 0) {
			ctx.addMessage(null, new FacesMessage("Wpisz imiê"));
		}
		
		if (surname == null || surname.trim().length() == 0) {
			ctx.addMessage(null, new FacesMessage("Wpisz nazwisko"));
		}
		
		if (email == null || email.trim().length() == 0) {
			ctx.addMessage(null, new FacesMessage("Wpisz adres e-mail"));
		}
		
		if (!isEdited && !userDAO.isEmailUnique(email)) {
			ctx.addMessage(null, new FacesMessage("Podany adres e-mail jest ju¿ u¿ywany"));
		}
		
		if (ctx.getMessageList().isEmpty()) {
			user.setName(name);
			user.setSurname(surname);
			user.setEmail(email);
			user.setPassword("user");
			user.setInstitution(institutionDAO.find(idInstitution));
			result = true;
		}

		return result;
	}
	
	public String saveData() {
		FacesContext context = FacesContext.getCurrentInstance();

		if (user == null) {
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, 
					txtMain.getString("typeMessageError"), txtMain.getString("errorSystem")));
			return PAGE_STAY_AT_THE_SAME;
		}

		if (!validate()) {
			return PAGE_STAY_AT_THE_SAME;
		}

		try {
			if (user.getIdUser() == null) {
				userDAO.create(user);
				context.addMessage(null, new FacesMessage(txtMain.getString("typeMessage"), txtMain.getString("addRecord")));
				context.getExternalContext().getFlash().setKeepMessages(true);
			} else {
				userDAO.merge(user);
				context.addMessage(null, new FacesMessage(txtMain.getString("typeMessage"), txtMain.getString("editRecord")));
				context.getExternalContext().getFlash().setKeepMessages(true);
			}
		} catch (Exception e) {
			e.printStackTrace();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, 
					txtMain.getString("typeMessageError"), txtMain.getString("errorSave")));
			return PAGE_STAY_AT_THE_SAME;
		}

		return PAGE_USER_LIST;
	}

}
