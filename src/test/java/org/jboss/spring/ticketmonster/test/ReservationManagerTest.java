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
	
	// createSectionRequests() method cannot be tested without commenting out code that binds username to the BookingState User member.
	
	@Ignore
	public void testCreateSectionRequests() {
		BookingRequest booking = new BookingRequest();
		booking.setShowId((long) 3);
		
		List<PriceCategory> categories = showDao.getCategories(1l, 2l);
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
		
		boolean success = reservationManager.findContiguousSeats(3l, 100l, 10);
		Assert.assertEquals(true, success);
		success = reservationManager.findContiguousSeats(3l, 100l, 5);
		Assert.assertEquals(true, success);
		
		CacheKey key = new CacheKey(3l, 1l);
		Assert.assertEquals(3l, key.getShowId(), 0);
		Assert.assertEquals(1l, key.getRowId(), 0);
		
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
		
		boolean success = reservationManager.findContiguousSeats(3l, 101l, 10);
		Assert.assertEquals(true, success);
		Assert.assertEquals(3, reservationManager.getBookingState().getReserved().size());		
		Assert.assertEquals(1, reservationManager.getBookingState().getReserved().get(2).getStartSeat());
		Assert.assertEquals(10, reservationManager.getBookingState().getReserved().get(2).getEndSeat());
		
		// Test updateSeatReservation() method when a reservation already exists in the current session.

		Assert.assertEquals(true, reservationManager.getBookingState().reservationExists((long) 3, (long) 101));
		success = reservationManager.updateSeatReservation(3l, 101l, 15);
		Assert.assertEquals(true, success);
		Assert.assertEquals(3, reservationManager.getBookingState().getReserved().size());
		Assert.assertEquals(1, reservationManager.getBookingState().getReserved().get(2).getStartSeat());
		Assert.assertEquals(15, reservationManager.getBookingState().getReserved().get(2).getEndSeat());
		Assert.assertEquals((long) 51, reservationManager.getBookingState().getReserved().get(2).getKey().getRowId(), 0);
		
		// Test updateSeatReservation() method when a reservation does not already exist.
		
		Assert.assertEquals(false, reservationManager.getBookingState().reservationExists(3l, 102l));
		success = reservationManager.updateSeatReservation(3l, 102l, 15);
		Assert.assertEquals(true, success);
		Assert.assertEquals(4, reservationManager.getBookingState().getReserved().size());
		Assert.assertEquals(1, reservationManager.getBookingState().getReserved().get(3).getStartSeat());
		Assert.assertEquals(15, reservationManager.getBookingState().getReserved().get(3).getEndSeat());
		Assert.assertEquals(true, reservationManager.getBookingState().reservationExists(3l, 102l));
	}

	@Test
	public void testReservationExists() {
		// Test reservationExists() method of the BookingState class.
		
		Assert.assertEquals(4, reservationManager.getBookingState().getReserved().size());
		Assert.assertEquals(true, reservationManager.getBookingState().reservationExists(3l, 100l));
		Assert.assertEquals(true, reservationManager.getBookingState().reservationExists(3l, 101l));
		Assert.assertEquals(true, reservationManager.getBookingState().reservationExists(3l, 102l));
	}
	
	@Test
	public void testRemoveSeatReservation() {	
		// Test removeSeatReservation() method within the updateSeatReservation() method when removing a reservation.

		Assert.assertEquals(true, reservationManager.getBookingState().reservationExists(3l, 102l));
		Assert.assertEquals(4, reservationManager.getBookingState().getReserved().size());		
		boolean success = reservationManager.updateSeatReservation(3l, 102l, 0);
		Assert.assertEquals(true, success);
		Assert.assertEquals(3, reservationManager.getBookingState().getReserved().size());
		Assert.assertEquals(false, reservationManager.getBookingState().reservationExists(3l, 102l));
		
		// Test removeSeatReservation() method on its own.
		
		Assert.assertEquals(true, reservationManager.getBookingState().reservationExists(3l, 101l));
		reservationManager.removeSeatReservation(3l, 101l);
		Assert.assertEquals(2, reservationManager.getBookingState().getReserved().size());
		reservationManager.updateSeatReservation(3l, 100l, 0);
		Assert.assertEquals(true, success);
		reservationManager.updateSeatReservation(3l, 100l, 0);
		Assert.assertEquals(true, success);
		Assert.assertEquals(true, reservationManager.getBookingState().getReserved().isEmpty());
	}
	
}
