package com.jsf.dao;

import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import com.jsf.entities.Department;
import com.jsf.entities.Institution;

@Stateless
public class DepartmentDAO {
	
	private final static String UNIT_NAME = "jsfcourse-simplePU";

	// Dependency injection (no setter method is needed)
	@PersistenceContext(unitName = UNIT_NAME)
	protected EntityManager em;
	
	public void create(Department department) {
		em.persist(department);
	}

	public Department merge(Department department) {
		return em.merge(department);
	}

	public void remove(Department department) {
		em.remove(em.merge(department));
	}
	
	public Department find(Object id) {
		return em.find(Department.class, id);
	}
	
	public void archiveDepartment(Department department) {
		String select = "select d ";
        String from = "from Department d ";
        String where = "where id like :id";
 
        Query query = em.createQuery(select + from + where);
 
        query.setParameter("id", department.getIdDepartment());
       
        Department edited = null;
        try {
            edited = (Department) query.getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
       
        edited.setArchive(true);
       
        merge(edited);
	}
	
	public void unarchiveDepartment(Department department) {
		String select = "select d ";
        String from = "from Department d ";
        String where = "where id like :id";
 
        Query query = em.createQuery(select + from + where);
 
        query.setParameter("id", department.getIdDepartment());
       
        Department edited = null;
        try {
            edited = (Department) query.getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
       
        edited.setArchive(false);
       
        merge(edited);
	}
	
	public List<Department> getFullList(Institution institution) {
		List<Department> list = null;

		Query query = em.createQuery("select d from Department d where d.institution like :institution");

		query.setParameter("institution", institution);

		try {
			list = query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}
	
	public List<Department> getList(Map<String, Object> searchParams) {
		List<Department> list = null;

		// 1. Build query string with parameters
		String select = "select d ";
		String from = "from Department d ";
		String where = "";
		String orderby = "order by d.nameDepartment asc, d.nameDepartment";

		// search for institution
		String nameDepartment = (String) searchParams.get("nameDepartment");
		if (nameDepartment != null) {
			if (where.isEmpty()) {
				where = "where ";
			} else {
				where += "and ";
			}
			where += "d.nameDepartment like :department ";
		}
		
		// ... other parameters ... 

		// 2. Create query object
		Query query = em.createQuery(select + from + where + orderby);

		// 3. Set configured parameters
		if (nameDepartment != null) {
			query.setParameter("department", nameDepartment+"%");
		}

		// ... other parameters ... 

		// 4. Execute query and retrieve list of Person objects
		try {
			list = query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}
	
	//Lazy Loading support
	public int getRowsCount(Institution institution, String departmentName, boolean archive) {

		Query query = null;
		
		String select = "select COUNT(*) ";
		String from = "from Department d ";
		String where = "where d.archive like :archive and d.institution like :institution ";
		String orderby = "order by d.nameDepartment asc, d.nameDepartment";

		if (departmentName != null) {
			if (where.isEmpty()) {
				where = "where ";
			} else {
				where += "and ";
			}
			where += "d.nameDepartment like :department ";
		}
		
		query = em.createQuery(select + from + where + orderby);

		if (departmentName != null) {
			query.setParameter("department", departmentName + "%");
		}
		
		query.setParameter("archive", archive);
		query.setParameter("institution", institution);

		return ((Long) query.getSingleResult()).intValue();
	}

	public List<Department> retrieveDepartments(int first, int pageSize, Institution institution, String departmentName, boolean archive) {

		TypedQuery<Department> query = null;
		
		String select = "select d ";
		String from = "from Department d ";
		String where = "where d.archive like :archive and d.institution like :institution ";
		String orderby = "order by d.nameDepartment asc, d.nameDepartment";
		
		if (departmentName != null) {
			if (where.isEmpty()) {
				where = "where ";
			} else {
				where += "and ";
			}
			where += "d.nameDepartment like :department ";
		}
		
		query = em.createQuery(select + from + where + orderby, Department.class)
				.setFirstResult(first)
				.setMaxResults(pageSize);

		if (departmentName != null) {
			query.setParameter("department", departmentName + "%");
		}
		query.setParameter("archive", archive);
		query.setParameter("institution", institution);
		
		return query.getResultList();
	}	

}
