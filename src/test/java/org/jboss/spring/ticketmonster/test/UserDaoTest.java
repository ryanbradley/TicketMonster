package org.jboss.spring.ticketmonster.test;

import junit.framework.Assert;

import org.jboss.spring.ticketmonster.domain.User;
import org.jboss.spring.ticketmonster.repo.UserDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 * JUnit tests for the implementation of the UserDao interface.
 * 
 * @author Ryan Bradley
 *
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test-context.xml",
"classpath:/META-INF/spring/ticketmonster-business-context.xml",
"classpath:/META-INF/test-bookingState.xml"})
@Transactional
@TransactionConfiguration(defaultRollback=true)
public class UserDaoTest {
	
	@Autowired
	private UserDao userDao;
	
	@Test
	public void testGetByName() {
		User user = new User();
		user.setFirstName("Ryan");
		user.setLastName("Bradley");
		user.setUsername("rbradley");
		
		User found = userDao.getByName("rbradley");
		
		Assert.assertNotNull(found);
		Assert.assertEquals("rbradley", found.getUsername());
		Assert.assertEquals("Ryan", found.getFirstName());
		Assert.assertEquals("Bradley", found.getLastName());
	}
	
	@Test
	public void testGetFirstName() {
		String firstName = userDao.getFirstName("rbradley");
		Assert.assertEquals("Ryan", firstName);
	}
	
	@Test
	public void testGetLastName() {
		String lastName = userDao.getLastName("rbradley");
		Assert.assertEquals("Bradley", lastName);
	}

}
