package com.jsf.dao;

import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import com.jsf.entities.Department;
import com.jsf.entities.Groups;
import com.jsf.entities.Invobject;

@Stateless
public class GroupDAO {
	
	private final static String UNIT_NAME = "jsfcourse-simplePU";

	// Dependency injection (no setter method is needed)
	@PersistenceContext(unitName = UNIT_NAME)
	protected EntityManager em;
	
	public void create(Groups group) {
		em.persist(group);
	}

	public Groups merge(Groups group) {
		return em.merge(group);
	}

	public void remove(Groups group) {
		em.remove(em.merge(group));
	}
	
	public Groups find(Object id) {
		return em.find(Groups.class, id);
	}
	
	public List<Groups> getFullList() {
		List<Groups> list = null;

		Query query = em.createQuery("select g from Groups g where g.archive = false ");

		try {
			list = query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}
	
	public List<Groups> getList(Map<String, Object> searchParams) {
		List<Groups> list = null;

		// 1. Build query string with parameters
		String select = "select g ";
		String from = "from Groups g ";
		String where = "";
		String orderby = "order by g.category asc, g.category";

		// search for institution
		String category = (String) searchParams.get("category");
		if (category != null) {
			if (where.isEmpty()) {
				where = "where ";
			} else {
				where += "and ";
			}
			where += "g.category like :category ";
		}
		
		Query query = em.createQuery(select + from + where + orderby);

		if (category != null) {
			query.setParameter("category", category+"%");
		}

		try {
			list = query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}
	
	public void archiveGroup(Groups group) {
		String select = "select g ";
        String from = "from Groups g ";
        String where = "where id like :id";
 
        Query query = em.createQuery(select + from + where);
 
        query.setParameter("id", group.getIdGroup());
       
        Groups edited = null;
        try {
            edited = (Groups) query.getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
       
        edited.setArchive(true);
       
        merge(edited);
	}
	
	public void unarchiveGroup(Groups group) {
		String select = "select g ";
		String from = "from Groups g ";
		String where = "where id like :id";
		
		Query query = em.createQuery(select + from + where);
		
		query.setParameter("id", group.getIdGroup());
		
		Groups edited = null;
		try {
			edited = (Groups) query.getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		edited.setArchive(false);
		
		merge(edited);
	}
	
	//Lazy Loading support
	public int getRowsCount(String category, boolean archive) {
		Query query = null;
		
		String select = "SELECT COUNT(*) ";
		String from = "FROM Groups g ";
		String where = "where g.archive like :archive ";
		String orderby = "order by g.category asc, g.category";

		if (category != null) {
			if (where.isEmpty()) {
				where = "where ";
			} else {
				where += "and ";
			}
			where += "g.category like :category ";
		}
		
		query = em.createQuery(select + from + where + orderby);
		
		if (category != null) {
			query.setParameter("category", category+"%");
		}
		query.setParameter("archive", archive);
		
        return ((Long) query.getSingleResult()).intValue();
	}

	public List<Groups> retrieveGroups(int first, int pageSize, String category, boolean archive) {

		TypedQuery<Groups> query = null;
		
		String select = "select g ";
		String from = "from Groups g ";
		String where = "where g.archive like :archive ";
		String orderby = "order by g.category asc, g.category";

		if (category != null) {
			if (where.isEmpty()) {
				where = "where ";
			} else {
				where += "and ";
			}
			where += "g.category like :category ";
		}
		
		query = em.createQuery((select + from + where + orderby), Groups.class)
				.setFirstResult(first)
				.setMaxResults(pageSize);
		
		if (category != null) {
			query.setParameter("category", category+"%");
		}
		query.setParameter("archive", archive);
		
		return query.getResultList();
	}

}
