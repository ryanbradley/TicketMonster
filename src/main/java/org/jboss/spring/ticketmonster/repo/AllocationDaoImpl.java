package org.jboss.spring.ticketmonster.repo;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.jboss.spring.ticketmonster.domain.Allocation;
import org.jboss.spring.ticketmonster.domain.CacheKey;
import org.jboss.spring.ticketmonster.domain.RowReservation;
import org.jboss.spring.ticketmonster.domain.SeatBlock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCache;

/**
 * Implementation of the AllocationDao interface.
 * 
 * @author Ryan Bradley
 *
 */

public class AllocationDaoImpl implements AllocationDao {

	@Autowired
	private EntityManager entityManager;
	
	@Autowired
	private CacheManager cacheManager;
	
	private static final boolean PURCHASED = true;
	
	public void persistAllocation(Allocation allocation) {
		entityManager.persist(allocation);

		return;
	}
	
	@SuppressWarnings("unchecked")
	public List<Allocation> getAllocations() {
		Query query = entityManager.createQuery("select a from Allocation a");
		List<Allocation> allocations = (List<Allocation>) query.getResultList();
		return allocations;
	}
	
	public void populateCache() {
		List<Allocation> allocations = this.getAllocations();
		
		for(Allocation allocation : allocations) {
			this.insertSeatBlock(allocation);
		}
		
		return;
	}
	
	public void insertSeatBlock(Allocation allocation) {
		ConcurrentMapCache reservationsCache = (ConcurrentMapCache) cacheManager.getCache("reservations");
		CacheKey key = new CacheKey(allocation.getShow().getId(), allocation.getRow().getId());
		
		RowReservation reservation = (RowReservation) reservationsCache.get(key).get();
		LinkedList<SeatBlock> reserved = reservation.getReservedSeats();
		
		for(SeatBlock block : reserved) {
			if(reserved.isEmpty()) {
				SeatBlock newBlock = this.createSeatBlock(allocation);
				reserved.add(newBlock);
				reservation.setReservedSeats(reserved);
				reservationsCache.put(key, reservation);
				return;
			}

			else if(block == reserved.getLast()) {
				SeatBlock newBlock = this.createSeatBlock(allocation);
				reserved.add(newBlock);
				reservation.setReservedSeats(reserved);
				reservationsCache.put(key, reservation);				
				return;
			}

			else {
				SeatBlock beforeBlock = reserved.get(reserved.indexOf(block)-1);
				
				if((beforeBlock.getEndSeat() < allocation.getStartSeat()) && (block.getStartSeat() > allocation.getEndSeat())) {
					SeatBlock newBlock = this.createSeatBlock(allocation);
					reserved.add(reserved.indexOf(beforeBlock)+1, newBlock);
					reservation.setReservedSeats(reserved);
					reservationsCache.put(key, reservation);
					return;
				}
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
