package org.jboss.spring.ticketmonster.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * 
 * @author Shane Bryzak
 */

@Entity
public class User implements Serializable
{
   private static final long serialVersionUID = -4501716573185869164L;
   
   private Long id;
//   private IdentityObject identity;
   private String username;
   
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
   
/*
   @OneToOne
   public IdentityObject getIdentity()
   {
      return identity;
   }
   
   public void setIdentity(IdentityObject identity)
   {
      this.identity = identity;
   }
*/
   
   public String getUsername()
   {
      return username;
   }
   
   public void setUsername(String username)
   {
      this.username = username;
   }
}
