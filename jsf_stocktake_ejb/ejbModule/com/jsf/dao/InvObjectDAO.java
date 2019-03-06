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
import com.jsf.entities.Institution;
import com.jsf.entities.Invobject;
import com.jsf.entities.Personresponsible;
import com.jsf.entities.Room;
import com.jsf.entities.Roomperson;

@Stateless
public class InvObjectDAO {

	private final static String UNIT_NAME = "jsfcourse-simplePU";

	// Dependency injection (no setter method is needed)
	@PersistenceContext(unitName = UNIT_NAME)
	protected EntityManager em;
	
	public void create(Invobject invObject) {
		em.persist(invObject);
	}

	public Invobject merge(Invobject invObject) {
		return em.merge(invObject);
	}

	public void remove(Invobject invObject) {
		em.remove(em.merge(invObject));
	}
	
	public Invobject find(Object id) {
		return em.find(Invobject.class, id);
	}
	
	public List<Invobject> getFullList() {
		List<Invobject> list = null;

		Query query = em.createQuery("select i from Invobject i");

		try {
			list = query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}
	
	public List<Invobject> getList(Map<String, Object> searchParams) {
		List<Invobject> list = null;

		// 1. Build query string with parameters
		String select = "select i ";
		String from = "from Invobject i ";
		String where = "";
		String orderby = "order by i.name asc, i.name";

		// search for institution
		String nameInvObject = (String) searchParams.get("invObjectName");
		if (nameInvObject != null) {
			if (where.isEmpty()) {
				where = "where ";
			} else {
				where += "and ";
			}
			where += "i.name like :nameInvObject ";
		}
		
		// ... other parameters ... 

		// 2. Create query object
		Query query = em.createQuery(select + from + where + orderby);

		// 3. Set configured parameters
		if (nameInvObject != null) {
			query.setParameter("nameInvObject", nameInvObject+"%");
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
	
	public void archiveInvObject(Invobject invObject) {
		String select = "select i ";
        String from = "from Invobject i ";
        String where = "where id like :id";
 
        Query query = em.createQuery(select + from + where);
 
        query.setParameter("id", invObject.getIdInvObject());
       
        Invobject edited = null;
        try {
            edited = (Invobject) query.getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
       
        edited.setArchive(true);
       
        merge(edited);
	}
	
	public void unarchiveInvObject(Invobject invObject) {
		String select = "select i ";
		String from = "from Invobject i ";
		String where = "where id like :id";
		
		Query query = em.createQuery(select + from + where);
		
		query.setParameter("id", invObject.getIdInvObject());
		
		Invobject edited = null;
		try {
			edited = (Invobject) query.getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		edited.setArchive(false);
		
		merge(edited);
	}
	
	//Lazy Loading support
	public int getRowsCountRoom(Room room, String nameInvObject, boolean archive) {
		Query query = null;
		
		String select = "SELECT COUNT(*) ";
		String from = "FROM Invobject i ";
		String where = "where i.room like :room and i.archive like :archive ";
		String orderby = "order by i.name asc, i.name";
		
		if (nameInvObject != null) {
			if (where.isEmpty()) {
				where = "where ";
			} else {
				where += "and ";
			}
			where += "i.name like :nameInvObject ";
		}
		
		query = em.createQuery(select + from + where + orderby);
		
		query.setParameter("room", room);
		query.setParameter("archive", archive);
		if (nameInvObject != null) {
			query.setParameter("nameInvObject", nameInvObject+"%");
		}

		return ((Long) query.getSingleResult()).intValue();
	}
	
	public List<Invobject> retrieveInvObjectRoom(int first, int pageSize, Room room, String nameInvObject, boolean archive) {
		
		TypedQuery<Invobject> query = null;
		
		String select = "select i ";
		String from = "from Invobject i ";
		String where = "where i.room like :room and i.archive like :archive ";
		String orderby = "order by i.name asc, i.name";
		
		if (nameInvObject != null) {
			if (where.isEmpty()) {
				where = "where ";
			} else {
				where += "and ";
			}
			where += "i.name like :nameInvObject ";
		}
		
		query = em.createQuery(
				select + from + where + orderby, Invobject.class)
				.setFirstResult(first)
				.setMaxResults(pageSize);
		
		query.setParameter("room", room);
		query.setParameter("archive", archive);
		if (nameInvObject != null) {
			query.setParameter("nameInvObject", nameInvObject+"%");
		}
		
		return query.getResultList();
	}
	
	public int getRowsCountGroup(List<Room> roomList, Groups group, String nameInvObject, boolean archive) {
		
		Query query = null;
		
		String select = "SELECT COUNT(*) ";
		String from = "FROM Invobject i ";
		String where = "where i.group like :group and i.archive like :archive and i.room in (:rooms) ";
		String orderby = "order by i.name asc, i.name";
		
		if (nameInvObject != null) {
			if (where.isEmpty()) {
				where = "where ";
			} else {
				where += "and ";
			}
			where += "i.name like :nameInvObject ";
		}
		
		query = em.createQuery(select + from + where + orderby);
		
		if (nameInvObject != null) {
			query.setParameter("nameInvObject", nameInvObject+"%");
		}
		query.setParameter("group", group);
		query.setParameter("archive", archive);
		
		if (!roomList.isEmpty()) {
			query.setParameter("rooms", roomList);
		}else {
			query.setParameter("rooms", null);
		}
		
		return ((Long) query.getSingleResult()).intValue();
	}
	
	public List<Invobject> retrieveInvObjectGroup(int first, int pageSize, List<Room> roomList, Groups group, String nameInvObject, boolean archive) {
		
		TypedQuery<Invobject> query = null;
		
		String select = "select i ";
		String from = "from Invobject i ";
		String where = "where i.group like :group and i.archive like :archive and i.room in (:rooms) ";
		String orderby = "order by i.name asc, i.name";
		
		if (nameInvObject != null) {
			if (where.isEmpty()) {
				where = "where ";
			} else {
				where += "and ";
			}
			where += "i.name like :nameInvObject ";
		}
		
		query = em.createQuery(
				select + from + where + orderby, Invobject.class)
				.setFirstResult(first)
				.setMaxResults(pageSize);
		
		if (nameInvObject != null) {
			query.setParameter("nameInvObject", nameInvObject+"%");
		}
		query.setParameter("group", group);
		query.setParameter("archive", archive);
		
		if (!roomList.isEmpty()) {
			query.setParameter("rooms", roomList);
		}else {
			query.setParameter("rooms", null);
		}
		
		return query.getResultList();
	}
	
	public int getRowsCountPersonResponsible(Personresponsible personResponsible, String nameInvObject, boolean archive) {
		
		Query query = null;
		
		String select = "SELECT COUNT(*) ";
		String from = "FROM Invobject i ";
		String where = "where i.personresponsible like :personResponsible and i.archive like :archive ";
		String orderby = "order by i.name asc, i.name";

		if (nameInvObject != null) {
			if (where.isEmpty()) {
				where = "where ";
			} else {
				where += "and ";
			}
			where += "i.name like :nameInvObject ";
		}
		
		query = em.createQuery(select + from + where + orderby);
		
		if (nameInvObject != null) {
			query.setParameter("nameInvObject", nameInvObject+"%");
		}
		query.setParameter("personResponsible", personResponsible);
		query.setParameter("archive", archive);
		
		return ((Long) query.getSingleResult()).intValue();
	}
	
	public List<Invobject> retrieveInvObjectPersonResponsible(int first, int pageSize, Personresponsible personResponsible, String nameInvObject, boolean archive) {
		
		TypedQuery<Invobject> query = null;
		
		String select = "select i ";
		String from = "from Invobject i ";
		String where = "where i.personresponsible like :personResponsible and i.archive like :archive ";
		String orderby = "order by i.name asc, i.name";
		
		if (nameInvObject != null) {
			if (where.isEmpty()) {
				where = "where ";
			} else {
				where += "and ";
			}
			where += "i.name like :nameInvObject ";
		}
		
		query = em.createQuery(
				select + from + where + orderby, Invobject.class)
				.setFirstResult(first)
				.setMaxResults(pageSize);
		
		query.setParameter("personResponsible", personResponsible);
		query.setParameter("archive", archive);
		if (nameInvObject != null) {
			query.setParameter("nameInvObject", nameInvObject+"%");
		}
		
		return query.getResultList();
	}
	
	public int getRowsCount(List<Room> roomList, String nameInvObject, boolean archive) {
		
		Query query = null;
		
		String select = "SELECT COUNT(*) ";
		String from = "FROM Invobject i ";
		String where = "where i.archive like :archive and i.room in (:rooms) ";
		String orderby = "order by i.name asc, i.name";

		if (nameInvObject != null) {
			if (where.isEmpty()) {
				where = "where ";
			} else {
				where += "and ";
			}
			where += "i.name like :nameInvObject ";
		}
		
		query = em.createQuery(select + from + where + orderby);
		
		if (nameInvObject != null) {
			query.setParameter("nameInvObject", nameInvObject+"%");
		}
		query.setParameter("archive", archive);
		
		if (!roomList.isEmpty()) {
			query.setParameter("rooms", roomList);
		}else {
			query.setParameter("rooms", null);
		}
		
        return ((Long) query.getSingleResult()).intValue();
	}

	public List<Invobject> retrieveInvObjects(int first, int pageSize, List<Room> roomList, String nameInvObject, boolean archive) {

		TypedQuery<Invobject> query = null;
		
		String select = "select i ";
		String from = "from Invobject i ";
		String where = "where i.archive like :archive and i.room in (:rooms) ";
		String orderby = "order by i.name asc, i.name";

		if (nameInvObject != null) {
			if (where.isEmpty()) {
				where = "where ";
			} else {
				where += "and ";
			}
			where += "i.name like :nameInvObject ";
		}
		
		query = em.createQuery((select + from + where + orderby), Invobject.class)
				.setFirstResult(first)
				.setMaxResults(pageSize);
		
		if (nameInvObject != null) {
			query.setParameter("nameInvObject", nameInvObject+"%");
		}
		
		query.setParameter("archive", archive);
		
		if (!roomList.isEmpty()) {
			query.setParameter("rooms", roomList);
		}else {
			query.setParameter("rooms", null);
		}
		
		return query.getResultList();
	}

}
