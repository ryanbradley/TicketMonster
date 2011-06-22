package org.jboss.spring.ticketmonster.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * Represents a single seating layout for a venue.  A venue may be capable of
 * multiple seating layouts, depending on the type of event.  A layout can
 * contain many sections.
 * 
 * @author Shane Bryzak
 *
 */

@Entity
public class VenueLayout implements Serializable
{
   private static final long serialVersionUID = -6988617479016327717L;
   
   private Long id;
   private Venue venue;
   private String name;
   private int capacity;
   
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
   @JoinColumn(name = "VENUE_ID")
   public Venue getVenue()
   {
      return venue;
   }
   
   public void setVenue(Venue venue)
   {
      this.venue = venue;
   }
   
   public String getName()
   {
      return name;
   }
   
   public void setName(String name)
   {
      this.name = name;
   }
   
   public int getCapacity()
   {
      return capacity;
   }
   
   public void setCapacity(int capacity)
   {
      this.capacity = capacity;
   }
}
