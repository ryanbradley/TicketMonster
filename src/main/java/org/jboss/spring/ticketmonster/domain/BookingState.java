package org.jboss.spring.ticketmonster.domain;

import java.util.List;

import org.jboss.spring.ticketmonster.repo.ShowDao;
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
	
	private User user;
	
	private List<SeatBlock> allocated;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<SeatBlock> getAllocated() {
		return allocated;
	}

	public void setAllocated(List<SeatBlock> allocated) {
		this.allocated = allocated;
	}
	
	public void addSeatBlock(SeatBlock block) {
		this.allocated.add(block);
	}

	public boolean allocationExists(CacheKey key) {
		boolean found = false;
		
		for(SeatBlock block : allocated) {
			if(block.getKey().getShowId() == key.getShowId()) {
				if(showDao.getSectionIdByRowId(block.getKey().getRowId()) == showDao.getSectionIdByRowId(key.getRowId())) {
					found = true;
				}
			}
		}
		
		return found;
	}
}
