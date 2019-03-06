package com.jsf.dao;

import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.jsf.entities.Role;

@Stateless
public class RoleDAO {

	private final static String UNIT_NAME = "jsfcourse-simplePU";

	// Dependency injection (no setter method is needed)
	@PersistenceContext(unitName = UNIT_NAME)
	protected EntityManager em;
	
	public void create(Role role) {
		em.persist(role);
	}

	public Role merge(Role role) {
		return em.merge(role);
	}

	public void remove(Role role) {
		em.remove(em.merge(role));
	}

	public Role find(Object id) {
		return em.find(Role.class, id);
	}
	
	public List<Role> getFullList() {
		List<Role> list = null;

		Query query = em.createQuery("select r from Role r");

		try {
			list = query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}
	
	public List<Role> getList(Map<String, Object> searchParams) {
		List<Role> list = null;

		// 1. Build query string with parameters
		String select = "select r ";
		String from = "from Role r ";
		String where = "";
		String orderby = "order by r.roleName asc, r.roleName";

		// search for institution
		String roleName = (String) searchParams.get("roleName");
		if (roleName != null) {
			if (where.isEmpty()) {
				where = "where ";
			} else {
				where += "and ";
			}
			where += "r.roleName like :roleName ";
		}
		
		// ... other parameters ... 

		// 2. Create query object
		Query query = em.createQuery(select + from + where + orderby);

		// 3. Set configured parameters
		if (roleName != null) {
			query.setParameter("roleName", roleName+"%");
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

}
