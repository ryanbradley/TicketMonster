package org.jboss.spring.ticketmonster.mvc;

import java.util.List;

import org.jboss.spring.ticketmonster.domain.BookingRequest;
import org.jboss.spring.ticketmonster.domain.PriceCategory;
import org.jboss.spring.ticketmonster.domain.PriceCategoryRequest;
import org.jboss.spring.ticketmonster.domain.Section;
import org.jboss.spring.ticketmonster.domain.Show;
import org.jboss.spring.ticketmonster.repo.ShowDao;
import org.jboss.spring.ticketmonster.service.AllocationManager;
import org.jboss.spring.ticketmonster.service.ReservationManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/bookings")
public class BookingFormController {
	
	@Autowired
	private ShowDao showDao;
	
	@Autowired
	private AllocationManager allocationManager;
	
	@Autowired
	private ReservationManager reservationManager;
	
	@RequestMapping(value = "/{id}", method=RequestMethod.GET)
	public String viewShow(@PathVariable("id") Long id, Model model) {
		Show show = showDao.getShow(id);
		model.addAttribute("show", show);
		
		Long eventId = show.getEvent().getId();
		Long venueId = show.getVenue().getId();
		
		List<PriceCategory> categories = showDao.getCategories(eventId, venueId);
		model.addAttribute("categories", categories);
		
		BookingRequest bookingRequest = new BookingRequest();
		bookingRequest.setShowId(id);
		bookingRequest.initializeRequest(categories);
		model.addAttribute("bookingRequest", bookingRequest);
		
		return "showDetails";		
	}
	
	@RequestMapping(value="/submit", method=RequestMethod.POST)
	public String onSubmit(Model model) {
		allocationManager.finalizeReservations(allocationManager.getBookingState().getReserved());
		model.addAttribute("allocations", allocationManager.getBookingState().getAllocations());
		Double total = allocationManager.calculateTotal(allocationManager.getBookingState().getCategoryRequests());
		model.addAttribute("total", total);
		
		return "checkout";
	}
	
	@RequestMapping(value = "/allocate", method=RequestMethod.GET, produces = "application/json")
	public @ResponseBody boolean updateAllocation(Long showId, Long priceCategoryId, int quantity) {
		boolean success = false;
		
		Long sectionId = showDao.findPriceCategory(priceCategoryId).getSection().getId();
		success = reservationManager.updateSeatReservation(showId, sectionId, quantity);
		
		PriceCategory category = showDao.findPriceCategory(priceCategoryId);
		PriceCategoryRequest categoryRequest = new PriceCategoryRequest(category);
		categoryRequest.setQuantity(quantity);
		
		if(success == true) {
			reservationManager.getBookingState().updateCategoryRequests(priceCategoryId, quantity);
		}
		
		return success;
	}
	
}
