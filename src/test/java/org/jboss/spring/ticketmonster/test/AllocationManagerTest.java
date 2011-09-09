package org.jboss.spring.ticketmonster.test;

import java.util.List;

import junit.framework.Assert;

import org.jboss.spring.ticketmonster.domain.Allocation;
import org.jboss.spring.ticketmonster.domain.CacheKey;
import org.jboss.spring.ticketmonster.domain.RowReservation;
import org.jboss.spring.ticketmonster.domain.SeatBlock;
import org.jboss.spring.ticketmonster.service.AllocationManager;
import org.jboss.spring.ticketmonster.service.ReservationManager;
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
 * JUnit tests for the implementation of the AllocationManager interface.
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
public class AllocationManagerTest {
	
	@Autowired
	private CacheManager cacheManager;
	
	@Autowired
	private ReservationManager reservationManager;
	
	@Autowired
	private AllocationManager allocationManager;
	
	@Test
	public void testCreateAllocation() {
		SeatBlock block = new SeatBlock();
		block.setStartSeat(1);
		block.setEndSeat(10);
		CacheKey key = new CacheKey((long) 3, (long) 1);
		block.setKey(key);
		
		Allocation allocation = allocationManager.createAllocation(block);
		Assert.assertEquals(1, allocation.getStartSeat());
		Assert.assertEquals(10, allocation.getEndSeat());
		Assert.assertEquals(10, allocation.getQuantity());
		Assert.assertEquals(allocation.getRow().getId(), block.getKey().getRowId(), 0);
		Assert.assertEquals(allocation.getShow().getId(), block.getKey().getShowId(), 0);
	}
	
	@Test
	public void testPersistChanges() {
		ConcurrentMapCache reservationsCache = (ConcurrentMapCache)	cacheManager.getCache("reservations");
		reservationManager.findContiguousSeats((long) 3, (long) 100, 10);
		CacheKey key = new CacheKey((long) 3, (long) 1);
		
		RowReservation reservation = (RowReservation) reservationsCache.get(key).get();
		SeatBlock block = reservation.getReservedSeats().getFirst();
		allocationManager.persistChanges(block);
		
		reservation = (RowReservation) reservationsCache.get(key).get();
		block = reservation.getReservedSeats().getFirst();
		Assert.assertEquals(1, block.getStartSeat());
		Assert.assertEquals(10, block.getEndSeat());
		Assert.assertEquals(true, block.isPurchased());
	}
	
	@Test
	public void testFinalizeReservations() {
		reservationManager.findContiguousSeats((long) 3, (long) 100, 10);
		reservationManager.findContiguousSeats((long) 3, (long) 101, 25);
		reservationManager.findContiguousSeats((long) 3, (long) 102, 50);
		
		List<Allocation> allocations = allocationManager.finalizeReservations(reservationManager.getBookingState().getReserved());
		Assert.assertEquals(3, allocations.size());
		Assert.assertEquals(10, allocations.get(0).getQuantity());
		Assert.assertEquals(1, allocations.get(0).getStartSeat());
		Assert.assertEquals(10, allocations.get(0).getEndSeat());
		Assert.assertEquals(3, allocations.get(0).getShow().getId(), 0);
		Assert.assertEquals(1, allocations.get(0).getRow().getId(), 0);
		Assert.assertEquals(25, allocations.get(1).getQuantity());
		Assert.assertEquals(1, allocations.get(1).getStartSeat());
		Assert.assertEquals(25, allocations.get(1).getEndSeat());
		Assert.assertEquals(3, allocations.get(1).getShow().getId(), 0);
		Assert.assertEquals(51, allocations.get(1).getRow().getId(), 0);
		Assert.assertEquals(50, allocations.get(2).getQuantity());
		Assert.assertEquals(1, allocations.get(2).getStartSeat());
		Assert.assertEquals(50, allocations.get(2).getEndSeat());
		Assert.assertEquals(3, allocations.get(2).getShow().getId(), 0);
		Assert.assertEquals(101, allocations.get(2).getRow().getId(), 0);
	}
	
	@Test
	public void testCalculateTotal() {
		
	}
	
}
