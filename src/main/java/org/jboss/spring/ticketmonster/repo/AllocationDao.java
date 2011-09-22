package org.jboss.spring.ticketmonster.repo;

import java.util.List;

import org.jboss.spring.ticketmonster.domain.Allocation;
import org.jboss.spring.ticketmonster.domain.SeatBlock;
import org.springframework.transaction.annotation.Transactional;

/**
 * Interface for data access related to either recently made allocations or allocations made in a previous session.
 * 
 * @author Ryan Bradley
 *
 */

@Transactional
public interface AllocationDao {
	
	void persistAllocation(Allocation allocation);
	
	List<Allocation> getAllocations();
	
	void populateCache();
	
	void insertSeatBlock(Allocation allocation);
	
	SeatBlock createSeatBlock(Allocation allocation);

}
