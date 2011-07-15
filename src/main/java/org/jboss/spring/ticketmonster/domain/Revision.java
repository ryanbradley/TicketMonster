package org.jboss.spring.ticketmonster.domain;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * Stores a single revision of a document.  A document may have multiple
 * revisions, only one of them being active at one time.  This allows the
 * document to go through a drafting/approval process before any new revisions
 * are made "live".
 * 
 * @author Shane Bryzak
 *
 */

@JsonIgnoreProperties(value = "document")
@Entity
public class Revision implements Serializable
{
   private static final long serialVersionUID = 6197879518040782042L;

   private Long id;

   private Document document;

   private Date created;
   private String createdBy;
   private Date modified;
   private String modifiedBy;
   private String content;
   
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
   
   @ManyToOne
   @JoinColumn(name = "DOCUMENT_ID")
   public Document getDocument()
   {
      return document;
   }
   
   public void setDocument(Document document)
   {
      this.document = document;
   }
   
   public Date getCreated()
   {
      return created;
   }
   
   public void setCreated(Date created)
   {
      this.created = created;
   }
   
   public String getCreatedBy()
   {
      return createdBy;
   }
   
   public void setCreatedBy(String createdBy)
   {
      this.createdBy = createdBy;
   }
   
   public Date getModified()
   {
      return modified;
   }
   
   public void setModified(Date modified)
   {
      this.modified = modified;
   }
   
   public String getModifiedBy()
   {
      return modifiedBy;
   }
   
   public void setModifiedBy(String modifiedBy)
   {
      this.modifiedBy = modifiedBy;
   }

   @Column(length = 2000)
   public String getContent()
   {
      return content;
   }
   
   public void setContent(String content)
   {
      this.content = content;
   }
}
