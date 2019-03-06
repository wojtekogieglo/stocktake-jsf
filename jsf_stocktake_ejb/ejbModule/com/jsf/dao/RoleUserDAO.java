package com.jsf.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.jsf.entities.User;
import com.jsf.entities.Roleuser;

@Stateless
public class RoleUserDAO {
	
	private final static String UNIT_NAME = "jsfcourse-simplePU";

	// Dependency injection (no setter method is needed)
	@PersistenceContext(unitName = UNIT_NAME)
	protected EntityManager em;
	
	public void create(Roleuser roleuser) {
		em.persist(roleuser);
	}

	public Roleuser merge(Roleuser roleuser) {
		return em.merge(roleuser);
	}
	
	public Roleuser find(Object id) {
		return em.find(Roleuser.class, id);
	}
	
	public void remove(Roleuser roleuser) {
		em.remove(em.merge(roleuser));
	}
	
	public List<Roleuser> getFullList(User user){
		
		List<Roleuser> list = null;
			
		Query query = em.createQuery("select u from Roleuser u where u.user like :user");
		query.setParameter("user", user);
		
		try {
			list = query.getResultList();
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return list;
	}

}
