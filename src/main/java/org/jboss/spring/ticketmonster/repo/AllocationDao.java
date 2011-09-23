package org.jboss.spring.ticketmonster.repo;

import java.util.List;

import org.jboss.spring.ticketmonster.domain.Allocation;
import org.jboss.spring.ticketmonster.domain.SeatBlock;

/**
 * Interface for data access related to either recently made allocations or allocations made in a previous session.
 * 
 * @author Ryan Bradley
 *
 */

public interface AllocationDao {
	
	void persistAllocation(Allocation allocation);
	
	List<Allocation> getAllocations();
	
	void populateCache(Long showId, Long rowId);
	
	void insertSeatBlock(Allocation allocation);
	
	SeatBlock createSeatBlock(Allocation allocation);

}
