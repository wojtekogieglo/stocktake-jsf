package com.jsf.group;

import java.io.Serializable;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import com.jsf.dao.GroupDAO;
import com.jsf.entities.Groups;

@ManagedBean
@ViewScoped
public class GroupEditBB implements Serializable{

	private static final long serialVersionUID = 1L;

	private static final String PAGE_GROUP_LIST = "/pages/user/group/groupList?faces-redirect=true";
	private static final String PAGE_STAY_AT_THE_SAME = null;

	@EJB
	GroupDAO groupDAO;
	
	private ResourceBundle txtMain;
	private String category;
	private String description;
	
	public String getCategory() {
		return category;
	}
	
	public void setCategory(String category) {
		this.category = category;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	private Groups group = null;
	
	@PostConstruct
	public void postConstruct() {
		FacesContext context = FacesContext.getCurrentInstance();
		txtMain = ResourceBundle.getBundle("resources.textMain", context.getViewRoot().getLocale());
		
		// A. load person if passed through session
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(true);
		group = (Groups) session.getAttribute("group");

		if (group != null) {
			session.removeAttribute("group");
		}

		// if loaded record is to be edited then copy data to properties
		if (group != null && group.getIdGroup() != null) {
			setCategory(group.getCategory());
			setDescription(group.getDescription());
		}
	}
	
	private boolean validate() {
		FacesContext ctx = FacesContext.getCurrentInstance();
		boolean result = false;

		if (category == null || category.trim().length() == 0) {
			ctx.addMessage(null, new FacesMessage("Wpisz kategorie"));
		}

		if (ctx.getMessageList().isEmpty()) {
			group.setCategory(category.trim());
			group.setDescription(description.trim());
			result = true;
		}

		return result;
	}
	
	public String saveData() {
		FacesContext context = FacesContext.getCurrentInstance();

		// no Person object passed
		if (group == null) {
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, 
					txtMain.getString("typeMessageError"), txtMain.getString("errorSystem")));
			return PAGE_STAY_AT_THE_SAME;
		}

		if (!validate()) {
			return PAGE_STAY_AT_THE_SAME;
		}

		try {
			if (group.getIdGroup() == null) {
				groupDAO.create(group);
				context.addMessage(null, new FacesMessage(txtMain.getString("typeMessage"), txtMain.getString("addRecord")));
				context.getExternalContext().getFlash().setKeepMessages(true);
			} else {
				groupDAO.merge(group);
				context.addMessage(null, new FacesMessage(txtMain.getString("typeMessage"), txtMain.getString("editRecord")));
				context.getExternalContext().getFlash().setKeepMessages(true);
			}
		} catch (Exception e) {
			e.printStackTrace();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, 
					txtMain.getString("typeMessageError"), txtMain.getString("errorSave")));
			return PAGE_STAY_AT_THE_SAME;
		}

		return PAGE_GROUP_LIST;
	}
	
}
