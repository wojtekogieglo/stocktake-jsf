package com.jsf.institution;

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
import javax.servlet.http.HttpSession;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import com.jsf.entities.Institution;
import com.jsf.dao.InstitutionDAO;

@ManagedBean
@ViewScoped
public class InstitutionListBB implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private static final String PAGE_INSTITUTION_EDIT = "/pages/admin/institution/institutionEdit?faces-redirect=true";
	private static final String PAGE_STAY_AT_THE_SAME = null;
	
	private String nameInstitution;
	private boolean archive;
	private ResourceBundle txtMain;

	public String getNameInstitution() {
		return nameInstitution;
	}

	public void setNameInstitution(String nameInstitution) {
		this.nameInstitution = nameInstitution;
	}
	
	public boolean isArchive() {
		return archive;
	}

	public void setArchive(boolean archive) {
		this.archive = archive;
	}


	@EJB
	InstitutionDAO institutionDAO;
	
	private LazyDataModel<Institution> institution;
	
	@PostConstruct
	public void initialization() {
		FacesContext context = FacesContext.getCurrentInstance();
		txtMain = ResourceBundle.getBundle("resources.textMain", context.getViewRoot().getLocale());
		
        institution = new LazyDataModel<Institution>() {
            private static final long serialVersionUID = 1L;
           
            @Override
            public List<Institution> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
                setRowCount(institutionDAO.getRowsCount(nameInstitution, archive));
               
                return institutionDAO.retrieveInstitutions(first, pageSize, nameInstitution, archive);
            }
        };
    }
	
	public LazyDataModel<Institution> getInstitution(){
		return institution;
	}
		
	public List<Institution> getFullList(){
		return institutionDAO.getFullList();
	}
	
	public List<Institution> getList(){
		List<Institution> list = null;
		
		//1. Prepare search params
		Map<String,Object> searchParams = new HashMap<String, Object>();
		
		if (nameInstitution != null && nameInstitution.length() > 0){
			searchParams.put("nameInstitution", nameInstitution);
		}
		
		//2. Get list
		list = institutionDAO.getList(searchParams);
		
		return list;
	}
	
	public String newInstitution(){
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(true);
		Institution institution = new Institution();
		session.setAttribute("institution", institution);
		return PAGE_INSTITUTION_EDIT;
	}
	
	public String editInstitution(Institution institution){
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(true);
		session.setAttribute("institution", institution);
		return PAGE_INSTITUTION_EDIT;
	}
	
	public String displayUnarchive() {
		setArchive(false);
		return PAGE_STAY_AT_THE_SAME;
	}
	
	public String displayArchive() {
		setArchive(true);
		return PAGE_STAY_AT_THE_SAME;
	}

	public String archiveInstitution(Institution institution){
		institutionDAO.archiveInstitution(institution);
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, 
				txtMain.getString("typeMessage"), txtMain.getString("archiveRecord")));
		return PAGE_STAY_AT_THE_SAME;
	}
	
	public String unarchiveInstitution(Institution institution){
		institutionDAO.unarchiveInstitution(institution);
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, 
				txtMain.getString("typeMessage"), txtMain.getString("unarchiveRecord")));
		return PAGE_STAY_AT_THE_SAME;
	}
}
