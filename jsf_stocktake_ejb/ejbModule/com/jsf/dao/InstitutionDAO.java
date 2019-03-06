package com.jsf.dao;

import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.faces.application.FacesMessage;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import com.jsf.entities.Institution;

@Stateless
public class InstitutionDAO {

	private final static String UNIT_NAME = "jsfcourse-simplePU";

	// Dependency injection (no setter method is needed)
	@PersistenceContext(unitName = UNIT_NAME)
	protected EntityManager em;
	
	public void create(Institution institution) {
		em.persist(institution);
	}

	public Institution merge(Institution institution) {
		return em.merge(institution);
	}

	public void remove(Institution institution) {
		em.remove(em.merge(institution));
	}
	
	public void archiveInstitution(Institution institution) {
		String select = "select i ";
        String from = "from Institution i ";
        String where = "where id like :id";
 
        Query query = em.createQuery(select + from + where);
 
        query.setParameter("id", institution.getIdInstitution());
       
        Institution edited = null;
        try {
            edited = (Institution) query.getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
       
        edited.setArchive(true);
       
        merge(edited);
	}
	
	public void unarchiveInstitution(Institution institution) {
		String select = "select i ";
        String from = "from Institution i ";
        String where = "where id like :id";
 
        Query query = em.createQuery(select + from + where);
 
        query.setParameter("id", institution.getIdInstitution());
       
        Institution edited = null;
        try {
            edited = (Institution) query.getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
       
        edited.setArchive(false);
       
        merge(edited);
	}

	public Institution find(Object id) {
		return em.find(Institution.class, id);
	}
	
	public List<Institution> getFullList() {
		List<Institution> list = null;

		Query query = em.createQuery("select i from Institution i");

		try {
			list = query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}
	
	public List<Institution> getList(Map<String, Object> searchParams) {
		List<Institution> list = null;

		// 1. Build query string with parameters
		String select = "select i ";
		String from = "from Institution i ";
		String where = "";
		String orderby = "order by i.nameInstitution asc, i.nameInstitution";

		// search for institution
		String nameInstitution = (String) searchParams.get("nameInstitution");
		if (nameInstitution != null) {
			if (where.isEmpty()) {
				where = "where ";
			} else {
				where += "and ";
			}
			where += "i.nameInstitution like :institution ";
		}
		
		// ... other parameters ... 

		// 2. Create query object
		Query query = em.createQuery(select + from + where + orderby);

		// 3. Set configured parameters
		if (nameInstitution != null) {
			query.setParameter("institution", nameInstitution+"%");
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
	public int getRowsCount(String nameInstitution, boolean archive) {
		
		Query query = null;
		
		String select = "select COUNT(*) ";
		String from = "from Institution i ";
		String where = "where i.archive like :archive ";
		String orderby = "order by i.nameInstitution asc, i.nameInstitution";

		if (nameInstitution != null) {
			if (where.isEmpty()) {
				where = "where ";
			} else {
				where += "and ";
			}
			where += "i.nameInstitution like :institution ";
		}
		
		query = em.createQuery(select + from + where + orderby);
		
		query.setParameter("archive", archive);
		if (nameInstitution != null) {
			query.setParameter("institution", nameInstitution+"%");
		}

        return ((Long) query.getSingleResult()).intValue();
	}
	
	public List<Institution> retrieveInstitutions(int first, int pageSize, String nameInstitution, boolean archive) {
		
		TypedQuery<Institution> query = null;
		
		String select = "select i ";
		String from = "from Institution i ";
		String where = "where i.archive like :archive ";
		String orderby = "order by i.nameInstitution asc, i.nameInstitution";

		if (nameInstitution != null) {
			if (where.isEmpty()) {
				where = "where ";
			} else {
				where += "and ";
			}
			where += "i.nameInstitution like :institution ";
		}
		
		query = em.createQuery((select + from + where + orderby), Institution.class)
				.setFirstResult(first)
				.setMaxResults(pageSize);
		
		query.setParameter("archive", archive);
		if(nameInstitution != null) {
			query.setParameter("institution", nameInstitution+"%");
		}
		
		return query.getResultList();
	}

}
