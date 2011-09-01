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
		
		for(SeatBlock block : reserved) {
			if(block.getKey().getShowId() == key.getShowId()) {
				if(showDao.getSectionIdByRowId(block.getKey().getRowId()) == showDao.getSectionIdByRowId(key.getRowId())) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	@PreDestroy
	public void cleanup() {
		
		for(SeatBlock block : this.getReserved()) {
			Long showId = block.getKey().getShowId();
			Long rowId = block.getKey().getRowId();
			reservationManager.removeSeatReservation(showId, rowId);
		}
		
		return;
	}
}
