package org.jboss.spring.ticketmonster.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

/**
 * Represents a single venue
 * 
 * @author Shane Bryzak
 *
 */

@Entity
public class Venue implements Serializable
{
   private static final long serialVersionUID = -6588912817518967721L;
   
   private Long id;
   private String name;
   private String address;
   private Document description;
   
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
   
   public String getAddress()
   {
      return address;
   }
   
   public void setAddress(String address)
   {
      this.address = address;
   }
   
   @OneToOne
   @JoinColumn(name = "DESCRIPTION_ID")
   public Document getDescription()
   {
      return description;
   }
   
   public void setDescription(Document description)
   {
      this.description = description;
   }

}
