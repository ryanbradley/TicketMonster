package org.jboss.spring.ticketmonster.mvc;

import java.util.List;

import org.jboss.spring.ticketmonster.domain.BookingRequest;
import org.jboss.spring.ticketmonster.domain.PriceCategory;
import org.jboss.spring.ticketmonster.domain.PriceCategoryRequest;
import org.jboss.spring.ticketmonster.domain.Show;
import org.jboss.spring.ticketmonster.domain.User;
import org.jboss.spring.ticketmonster.repo.ShowDao;
import org.jboss.spring.ticketmonster.service.ReservationManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
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
		User user = new User();
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		user.setUsername(username);
		reservationManager.getBookingState().setUser(user);
		
		return "checkout";
	}
	
	@RequestMapping(value = "/allocate", method=RequestMethod.GET, produces = "application/json")
	public @ResponseBody boolean updateAllocation(Long showId, Long priceCategoryId, int quantity) {
		boolean success = false;
		int sectionQuantity = 0;
		
		Long sectionId = showDao.findPriceCategory(priceCategoryId).getSection().getId();
		int previousQuantity = reservationManager.getBookingState().updateCategoryRequests(priceCategoryId, quantity);			
		
		for(PriceCategoryRequest categoryRequest : reservationManager.getBookingState().getCategoryRequests()) {
			if(categoryRequest.getPriceCategory().getSection().getId().equals(sectionId)) {
				sectionQuantity += categoryRequest.getQuantity();
			}
		}
		
		success = reservationManager.updateSeatReservation(showId, sectionId, sectionQuantity);
		
		if(success == false) {
			reservationManager.getBookingState().updateCategoryRequests(priceCategoryId, previousQuantity);
		}
		
		return success;
	}
	
}
