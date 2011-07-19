package org.jboss.spring.ticketmonster.service;

import java.util.List;

import org.jboss.spring.ticketmonster.domain.PriceCategory;
import org.jboss.spring.ticketmonster.domain.Show;
import org.jboss.spring.ticketmonster.repo.ShowDao;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Class for a booking request, potentially consisting of multiple different PriceCategory objects, for a specific show.
 * 
 * @author Ryan Bradley
 *
 */

public class BookingRequest {
	
	@Autowired
	private ShowDao showDao;
	
	private Long showId;
	
	private List<PriceCategoryRequest> requests;
	
	public Long getShowId() {
		return showId;
	}

	public void setShowId(Long showId) {
		this.showId = showId;
	}

	public List<PriceCategoryRequest> getRequests() {
		return requests;
	}

	public void setRequests(List<PriceCategoryRequest> requests) {
		this.requests = requests;
	}
	
	public void initializeRequest() {
		Show show = showDao.getShow(showId);
		
		List<PriceCategory> priceCategories = showDao.getCategories(show.getEvent().getId(), show.getVenue().getId());
		
		for(PriceCategory priceCategory : priceCategories) {
			requests.add(new PriceCategoryRequest(priceCategory));
		}
		
		for(PriceCategoryRequest request : this.requests) {
			request.setQuantity(0);
		}
	}
}
