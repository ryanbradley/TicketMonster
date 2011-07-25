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
	
	public void setSectionId(PriceCategoryRequest one, PriceCategoryRequest two) {
		if(one.getPriceCategory().getSection().getId() == two.getPriceCategory().getSection().getId())
			this.sectionId = one.getPriceCategory().getSection().getId();
		else
			this.sectionId = (long) -1;
		
	}

	public int getQuantity() {
		return quantity;
	}
	
	public void setQuantity(PriceCategoryRequest one, PriceCategoryRequest two) {
		if(one.getPriceCategory().getSection().getId() == two.getPriceCategory().getSection().getId())
			this.quantity = one.getQuantity() + two.getQuantity();
		else
			this.quantity = 0;
	}
		
}
