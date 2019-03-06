package com.jsf.department;

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

import com.jsf.entities.Department;
import com.jsf.entities.Institution;
import com.jsf.entities.Personresponsible;
import com.jsf.dao.DepartmentDAO;

@ManagedBean
@ViewScoped
public class DepartmentListBB implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private static final String PAGE_DEPARTMENT_EDIT = "/pages/institution_admin/department/departmentEdit?faces-redirect=true";
	private static final String PAGE_DEPARTMENT_DETAILS = "/pages/institution_admin/department/departmentDetails?faces-redirect=true";
	private static final String PAGE_STAY_AT_THE_SAME = null;
	
	private String nameDepartment;
	private boolean archive;
	private Institution institution;
	private ResourceBundle txtMain;

	public String getNameDepartment() {
		return nameDepartment;
	}

	public void setNameDepartment(String nameDepartment) {
		this.nameDepartment = nameDepartment;
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
	DepartmentDAO departmentDAO;
	
	private LazyDataModel<Department> departments;
	
	@PostConstruct
	public void initialization() {
		FacesContext context = FacesContext.getCurrentInstance();
		txtMain = ResourceBundle.getBundle("resources.textMain", context.getViewRoot().getLocale());
        departments = new LazyDataModel<Department>() {
            private static final long serialVersionUID = 1L;
           
            @Override
            public List<Department> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
                setRowCount(departmentDAO.getRowsCount(institution, nameDepartment, archive));
               
                return departmentDAO.retrieveDepartments(first, pageSize, institution, nameDepartment, archive);
            }
        };
    }
	
	public LazyDataModel<Department> getDepartments(Institution institution){
		setInstitution(institution);
		return departments;
	}
	
	public List<Department> getList(){
		List<Department> list = null;
		
		//1. Prepare search params
		Map<String,Object> searchParams = new HashMap<String, Object>();
		
		if (nameDepartment != null && nameDepartment.length() > 0){
			searchParams.put("nameDepartment", nameDepartment);
		}
		
		//2. Get list
		list = departmentDAO.getList(searchParams);
		
		return list;
	}
	
	public String newDepartment(){
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(true);
		Department department = new Department();
		session.setAttribute("department", department);
		return PAGE_DEPARTMENT_EDIT;
	}
	
	public String editDepartment(Department department){
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(true);
		session.setAttribute("department", department);
		return PAGE_DEPARTMENT_EDIT;
	}
	
	public String detailsDepartment(Department department){
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(true);
		session.setAttribute("department", department);
		return PAGE_DEPARTMENT_DETAILS;
	}
	
	public String displayUnarchive() {
		setArchive(false);
		return PAGE_STAY_AT_THE_SAME;
	}
	
	public String displayArchive() {
		setArchive(true);
		return PAGE_STAY_AT_THE_SAME;
	}

	public String archiveDepartment(Department department){
		departmentDAO.archiveDepartment(department);
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, 
				txtMain.getString("typeMessage"), txtMain.getString("archiveRecord")));
		return PAGE_STAY_AT_THE_SAME;
	}
	
	public String unarchiveDepartment(Department department){
		departmentDAO.unarchiveDepartment(department);
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, 
				txtMain.getString("typeMessage"), txtMain.getString("unarchiveRecord")));
		return PAGE_STAY_AT_THE_SAME;
	}

}
