package com.jsf.worker;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import com.jsf.dao.InstitutionDAO;
import com.jsf.dao.RoleUserDAO;
import com.jsf.dao.UserDAO;
import com.jsf.entities.Institution;
import com.jsf.entities.Roleuser;
import com.jsf.entities.User;

@ManagedBean
@ViewScoped
public class WorkerEditBB implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private static final String PAGE_WORKER_LIST = "/pages/institution_admin/user/userList?faces-redirect=true";
	private static final String PAGE_STAY_AT_THE_SAME = null;
	
	@EJB
	UserDAO userDAO;
	@EJB
	InstitutionDAO institutionDAO;
	
	private ResourceBundle txtMain;
	private String name;
	private String surname;
	private String email;
	private String idInstitution;
	
	private boolean isEdited = false;
	
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
	
	public String getIdInstitution() {
		return idInstitution;
	}

	public void setIdInstitution(String idInstitution) {
		this.idInstitution = idInstitution;
	}



	User user = null;
	
	@PostConstruct
	public void postConstruct() {
		FacesContext context = FacesContext.getCurrentInstance();
		txtMain = ResourceBundle.getBundle("resources.textMain", context.getViewRoot().getLocale());
		// A. load person if passed through session
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(true);
		user = (User) session.getAttribute("user");
		
		if (user != null) {
			session.removeAttribute("user");
		}
		
		if(user != null && user.getIdUser() != null) {
			setName(user.getName());
			setSurname(user.getSurname());
			setEmail(user.getEmail());
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
				Map<String,String> params = context.getExternalContext().getRequestParameterMap();
				idInstitution = params.get("idInstitution");
				Institution institution = institutionDAO.find(Integer.parseInt(idInstitution));
				
				user.setInstitution(institution);
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

		return PAGE_WORKER_LIST;
	}
	
	
	
	
	

}
