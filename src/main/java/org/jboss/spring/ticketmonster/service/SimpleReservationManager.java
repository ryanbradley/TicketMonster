package org.jboss.spring.ticketmonster.service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.jboss.spring.ticketmonster.domain.BookingRequest;
import org.jboss.spring.ticketmonster.domain.BookingState;
import org.jboss.spring.ticketmonster.domain.CacheKey;
import org.jboss.spring.ticketmonster.domain.PriceCategoryRequest;
import org.jboss.spring.ticketmonster.domain.RowAllocation;
import org.jboss.spring.ticketmonster.domain.SeatBlock;
import org.jboss.spring.ticketmonster.domain.Section;
import org.jboss.spring.ticketmonster.domain.SectionRequest;
import org.jboss.spring.ticketmonster.domain.SectionRow;
import org.jboss.spring.ticketmonster.repo.ShowDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCache;

/**
 * Implementation of the ReservationManager interface. 
 *
 * @author Ryan Bradley
 *
 */

public class SimpleReservationManager implements ReservationManager {

	@Autowired
	private ShowDao showDao;
	
	@Autowired
	private CacheManager cacheManager;
	
	@Autowired
	private BookingState bookingState;
	
	private static final boolean TEMPORARY = false;

	public BookingState getBookingState() {
		return bookingState;
	}

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

	public boolean findContiguousSeats(Long showId, Long sectionId, int quantity) {
		ConcurrentMapCache reservationsCache = (ConcurrentMapCache) cacheManager.getCache("reservations");
		
		Section section = showDao.findSection(sectionId);
		List<SectionRow> rows = showDao.getRowsBySection(section, quantity);
		
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
				block.setPurchased(TEMPORARY);
				block.setKey(key);
				allocatedSeats.add(block);
				bookingState.addSeatBlock(block);
				allocation.setAllocatedSeats(allocatedSeats);
				reservationsCache.put(key, allocation);
				return true;
			}
			
			// Case for the second seat allocation in a certain row.
			
			if(allocatedSeats.size() == 1) {
				SeatBlock frontBlock = allocatedSeats.get(0);
				if(row.getCapacity() - frontBlock.getEndSeat() >= quantity) {
					SeatBlock block = this.allocateSeats(frontBlock, quantity, key);
					allocatedSeats.add(block);
					bookingState.addSeatBlock(block);
					allocation.setAllocatedSeats(allocatedSeats);
					reservationsCache.put(key, allocation);
					return true;
				}
			}
			
			// General case for seat allocation in a certain row.
			
			for(SeatBlock firstBlock : allocatedSeats) {
				
				// Check if the block is the last block in the list.
				
				if(firstBlock == allocatedSeats.getLast()) {
					if(row.getCapacity() - firstBlock.getEndSeat() >= quantity) {
						SeatBlock block = this.allocateSeats(firstBlock, quantity, key);
						allocatedSeats.add(block);
						bookingState.addSeatBlock(block);
						allocation.setAllocatedSeats(allocatedSeats);
						reservationsCache.put(key, allocation);
						return true;
					}
					
					break;
				}
				SeatBlock secondBlock = allocatedSeats.get(allocatedSeats.indexOf(firstBlock)+1);
				
				if(firstBlock.getStartSeat() - secondBlock.getEndSeat() >= quantity) {
					SeatBlock block = this.allocateSeats(firstBlock, quantity, key);
					allocatedSeats.add(allocatedSeats.indexOf(secondBlock)+1, block);
					bookingState.addSeatBlock(block);
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
		block.setPurchased(TEMPORARY);
		
		return block;
	}
	
	public boolean updateSeatAllocation(Long showId, Long sectionId, int quantity) {
		boolean found = false, success = false;
		
		Section section = showDao.findSection(sectionId);
		List<SectionRow> rows = showDao.getRowsBySection(section, quantity);
		
		if(quantity < 0) {
			return success;
		}
		
		for(SectionRow row : rows) {
			CacheKey key = new CacheKey(showId, row.getId());
			found = bookingState.allocationExists(key);
			if(found == true) {
				if(quantity == 0) {
					this.removeSeatAllocation(showId, row.getId());
					return true;
				}
				
				SeatBlock block = this.update(showId, row.getId(), quantity);
				if(block != null) {
					success = true;
				}
				else {
					success = false;
				}
			}
		}
		
		if(found == false) {
			success = this.findContiguousSeats(showId, sectionId, quantity);
		}
		
		return success;
	}
	
	public SeatBlock update(Long showId, Long rowId, int quantity) {
		ConcurrentMapCache reservationsCache = (ConcurrentMapCache) cacheManager.getCache("reservations");
		
		CacheKey key = new CacheKey(showId, rowId);
		RowAllocation allocation = new RowAllocation();
		LinkedList<SeatBlock> allocatedSeats = new LinkedList<SeatBlock>();
		
		if(reservationsCache.get(key) != null) {
			allocation = (RowAllocation) reservationsCache.get(key).get();
			allocatedSeats = allocation.getAllocatedSeats();
		}
		
		for(SeatBlock block : allocatedSeats) {
			if(bookingState.getAllocated().contains(block)) {
				if(allocatedSeats.getLast() == block) {
					if((block.getStartSeat()+quantity-1) <= allocation.getCapacity()) {
						block.setEndSeat(block.getStartSeat()+quantity-1);
						allocation.setAllocatedSeats(allocatedSeats);
						reservationsCache.put(key, allocation);
						return block;
					}
					else {
						return null;
					}
				}
				else {
					SeatBlock nextBlock = allocatedSeats.get(allocatedSeats.indexOf(block)+1);
					if((nextBlock.getStartSeat()-block.getStartSeat()) <= quantity) {
						block.setEndSeat(block.getStartSeat()+quantity-1);
						allocation.setAllocatedSeats(allocatedSeats);
						reservationsCache.put(key, allocation);
						return block;
					}
					else {
						return null;
					}
				}
			}
		}
		
		return null;
	}
	
	public void removeSeatAllocation(Long showId, Long rowId)	{
		ConcurrentMapCache reservationsCache = (ConcurrentMapCache) cacheManager.getCache("reservations");
		CacheKey key = new CacheKey(showId, rowId);
		
		if(reservationsCache.get(key) == null) {
			return;
		}
		
		RowAllocation allocation = (RowAllocation) reservationsCache.get(key).get();
		LinkedList<SeatBlock> allocatedSeats = allocation.getAllocatedSeats();
		
		for(SeatBlock block : allocatedSeats) {
			if(this.bookingState.getAllocated().contains(block)) {
				allocatedSeats.remove(block);
				allocation.setAllocatedSeats(allocatedSeats);
				reservationsCache.put(key, allocation);
				return;
			}
		}
		
		return;
	}

}
