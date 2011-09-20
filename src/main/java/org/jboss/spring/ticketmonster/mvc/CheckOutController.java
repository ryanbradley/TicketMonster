package org.jboss.spring.ticketmonster.mvc;

import org.jboss.spring.ticketmonster.service.AllocationManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/checkout")
public class CheckOutController {

	@Autowired
	private AllocationManager allocationManager;
	
	@RequestMapping(method=RequestMethod.GET)
	public String checkOut(Model model) {
		Double total = allocationManager.calculateTotal(allocationManager.getBookingState().getCategoryRequests());
		allocationManager.getBookingState().clear();		
		model.addAttribute("total", total);
		model.addAttribute("user", allocationManager.getBookingState().getUser());
		model.addAttribute("allocations", allocationManager.getBookingState().getAllocations());
		
		return "checkout";
	}
}
