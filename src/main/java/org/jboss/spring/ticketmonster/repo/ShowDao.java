package org.jboss.spring.ticketmonster.repo;

import java.util.List;

import org.jboss.spring.ticketmonster.domain.PriceCategory;
import org.jboss.spring.ticketmonster.domain.Section;
import org.jboss.spring.ticketmonster.domain.SectionRow;
import org.jboss.spring.ticketmonster.domain.Show;
import org.jboss.spring.ticketmonster.domain.ShowTime;

/**
 * Interface for Show-related database access.
 * 
 * @author Ryan Bradley
 *
 */

public interface ShowDao {

	List<ShowTime> getShowTimes(Long eventId, Long venueId);
	
	Show getShow(Long showId);
	
	List<PriceCategory> getCategories(Long eventId, Long venueId);
	
	Section getSectionByPriceCategory(Long categoryId);
	
	Section findSection(Long sectionId);
	
	Long getSectionIdByRowId(Long rowId);
	
	List<SectionRow> getRowsBySection(Section section, int quantity);	
}
