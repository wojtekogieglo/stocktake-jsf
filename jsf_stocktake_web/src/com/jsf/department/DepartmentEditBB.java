package com.jsf.department;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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

import com.jsf.entities.Department;
import com.jsf.entities.Institution;
import com.jsf.dao.DepartmentDAO;
import com.jsf.dao.InstitutionDAO;

@ManagedBean
@ViewScoped
public class DepartmentEditBB implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private static final String PAGE_DEPARTMENT_LIST = "/pages/institution_admin/department/departmentList?faces-redirect=true";
	private static final String PAGE_STAY_AT_THE_SAME = null;
	
	@EJB
	DepartmentDAO departmentDAO;
	@EJB
	InstitutionDAO institutionDAO;
	
	private ResourceBundle txtMain;
	private String idDepartment;
	private String idInstitution;
	private String departmentName;
	private Boolean departmentArchive;
	
	public String getIdDepartment() {
		return idDepartment;
	}
	
	public void setIdDepartment(String idDepartment) {
		this.idDepartment = idDepartment;
	}
	
	public String getIdInstitution() {
		return idInstitution;
	}

	public void setIdInstitution(String idInstitution) {
		this.idInstitution = idInstitution;
	}

	public String getDepartmentName() {
		return departmentName;
	}
	
	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}
	
	public Boolean getDepartmentArchive() {
		return departmentArchive;
	}
	
	public void setDepartmentArchive(Boolean departmentArchive) {
		this.departmentArchive = departmentArchive;
	}
	
	private Department department = null;
	
	@PostConstruct
	public void postConstruct() {
		FacesContext context = FacesContext.getCurrentInstance();
		txtMain = ResourceBundle.getBundle("resources.textMain", context.getViewRoot().getLocale());
		
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(true);
		department = (Department) session.getAttribute("department");

		if (department != null) {
			session.removeAttribute("department");
		}

		if (department != null && department.getIdDepartment() != null) {
			setDepartmentName(department.getNameDepartment());
		}
	}

	private boolean validate() {
		FacesContext ctx = FacesContext.getCurrentInstance();
		boolean result = false;

		if (departmentName == null || departmentName.trim().length() == 0) {
			ctx.addMessage(null, new FacesMessage("Wpisz nazwe wydzia³u"));
		}

		if (ctx.getMessageList().isEmpty()) {
			department.setNameDepartment(departmentName.trim());
			result = true;
		}

		return result;
	}
	
	public String saveData() {
		FacesContext context = FacesContext.getCurrentInstance();

		// no Person object passed
		if (department == null) {
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, 
					txtMain.getString("typeMessageError"), txtMain.getString("errorSystem")));
			return PAGE_STAY_AT_THE_SAME;
		}

		if (!validate()) {
			return PAGE_STAY_AT_THE_SAME;
		}

		try {
			if (department.getIdDepartment() == null) {
				Map<String,String> params = context.getExternalContext().getRequestParameterMap();
				idInstitution = params.get("idInstitution");
				Institution institution = institutionDAO.find(Integer.parseInt(idInstitution));
				
				department.setInstitution(institution);
				departmentDAO.create(department);
				context.addMessage(null, new FacesMessage(txtMain.getString("typeMessage"), txtMain.getString("addRecord")));
				context.getExternalContext().getFlash().setKeepMessages(true);
			} else {
				// existing record
				departmentDAO.merge(department);
				context.addMessage(null, new FacesMessage(txtMain.getString("typeMessage"), txtMain.getString("editRecord")));
				context.getExternalContext().getFlash().setKeepMessages(true);
			}
		} catch (Exception e) {
			e.printStackTrace();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, 
					txtMain.getString("typeMessageError"), txtMain.getString("errorSave")));
			return PAGE_STAY_AT_THE_SAME;
		}

		return PAGE_DEPARTMENT_LIST;
	}
	
}
