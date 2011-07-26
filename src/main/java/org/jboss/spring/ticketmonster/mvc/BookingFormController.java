package org.jboss.spring.ticketmonster.mvc;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.spring.ticketmonster.domain.BookingRequest;
import org.jboss.spring.ticketmonster.domain.PriceCategory;
import org.jboss.spring.ticketmonster.domain.Show;
import org.jboss.spring.ticketmonster.repo.ShowDao;
import org.springframework.beans.factory.annotation.Autowired;
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
	
	@RequestMapping(method=RequestMethod.POST)
	public String onSubmit(BookingRequest command, Model model) {
		
		// for(PriceCategoryRequest categoryRequest : command.getCategoryRequests()) {
			// int quantity = categoryRequest.getQuantity();
			// Reserve that quantity of seats in the specific category.
			// bookingManager.reserve(categoryRequest);
		// }
		
		return "showDetails";
	}
}
