package org.jboss.spring.ticketmonster.test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.jboss.spring.ticketmonster.domain.Event;
import org.jboss.spring.ticketmonster.domain.Venue;
import org.jboss.spring.ticketmonster.repo.EventDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test-context.xml",
"classpath:META-INF/spring/ticketmonster-business-context.xml"})
@TransactionConfiguration(defaultRollback=true)
public class EventDaoTest {
	
	@Autowired
	private EventDao eventDao;
	
	@Transactional
	@Test
	public void testGetEvents() {
		List<Event> events = eventDao.getEvents();
		Assert.assertEquals(2, events.size());
		return;
	}
	
	@Transactional
	@Test
	public void testGetEventsById() {
		Event e1 = eventDao.getEvent((long)1);
		Event e2 = eventDao.getEvent((long)2);
		Assert.assertEquals("Rock concert of the decade", e1.getName());
		Assert.assertEquals("Shane's Sock Puppets", e2.getName());
		return;
	}
	
	@Transactional
	@Test
	public void testSearchMajor() {
		List<Event> events = eventDao.searchMajor(true);
		Assert.assertEquals(2, events.size());
		Assert.assertEquals("Rock concert of the decade", events.get(0).getName());
		Assert.assertEquals("Shane's Sock Puppets", events.get(1).getName());
		return;
	}
	
	@Transactional
	@Test
	public void testSearchCategory() {
		List<Event> events = eventDao.searchCategory((long)1);
		Assert.assertEquals("Rock concert of the decade", events.get(0).getName());
		events = eventDao.searchCategory((long)2);
		Assert.assertEquals("Shane's Sock Puppets", events.get(1).getName());
		return;
	}

	@Transactional
	@Test
	public void testSearchDateSuccess() {
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		List<Event> events = new ArrayList<Event>();
		try {
			events = eventDao.searchDate(formatter.parse("01-01-2011"),formatter.parse("01-02-2011"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Assert.assertEquals(2, events.size());
		Assert.assertEquals("Rock concert of the decade", events.get(0).getName());
		Assert.assertEquals("Shane's Sock Puppets", events.get(1).getName());
		return;
	}
	
	@Transactional
	@Test
	public void testSearchDateFail() {
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		List<Event> events = new ArrayList<Event>();
		try {
			events = eventDao.searchDate(formatter.parse("01-12-2010"),formatter.parse("01-03-2011"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Assert.assertEquals(true, events.isEmpty());
		return;
	}
	
	@Transactional
	@Test
	public void testGetVenues() {
		Event event = eventDao.getEvent((long)1);
		List<Venue> venues = eventDao.getVenues(event);
		Assert.assertEquals(2, venues.size());
		Assert.assertEquals("City Central Concert Hall", venues.get(0));
		Assert.assertEquals("Sydney Opera House", venues.get(1));
		return;
	}
}
