package org.jboss.spring.ticketmonster.repo;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.jboss.spring.ticketmonster.domain.Allocation;
import org.jboss.spring.ticketmonster.domain.BookingRequest;
import org.jboss.spring.ticketmonster.domain.PriceCategoryRequest;
import org.jboss.spring.ticketmonster.domain.SectionRow;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Implementation of the ReservationDao interface. 
 *
 * @author Ryan Bradley
 *
 */

public class ReservationDaoImpl implements ReservationDao {

	@Autowired
	private EntityManager entityManager;

	public List<Allocation> reserveSeats(BookingRequest booking) {
		// TODO Auto-generated method stub
		return null;
	}

	public Allocation findContiguousSeats(PriceCategoryRequest priceCategoryRequest) {
		Query query = entityManager.createQuery("select r from SectionRow r where r.section = :section and r.quantity >= :quantity");
		query.setParameter("section", priceCategoryRequest.getPriceCategory().getSection());
		query.setParameter("quantity", priceCategoryRequest.getQuantity());
		
		SectionRow row = (SectionRow) query.getSingleResult();				
		
		Allocation allocation = new Allocation();
		allocation.setQuantity(priceCategoryRequest.getQuantity());
		allocation.setStartSeat(0);
		allocation.setEndSeat(allocation.getQuantity());
		allocation.setRow(row);

		return allocation;
	}

}
