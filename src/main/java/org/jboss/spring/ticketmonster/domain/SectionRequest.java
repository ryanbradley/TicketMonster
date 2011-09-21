package org.jboss.spring.ticketmonster.domain;

/**
 * Class to merge PriceCategoryRequest objects for the same section, but different TicketCategory objects, into one object for allocation purposes.
 * 
 * @author Ryan Bradley
 *
 */

public class SectionRequest {

	private Long sectionId;
	
	private Long showId;

	private int quantity;
	
	public Long getSectionId() {
		return sectionId;
	}
	
	public void setSectionId(PriceCategoryRequest one) {
		this.sectionId = one.getPriceCategory().getSection().getId();
	}
	
	public Long getShowId() {
		return showId;
	}

	public void setShowId(Long showId) {
		this.showId = showId;
	}

	public int getQuantity() {
		return quantity;
	}
	
	public void setQuantity(PriceCategoryRequest one) {
		this.quantity = one.getQuantity();
	}

	public void addQuantity(PriceCategoryRequest categoryRequest) {
		this.quantity += categoryRequest.getQuantity();		
	}

	public void setSectionId(Long sectionId) {
		this.sectionId = sectionId;		
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
		
}
