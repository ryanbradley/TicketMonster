package org.jboss.spring.ticketmonster.service;

import org.jboss.spring.ticketmonster.domain.PriceCategory;

/**
 * Class representing the booking request for a specific PriceCategory object.
 * 
 * @author Ryan Bradley
 *
 */

public class PriceCategoryRequest {
	
	private Long priceCategoryId;
	
	private int quantity;
	
	private String description;
	
	public PriceCategoryRequest(PriceCategory category) {
		this.priceCategoryId = category.getId();
		this.description = category.getCategory().getDescription();
		this.quantity = 0;
	}

	public Long getPriceCategoryId() {
		return priceCategoryId;
	}

	public void setPriceCategoryId(Long priceCategoryId) {
		this.priceCategoryId = priceCategoryId;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
