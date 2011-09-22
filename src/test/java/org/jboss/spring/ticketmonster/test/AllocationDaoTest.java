package org.jboss.spring.ticketmonster.test;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import junit.framework.Assert;

import org.jboss.spring.ticketmonster.domain.Allocation;
import org.jboss.spring.ticketmonster.domain.CacheKey;
import org.jboss.spring.ticketmonster.domain.RowReservation;
import org.jboss.spring.ticketmonster.domain.SeatBlock;
import org.jboss.spring.ticketmonster.domain.SectionRow;
import org.jboss.spring.ticketmonster.domain.Show;
import org.jboss.spring.ticketmonster.domain.User;
import org.jboss.spring.ticketmonster.repo.AllocationDao;
import org.jboss.spring.ticketmonster.repo.ShowDao;
import org.jboss.spring.ticketmonster.repo.UserDao;
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
 * JUnit tests for the implementation of the AllocationDao interface.
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
public class AllocationDaoTest {

	@Autowired
	private AllocationDao allocationDao;
	
	@Autowired
	private ShowDao showDao;
	
	@Autowired
	private UserDao userDao;

	@Autowired
	private AllocationManager allocationManager;
	
	@Autowired
	private CacheManager cacheManager;
	
	@Autowired
	private EntityManager entityManager;
	
	@Autowired
	private ReservationManager reservationManager;
	
	@SuppressWarnings("unchecked")
	@Test
	public void testPersistToDatabase() {
		Allocation allocation = new Allocation();
		User user = userDao.getByName("rbradley");
		Show show = showDao.findShow(3l);
		SectionRow row = showDao.findSectionRow(1l);
		
		allocation.setUser(user);
		allocation.setAssigned(new Date());
		allocation.setShow(show);
		allocation.setRow(row);
		allocation.setQuantity(10);
		allocation.setStartSeat(1);
		allocation.setEndSeat(10);
		
		allocationDao.persistAllocation(allocation);
		
		Query query = entityManager.createQuery("select a from Allocation a");
		List<Allocation> allocations = (List<Allocation>) query.getResultList();
		
		Assert.assertEquals(1, allocations.size());
		Assert.assertEquals(1, allocations.get(0).getStartSeat());
		Assert.assertEquals(10, allocations.get(0).getEndSeat());
		Assert.assertEquals(10, allocations.get(0).getQuantity());
		Assert.assertEquals(row, allocations.get(0).getRow());
		Assert.assertEquals(show, allocations.get(0).getShow());
		Assert.assertEquals("rbradley", allocations.get(0).getUser().getUsername());
		Assert.assertEquals("Ryan", allocations.get(0).getUser().getFirstName());
		Assert.assertEquals("Bradley", allocations.get(0).getUser().getLastName());
	}
	
	@Test
	public void testGetAllocations() {
		reservationManager.findContiguousSeats(3l, 100l, 10);
		reservationManager.findContiguousSeats(3l, 101l, 15);
		reservationManager.findContiguousSeats(3l, 102l, 20);
		
		User user = userDao.getByName("rbradley");
		reservationManager.getBookingState().setUser(user);
		
		allocationManager.finalizeReservations(reservationManager.getBookingState().getReserved());
		allocationManager.persistToDatabase(allocationManager.getBookingState().getAllocations());
		
		List<Allocation> allocations = allocationDao.getAllocations();
		Assert.assertEquals(3, allocations.size());
		Assert.assertEquals(10, allocations.get(0).getQuantity());
		Assert.assertEquals(15, allocations.get(1).getQuantity());
		Assert.assertEquals(20, allocations.get(2).getQuantity());
	}
	
	
	// This test could be made more thorough.
	@Test
	public void testPopulateCache() {
		ConcurrentMapCache reservationsCache = (ConcurrentMapCache) cacheManager.getCache("reservations");

		allocationDao.populateCache();

		RowReservation reservation = (RowReservation) reservationsCache.get(new CacheKey(3l, 1l)).get();
		Assert.assertNotNull(reservation);
		Assert.assertEquals(1, reservation.getReservedSeats().size());
		Assert.assertEquals(1, reservation.getReservedSeats().get(0).getStartSeat());
		Assert.assertEquals(10, reservation.getReservedSeats().get(0).getEndSeat());
		Assert.assertEquals(true, reservation.getReservedSeats().get(0).isPurchased());
	}
	
	@Test
	public void testInsertSeatBlock() {
		ConcurrentMapCache reservationsCache = (ConcurrentMapCache) cacheManager.getCache("reservations");

		Allocation allocation = new Allocation();
		User user = userDao.getByName("rbradley");
		Show show = showDao.findShow(3l);
		SectionRow row = showDao.findSectionRow(1l);
		
		allocation.setUser(user);
		allocation.setAssigned(new Date());
		allocation.setShow(show);
		allocation.setRow(row);
		allocation.setQuantity(10);
		allocation.setStartSeat(11);
		allocation.setEndSeat(20);
		
		allocationDao.insertSeatBlock(allocation);

		RowReservation reservation = (RowReservation) reservationsCache.get(new CacheKey(3l, 1l)).get();
		
		Assert.assertNotNull(reservation);
		Assert.assertEquals(2, reservation.getReservedSeats().size());
		Assert.assertEquals(1, reservation.getReservedSeats().get(0).getStartSeat());
		Assert.assertEquals(10, reservation.getReservedSeats().get(0).getEndSeat());
		Assert.assertEquals(true, reservation.getReservedSeats().get(0).isPurchased());
		Assert.assertEquals(11, reservation.getReservedSeats().get(1).getStartSeat());
		Assert.assertEquals(20, reservation.getReservedSeats().get(1).getEndSeat());
		Assert.assertEquals(true, reservation.getReservedSeats().get(1).isPurchased());
	}
	
	@Test
	public void testCreateSeatBlock() {
		Allocation allocation = new Allocation();
		User user = userDao.getByName("rbradley");
		Show show = showDao.findShow(3l);
		SectionRow row = showDao.findSectionRow(51l);
		
		allocation.setUser(user);
		allocation.setAssigned(new Date());
		allocation.setShow(show);
		allocation.setRow(row);
		allocation.setQuantity(20);
		allocation.setStartSeat(1);
		allocation.setEndSeat(20);
		
		SeatBlock block = allocationDao.createSeatBlock(allocation);
		
		Assert.assertEquals(new CacheKey(3l, 51l), block.getKey());
		Assert.assertEquals(1, block.getStartSeat());
		Assert.assertEquals(20, block.getEndSeat());
		Assert.assertEquals(true, block.isPurchased());
	}
}
