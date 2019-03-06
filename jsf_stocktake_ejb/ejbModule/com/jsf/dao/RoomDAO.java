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
import com.jsf.entities.Invobject;
import com.jsf.entities.Room;
import com.jsf.entities.User;

@Stateless
public class RoomDAO {
	
	private final static String UNIT_NAME = "jsfcourse-simplePU";

	// Dependency injection (no setter method is needed)
	@PersistenceContext(unitName = UNIT_NAME)
	protected EntityManager em;
	
	public void create(Room room) {
		em.persist(room);
	}

	public Room merge(Room room) {
		return em.merge(room);
	}
	
	public Room find(Object id) {
		return em.find(Room.class, id);
	}
	
	public List<Room> getFullList(List<Department> departmentList){
		List<Room> list = null;

		Query query = em.createQuery("select r from Room r where r.archive = false and r.department in (:departments) order by r.number asc, r.number");

		if (!departmentList.isEmpty()) {
			query.setParameter("departments", departmentList);
		}else {
			query.setParameter("departments", null);
		}	
		
		try {
			list = query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		

		return list;
		
	}	
	public List<Room> getList(Map<String, Object> searchParams) {
		List<Room> list = null;

		String select = "select r ";
		String from = "from Room r ";
		String where = "";
		String orderby = "order by r.number asc, r.number";

		String roomNumber = (String) searchParams.get("roomNumber");
		if (roomNumber != null) {
			if (where.isEmpty()) {
				where = "where ";
			} else {
				where += "and ";
			}
			where += "r.number like :roomNumber ";
		}
		
		Query query = em.createQuery(select + from + where + orderby);

		if (roomNumber != null) {
			query.setParameter("roomNumber", roomNumber+"%");
		}

		try {
			list = query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}	
	
	public void archiveRoom(Room room) {
		String select = "select r ";
        String from = "from Room r ";
        String where = "where id like :id";
 
        Query query = em.createQuery(select + from + where);
 
        query.setParameter("id", room.getIdRoom());
       
        Room edited = null;
        try {
            edited = (Room) query.getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
       
        edited.setArchive(true);
       
        merge(edited);
	}
	
	public void unarchiveRoom(Room room) {
		String select = "select r ";
        String from = "from Room r ";
        String where = "where id like :id";
 
        Query query = em.createQuery(select + from + where);
 
        query.setParameter("id", room.getIdRoom());
       
        Room edited = null;
        try {
            edited = (Room) query.getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
       
        edited.setArchive(false);
       
        merge(edited);
	}
	
	//Lazy Loading support
	public int getRowsCountDepartment(Department department, String roomNumber, boolean archive) {

		Query query = null;
		
		String select = "SELECT COUNT(*) ";
		String from = "FROM Room r ";
		String where = "where r.department like :department and r.archive like :archive ";
		String orderby = "order by r.number asc, r.number";
		
		if (roomNumber != null) {
			if (where.isEmpty()) {
				where = "where ";
			} else {
				where += "and ";
			}
			where += "r.number like :roomNumber ";
		}
		
		query = em.createQuery(select + from + where + orderby);

		if (roomNumber != null) {
			query.setParameter("roomNumber", roomNumber + "%");
		}
		
		query.setParameter("department", department);
		query.setParameter("archive", archive);

		return ((Long) query.getSingleResult()).intValue();
	}
	
	public List<Room> retrieveRoomsDepartment(int first, int pageSize, Department department, String roomNumber, boolean archive) {

		TypedQuery<Room> query = null;
		
		String select = "select r ";
		String from = "from Room r ";
		String where = "where r.department like :department and r.archive like :archive ";
		String orderby = "order by r.number asc, r.number";
		
		if (roomNumber != null) {
			if (where.isEmpty()) {
				where = "where ";
			} else {
				where += "and ";
			}
			where += "r.number like :roomNumber ";
		}
		
		query = em.createQuery(select + from + where + orderby, Room.class)
				.setFirstResult(first)
				.setMaxResults(pageSize);

		if (roomNumber != null) {
			query.setParameter("roomNumber", roomNumber + "%");
		}
		
		query.setParameter("department", department);
		query.setParameter("archive", archive);
		
		return query.getResultList();
	}
		
	public int getRowsCount(List<Department> departmentList, String roomNumber, boolean archive) {

		Query query = null;
		
		String select = "SELECT COUNT(*) ";
		String from = "FROM Room r ";
		String where = "where r.archive like :archive and r.department in (:departments) ";
		String orderby = "order by r.number asc, r.number";
		
		if (roomNumber != null) {
			if (where.isEmpty()) {
				where = "where ";
			} else {
				where += "and ";
			}
			where += "r.number like :roomNumber ";
		}
		
		query = em.createQuery(select + from + where + orderby);

		if (roomNumber != null) {
			query.setParameter("roomNumber", roomNumber + "%");
		}
		
		query.setParameter("archive", archive);
		if (!departmentList.isEmpty()) {
			query.setParameter("departments", departmentList);
		}else {
			query.setParameter("departments", null);
		}
		

		return ((Long) query.getSingleResult()).intValue();
	}

	public List<Room> retrieveRooms(int first, int pageSize, List<Department> departmentList, String roomNumber, boolean archive) {

		TypedQuery<Room> query = null;
		
		String select = "select r ";
		String from = "from Room r ";
		String where = "where r.archive like :archive and r.department in (:departments) ";
		String orderby = "order by r.number asc, r.number";
		
		if (roomNumber != null) {
			if (where.isEmpty()) {
				where = "where ";
			} else {
				where += "and ";
			}
			where += "r.number like :roomNumber ";
		}
		
		query = em.createQuery(select + from + where + orderby, Room.class)
				.setFirstResult(first)
				.setMaxResults(pageSize);

		if (roomNumber != null) {
			query.setParameter("roomNumber", roomNumber + "%");
		}
		
		query.setParameter("archive", archive);
		if (!departmentList.isEmpty()) {
			query.setParameter("departments", departmentList);
		}else {
			query.setParameter("departments", null);
		}		
		return query.getResultList();
	}
	
}
