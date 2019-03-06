package com.jsf.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.jsf.entities.User;
import com.jsf.entities.Institution;
import com.jsf.entities.Roleuser;

@Stateless
public class UserDAO {
	private final static String UNIT_NAME = "jsfcourse-simplePU";

	// Dependency injection (no setter method is needed)
	@PersistenceContext(unitName = UNIT_NAME)
	protected EntityManager em;
	
	public void create(User user) {
		em.persist(user);
	}

	public User merge(User user) {
		return em.merge(user);
	}
	
	public User find(Object id) {
		return em.find(User.class, id);
	}
	
	public void inactivateUser(User user) {
		String select = "select u ";
        String from = "from User u ";
        String where = "where id like :id";
 
        Query query = em.createQuery(select + from + where);
 
        query.setParameter("id", user.getIdUser());
       
        User edited = null;
        try {
            edited = (User) query.getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
       
        edited.setActive(false);
       
        merge(edited);
	}
	
	public void activateUser(User user) {
		String select = "select u ";
        String from = "from User u ";
        String where = "where id like :id";
 
        Query query = em.createQuery(select + from + where);
 
        query.setParameter("id", user.getIdUser());
       
        User edited = null;
        try {
            edited = (User) query.getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
       
        edited.setActive(true);
       
        merge(edited);
	}
	
	public List<User> getFullList() {
		List<User> list = null;

		Query query = em.createQuery("select u from User u");

		try {
			list = query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}
	
	public List<User> getList(Map<String, Object> searchParams) {
		List<User> list = null;

		String select = "select u ";
		String from = "from User u ";
		String where = "";
		String orderby = "order by u.surname asc, u.surname";

		String surname = (String) searchParams.get("surname");
		Institution institution = (Institution) searchParams.get("institution");
		if (surname != null) {
			if (where.isEmpty()) {
				where = "where ";
			} else {
				where += "and ";
			}
			where += "u.surname like :surname ";
		}
		
		if (institution != null) {
			if(where.isEmpty()) {
				where = "where ";
			} else {
				where += "and ";
			}
			where += "u.institution like :institution ";
		}
		
		Query query = em.createQuery(select + from + where + orderby);

		if (surname != null) {
			query.setParameter("surname", surname+"%");
		}
		
		if (institution != null) {
			query.setParameter("institution", institution);
		}

		try {
			list = query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}
	
	public User getUserFromDatabase(String email, String password) {
		User user = null;
		
		String select = "select u ";
		String from = "from User u ";
		String where = "where u.email like :email and u.password like :password";
		
		Query query = em.createQuery(select + from + where);
		
		query.setParameter("email", email);
		query.setParameter("password", password);

		try {
			user = (User) query.getSingleResult();
		} catch (Exception e) {
				e.printStackTrace();
		}
		
		return user;
	}
	
	public List<String> getUserRolesFromDatabase(User user){
		
		ArrayList<String> roles = new ArrayList<String>();
		List<Roleuser> list = null;
		
		Query query = em.createQuery("select u from Roleuser u where u.user like :user");
		query.setParameter("user", user);
		
		try {
			list = query.getResultList();
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		for(Roleuser role : list) {
			roles.add(role.getRole().getRoleName());
		}
		
		return roles;
	}
	
	public boolean isEmailUnique(String email) {
		User u = null;
		Query query = em.createQuery("select u from User u where u.email like :email");
		query.setParameter("email", email);
		
		try {
			u = (User) query.getSingleResult();
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		if(u == null) {
			return true;
		}else {
			return false;
		}		
	}

}
