package org.jboss.spring.ticketmonster.test;

import java.util.ArrayList;
import java.util.List;

import org.jboss.spring.ticketmonster.domain.BookingRequest;
import org.jboss.spring.ticketmonster.domain.CacheKey;
import org.jboss.spring.ticketmonster.domain.PriceCategory;
import org.jboss.spring.ticketmonster.domain.PriceCategoryRequest;
import org.jboss.spring.ticketmonster.domain.RowAllocation;
import org.jboss.spring.ticketmonster.domain.SeatBlock;
import org.jboss.spring.ticketmonster.domain.SectionRequest;
import org.jboss.spring.ticketmonster.repo.ShowDao;
import org.jboss.spring.ticketmonster.service.ReservationManager;

import junit.framework.Assert;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCache;
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
"classpath:META-INF/spring/ticketmonster-business-context.xml",
"classpath:/META-INF/test-bookingState.xml"})
@TransactionConfiguration(defaultRollback=true)
public class ReservationManagerTest {

	@Autowired
	private ReservationManager reservationManager;
	
	@Autowired
	private ShowDao showDao;
	
	@Autowired
	private CacheManager cacheManager;
	
	@Transactional
	@Test
	public void testCreateSectionRequests() {
		BookingRequest booking = new BookingRequest();
		booking.setShowId((long) 3);
		
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
	@Ignore
	public void testFindContiguousSeats() {		
		ConcurrentMapCache reservationsCache = (ConcurrentMapCache) cacheManager.getCache("reservations");
		reservationsCache.clear();
		Assert.assertNotNull(reservationsCache);
		Assert.assertEquals("reservations", reservationsCache.getName());
		
		boolean success = reservationManager.findContiguousSeats((long) 3, (long) 100, 10);
		Assert.assertEquals(true, success);
		success = reservationManager.findContiguousSeats((long) 3, (long) 100, 5);
		Assert.assertEquals(true, success);
		
		CacheKey key = new CacheKey((long) 3, (long) 1);
		Assert.assertEquals((long) 3, key.getShowId(), 0);
		Assert.assertEquals((long) 1, key.getRowId(), 0);
		
		RowAllocation allocation = (RowAllocation) reservationsCache.get(key).get();
		Assert.assertNotNull(allocation);
		Assert.assertEquals(2, allocation.getAllocatedSeats().size());
		
		SeatBlock firstBlock = allocation.getAllocatedSeats().get(0);
		SeatBlock secondBlock = allocation.getAllocatedSeats().get(1);
		
		Assert.assertEquals(1, firstBlock.getStartSeat());
		Assert.assertEquals(10, firstBlock.getEndSeat());
		Assert.assertEquals(11, secondBlock.getStartSeat());
		Assert.assertEquals(15, secondBlock.getEndSeat());
	}
	
	@Transactional
	@Test
	public void testUpdateSeatAllocation() {
		boolean success = reservationManager.findContiguousSeats((long) 3, (long) 100, 10);
		Assert.assertEquals(true, success);
		Assert.assertEquals(1, reservationManager.getBookingState().getAllocated().get(0).getStartSeat());
		Assert.assertEquals(10, reservationManager.getBookingState().getAllocated().get(0).getEndSeat());
		Assert.assertEquals(1, reservationManager.getBookingState().getAllocated().size());
		
		success = reservationManager.updateSeatAllocation((long) 3, (long) 100, 15);
		Assert.assertEquals(1, reservationManager.getBookingState().getAllocated().size());
		Assert.assertEquals(1, reservationManager.getBookingState().getAllocated().get(0).getStartSeat());
		Assert.assertEquals(15, reservationManager.getBookingState().getAllocated().get(0).getEndSeat());
		Assert.assertEquals((long) 1, reservationManager.getBookingState().getAllocated().get(0).getKey().getRowId(), 0);
		
		success = reservationManager.findContiguousSeats((long) 3, (long) 100, 50);
		Assert.assertEquals((long) 2, reservationManager.getBookingState().getAllocated().get(1).getKey().getRowId(), 0);
		Assert.assertEquals(1, reservationManager.getBookingState().getAllocated().get(1).getStartSeat());
		Assert.assertEquals(50, reservationManager.getBookingState().getAllocated().get(1).getEndSeat());
	}
}
