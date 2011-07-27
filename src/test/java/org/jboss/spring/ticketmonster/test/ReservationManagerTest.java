package org.jboss.spring.ticketmonster.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jboss.spring.ticketmonster.domain.Allocation;
import org.jboss.spring.ticketmonster.domain.BookingRequest;
import org.jboss.spring.ticketmonster.domain.PriceCategory;
import org.jboss.spring.ticketmonster.domain.PriceCategoryRequest;
import org.jboss.spring.ticketmonster.domain.SectionRequest;
import org.jboss.spring.ticketmonster.repo.ShowDao;
import org.jboss.spring.ticketmonster.service.ReservationManager;

import junit.framework.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;


/**
 * JUnit tests for the implementation of the ReservationDao interface.
 * 
 * @author Ryan Bradley
 *
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test-context.xml",
"classpath:META-INF/spring/ticketmonster-business-context.xml"})
@TransactionConfiguration(defaultRollback=true)
public class ReservationManagerTest {

	@Autowired
	private ReservationManager reservationManager;
	
	@Autowired
	private ShowDao showDao;
	
	@Transactional
	@Test
	public void testCreateSectionRequests() {
		BookingRequest booking = new BookingRequest();
		booking.setShowId((long)3);
		
		List<PriceCategory> categories = showDao.getCategories((long) 1, (long) 2);
		List<PriceCategoryRequest> categoryRequests = new ArrayList<PriceCategoryRequest>();
		
		for(PriceCategory category : categories) {
			PriceCategoryRequest categoryRequest = new PriceCategoryRequest(category);
			categoryRequests.add(categoryRequest);
		}

		categoryRequests.get(0).setQuantity(5);
		categoryRequests.get(1).setQuantity(10);
		booking.setCategoryRequests(categoryRequests);
		
		List<SectionRequest> sectionRequests = reservationManager.createSectionRequests(booking);
		Assert.assertEquals(15, sectionRequests.get(0).getQuantity());
	}
	
	@Transactional
	@Test
	public void testFindContiguousSeats() {
		BookingRequest booking = new BookingRequest();
		booking.setShowId((long)3);
		
		List<PriceCategory> categories = showDao.getCategories((long) 1, (long) 2);
		List<PriceCategoryRequest> categoryRequests = new ArrayList<PriceCategoryRequest>();
		
		for(PriceCategory category : categories) {
			PriceCategoryRequest categoryRequest = new PriceCategoryRequest(category);
			categoryRequests.add(categoryRequest);
		}

		categoryRequests.get(0).setQuantity(5);
		categoryRequests.get(1).setQuantity(10);
		booking.setCategoryRequests(categoryRequests);
		
		List<SectionRequest> sectionRequests = new ArrayList<SectionRequest>();
		sectionRequests = reservationManager.createSectionRequests(booking);
		Allocation allocation = reservationManager.findContiguousSeats(sectionRequests.get(0));
		Assert.assertEquals(15, allocation.getQuantity());
		Assert.assertEquals(1, allocation.getStartSeat());
		Assert.assertEquals(16, allocation.getEndSeat());
		Assert.assertEquals(new Date(), allocation.getAssigned());
		Assert.assertEquals("AA", allocation.getRow().getName());
	}
	
	@Transactional
	@Test
	public void testReserveSeats() {
		BookingRequest booking = new BookingRequest();
		booking.setShowId((long)1);
		
		List<PriceCategory> categories = showDao.getCategories((long) 1, (long) 2);
		List<PriceCategoryRequest> categoryRequests = new ArrayList<PriceCategoryRequest>();
		
		for(PriceCategory category : categories) {
			PriceCategoryRequest categoryRequest = new PriceCategoryRequest(category);
			categoryRequest.setQuantity(10);
			categoryRequests.add(categoryRequest);
		}
		booking.setCategoryRequests(categoryRequests);
		
		List<SectionRequest> sectionRequests = new ArrayList<SectionRequest>();
		sectionRequests = reservationManager.createSectionRequests(booking);
		Assert.assertEquals(7, sectionRequests.size());
		
		List<Allocation> allocations = new ArrayList<Allocation>();
		allocations = reservationManager.reserveSeats(sectionRequests);
		Assert.assertEquals(7, allocations.size());
	}
}
