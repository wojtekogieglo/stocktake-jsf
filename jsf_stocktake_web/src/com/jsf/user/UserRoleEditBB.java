package com.jsf.user;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
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

import com.jsf.entities.User;
import com.jsf.entities.Institution;
import com.jsf.entities.Role;
import com.jsf.entities.Roleuser;
import com.jsf.dao.UserDAO;
import com.jsf.dao.InstitutionDAO;
import com.jsf.dao.RoleDAO;
import com.jsf.dao.RoleUserDAO;
import com.jsf.entities.RoleuserPK;

@ManagedBean
@ViewScoped
public class UserRoleEditBB implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private static final String PAGE_ROLE_LIST = "/pages/admin/users/userDetails?faces-redirect=true";
	private static final String PAGE_WORKER_LIST = "/pages/institution_admin/user/userDetails?faces-redirect=true";
	private static final String PAGE_STAY_AT_THE_SAME = null;
	
	@EJB
	UserDAO userDAO;
	@EJB
	RoleDAO roleDAO;
	@EJB
	RoleUserDAO roleuserDAO;
	
	private ResourceBundle txtMain;
	private Integer idRole;
	private Date grantDate;
	private Date expirationDate;
	private List<Role> roleNames;
	
	public Integer getIdRole() {
		return idRole;
	}
	
	public void setIdRole(Integer idRole) {
		this.idRole = idRole;
	}
	
	public Date getGrantDate() {
		return grantDate;
	}
	
	public void setGrantDate(Date grantDate) {
		this.grantDate = grantDate;
	}
	
	public Date getExpirationDate() {
		return expirationDate;
	}
	
	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	public List<Role> getRoleNames() {
		return roleNames;
	}

	public void setRoleNames(List<Role> roleNames) {
		this.roleNames = roleNames;
	}
	
	private User user = null;
	private Roleuser roleuser = null;
	
	
	@PostConstruct
	public void postConstruct() {
		FacesContext context = FacesContext.getCurrentInstance();
		txtMain = ResourceBundle.getBundle("resources.textMain", context.getViewRoot().getLocale());
		// A. load person if passed through session
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(true);
		user = (User) session.getAttribute("user");
		roleuser = (Roleuser) session.getAttribute("roleuser");
		roleNames = roleDAO.getFullList();
		
		// cleaning: attribute received => delete it from session
		if (roleuser != null) {
			session.removeAttribute("roleuser");
		}
		
		RemoteClient client = RemoteClient.load(session);
		
		if(client.isInstitutionAdmin() && !client.isSuperAdmin()) {
			for(Role role : roleNames) {
				if (role.getRoleName().equals("ROLE_USER")) {
					setIdRole(role.getIdRole());
				}
			}
		}
		
		if(roleuser != null && roleuser.getId() != null) {
			setIdRole(roleuser.getRole().getIdRole());
			setGrantDate(roleuser.getGrantDate());
			setExpirationDate(roleuser.getExpirationDate());
		}
	}
	
	private boolean validate() {
		FacesContext ctx = FacesContext.getCurrentInstance();
		boolean result = false;

		if (grantDate == null) {
			ctx.addMessage(null, new FacesMessage("Wpisz datê przydzielenia"));
		}

		if (ctx.getMessageList().isEmpty()) {
			roleuser.setRole(roleDAO.find(idRole));
			roleuser.setUser(user);
			roleuser.setGrantDate(grantDate);
			
			if(expirationDate != null) {
				roleuser.setExpirationDate(expirationDate);
			}else {
				roleuser.setExpirationDate(null);
			}
			
			result = true;
		}

		return result;
	}
	
	public String saveData() {
		FacesContext context = FacesContext.getCurrentInstance();

		// no Person object passed
		if (roleuser == null) {
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, 
					txtMain.getString("typeMessageError"), txtMain.getString("errorSystem")));
			return PAGE_STAY_AT_THE_SAME;
		}

		if (!validate()) {
			return PAGE_STAY_AT_THE_SAME;
		}

		try {
			if (roleuser.getId() == null) {
				RoleuserPK ruPK = new RoleuserPK();
				ruPK.setRole_idRole(idRole);
				ruPK.setUser_idUser(user.getIdUser());
				
				roleuser.setId(ruPK);
				
				roleuserDAO.create(roleuser);
				context.addMessage(null, new FacesMessage(txtMain.getString("typeMessage"), txtMain.getString("addRecord")));
				context.getExternalContext().getFlash().setKeepMessages(true);
			} else {
				// existing record
				roleuserDAO.merge(roleuser);
				context.addMessage(null, new FacesMessage(txtMain.getString("typeMessage"), txtMain.getString("editRecord")));
				context.getExternalContext().getFlash().setKeepMessages(true);
			}
		} catch (Exception e) {
			e.printStackTrace();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, 
					txtMain.getString("typeMessageError"), txtMain.getString("errorSave")));
			return PAGE_STAY_AT_THE_SAME;
		}

		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(true);
		session.setAttribute("user", roleuser.getUser());
		
		RemoteClient client = RemoteClient.load(session);
		
		if(client.isInRole("ROLE_ADMIN")) {
			return PAGE_ROLE_LIST;
		}else {
			return PAGE_WORKER_LIST;
		}
	}
	
	

}
