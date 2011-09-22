package org.jboss.spring.ticketmonster.repo;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.jboss.spring.ticketmonster.domain.User;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Implementation of the UserDao interface for databases access of User information.
 * 
 * @author rbradley
 *
 */

public class UserDaoImpl implements UserDao {

	@Autowired
	private EntityManager entityManager;
	
	public User getByName(String username) {
		Query query = entityManager.createQuery("select u from User u where u.username = :username");
		query.setParameter("username", username);
		User user = (User) query.getSingleResult();
		return user;
	}

	public String getFirstName(String username) {
		User user = this.getByName(username);
		return user.getFirstName();
	}

	public String getLastName(String username) {
		User user = this.getByName(username);
		return user.getLastName();
	}
	
}
