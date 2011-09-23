package org.jboss.spring.ticketmonster.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.springframework.security.access.prepost.PreAuthorize;

/**
 * 
 * @author Shane Bryzak
 */

@Entity
public class User implements Serializable
{
   private static final long serialVersionUID = -4501716573185869164L;
   
   private Long id;
// private IdentityObject identity;
   private String username;
   private String firstName;
   private String lastName;
   private String password;
   private boolean enabled;

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
   
   public String getUsername()
   {
      return username;
   }
   
   public void setUsername(String username)
   {
      this.username = username;
   }

   public String getFirstName() {
	   return firstName;
   }

   public void setFirstName(String firstName) {
	   this.firstName = firstName;
   }

   public String getLastName() {
	   return lastName;
   }

   public void setLastName(String lastName) {
	   this.lastName = lastName;
   }
   
   public boolean isEnabled() {
	   return enabled;
   }

   public void setEnabled(boolean enabled) {
		this.enabled = enabled;
   }

   public String getPassword() {
	   return password;
   }

   @PreAuthorize("hasRole('ROLE_ADMIN')")
   public void setPassword(String password) {
	   this.password = password;
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
}
