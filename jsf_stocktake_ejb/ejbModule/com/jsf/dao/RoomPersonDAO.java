package com.jsf.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import com.jsf.entities.Department;
import com.jsf.entities.Roleuser;
import com.jsf.entities.Room;
import com.jsf.entities.Roomperson;
import com.jsf.entities.User;

@Stateless
public class RoomPersonDAO {

	private final static String UNIT_NAME = "jsfcourse-simplePU";

	// Dependency injection (no setter method is needed)
	@PersistenceContext(unitName = UNIT_NAME)
	protected EntityManager em;
	
	public void create(Roomperson roomPerson) {
		em.persist(roomPerson);
	}

	public Roomperson merge(Roomperson roomPerson) {
		return em.merge(roomPerson);
	}
	
	public Roomperson find(Object id) {
		return em.find(Roomperson.class, id);
	}
	
	public void remove(Roomperson roomPerson) {
		em.remove(em.merge(roomPerson));
	}

	public List<Roomperson> getFullList(Room room){
		
		List<Roomperson> list = null;
			
		Query query = em.createQuery("select r from Roomperson r where r.room like :room");
		query.setParameter("room", room);
		
		try {
			list = query.getResultList();
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return list;
	}
	
	//Lazy Loading support
	public int getRowsCount(Room room) {

		Query query = em.createQuery("SELECT COUNT(*) FROM Roomperson r where r.room like :room");
		query.setParameter("room", room);

		return ((Long) query.getSingleResult()).intValue();
	}

	public List<Roomperson> retrieveRoomPerson(int first, int pageSize, Room room) {

		TypedQuery<Roomperson> query = em.createQuery(
				"select r from Roomperson r where r.room like :room", Roomperson.class)
				.setFirstResult(first)
				.setMaxResults(pageSize);
		query.setParameter("room", room);
		return query.getResultList();
	}
}
