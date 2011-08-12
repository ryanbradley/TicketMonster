package org.jboss.spring.ticketmonster.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.jboss.spring.ticketmonster.domain.Allocation;
import org.jboss.spring.ticketmonster.domain.BookingRequest;
import org.jboss.spring.ticketmonster.domain.CacheKey;
import org.jboss.spring.ticketmonster.domain.PriceCategory;
import org.jboss.spring.ticketmonster.domain.PriceCategoryRequest;
import org.jboss.spring.ticketmonster.domain.RowAllocation;
import org.jboss.spring.ticketmonster.domain.SeatBlock;
import org.jboss.spring.ticketmonster.domain.Section;
import org.jboss.spring.ticketmonster.domain.SectionRequest;
import org.jboss.spring.ticketmonster.domain.SectionRow;
import org.jboss.spring.ticketmonster.domain.Show;
import org.jboss.spring.ticketmonster.domain.User;
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
	
	// User member is not necessary once full user functionality has been implemented.	
	private User user;
	
	private static final boolean TEMPORARY = false;
	
	public SimpleReservationManager() {
		user = new User();
		user.setUsername("sbryzak");
		user.setId((long) 1);
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
	
	public void reserveSeats(List<SectionRequest> sectionRequests) {
		
		for(SectionRequest sectionRequest : sectionRequests) {
			this.findContiguousSeats(sectionRequest);
		}
	}

	@SuppressWarnings("unchecked")
	public Allocation findContiguousSeats(SectionRequest sectionRequest) {
		Section section = entityManager.find(Section.class, sectionRequest.getSectionId());
		
		Query query = entityManager.createQuery("select r from SectionRow r where r.section = :section and r.capacity >= :quantity");
		query.setParameter("section", section);
		query.setParameter("quantity", sectionRequest.getQuantity());
		List<SectionRow> rows = query.getResultList();
		
		Long showId = sectionRequest.getShowId();
		
		ConcurrentMapCache reservationsCache = (ConcurrentMapCache) cacheManager.getCache("reservations");
		
		for(SectionRow row : rows) {
			CacheKey key = new CacheKey(showId, row.getId());
			RowAllocation allocated = (RowAllocation) reservationsCache.get(key);
			LinkedList<SeatBlock> allocatedSeats = allocated.getAllocatedSeats();
			boolean first = true;
			
			SeatBlock secondBlock = new SeatBlock();
			
			for(SeatBlock firstBlock : allocatedSeats) {	
				if(first == true) {
					first = false;
					continue;
				}
				
				if(firstBlock.getStartSeat()-secondBlock.getEndSeat() <= sectionRequest.getQuantity()) {
					SeatBlock newBlock = this.allocateSeats(firstBlock, sectionRequest.getQuantity(), key);
					allocatedSeats.add(allocatedSeats.indexOf(secondBlock)+1, newBlock);
					allocated.setAllocatedSeats(allocatedSeats);
					reservationsCache.put(key, allocated);
					Allocation allocation = this.populateAllocation(sectionRequest.getQuantity(), newBlock);
					return allocation;
				}
				secondBlock = firstBlock;
			}
			
		}

		return null;
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
	public Allocation createAllocation(Long showId, Long priceCategoryId, int quantity) {
		PriceCategory priceCategory = entityManager.find(PriceCategory.class, priceCategoryId);
		Section section = entityManager.find(Section.class, priceCategory.getSection().getId());
		Show show = entityManager.find(Show.class, showId);
		
		Query query = entityManager.createQuery("select r from SectionRow r where r.section = :section and r.capacity >= :quantity");
		query.setParameter("quantity", quantity);
		query.setParameter("section", section);
		List<SectionRow> rows = query.getResultList();
		SectionRow row = rows.get(0);
		
		Allocation allocation = new Allocation();
		allocation.setAssigned(new Date());
		allocation.setStartSeat(1);
		allocation.setEndSeat(quantity);
		allocation.setShow(show);
		allocation.setUser(user);
		allocation.setRow(row);
		allocation.setQuantity(quantity);
		
		return allocation;
	}
	
	public Allocation updateAllocation(Allocation allocation, int quantity) {
		boolean first = true;
		
		ConcurrentMapCache reservationsCache = (ConcurrentMapCache) cacheManager.getCache("reservations");
		CacheKey key = new CacheKey(allocation.getShow().getId(), allocation.getRow().getId());
		RowAllocation allocated = (RowAllocation) reservationsCache.get(key);
		LinkedList<SeatBlock> allocatedSeats = allocated.getAllocatedSeats();
		SeatBlock secondBlock = new SeatBlock();
		
		for(SeatBlock firstBlock : allocatedSeats) {	
			if(first == true) {
				first = false;
				continue;
			}
			
			if(firstBlock.getStartSeat()-secondBlock.getEndSeat() <= quantity) {
				SeatBlock newBlock = new SeatBlock();
				newBlock.setStartSeat(secondBlock.getEndSeat()+1);
				newBlock.setEndSeat(newBlock.getStartSeat()+quantity-1);
				newBlock.setKey(key);
				newBlock.setStatus(TEMPORARY);
				allocatedSeats.add(allocatedSeats.indexOf(secondBlock)+1, newBlock);
				allocated.setAllocatedSeats(allocatedSeats);
				reservationsCache.put(key, allocated);
				allocation = this.populateAllocation(quantity, newBlock);
				return allocation;
			}
			secondBlock = firstBlock;
		}
		
		return allocation;
	}
	
	public Allocation populateAllocation(int quantity, SeatBlock block) {
		Allocation allocation = new Allocation();
		allocation.setAssigned(new Date());
		allocation.setEndSeat(block.getEndSeat());
		allocation.setQuantity(quantity);
		
		Long rowId = block.getKey().getRowId();
		SectionRow row = entityManager.find(SectionRow.class, rowId);
		allocation.setRow(row);
		
		Long showId = block.getKey().getShowId();
		Show show = entityManager.find(Show.class, showId);
		allocation.setShow(show);
		
		allocation.setStartSeat(block.getStartSeat());
		allocation.setUser(user);
		
		return allocation;
	}

}
