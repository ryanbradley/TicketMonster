package org.jboss.spring.ticketmonster.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * Contains price categories - each category represents the price for a ticket
 * in a particular section at a particular venue for a particular event, for a
 * particular ticket category.
 *  
 * @author Shane Bryzak
 */

@Entity
public class PriceCategory implements Serializable
{
   private static final long serialVersionUID = 6649855367578381386L;
   
   private Long id;
   private Event event;
   private Venue venue;
   private Section section;
   private TicketCategory category;
   private float price;
   
   @Id
   @GeneratedValue
   public Long getId()
   {
      return id;
   }
   
   public void setId(Long id)
   {
      this.id = id;
   }
   
   @ManyToOne
   @JoinColumn(name = "EVENT_ID")
   public Event getEvent()
   {
      return event;
   }
   
   public void setEvent(Event event)
   {
      this.event = event;
   }
   
   @ManyToOne
   @JoinColumn(name = "VENUE_ID")
   public Venue getVenue()
   {
      return venue;
   }
   
   public void setVenue(Venue venue)
   {
      this.venue = venue;
   }
   
   @ManyToOne
   @JoinColumn(name = "SECTION_ID")
   public Section getSection()
   {
      return section;
   }
   
   public void setSection(Section section)
   {
      this.section = section;
   }
   
   @ManyToOne
   @JoinColumn(name = "CATEGORY_ID")
   public TicketCategory getCategory()
   {
      return category;
   }
   
   public void setCategory(TicketCategory category)
   {
      this.category = category;
   }
   
   public float getPrice()
   {
      return price;
   }
   
   public void setPrice(float price)
   {
      this.price = price;
   }
}
