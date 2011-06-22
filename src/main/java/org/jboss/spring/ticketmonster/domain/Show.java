package org.jboss.spring.ticketmonster.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * A show is a single instance of an event, at a particular time and venue.
 * 
 * @author Shane Bryzak
 *
 */

@Entity
public class Show implements Serializable
{
   private static final long serialVersionUID = -108405033615497885L;
   
   private Long id;
   private Event event;
   private Venue venue;
   private Date showDate;   
   private VenueLayout venueLayout;
   
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
   
   public Date getShowDate()
   {
      return showDate;
   }
   
   public void setShowDate(Date showDate)
   {
      this.showDate = showDate;
   }
   
   @ManyToOne
   @JoinColumn(name = "LAYOUT_ID")
   public VenueLayout getVenueLayout()
   {
      return venueLayout;
   }
   
   public void setVenueLayout(VenueLayout venueLayout)
   {
      this.venueLayout = venueLayout;
   }
   
}
