package org.jboss.spring.ticketmonster.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * Seat allocations for a show. An allocation consists of one or more seats
 * within a SectionRow.
 * 
 * @author Shane Bryzak
 * Code re-used by Ryan Bradley for Spring implementation of TicketMonster application
 */

@Entity
public class Allocation implements Serializable
{
   private static final long serialVersionUID = 8738724150877088864L;
   
   private Long id;
   private Date assigned;
   
   private User user;
   private Show show;
   private SectionRow row;
   private int quantity;
   private int startSeat;
   private int endSeat;
   
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
   
   public Date getAssigned()
   {
      return assigned;
   }
   
   public void setAssigned(Date assigned)
   {
      this.assigned = assigned;
   }
   
   @ManyToOne
   public User getUser()
   {
      return user;
   }
   
   public void setUser(User user)
   {
      this.user = user;
   }
   
   @ManyToOne
   public Show getShow()
   {
      return show;
   }
   
   public void setShow(Show show)
   {
      this.show = show;
   }
   
   @ManyToOne
   public SectionRow getRow()
   {
      return row;
   }
   
   public void setRow(SectionRow row)
   {
      this.row = row;
   }
   
   public int getQuantity()
   {
      return quantity;
   }
   
   public void setQuantity(int quantity)
   {
      this.quantity = quantity;
   }
   
   public int getStartSeat()
   {
      return startSeat;
   }
   
   public void setStartSeat(int startSeat)
   {
      this.startSeat = startSeat;
   }
   
   public int getEndSeat()
   {
      return endSeat;
   }
   
   public void setEndSeat(int endSeat)
   {
      this.endSeat = endSeat;
   }
}
