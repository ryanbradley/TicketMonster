package org.jboss.spring.ticketmonster.service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.infinispan.manager.CacheContainer;
import org.jboss.spring.ticketmonster.domain.BookingRequest;
import org.jboss.spring.ticketmonster.domain.BookingState;
import org.jboss.spring.ticketmonster.domain.CacheKey;
import org.jboss.spring.ticketmonster.domain.PriceCategoryRequest;
import org.jboss.spring.ticketmonster.domain.RowReservation;
import org.jboss.spring.ticketmonster.domain.SeatBlock;
import org.jboss.spring.ticketmonster.domain.Section;
import org.jboss.spring.ticketmonster.domain.SectionRequest;
import org.jboss.spring.ticketmonster.domain.SectionRow;
import org.jboss.spring.ticketmonster.repo.AllocationDao;
import org.jboss.spring.ticketmonster.repo.ShowDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of the ReservationManager interface. 
 *
 * @author Ryan Bradley
 *
 */

@PreAuthorize("hasRole('ROLE_USER')")
@Transactional
public class SimpleReservationManager implements ReservationManager {

	@Autowired
	private AllocationDao allocationDao;

	@Autowired
	private ShowDao showDao;
	
/*	@Autowired
	private CacheManager cacheManager;*/
	
	@Autowired
	private BookingState bookingState;
	
	@Autowired
	private CacheContainer cacheContainer;
	
	private static final boolean RESERVED = false;
	
	protected final Log logger = LogFactory.getLog(getClass());
	
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
		ConcurrentMapCache reservationsCache = (ConcurrentMapCache) cacheContainer.getCache();
		
		Section section = showDao.findSection(sectionId);
		List<SectionRow> rows = showDao.getRowsBySection(section, quantity);
		
		RowReservation reservation = new RowReservation();
		
		for(SectionRow row : rows) {
			CacheKey key = new CacheKey(showId, row.getId());
			allocationDao.populateCache(showId, row.getId());
			
			if(reservationsCache.get(key) != null) {
				reservation = (RowReservation) reservationsCache.get(key).get();
			}
			else {
				reservation.setCapacity(row.getCapacity());
				reservation.setReservedSeats(new LinkedList<SeatBlock>());
			}
			
			LinkedList<SeatBlock> reservedSeats = reservation.getReservedSeats();
			
			// Case for the first seat reservation in a certain row.
			
			if(reservedSeats.isEmpty()) {
				SeatBlock block = new SeatBlock();
				block.setStartSeat(1);
				block.setEndSeat(quantity);
				block.setPurchased(RESERVED);
				block.setKey(key);
				reservedSeats.add(block);
				bookingState.addSeatBlock(block);
				reservation.setReservedSeats(reservedSeats);
				reservationsCache.put(key, reservation);
				return true;
			}
			
			// Case for the second seat reservation in a certain row.
			
			if(reservedSeats.size() == 1) {
				SeatBlock frontBlock = reservedSeats.get(0);
				if(row.getCapacity() - frontBlock.getEndSeat() >= quantity) {
					SeatBlock block = this.reserveSeats(frontBlock, quantity, key);
					reservedSeats.add(block);
					bookingState.addSeatBlock(block);
					reservation.setReservedSeats(reservedSeats);
					reservationsCache.put(key, reservation);
					return true;
				}
			}
			
			// General case for seat reservation in a certain row.
			
			for(SeatBlock firstBlock : reservedSeats) {
				
				// Check if the block is the last block in the list.
				
				if(firstBlock == reservedSeats.getLast()) {
					if(row.getCapacity() - firstBlock.getEndSeat() >= quantity) {
						SeatBlock block = this.reserveSeats(firstBlock, quantity, key);
						reservedSeats.add(block);
						bookingState.addSeatBlock(block);
						reservation.setReservedSeats(reservedSeats);
						reservationsCache.put(key, reservation);
						return true;
					}
					
					break;
				}
				SeatBlock secondBlock = reservedSeats.get(reservedSeats.indexOf(firstBlock)+1);
				
				if(firstBlock.getStartSeat() - secondBlock.getEndSeat() >= quantity) {
					SeatBlock block = this.reserveSeats(firstBlock, quantity, key);
					reservedSeats.add(reservedSeats.indexOf(secondBlock)+1, block);
					bookingState.addSeatBlock(block);
					reservation.setReservedSeats(reservedSeats);
					reservationsCache.put(key, reservation);
					return true;
				}
			}
			
		}

		return false;
	}
	
	public SeatBlock reserveSeats(SeatBlock frontBlock, int quantity, CacheKey key) {
		SeatBlock block = new SeatBlock();
		
		block.setStartSeat(frontBlock.getEndSeat()+1);
		block.setEndSeat(block.getStartSeat()+quantity-1);
		block.setKey(key);
		block.setPurchased(RESERVED);
		
		return block;
	}
	
	public boolean updateSeatReservation(Long showId, Long sectionId, int quantity) {
		boolean found = false, success = false;
		found = this.bookingState.reservationExists(showId, sectionId);
		
		if(quantity < 0) {
			return false;
		}
		else if(quantity == 0) {
			found = bookingState.reservationExists(showId, sectionId);
			
			if(found == true) {
				this.removeSeatReservation(showId, sectionId);
				return true;
			}
			else {
				return true;
			}
			
		}
		
		else if(quantity > 0 && found == true) {
			SeatBlock block = this.update(showId, sectionId, quantity);
			if(block != null) {
				return true;
			}
			else {
				return false;
			}
		}
		
		// No reservation for the requested section and show is currently in the cache of reservations.
			
		else if(quantity > 0 && found == false) {
			success = this.findContiguousSeats(showId, sectionId, quantity);
		}

		return success;
		
	}
	
	public SeatBlock update(Long showId, Long sectionId, int quantity) {
		ConcurrentMapCache reservationsCache = (ConcurrentMapCache) cacheContainer.getCache();
		
		Long rowId = 0l;
		
		for(SeatBlock block : this.bookingState.getReserved()) {
			if(block.getKey().getShowId().equals(showId)) {
				if(showDao.getSectionIdByRowId(block.getKey().getRowId()).equals(sectionId)) {
					rowId = block.getKey().getRowId();
				}
			}
		}
		
		if(rowId == 0l) {
			return null;
		}
		
		CacheKey key = new CacheKey(showId, rowId);
		RowReservation reservation = new RowReservation();
		LinkedList<SeatBlock> reservedSeats = new LinkedList<SeatBlock>();
		
		if(reservationsCache.get(key) != null) {
			reservation = (RowReservation) reservationsCache.get(key).get();
			reservedSeats = reservation.getReservedSeats();
		}
		
		for(SeatBlock block : reservedSeats) {
			if(bookingState.getReserved().contains(block)) {
				if(reservedSeats.getLast() == block) {
					if((block.getStartSeat()+quantity-1) <= reservation.getCapacity()) {
						block.setEndSeat(block.getStartSeat()+quantity-1);
						reservation.setReservedSeats(reservedSeats);
						reservationsCache.put(key, reservation);
						return block;
					}
					else {
						return null;
					}
				}
				else {
					SeatBlock nextBlock = reservedSeats.get(reservedSeats.indexOf(block)+1);
					if((nextBlock.getStartSeat()-block.getStartSeat()) <= quantity) {
						block.setEndSeat(block.getStartSeat()+quantity-1);
						reservation.setReservedSeats(reservedSeats);
						reservationsCache.put(key, reservation);
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
	
	public void removeSeatReservation(Long showId, Long sectionId)	{
		ConcurrentMapCache reservationsCache = (ConcurrentMapCache) cacheContainer.getCache();
		
		Long rowId = 0l;
		
		for(SeatBlock block : bookingState.getReserved()) {
			if(block.getKey().getShowId().intValue() == showId.intValue()) {
				if(showDao.getSectionIdByRowId(block.getKey().getRowId()).intValue() == sectionId.intValue()) {
					bookingState.removeReservation(block);
					rowId = block.getKey().getRowId();
					break;
				}
			}
		}
			
		CacheKey key = new CacheKey(showId, rowId);
			
		if(rowId == 0l) {
		// In this case, no reservation was found in the booking state to be removed.			
			return;
		}
				
		RowReservation reservation = (RowReservation) reservationsCache.get(key).get();
		LinkedList<SeatBlock> reservedSeats = reservation.getReservedSeats();
		
		for(SeatBlock block : reservedSeats) {
			if(this.bookingState.getReserved().contains(block)) {
				reservedSeats.remove(block);
				reservation.setReservedSeats(reservedSeats);
				reservationsCache.put(key, reservation);
				return;
			}
		}
		
		return;
	}
	
	public boolean checkAvailability(Long showId, Long sectionId, int quantity) {
		boolean available = false;
		ConcurrentMapCache reservationsCache = (ConcurrentMapCache) cacheContainer.getCache();
		
		Long rowId = 0l;
		
		for(SeatBlock block : this.bookingState.getReserved()) {
			if(block.getKey().getShowId().equals(showId)) {
				if(showDao.getSectionIdByRowId(block.getKey().getRowId()).equals(sectionId)) {
					rowId = block.getKey().getRowId();
				}
			}
		}
		
		if(rowId == 0l) {
			Section section = showDao.findSection(sectionId);
			List<SectionRow> rows = showDao.getRowsBySection(section, quantity);
			available = !rows.isEmpty();
			return available;
		}
		
		CacheKey key = new CacheKey(showId, rowId);
		RowReservation reservation = new RowReservation();
		LinkedList<SeatBlock> reservedSeats = new LinkedList<SeatBlock>();
		
		if(reservationsCache.get(key) != null) {
			reservation = (RowReservation) reservationsCache.get(key).get();
			reservedSeats = reservation.getReservedSeats();
		}
		
		for(SeatBlock block : reservedSeats) {
			if(bookingState.getReserved().contains(block)) {
				if(reservedSeats.getLast() == block) {
					if((block.getStartSeat()+quantity-1) <= reservation.getCapacity()) {
						block.setEndSeat(block.getStartSeat()+quantity-1);
						reservation.setReservedSeats(reservedSeats);
						reservationsCache.put(key, reservation);
						return true;
					}
					else {
						return false;
					}
				}
				else {
					SeatBlock nextBlock = reservedSeats.get(reservedSeats.indexOf(block)+1);
					if((nextBlock.getStartSeat()-block.getStartSeat()) <= quantity) {
						block.setEndSeat(block.getStartSeat()+quantity-1);
						reservation.setReservedSeats(reservedSeats);
						reservationsCache.put(key, reservation);
						return true;
					}
					else {
						return false;
					}
				}
			}
		}
		
		return false;
	}
	
}
