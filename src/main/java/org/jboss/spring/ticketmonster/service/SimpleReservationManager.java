package org.jboss.spring.ticketmonster.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.jboss.spring.ticketmonster.domain.Allocation;
import org.jboss.spring.ticketmonster.domain.BookingRequest;
import org.jboss.spring.ticketmonster.domain.PriceCategoryRequest;
import org.jboss.spring.ticketmonster.domain.Section;
import org.jboss.spring.ticketmonster.domain.SectionRequest;
import org.jboss.spring.ticketmonster.domain.SectionRow;
import org.jboss.spring.ticketmonster.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of the ReservationManager interface. 
 *
 * @author Ryan Bradley
 *
 */

@Transactional
public class SimpleReservationManager implements ReservationManager {

	@Autowired
	private EntityManager entityManager;
	
	private User user;
	
	public SimpleReservationManager() {
		user = new User();
		user.setFirstName("sbryzak");
	}

	public List<SectionRequest> createSectionRequests(BookingRequest booking) {
		
		boolean found = false;
		
		List<PriceCategoryRequest> categoryRequests = booking.getCategoryRequests();
		List<SectionRequest> sectionRequests = new ArrayList<SectionRequest>();
		
		for(PriceCategoryRequest categoryRequest : categoryRequests) {
			for(SectionRequest sectRequest : sectionRequests) {
				if(sectRequest.getSectionId() == categoryRequest.getPriceCategory().getSection().getId()) {
					found = true;
					sectRequest.addQuantity(categoryRequest);
					continue;
				}
			}
			if(found == false) {
				SectionRequest sectionRequest = new SectionRequest();
				sectionRequest.setQuantity(categoryRequest);
				sectionRequest.setSectionId(categoryRequest);
				sectionRequests.add(sectionRequest);
			}
			found = false;
		}
		
		return sectionRequests;
	}
	
	public List<Allocation> reserveSeats(List<SectionRequest> sectionRequests) {
		List<Allocation> allocations = new ArrayList<Allocation>();
		
		for(SectionRequest sectionRequest : sectionRequests) {
			Allocation allocation = new Allocation();
			allocation = this.findContiguousSeats(sectionRequest);
			allocations.add(allocation);
		}
		
		return allocations;
	}

	@SuppressWarnings("unchecked")
	public Allocation findContiguousSeats(SectionRequest sectionRequest) {
		Section section = entityManager.find(Section.class, sectionRequest.getSectionId());
		
		Query query = entityManager.createQuery("select r from SectionRow r where r.section = :section and r.capacity >= :quantity");
		query.setParameter("section", section);
		query.setParameter("quantity", sectionRequest.getQuantity());
		List<SectionRow> rows = query.getResultList();
		SectionRow row = rows.get(0);
		
		Allocation allocation = new Allocation();
		allocation.setQuantity(sectionRequest.getQuantity());
		allocation.setAssigned(new Date());
		allocation.setUser(user);
		allocation.setStartSeat(1);
		allocation.setEndSeat(allocation.getQuantity()+1);
		allocation.setRow(row);

		return allocation;
	}

}
