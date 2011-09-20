package org.jboss.spring.ticketmonster.domain;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PreDestroy;

import org.jboss.spring.ticketmonster.repo.ShowDao;
import org.jboss.spring.ticketmonster.service.ReservationManager;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Class holding all the allocations made by the user in the current booking session.
 * 
 * @author Ryan Bradley
 *
 */

public class BookingState {

	@Autowired
	private ShowDao showDao;
	
	@Autowired
	private ReservationManager reservationManager;
	
	private User user;
	
	private List<Allocation> allocations;
	
	private List<SeatBlock> reserved;
	
	private List<PriceCategoryRequest> categoryRequests;
	
	public BookingState() {
		this.reserved = new ArrayList<SeatBlock>();
		this.allocations = new ArrayList<Allocation>();
		this.setCategoryRequests(new ArrayList<PriceCategoryRequest>());
		this.user = new User();
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<Allocation> getAllocations() {
		return allocations;
	}

	public void setAllocations(List<Allocation> allocations) {
		this.allocations = allocations;
	}

	public List<SeatBlock> getReserved() {
		return reserved;
	}

	public void setReserved(List<SeatBlock> reserved) {
		this.reserved = reserved;
	}
	
	public List<PriceCategoryRequest> getCategoryRequests() {
		return categoryRequests;
	}

	public void setCategoryRequests(List<PriceCategoryRequest> categoryRequests) {
		this.categoryRequests = categoryRequests;
	}

	public void addSeatBlock(SeatBlock block) {
		this.reserved.add(block);
	}

	public boolean reservationExists(Long showId, Long sectionId) {
		
		for(SeatBlock block : this.reserved) {
			if(block.getKey().getShowId().intValue() == showId.intValue()) {
				if(showDao.getSectionIdByRowId(block.getKey().getRowId()).intValue() == sectionId.intValue()) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	public void removeReservation(SeatBlock block) {
		this.reserved.remove(block);
		return;
	}
	
	public void addCategoryRequest(PriceCategoryRequest categoryRequest) {
		
		for(PriceCategoryRequest priceCategoryRequest : categoryRequests) {
			if(priceCategoryRequest.getPriceCategoryId() == categoryRequest.getPriceCategoryId()) {
				priceCategoryRequest = categoryRequest;
				return;
			}
		}
		
		this.categoryRequests.add(categoryRequest);
		return;
	}
	
	public void clear() {
		this.reserved.clear();
		this.categoryRequests.clear();
	}
	
	@PreDestroy
	public void cleanup() {
		
		for(SeatBlock block : this.getReserved()) {
			Long showId = block.getKey().getShowId();
			Long rowId = block.getKey().getRowId();
			reservationManager.removeSeatReservation(showId, rowId);
		}
		
		this.categoryRequests.clear();
		this.reserved.clear();
		
		return;
	}
	
	public void addAllocation(Allocation allocation) {
		if(allocation != null) {
			this.allocations.add(allocation);
		}
	
		return;
	}
}
