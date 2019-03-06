package com.jsf.person;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.jsf.dao.DepartmentDAO;
import com.jsf.dao.PersonResponsibleDAO;
import com.jsf.entities.Department;
import com.jsf.entities.Institution;
import com.jsf.entities.Personresponsible;

@ManagedBean
@ViewScoped
public class PersonEditBB implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private static final String PAGE_PERSON_LIST = "/pages/institution_admin/person/personList?faces-redirect=true";
	private static final String PAGE_STAY_AT_THE_SAME = null;
	
	@EJB
	PersonResponsibleDAO personResponsibleDAO;
	@EJB
	DepartmentDAO departmentDAO;

	private ResourceBundle txtMain;
	private String name;
	private String surname;
	private String description;
	private Integer idDepartment;
	private List<Department> departmentNames;
	
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
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getIdDepartment() {
		return idDepartment;
	}
	
	public void setIdDepartment(Integer idDepartment) {
		this.idDepartment = idDepartment;
	}
	
	public List<Department> getDepartmentNames() {
		return departmentNames;
	}

	public void setDepartmentNames(List<Department> departmentNames) {
		this.departmentNames = departmentNames;
	}

	private Personresponsible personResponsible = null;
	private Institution institution = null;
	
	@PostConstruct
	public void postConstruct() {
		FacesContext context = FacesContext.getCurrentInstance();
		txtMain = ResourceBundle.getBundle("resources.textMain", context.getViewRoot().getLocale());
		// A. load person if passed through session
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(true);
		personResponsible = (Personresponsible) session.getAttribute("person");
		institution = (Institution) session.getAttribute("institution");
		departmentNames = departmentDAO.getFullList(institution);

		// cleaning: attribute received => delete it from session
		if (personResponsible != null) {
			session.removeAttribute("person");
		}

		if (personResponsible != null && personResponsible.getIdPersonResponsible() != null) {
			setName(personResponsible.getName());
			setSurname(personResponsible.getSurname());
			setDescription(personResponsible.getDescription());
			setIdDepartment(personResponsible.getDepartment().getIdDepartment());
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

		if (ctx.getMessageList().isEmpty()) {
			personResponsible.setName(name);
			personResponsible.setSurname(surname);
			personResponsible.setDescription(description);
			personResponsible.setDepartment(departmentDAO.find(idDepartment));
			result = true;
		}

		return result;
	}
	
	public String saveData() {
		FacesContext context = FacesContext.getCurrentInstance();

		if (personResponsible == null) {
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, 
					txtMain.getString("typeMessageError"), txtMain.getString("errorSystem")));
			return PAGE_STAY_AT_THE_SAME;
		}

		if (!validate()) {
			return PAGE_STAY_AT_THE_SAME;
		}

		try {
			if (personResponsible.getIdPersonResponsible() == null) {
				personResponsibleDAO.create(personResponsible);
				context.addMessage(null, new FacesMessage(txtMain.getString("typeMessage"), txtMain.getString("addRecord")));
				context.getExternalContext().getFlash().setKeepMessages(true);
			} else {
				personResponsibleDAO.merge(personResponsible);
				context.addMessage(null, new FacesMessage(txtMain.getString("typeMessage"), txtMain.getString("editRecord")));
				context.getExternalContext().getFlash().setKeepMessages(true);
			}
		} catch (Exception e) {
			e.printStackTrace();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, 
					txtMain.getString("typeMessageError"), txtMain.getString("errorSave")));
			return PAGE_STAY_AT_THE_SAME;
		}

		return PAGE_PERSON_LIST;
	}
	
}
