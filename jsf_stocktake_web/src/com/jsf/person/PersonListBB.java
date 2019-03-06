package com.jsf.person;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.simplesecurity.RemoteClient;
import javax.servlet.http.HttpSession;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import com.jsf.dao.DepartmentDAO;
import com.jsf.dao.PersonResponsibleDAO;
import com.jsf.entities.Department;
import com.jsf.entities.Institution;
import com.jsf.entities.Personresponsible;
import com.jsf.entities.Room;
import com.jsf.entities.User;

@ManagedBean
@ViewScoped
public class PersonListBB implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private static final String PAGE_PERSON_EDIT = "/pages/institution_admin/person/personEdit?faces-redirect=true";
	private static final String PAGE_PERSON_DETAILS = "/pages/institution_admin/person/personDetails?faces-redirect=true";
	private static final String PAGE_STAY_AT_THE_SAME = null;

	private String surname;
	private boolean archive;
	private Institution institution;
	private ResourceBundle txtMain;
	
	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public boolean isArchive() {
		return archive;
	}

	public void setArchive(boolean archive) {
		this.archive = archive;
	}

	public Institution getInstitution() {
		return institution;
	}

	public void setInstitution(Institution institution) {
		this.institution = institution;
	}

	@EJB
	PersonResponsibleDAO personResponsibleDAO;
	@EJB
	DepartmentDAO departmentDAO;
	
	private LazyDataModel<Personresponsible> personResponsible;
	private List<Department> listDepartment;
	
	@PostConstruct
	public void initialization() {
		FacesContext context = FacesContext.getCurrentInstance();
		txtMain = ResourceBundle.getBundle("resources.textMain", context.getViewRoot().getLocale());
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(true);
		RemoteClient<User> client = RemoteClient.load(session);
		institution = client.getDetails().getInstitution();
		listDepartment = departmentDAO.getFullList(institution);
		
        personResponsible = new LazyDataModel<Personresponsible>() {
            private static final long serialVersionUID = 1L;
           
            @Override
            public List<Personresponsible> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
                setRowCount(personResponsibleDAO.getRowsCount(listDepartment, surname, archive));
               
                return personResponsibleDAO.retrievePerson(first, pageSize, listDepartment, surname, archive);
            }
        };
    }
	
	public LazyDataModel<Personresponsible> getPersonResponsible(){
		return personResponsible;
	}
	
	public String displayUnarchive() {
		setArchive(false);
		return PAGE_STAY_AT_THE_SAME;
	}
	
	public String displayArchive() {
		setArchive(true);
		return PAGE_STAY_AT_THE_SAME;
	}
	
	public String newPerson(){
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(true);
		Personresponsible personResponsible = new Personresponsible();
		session.setAttribute("person", personResponsible);
		session.setAttribute("institution", institution);
		return PAGE_PERSON_EDIT;
	}
	
	public String editPerson(Personresponsible personResponsible){
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(true);
		session.setAttribute("person", personResponsible);
		session.setAttribute("institution", institution);
		return PAGE_PERSON_EDIT;
	}
	
	public String detailsPerson(Personresponsible personResponsible){
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(true);
		session.setAttribute("personResponsible", personResponsible);
		return PAGE_PERSON_DETAILS;
	}
	
	public String archivePersonResponsible(Personresponsible personResponsible){
		personResponsibleDAO.archivePerson(personResponsible);
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, 
				txtMain.getString("typeMessage"), txtMain.getString("archiveRecord")));
		return PAGE_STAY_AT_THE_SAME;
	}
	
	public String unarchivePersonResponsible(Personresponsible personResponsible){
		personResponsibleDAO.unarchivePerson(personResponsible);
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, 
				txtMain.getString("typeMessage"), txtMain.getString("unarchiveRecord")));
		return PAGE_STAY_AT_THE_SAME;
	}
}
