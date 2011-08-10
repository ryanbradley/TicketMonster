package org.jboss.spring.ticketmonster.domain;

import java.util.List;

import javax.persistence.Entity;

/**
 * Contains a List of Allocation objects for a specific Show/User pair, for caching purposes.
 * 
 * @author Ryan Bradley
 *
 */

@Entity
public class Reservation {
	
	private List<Allocation> allocations;

	private CacheKey key;

	public List<Allocation> getAllocations() {
		return allocations;
	}

	public void setAllocations(List<Allocation> allocations) {
		this.allocations = allocations;
	}

	public CacheKey getKey() {
		return key;
	}

	public void setKey(CacheKey key) {
		this.key = key;
	}
}
