package org.jboss.spring.ticketmonster.repo;

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
 * Implementation of the ReservationDao interface. 
 *
 * @author Ryan Bradley
 *
 */

@Transactional
public class ReservationDaoImpl implements ReservationDao {

	@Autowired
	private EntityManager entityManager;
	
	private User user;
	
	public ReservationDaoImpl() {
		user = new User();
		user.setFirstName("sbryzak");
	}

	public List<SectionRequest> createSectionRequests(BookingRequest booking) {
		
		List<PriceCategoryRequest> categoryRequests = booking.getCategoryRequests();
		List<PriceCategoryRequest> temp = booking.getCategoryRequests();
		List<SectionRequest> sectionRequests = new ArrayList<SectionRequest>();
		
		for(PriceCategoryRequest categoryRequest : categoryRequests) {
			for(PriceCategoryRequest tempRequest : temp) {
				if(categoryRequest.getPriceCategoryId() != tempRequest.getPriceCategoryId()) {
					if(categoryRequest.getPriceCategory().getSection().getId() == tempRequest.getPriceCategory().getSection().getId()) {
						SectionRequest sectionRequest = new SectionRequest();
						sectionRequest.setQuantity(categoryRequest, tempRequest);
						sectionRequest.setSectionId(categoryRequest, tempRequest);
						sectionRequests.add(sectionRequest);
					}
				}
			}
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

	public Allocation findContiguousSeats(SectionRequest sectionRequest) {
		Section section = entityManager.find(Section.class, sectionRequest.getSectionId());
		
		Query query = entityManager.createQuery("select r from SectionRow r where r.section = :section and r.quantity >= :quantity");
		query.setParameter("section", section);
		query.setParameter("quantity", sectionRequest.getQuantity());
		
		SectionRow row = (SectionRow) query.getSingleResult();				
		
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
