package org.jboss.spring.ticketmonster.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * A section is a specific area within a venue layout.  A venue layout may 
 * consist of multiple sections, each with its own pricing scheme. 
 * 
 * @author Shane Bryzak
 *
 */

@Entity
public class Section implements Serializable
{
   private static final long serialVersionUID = 4293585694763708395L;
   
   private Long id;
   
   /**
    * The short name of the section, may be a code such as A12, G7, etc.
    */
   private String name;
   
   /**
    * The description of the section, such as 'Rear Balcony', etc.
    */
   private String description;
   
   /**
    * The total seating capacity of the section
    */
   private int capacity;
   
   /**
    * The layout to which this section belongs
    */
   private VenueLayout layout;
   
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

   public String getName()
   {
      return name;
   }
   
   public void setName(String name)
   {
      this.name = name;
   }
   
   public String getDescription()
   {
      return description;
   }
   
   public void setDescription(String description)
   {
      this.description = description;
   }
   
   public int getCapacity()
   {
      return capacity;
   }
   
   public void setCapacity(int capacity)
   {
      this.capacity = capacity;
   }
   
   @ManyToOne
   @JoinColumn(name = "LAYOUT_ID")
   public VenueLayout getLayout()
   {
      return layout;
   }
   
   public void setLayout(VenueLayout layout)
   {
      this.layout = layout;
   }
}
