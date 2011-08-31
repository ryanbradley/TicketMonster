package org.jboss.spring.ticketmonster.domain;

import java.util.ArrayList;
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
	
	private List<SeatBlock> reserved;
	
	public BookingState() {
		this.reserved = new ArrayList<SeatBlock>();
		this.user = new User();
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<SeatBlock> getReserved() {
		return reserved;
	}

	public void setReserved(List<SeatBlock> reserved) {
		this.reserved = reserved;
	}
	
	public void addSeatBlock(SeatBlock block) {
		this.reserved.add(block);
	}

	public boolean reservationExists(CacheKey key) {
		boolean found = false;
		
		for(SeatBlock block : reserved) {
			if(block.getKey().getShowId() == key.getShowId()) {
				if(showDao.getSectionIdByRowId(block.getKey().getRowId()) == showDao.getSectionIdByRowId(key.getRowId())) {
					found = true;
				}
			}
		}
		
		return found;
	}
}
