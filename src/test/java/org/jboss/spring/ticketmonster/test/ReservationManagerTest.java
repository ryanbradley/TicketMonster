package org.jboss.spring.ticketmonster.test;

import java.util.ArrayList;
import java.util.List;

import org.jboss.spring.ticketmonster.domain.BookingRequest;
import org.jboss.spring.ticketmonster.domain.CacheKey;
import org.jboss.spring.ticketmonster.domain.PriceCategory;
import org.jboss.spring.ticketmonster.domain.PriceCategoryRequest;
import org.jboss.spring.ticketmonster.domain.RowReservation;
import org.jboss.spring.ticketmonster.domain.SeatBlock;
import org.jboss.spring.ticketmonster.domain.SectionRequest;
import org.jboss.spring.ticketmonster.repo.ShowDao;
import org.jboss.spring.ticketmonster.service.ReservationManager;

import junit.framework.Assert;

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
 * JUnit tests for the implementation of the ReservationManager interface.
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
public class ReservationManagerTest {

	@Autowired
	private ReservationManager reservationManager;
	
	@Autowired
	private ShowDao showDao;
	
	@Autowired
	private CacheManager cacheManager;
	
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
	
	@Test
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
		
		RowReservation allocation = (RowReservation) reservationsCache.get(key).get();
		Assert.assertNotNull(allocation);
		Assert.assertEquals(2, allocation.getReservedSeats().size());
		
		SeatBlock firstBlock = allocation.getReservedSeats().get(0);
		SeatBlock secondBlock = allocation.getReservedSeats().get(1);
		
		Assert.assertEquals(1, firstBlock.getStartSeat());
		Assert.assertEquals(10, firstBlock.getEndSeat());
		Assert.assertEquals(11, secondBlock.getStartSeat());
		Assert.assertEquals(15, secondBlock.getEndSeat());
	}
	
	@Test
	public void testUpdateSeatAllocation() {
		
		// First test the current state of the BookingState object.
		
		Assert.assertEquals(2, reservationManager.getBookingState().getReserved().size());
		Assert.assertEquals(1, reservationManager.getBookingState().getReserved().get(0).getStartSeat());
		Assert.assertEquals(10, reservationManager.getBookingState().getReserved().get(0).getEndSeat());
		Assert.assertEquals(11, reservationManager.getBookingState().getReserved().get(1).getStartSeat());
		Assert.assertEquals(15, reservationManager.getBookingState().getReserved().get(1).getEndSeat());
		Assert.assertEquals(true, reservationManager.getBookingState().getReserved().get(0).getKey().equals(reservationManager.getBookingState().getReserved().get(1).getKey()));
		
		boolean success = reservationManager.findContiguousSeats((long) 3, (long) 101, 10);
		Assert.assertEquals(true, success);
		Assert.assertEquals(3, reservationManager.getBookingState().getReserved().size());		
		Assert.assertEquals(1, reservationManager.getBookingState().getReserved().get(2).getStartSeat());
		Assert.assertEquals(10, reservationManager.getBookingState().getReserved().get(2).getEndSeat());
		
		// Test updateSeatReservation() method when a reservation already exists in the current session.
		
		CacheKey key = new CacheKey((long) 3, (long) 51);
		Assert.assertEquals(true, reservationManager.getBookingState().reservationExists(key));
		success = reservationManager.updateSeatReservation((long) 3, (long) 101, 15);
		Assert.assertEquals(true, success);
		Assert.assertEquals(3, reservationManager.getBookingState().getReserved().size());
		Assert.assertEquals(1, reservationManager.getBookingState().getReserved().get(2).getStartSeat());
		Assert.assertEquals(15, reservationManager.getBookingState().getReserved().get(2).getEndSeat());
		Assert.assertEquals((long) 51, reservationManager.getBookingState().getReserved().get(2).getKey().getRowId(), 0);
		
		// Test updateSeatReservation() method when a reservation does not already exist.
		
		key = new CacheKey((long) 3, (long) 101);
		Assert.assertEquals(false, reservationManager.getBookingState().reservationExists(key));
		success = reservationManager.updateSeatReservation((long) 3, (long) 102, 15);
		Assert.assertEquals(true, success);
		Assert.assertEquals(4, reservationManager.getBookingState().getReserved().size());
		Assert.assertEquals(1, reservationManager.getBookingState().getReserved().get(3).getStartSeat());
		Assert.assertEquals(15, reservationManager.getBookingState().getReserved().get(3).getEndSeat());
		Assert.assertEquals((long) 101, reservationManager.getBookingState().getReserved().get(3).getKey().getRowId(), 0);
	}

	@Test
	public void testRemoveSeatReservation() {
		
		// Test removeSeatReservation() method within the updateSeatReservation() method when removing a reservation.
		
		boolean success = reservationManager.updateSeatReservation((long) 3, (long) 102, 0);
		Assert.assertEquals(true, success);
		Assert.assertEquals(3, reservationManager.getBookingState().getReserved().size());
		CacheKey key = new CacheKey((long) 3, (long) 101);
		Assert.assertEquals(false, reservationManager.getBookingState().reservationExists(key));
		
		// Test removeSeatReservation() method on its own.
		
		reservationManager.removeSeatReservation((long) 3, (long) 51);
		key = new CacheKey((long) 3, (long) 51);
		Assert.assertEquals(false, reservationManager.getBookingState().reservationExists(key));
		Assert.assertEquals(2, reservationManager.getBookingState().getReserved().size());
	}
}
