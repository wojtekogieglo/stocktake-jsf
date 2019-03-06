package com.jsf.institution;

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

import com.jsf.entities.Institution;
import com.jsf.dao.InstitutionDAO;

@ManagedBean
@ViewScoped
public class InstitutionEditBB implements Serializable{
	private static final long serialVersionUID = 1L;

	private static final String PAGE_INSTITUTION_LIST = "/pages/admin/institution/institutionList?faces-redirect=true";
	private static final String PAGE_STAY_AT_THE_SAME = null;
	
	@EJB
	InstitutionDAO institutionDAO;
	
	
	private ResourceBundle txtMain;
	private String idInstitution;
	private String institutionName;
	private Boolean institutionArchive;

	public String getInstitutionName() {
		return institutionName;
	}

	public void setInstitutionName(String institutionName) {
		this.institutionName = institutionName;
	}
	
	public Boolean getInstitutionArchive() {
		return institutionArchive;
	}

	public void setInstitutionArchive(Boolean institutionArchive) {
		this.institutionArchive = institutionArchive;
	}
	
	private Institution institution = null;
	
	@PostConstruct
	public void postConstruct() {

		FacesContext context = FacesContext.getCurrentInstance();
		txtMain = ResourceBundle.getBundle("resources.textMain", context.getViewRoot().getLocale());
		// A. load person if passed through session
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(true);
		institution = (Institution) session.getAttribute("institution");

		// cleaning: attribute received => delete it from session
		if (institution != null) {
			session.removeAttribute("institution");
		}


		// if loaded record is to be edited then copy data to properties
		if (institution != null && institution.getIdInstitution() != null) {
			setInstitutionName(institution.getNameInstitution());
		}
	}
	
	private boolean validate() {
		FacesContext ctx = FacesContext.getCurrentInstance();
		boolean result = false;

		if (institutionName == null || institutionName.trim().length() == 0) {
			ctx.addMessage(null, new FacesMessage("Wpisz nazwe instytucji"));
		}

		if (ctx.getMessageList().isEmpty()) {
			institution.setNameInstitution(institutionName.trim());
			result = true;
		}

		return result;
	}
	
	public String saveData() {
		FacesContext context = FacesContext.getCurrentInstance();

		// no Person object passed
		if (institution == null) {
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, 
					txtMain.getString("typeMessageError"), txtMain.getString("errorSystem")));
			return PAGE_STAY_AT_THE_SAME;
		}

		if (!validate()) {
			return PAGE_STAY_AT_THE_SAME;
		}

		try {
			if (institution.getIdInstitution() == null) {
				// new record
				institution.setArchive(false);
				institutionDAO.create(institution);
				context.addMessage(null, new FacesMessage(txtMain.getString("typeMessage"), txtMain.getString("addRecord")));
				context.getExternalContext().getFlash().setKeepMessages(true);
			} else {
				// existing record
				institutionDAO.merge(institution);
				context.addMessage(null, new FacesMessage(txtMain.getString("typeMessage"), txtMain.getString("editRecord")));
				context.getExternalContext().getFlash().setKeepMessages(true);
			}
		} catch (Exception e) {
			e.printStackTrace();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, 
					txtMain.getString("typeMessageError"), txtMain.getString("errorSave")));
			return PAGE_STAY_AT_THE_SAME;
		}

		return PAGE_INSTITUTION_LIST;
	}
	

}
