package com.jsf.role;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.jsf.entities.Role;
import com.jsf.dao.RoleDAO;

@ManagedBean
@ViewScoped
public class RoleEditBB implements Serializable{
	private static final long serialVersionUID = 1L;

	private static final String PAGE_ROLE_LIST = "/pages/admin/roles/roleList?faces-redirect=true";
	private static final String PAGE_STAY_AT_THE_SAME = null;
	
	@EJB
	RoleDAO roleDAO;
	
	private ResourceBundle txtMain;
	private String idRole;
	private String roleName;
	private String roleDescription;
	
	public String getIdRole() {
		return idRole;
	}

	public void setIdRole(String idRole) {
		this.idRole = idRole;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getRoleDescription() {
		return roleDescription;
	}

	public void setRoleDescription(String roleDescription) {
		this.roleDescription = roleDescription;
	}

	private Role role = null;
	
	@PostConstruct
	public void postConstruct() {
		FacesContext context = FacesContext.getCurrentInstance();
		txtMain = ResourceBundle.getBundle("resources.textMain", context.getViewRoot().getLocale());
		
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(true);
		role = (Role) session.getAttribute("role");

		if (role != null) {
			session.removeAttribute("role");
		}

		if (role != null && role.getIdRole() != null) {
			setRoleName(role.getRoleName());
			setRoleDescription(role.getDescription());
		}
	}
	
	private boolean validate() {
		FacesContext ctx = FacesContext.getCurrentInstance();
		boolean result = false;

		if (roleName == null || roleName.trim().length() == 0) {
			ctx.addMessage(null, new FacesMessage("Wpisz nazwe roli"));
		}
		
		if (roleDescription == null || roleDescription.trim().length() == 0) {
			ctx.addMessage(null, new FacesMessage("Wpisz opis roli"));
		}

		if (ctx.getMessageList().isEmpty()) {
			role.setRoleName(roleName.trim());
			role.setDescription(roleDescription.trim());
			result = true;
		}

		return result;
	}
	
	public String saveData() {
		FacesContext context = FacesContext.getCurrentInstance();

		// no Person object passed
		if (role == null) {
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, 
					txtMain.getString("typeMessageError"), txtMain.getString("errorSystem")));
			return PAGE_STAY_AT_THE_SAME;
		}

		if (!validate()) {
			return PAGE_STAY_AT_THE_SAME;
		}

		try {
			if (role.getIdRole() == null) {
				// new record
				roleDAO.create(role);
				context.addMessage(null, new FacesMessage(txtMain.getString("typeMessage"), txtMain.getString("addRecord")));
				context.getExternalContext().getFlash().setKeepMessages(true);
			} else {
				// existing record
				roleDAO.merge(role);
				context.addMessage(null, new FacesMessage(txtMain.getString("typeMessage"), txtMain.getString("editRecord")));
				context.getExternalContext().getFlash().setKeepMessages(true);
			}
		} catch (Exception e) {
			e.printStackTrace();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, 
					txtMain.getString("typeMessageError"), txtMain.getString("errorSave")));
			return PAGE_STAY_AT_THE_SAME;
		}

		return PAGE_ROLE_LIST;
	}
	

}
