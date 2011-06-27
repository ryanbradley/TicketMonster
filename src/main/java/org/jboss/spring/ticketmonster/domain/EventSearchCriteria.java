package org.jboss.spring.ticketmonster.domain;

import java.util.Date;

public class EventSearchCriteria {
	
	private EventCategory category;
	private Date startDate;
	private Date endDate;
	private boolean major;
	
	public EventCategory getCategory() {
		return category;
	}
	
	public void setCategory(EventCategory category) {
		this.category = category;
	}
	
	public Date getStartDate() {
		return startDate;
	}
	
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	
	public Date getEndDate() {
		return endDate;
	}
	
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	public boolean isMajor() {
		return major;
	}
	
	public void setMajor(boolean major) {
		this.major = major;
	}
}
