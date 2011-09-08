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

	public boolean reservationExists(CacheKey key) {
		
		Long showId = key.getShowId();
		Long sectionId = showDao.getSectionIdByRowId(key.getRowId());
		
		for(SeatBlock block : reserved) {
			if(block.getKey().getShowId() == showId) {
				if(showDao.getSectionIdByRowId(block.getKey().getRowId()) == sectionId) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	public void addCategoryRequest(PriceCategoryRequest categoryRequest) {
		
		for(PriceCategoryRequest priceCategoryRequest : categoryRequests) {
			if(priceCategoryRequest.getPriceCategoryId() == categoryRequest.getPriceCategoryId()) {
				priceCategoryRequest = categoryRequest;	
			}
			else
				this.categoryRequests.add(categoryRequest);
		}
	}
	
	@PreDestroy
	public void cleanup() {
		
		for(SeatBlock block : this.getReserved()) {
			Long showId = block.getKey().getShowId();
			Long rowId = block.getKey().getRowId();
			reservationManager.removeSeatReservation(showId, rowId);
			this.categoryRequests.clear();
		}
		
		return;
	}
	
	public void addAllocation(Allocation allocation) {
		this.allocations.add(allocation);
		return;
	}
}
