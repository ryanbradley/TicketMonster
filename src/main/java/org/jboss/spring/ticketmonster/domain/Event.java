package org.jboss.spring.ticketmonster.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * Represents an event, which may have multiple shows with different dates and venues.
 * 
 * @author Shane Bryzak
 *
 */

@Entity
public class Event implements Serializable
{
   private static final long serialVersionUID = -7237875436163170627L;
   
   private Long id;
   private String name;
   private Document description;
   private Date startDate;
   private Date endDate;
   private EventCategory category;
   private boolean major;

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
   
   @ManyToOne
   @JoinColumn(name = "DOCUMENT_ID")
   public Document getDescription()
   {
      return description;
   }
   
   public void setDescription(Document description)
   {
      this.description = description;
   }
   
   public Date getStartDate()
   {
      return startDate;
   }
   
   public void setStartDate(Date startDate)
   {
      this.startDate = startDate;
   }
   
   public Date getEndDate()
   {
      return endDate;
   }
   
   public void setEndDate(Date endDate)
   {
      this.endDate = endDate;
   }
   
   @ManyToOne
   @JoinColumn(name = "CATEGORY_ID")
   public EventCategory getCategory()
   {
      return category;
   }
   
   public void setCategory(EventCategory category)
   {
      this.category = category;
   }
   
   public boolean isMajor()
   {
      return major;
   }
   
   public void setMajor(boolean major)
   {
      this.major = major;
   }
}
