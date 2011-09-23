package org.jboss.spring.ticketmonster.repo;

import org.jboss.spring.ticketmonster.domain.User;

/**
 * Interface for database access relating to retrieving or updating User information.
 * 
 * @author Ryan Bradley
 *
 */

public interface UserDao {

	User getByName(String username);
	
	String getFirstName(String username);
	
	String getLastName(String username);
	
}
