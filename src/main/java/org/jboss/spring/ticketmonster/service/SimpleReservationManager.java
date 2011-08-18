package org.jboss.spring.ticketmonster.service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.jboss.spring.ticketmonster.domain.BookingRequest;
import org.jboss.spring.ticketmonster.domain.CacheKey;
import org.jboss.spring.ticketmonster.domain.PriceCategoryRequest;
import org.jboss.spring.ticketmonster.domain.RowAllocation;
import org.jboss.spring.ticketmonster.domain.SeatBlock;
import org.jboss.spring.ticketmonster.domain.Section;
import org.jboss.spring.ticketmonster.domain.SectionRequest;
import org.jboss.spring.ticketmonster.domain.SectionRow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of the ReservationManager interface. 
 *
 * @author Ryan Bradley
 *
 */

@Transactional
public class SimpleReservationManager implements ReservationManager {

	@Autowired
	private EntityManager entityManager;
	
	@Autowired
	private CacheManager cacheManager;
	
	private static final boolean TEMPORARY = false;

	public List<SectionRequest> createSectionRequests(BookingRequest booking) {
		
		boolean found = false;
		
		List<PriceCategoryRequest> categoryRequests = booking.getCategoryRequests();
		List<SectionRequest> sectionRequests = new ArrayList<SectionRequest>();
		
		for(PriceCategoryRequest categoryRequest : categoryRequests) {
			for(SectionRequest sectRequest : sectionRequests) {
				if(sectRequest.getSectionId() == categoryRequest.getPriceCategory().getSection().getId()) {
					found = true;
					sectRequest.addQuantity(categoryRequest);
					continue;
				}
			}
			if(found == false) {
				SectionRequest sectionRequest = new SectionRequest();
				sectionRequest.setQuantity(categoryRequest);
				sectionRequest.setSectionId(categoryRequest);
				sectionRequests.add(sectionRequest);
			}
			found = false;
		}
		
		return sectionRequests;
	}
	
	public void reserveSeats(List<SectionRequest> sectionRequests) {
		
		for(SectionRequest sectionRequest : sectionRequests) {
			this.findContiguousSeats(sectionRequest.getShowId(), sectionRequest.getSectionId(), sectionRequest.getQuantity());
		}
	}

	@SuppressWarnings("unchecked")
	public boolean findContiguousSeats(Long showId, Long sectionId, int quantity) {
		ConcurrentMapCache reservationsCache = (ConcurrentMapCache) cacheManager.getCache("reservations");
		
		Section section = entityManager.find(Section.class, sectionId);
		Query query = entityManager.createQuery("select r from SectionRow r where r.section = :section and r.capacity >= :quantity");
		query.setParameter("section", section);
		query.setParameter("quantity", quantity);
		List<SectionRow> rows = query.getResultList();
		
		RowAllocation allocation = new RowAllocation();
		
		for(SectionRow row : rows) {
			CacheKey key = new CacheKey(showId, row.getId());
			
			if(reservationsCache.get(key) != null) {
				allocation = (RowAllocation) reservationsCache.get(key).get();
			}
			else {
				allocation.setCapacity(row.getCapacity());
				allocation.setAllocatedSeats(new LinkedList<SeatBlock>());
			}
			
			LinkedList<SeatBlock> allocatedSeats = allocation.getAllocatedSeats();
			
			// Case for the first seat allocation in a certain row.
			
			if(allocatedSeats.isEmpty()) {
				SeatBlock block = new SeatBlock();
				block.setStartSeat(1);
				block.setEndSeat(quantity);
				block.setStatus(TEMPORARY);
				block.setKey(key);
				allocatedSeats.add(block);
				allocation.setAllocatedSeats(allocatedSeats);
				reservationsCache.put(key, allocation);
				return true;
			}
			
			// Case for the second seat allocation in a certain row.
			
			if(allocatedSeats.size()==1) {
				SeatBlock frontBlock = allocatedSeats.get(0);
				if(row.getCapacity()-frontBlock.getEndSeat() >= quantity) {
					SeatBlock allocated = this.allocateSeats(frontBlock, quantity, key);
					allocatedSeats.add(allocated);
					allocation.setAllocatedSeats(allocatedSeats);
					reservationsCache.put(key, allocation);
				}
			}
			
			// General case for seat allocation in a certain row.
			
			for(SeatBlock firstBlock : allocatedSeats) {
				SeatBlock secondBlock = allocatedSeats.get(allocatedSeats.indexOf(firstBlock)+1);
				
				if(firstBlock.getStartSeat()-secondBlock.getEndSeat() <= quantity) {
					SeatBlock newBlock = this.allocateSeats(firstBlock, quantity, key);
					allocatedSeats.add(allocatedSeats.indexOf(secondBlock)+1, newBlock);
					allocation.setAllocatedSeats(allocatedSeats);
					reservationsCache.put(key, allocation);
					return true;
				}
			}
			
		}

		return false;
	}
	
	public SeatBlock allocateSeats(SeatBlock frontBlock, int quantity, CacheKey key) {
		SeatBlock block = new SeatBlock();
		
		block.setStartSeat(frontBlock.getEndSeat()+1);
		block.setEndSeat(block.getStartSeat()+quantity-1);
		block.setKey(key);
		block.setStatus(TEMPORARY);
		
		return block;
	}
	
	@SuppressWarnings("unchecked")
	public boolean updateSeatAllocation(Long showId, Long sectionId, int quantity) {
		boolean found = false, success = false;
		ConcurrentMapCache reservationsCache = (ConcurrentMapCache) cacheManager.getCache("reservations");
		
		Section section = entityManager.find(Section.class, sectionId);

		Query query = entityManager.createQuery("select r from SectionRow r where r.section = :section");
		query.setParameter("section", section);
		List<SectionRow> rows = query.getResultList();
		
		// Search the user's session for a previous RowAllocation in this section.
		for(SectionRow row : rows) {
			CacheKey key = new CacheKey(showId, row.getId());
			RowAllocation allocated = (RowAllocation) reservationsCache.get(key);
			for(SeatBlock block : allocated.getAllocatedSeats()) {
				/*
				 * Check if the current User's HttpSession has already allocated a block with that 
				 * 
				 * 	if(session.getAllocatedSeats().contains(block)) {
				 * 		found = true;
				 * 	}
				 */
			}
		}
		
		// If the section is found in the User's current session, then try to expand the allocation.
		if(found == true) {
				/* 
				*	Call to update method.
				*
				*	if(block != null) {
				*		success = true;
				*	}
				*	else {
				*		success = false;
				*	}
				*/ 
				
				return success;
		}
		
		// If the section is not found, find a section of contiguous seats and allocate them.
		if(found == false) {
			success = this.findContiguousSeats(showId, sectionId, quantity);
		}
			
		return success;
	}

	// Method cannot be properly implemented until support for HttpSession has been added.
	
	public SeatBlock update(Long showId, Long rowId, int quantity) {
		 CacheKey key = new CacheKey(showId, rowId);
		 ConcurrentMapCache reservationsCache = (ConcurrentMapCache) cacheManager.getCache("reservations");
		 
		 RowAllocation allocation = (RowAllocation) reservationsCache.get(key);
		 LinkedList<SeatBlock> allocatedSeats = allocation.getAllocatedSeats();

		boolean first = true;
			
		SeatBlock secondBlock = new SeatBlock();
			
		for(SeatBlock firstBlock : allocatedSeats) {	
			if(first == true) {
				first = false;
				continue;
			}
				
			if(firstBlock.getStartSeat()-secondBlock.getEndSeat() <= quantity) {
				SeatBlock newBlock = this.allocateSeats(firstBlock, quantity, key);
				allocatedSeats.add(allocatedSeats.indexOf(secondBlock)+1, newBlock);
				allocation.setAllocatedSeats(allocatedSeats);
				reservationsCache.put(key, allocation);
				return newBlock;
			}
			secondBlock = firstBlock;
		}
		
		return null;
	}

}
