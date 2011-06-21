package org.jboss.spring.ticketmonster.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Lookup table containing event categories
 * 
 * @author Shane Bryzak
 *
 */

@Entity
public class EventCategory implements Serializable
{
   private static final long serialVersionUID = 2125778126462925768L;
   
   private Long id;
   private String description;
   
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
   
   public String getDescription()
   {
      return description;
   }
   
   public void setDescription(String description)
   {
      this.description = description;
   }

}
