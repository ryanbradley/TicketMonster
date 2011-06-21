package org.jboss.spring.ticketmonster.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

/**
 * Used to store rich text entries used for venue descriptions, event descriptions,
 * front page announcements, etc. A document may have multiple revisions, only
 * one of which is the 'active' revision.  This allows document changes to
 * go through an approval process before being made live.
 * 
 * @author Shane Bryzak
 *
 */

@Entity
public class Document implements Serializable
{
   private static final long serialVersionUID = -3190368407410663590L;
   
   private Long id;
   private Revision active;
   
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

   @OneToOne
   @JoinColumn(name = "REVISION_ID")
   public Revision getActive()
   {
      return active;
   }

   public void setActive(Revision active)
   {
      this.active = active;
   }
}
