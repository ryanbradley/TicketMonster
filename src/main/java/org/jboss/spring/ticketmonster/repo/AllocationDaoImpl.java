package org.jboss.spring.ticketmonster.repo;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.spring.ticketmonster.domain.Allocation;
import org.jboss.spring.ticketmonster.domain.CacheKey;
import org.jboss.spring.ticketmonster.domain.RowReservation;
import org.jboss.spring.ticketmonster.domain.SeatBlock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.Cache;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of the AllocationDao interface.
 * 
 * @author Ryan Bradley
 *
 */

@Transactional
public class AllocationDaoImpl implements AllocationDao {

	@Autowired
	private EntityManager entityManager;
	
	@Autowired
	@Qualifier("reservations")
	private Cache reservationsCache;
	
	protected final Log logger = LogFactory.getLog(getClass());
	
	private static final boolean PURCHASED = true;

	@PreAuthorize("hasRole('ROLE_USER')")	
	public void persistAllocation(Allocation allocation) {
		entityManager.merge(allocation);

		return;
	}
	
	@SuppressWarnings("unchecked")
	public List<Allocation> getAllocations() {
		Query query = entityManager.createQuery("select a from Allocation a");
		List<Allocation> allocations = (List<Allocation>) query.getResultList();
		return allocations;
	}
	
	public void populateCache(Long showId, Long rowId) {
		List<Allocation> allocations = this.getAllocations();
		
		for(Allocation allocation : allocations) {
			if(allocation.getShow().getId().equals(showId) && allocation.getRow().getId().equals(rowId)) {	    
			    logger.info("Inserting allocation.");
			    this.insertSeatBlock(allocation);
			}
		}
		
		return;
	}
	
	public void insertSeatBlock(Allocation allocation) {
		CacheKey key = new CacheKey(allocation.getShow().getId(), allocation.getRow().getId());
		RowReservation reservation = new RowReservation();
		LinkedList<SeatBlock> reserved = new LinkedList<SeatBlock>();
		logger.info("In insertSeatBlock() method.");
		
		if(reservationsCache.get(key) != null) {
		    reservation = (RowReservation) reservationsCache.get(key).get();
		    reserved = reservation.getReservedSeats();
		    logger.info("Cache entry is not null.");
		}
		else {
		    reservation.setReservedSeats(reserved);
		    logger.info("Cache entry is null");
		    logger.info("Reserved isEmpty(): " + reserved.isEmpty());
		}

        // If there are no reserved/allocated seats for that row currently in the cache:
        if(reserved.isEmpty()) {
            logger.info("Reserved seats linked list is empty.");                
            SeatBlock newBlock = this.createSeatBlock(allocation);
            reserved.add(newBlock);
            reservation.setReservedSeats(reserved);
            reservationsCache.put(key, reservation);
            return;
        }
		
		for(SeatBlock block : reserved) {
		    logger.info("Entering for loop.");
		    
		    // If the current block is the first in the linked list.
			if(block == reserved.getFirst()) {
                logger.info("First block.");			    
				SeatBlock newBlock = this.createSeatBlock(allocation);
				if(newBlock.getEndSeat() < block.getStartSeat()) {
				    reserved.add(0, newBlock);
				}
				else {
				    reserved.add(newBlock);
				}
	                reservation.setReservedSeats(reserved);
	                reservationsCache.put(key, reservation);		    
				    return;
			}
			
			// If the allocation represents the last block in the row:
			else if(block == reserved.getLast()) {
				SeatBlock newBlock = this.createSeatBlock(allocation);
				if(newBlock.getStartSeat() > block.getEndSeat()) {
    				reserved.add(newBlock);
    				reservation.setReservedSeats(reserved);
    				reservationsCache.put(key, reservation);				
    				return;
				}
			}

			// General case
			else {
				SeatBlock beforeBlock = reserved.get(reserved.indexOf(block)-1);
				
				if((beforeBlock.getEndSeat() < allocation.getStartSeat()) && (allocation.getEndSeat() < block.getStartSeat())) {
					SeatBlock newBlock = this.createSeatBlock(allocation);
					reserved.add(reserved.indexOf(block), newBlock);
					reservation.setReservedSeats(reserved);
					reservationsCache.put(key, reservation);
					return;
				}
				continue;
			}
			
		}
		
		return;
	}
	
	public SeatBlock createSeatBlock(Allocation allocation)	{
		SeatBlock block = new SeatBlock();
		CacheKey key = new CacheKey(allocation.getShow().getId(), allocation.getRow().getId());
		
		block.setKey(key);
		block.setPurchased(PURCHASED);
		block.setStartSeat(allocation.getStartSeat());
		block.setEndSeat(allocation.getEndSeat());
		block.setPurchased(true);
		
		return block;
	}

}
