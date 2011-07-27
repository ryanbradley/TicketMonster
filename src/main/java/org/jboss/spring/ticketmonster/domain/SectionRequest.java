package org.jboss.spring.ticketmonster.domain;

/**
 * Class to merge PriceCategoryRequest objects for the same section, but different TicketCategory objects, into one object for allocation purposes.
 * 
 * @author Ryan Bradley
 *
 */

public class SectionRequest {

	private Long sectionId;
	
	private int quantity;
	
	public Long getSectionId() {
		return sectionId;
	}
	
	public void setSectionId(PriceCategoryRequest one) {
		this.sectionId = one.getPriceCategory().getSection().getId();
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
		
}
