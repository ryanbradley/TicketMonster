package org.jboss.spring.ticketmonster.mvc;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.spring.ticketmonster.domain.Allocation;
import org.jboss.spring.ticketmonster.domain.BookingRequest;
import org.jboss.spring.ticketmonster.domain.CacheKey;
import org.jboss.spring.ticketmonster.domain.PriceCategory;
import org.jboss.spring.ticketmonster.domain.Reservation;
import org.jboss.spring.ticketmonster.domain.Section;
import org.jboss.spring.ticketmonster.domain.SectionRequest;
import org.jboss.spring.ticketmonster.domain.Show;
import org.jboss.spring.ticketmonster.repo.ShowDao;
import org.jboss.spring.ticketmonster.service.ReservationManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/bookings")
public class BookingFormController {
	
	protected final Log logger = LogFactory.getLog(getClass());
	
	@Autowired
	private ShowDao showDao;
	
	@Autowired
	private ReservationManager reservationManager;
	
	@Autowired
	private CacheManager cacheManager;
	
	@RequestMapping(value = "/{id}", method=RequestMethod.GET)
	public String viewShow(@PathVariable("id") Long id, Model model) {
		Show show = showDao.getShow(id);
		model.addAttribute("show", show);
		
		Long eventId = show.getEvent().getId();
		Long venueId = show.getVenue().getId();
		
		logger.info("Retrieving all PriceCategory objects for the Show specified by the id parameter");
		List<PriceCategory> categories = showDao.getCategories(eventId, venueId);
		model.addAttribute("categories", categories);
		
		logger.info("Create a new BookingRequest object, set the Show id and PriceCategory list, and initialize the list PriceCategoryRequest objects");
		BookingRequest bookingRequest = new BookingRequest();
		bookingRequest.setShowId(id);
		bookingRequest.initializeRequest(categories);
		model.addAttribute("bookingRequest", bookingRequest);
		
		logger.info("Return a web view displaying all the details for that Show.");
		return "showDetails";		
	}
	
	@RequestMapping(method=RequestMethod.POST)
	public String onSubmit(BookingRequest command, Model model) {
		logger.info("From a provided list of PriceCategoryRequest objects (provided by the BookingRequest object), create SectionRequest objects.");
		List<SectionRequest> sectionRequests = reservationManager.createSectionRequests(command);
		logger.info("Retrieve contiguous groups of seats for each section in the populated list of SectionRequest objects");
		List<Allocation> allocations = reservationManager.reserveSeats(sectionRequests);
		
		Reservation reservation = new Reservation();
		CacheKey key = new CacheKey(command.getShowId(), allocations.get(0).getUser().getId());
		reservation.setKey(key);
		reservation.setAllocations(allocations);
		
		// Call to a void method which updates the cache based on the list of Allocation objects?
		ConcurrentMapCache reservationsCache = this.getCache();
		reservationsCache.put(key, reservation);
		
		// In the future, the view returned should be a payment page.
		return "showDetails";
	}
	
	@RequestMapping(value = "/allocate", method=RequestMethod.GET, produces = "application/json")
	public boolean updateAllocation(Long showId, Long priceCategoryId, int quantity) {
		ConcurrentMapCache reservationsCache = this.getCache();
		CacheKey key = new CacheKey(showId, (long)1);
		Reservation reservation = (Reservation) reservationsCache.get(key);
		Section section = showDao.getSectionbyPriceCategory(priceCategoryId);
		
		for(Allocation allocation : reservation.getAllocations()) {
			if(section == allocation.getRow().getSection()) {
				if(allocation.getRow().getCapacity() < quantity) {
					return false;
				}
				else if((allocation.getRow().getCapacity()-allocation.getStartSeat()) <= quantity) {
					allocation.setAssigned(new Date());
					allocation.setEndSeat(allocation.getStartSeat()+quantity);
					allocation.setQuantity(quantity);
					reservationsCache.put(key, reservation);
					return true;
				}
			}
		}
		
		return false;
	}
	
	public ConcurrentMapCache getCache() {
		return (ConcurrentMapCache) cacheManager.getCache("reservations");
	}
}
