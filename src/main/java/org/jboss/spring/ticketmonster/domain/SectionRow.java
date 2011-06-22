package org.jboss.spring.ticketmonster.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * Represents a single row of seats within a section.  May also be used to 
 * represent a table, for events such as dinner shows. 
 * 
 * Seat allocations within the row are given a number, starting with 1.
 * 
 * @author Shane Bryzak
 *
 */

@Entity
public class SectionRow implements Serializable
{
   private static final long serialVersionUID = 8180924487630451004L;
   
   private Long id;
   private String name;
   private int capacity;
   private Section section;
   
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
   
   public int getCapacity()
   {
      return capacity;
   }
   
   public void setCapacity(int capacity)
   {
      this.capacity = capacity;
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
}
