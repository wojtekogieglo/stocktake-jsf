package com.jsf.dao;

import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import com.jsf.entities.Department;
import com.jsf.entities.Personresponsible;
import com.jsf.entities.Room;

@Stateless
public class PersonResponsibleDAO {

	private final static String UNIT_NAME = "jsfcourse-simplePU";

	// Dependency injection (no setter method is needed)
	@PersistenceContext(unitName = UNIT_NAME)
	protected EntityManager em;
	
	public void create(Personresponsible personResponsible) {
		em.persist(personResponsible);
	}

	public Personresponsible merge(Personresponsible personResponsible) {
		return em.merge(personResponsible);
	}

	public void remove(Personresponsible personResponsible) {
		em.remove(em.merge(personResponsible));
	}

	public Personresponsible find(Object id) {
		return em.find(Personresponsible.class, id);
	}
	
	public List<Personresponsible> getFullList(Department department) {
		List<Personresponsible> list = null;

		Query query = em.createQuery("select p from Personresponsible p where p.department like :department ");
		
		query.setParameter("department", department);

		try {
			list = query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}
	
	public List<Personresponsible> getList(Map<String, Object> searchParams) {
		List<Personresponsible> list = null;

		String select = "select p ";
		String from = "from Personresponsible p ";
		String where = "";
		String orderby = "order by p.surname asc, p.surname";

		String surname = (String) searchParams.get("surname");
		if (surname != null) {
			if (where.isEmpty()) {
				where = "where ";
			} else {
				where += "and ";
			}
			where += "p.surname like :surname ";
		}
		
		Query query = em.createQuery(select + from + where + orderby);

		if (surname != null) {
			query.setParameter("surname", surname+"%");
		}
		
		try {
			list = query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}
	
	public List<Personresponsible> getListByDepartment(Department department) {
		List<Personresponsible> list = null;

		String select = "select p ";
		String from = "from Personresponsible p ";
		String where = "where p.archive = false and p.department like :department ";
		String orderby = "order by p.surname asc, p.surname";
		
		Query query = em.createQuery(select + from + where + orderby);

		if (department != null) {
			query.setParameter("department", department);
		}
		
		try {
			list = query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}
	
	public void archivePerson(Personresponsible personResponsible) {
		String select = "select p ";
        String from = "from Personresponsible p ";
        String where = "where id like :id";
 
        Query query = em.createQuery(select + from + where);
 
        query.setParameter("id", personResponsible.getIdPersonResponsible());
       
        Personresponsible edited = null;
        try {
            edited = (Personresponsible) query.getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
       
        edited.setArchive(true);
       
        merge(edited);
	}
	
	public void unarchivePerson(Personresponsible personResponsible) {
		String select = "select p ";
		String from = "from Personresponsible p ";
		String where = "where id like :id";
		
		Query query = em.createQuery(select + from + where);
		
		query.setParameter("id", personResponsible.getIdPersonResponsible());
		
		Personresponsible edited = null;
		try {
			edited = (Personresponsible) query.getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		edited.setArchive(false);
		
		merge(edited);
	}
	
	
	//Lazy Loading support
	public int getRowsCountDepartment(Department department, String surname, boolean archive) {

		Query query = null;
		
		String select = "SELECT COUNT(*) ";
		String from = "FROM Personresponsible p ";
		String where = "where p.department like :department and p.archive like :archive ";
		String orderby = "order by p.surname asc, p.surname";
		
		if (surname != null) {
			if (where.isEmpty()) {
				where = "where ";
			} else {
				where += "and ";
			}
			where += "p.surname like :surname ";
		}
		
		query = em.createQuery(select + from + where + orderby);

		if (surname != null) {
			query.setParameter("surname", surname + "%");
		}
		
		query.setParameter("department", department);
		query.setParameter("archive", archive);

		return ((Long) query.getSingleResult()).intValue();
	}
	
	public List<Personresponsible> retrievePersonDepartment(int first, int pageSize, Department department, String surname, boolean archive) {

		TypedQuery<Personresponsible> query = null;
		
		String select = "select p ";
		String from = "from Personresponsible p ";
		String where = "where p.department like :department and p.archive like :archive ";
		String orderby = "order by p.surname asc, p.surname";
		
		if (surname != null) {
			if (where.isEmpty()) {
				where = "where ";
			} else {
				where += "and ";
			}
			where += "p.surname like :surname ";
		}
		
		query = em.createQuery(select + from + where + orderby, Personresponsible.class)
				.setFirstResult(first)
				.setMaxResults(pageSize);

		if (surname != null) {
			query.setParameter("surname", surname + "%");
		}
		
		query.setParameter("department", department);
		query.setParameter("archive", archive);
		
		return query.getResultList();
	}
	
	public int getRowsCount(List<Department> departmentList, String surname, boolean archive) {

		Query query = null;
		
		String select = "SELECT COUNT(*) ";
		String from = "FROM Personresponsible p ";
		String where = "where p.archive like :archive and p.department in (:departments) ";
		String orderby = "order by p.surname asc, p.surname";
		
		if (surname != null) {
			if (where.isEmpty()) {
				where = "where ";
			} else {
				where += "and ";
			}
			where += "p.surname like :surname ";
		}
		
		query = em.createQuery(select + from + where + orderby);

		if (surname != null) {
			query.setParameter("surname", surname + "%");
		}
		
		if (!departmentList.isEmpty()) {
			query.setParameter("departments", departmentList);
		}else {
			query.setParameter("departments", null);
		}
		
		query.setParameter("archive", archive);

		return ((Long) query.getSingleResult()).intValue();
	}

	public List<Personresponsible> retrievePerson(int first, int pageSize, List<Department> departmentList, String surname, boolean archive) {

		TypedQuery<Personresponsible> query = null;
		
		String select = "select p ";
		String from = "from Personresponsible p ";
		String where = "where p.archive like :archive and p.department in (:departments) ";
		String orderby = "order by p.surname asc, p.surname";
		
		if (surname != null) {
			if (where.isEmpty()) {
				where = "where ";
			} else {
				where += "and ";
			}
			where += "p.surname like :surname ";
		}
		
		query = em.createQuery(select + from + where + orderby, Personresponsible.class)
				.setFirstResult(first)
				.setMaxResults(pageSize);

		if (surname != null) {
			query.setParameter("surname", surname + "%");
		}
		
		if (!departmentList.isEmpty()) {
			query.setParameter("departments", departmentList);
		}else {
			query.setParameter("departments", null);
		}
		
		query.setParameter("archive", archive);
		
		return query.getResultList();
	}
}
