package org.jboss.spring.ticketmonster.test;

import java.util.List;

import junit.framework.Assert;

import org.jboss.spring.ticketmonster.domain.Event;
import org.jboss.spring.ticketmonster.domain.Venue;
import org.jboss.spring.ticketmonster.repo.VenueDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 * JUnit tests for the implementation of the VenueDao interface. 
 *
 * @author Ryan Bradley
 *
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test-context.xml",
"classpath:/META-INF/spring/ticketmonster-business-context.xml",
"classpath:/META-INF/test-bookingState.xml"})
@TransactionConfiguration(defaultRollback=true)
public class VenueDaoTest {
	
	@Autowired
	private VenueDao venueDao;
	
	@Transactional
	@Test
	public void testGetVenues() {
		List<Venue> venues = venueDao.getVenues();
		Assert.assertEquals(2, venues.size());
		return;
	}
	
	@Transactional
	@Test
	public void testGetVenueById() {
		Venue v1 = venueDao.getVenue(1l);
		Assert.assertEquals("City Central Concert Hall", v1.getName());
		Venue v2 = venueDao.getVenue(2l);
		Assert.assertEquals("Sydney Opera House", v2.getName());
		return;
	}
	
	@Transactional
	@Test
	public void testGetEvents() {
		Venue venue = venueDao.getVenue(1l);
		List<Event> events = venueDao.getEvents(venue);
		Assert.assertEquals(1, events.size());
		Assert.assertEquals("Rock Concert of the Decade", events.get(0).getName());
		return;
	}
}
