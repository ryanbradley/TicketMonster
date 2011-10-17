package org.jboss.spring.ticketmonster.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.infinispan.manager.CacheContainer;
import org.jboss.spring.ticketmonster.domain.Allocation;
import org.jboss.spring.ticketmonster.domain.BookingState;
import org.jboss.spring.ticketmonster.domain.CacheKey;
import org.jboss.spring.ticketmonster.domain.PriceCategoryRequest;
import org.jboss.spring.ticketmonster.domain.RowReservation;
import org.jboss.spring.ticketmonster.domain.SeatBlock;
import org.jboss.spring.ticketmonster.domain.SectionRow;
import org.jboss.spring.ticketmonster.domain.Show;
import org.jboss.spring.ticketmonster.repo.AllocationDao;
import org.jboss.spring.ticketmonster.repo.ShowDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of the AllocationManager interface.
 * 
 * @author Ryan Bradley
 *
 */

@PreAuthorize("hasRole('ROLE_USER')")
@Transactional
public class SimpleAllocationManager implements AllocationManager {

	@Autowired
	private ShowDao showDao;
	
	@Autowired
	private AllocationDao allocationDao;
	
/*	@Autowired
	private CacheManager cacheManager;*/
	
	@Autowired
	private BookingState bookingState;
	
	@Autowired
	private CacheContainer cacheContainer;
	
	public BookingState getBookingState() {
		return bookingState;
	}

	private static final boolean PURCHASED = true;
	
	public List<Allocation> finalizeReservations(List<SeatBlock> reservations) {
		List<Allocation> allocations = new ArrayList<Allocation>();
		
		for(SeatBlock block : reservations) {
			Allocation allocation = this.createAllocation(block);
			allocations.add(allocation);
			bookingState.addAllocation(allocation);
			persistToCache(block);
		}		
		
		return allocations;
	}

	public Allocation createAllocation(SeatBlock block) {
		Allocation allocation = new Allocation();
		allocation.setAssigned(new Date());
		allocation.setStartSeat(block.getStartSeat());
		allocation.setEndSeat(block.getEndSeat());
		
		SectionRow row = showDao.findSectionRow(block.getKey().getRowId());
		allocation.setRow(row);
		
		Show show = showDao.findShow(block.getKey().getShowId());
		allocation.setShow(show);
		
		allocation.setQuantity(block.getEndSeat()-block.getStartSeat()+1);
		allocation.setUser(bookingState.getUser());
		
		return allocation;
	}
	
	public void persistToCache(SeatBlock block) {
		ConcurrentMapCache reservationsCache = (ConcurrentMapCache) cacheContainer.getCache();
		CacheKey key = block.getKey();
		
		RowReservation reservation = (RowReservation) reservationsCache.get(key).get();
		LinkedList<SeatBlock> reservedSeats = reservation.getReservedSeats();
		
		for(SeatBlock reserved : reservedSeats) {
			if(reserved.equals(block)) {
				reserved.setPurchased(PURCHASED);
				continue;
			}
		}
		
		reservation.setReservedSeats(reservedSeats);
		reservationsCache.put(key, reservation);
	}
	
	public Double calculateTotal(List<PriceCategoryRequest> categoryRequests) {
		Double total = 0.00;
		
		for(PriceCategoryRequest categoryRequest : categoryRequests) {
			total += (categoryRequest.getPriceCategory().getPrice())*(categoryRequest.getQuantity());
		}
		
		return total;
	}
	
	public void persistToDatabase(List<Allocation> allocations) {

		for(Allocation allocation : allocations) {
			allocationDao.persistAllocation(allocation);
		}
		return;
	}
	
}
