package org.jboss.spring.ticketmonster.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * A lookup table containing the various ticket categories.  E.g. Adult, Child,
 * Pensioner, etc.
 * 
 * @author Shane Bryzak
 *
 */

@Entity
public class TicketCategory implements Serializable
{
   private static final long serialVersionUID = 6591486291129475067L;
   
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
