package org.jboss.spring.ticketmonster.domain;

import java.util.ArrayList;
import java.util.List;


/**
 * Class for a booking request, potentially consisting of multiple different PriceCategory objects, for a specific show.
 * 
 * @author Ryan Bradley
 *
 */

public class BookingRequest {
	
	private Long showId;
	
	private List<PriceCategoryRequest> categoryRequests;
	
	public Long getShowId() {
		return showId;
	}

	public void setShowId(Long showId) {
		this.showId = showId;
	}

	public List<PriceCategoryRequest> getCategoryRequests() {
		return categoryRequests;
	}

	public void setCategoryRequests(List<PriceCategoryRequest> categories) {
		this.categoryRequests = categories;
	}
	
	public void initializeRequest(List<PriceCategory> priceCategories) {
		categoryRequests = (List<PriceCategoryRequest>)(new ArrayList<PriceCategoryRequest>());
		
		for(PriceCategory priceCategory : priceCategories) {
			categoryRequests.add(new PriceCategoryRequest(priceCategory));
		}
	}

}
