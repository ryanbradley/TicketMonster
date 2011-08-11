package org.jboss.spring.ticketmonster.domain;

/**
 * Key object for look-ups in the cache, formed by a Show id and a User id.
 * 
 * @author Ryan Bradley
 *
 */

public class CacheKey {
	
	private Long showId;
	
	private Long rowId;

	public CacheKey(Long showId, Long rowId) {
		this.showId = showId;
		this.rowId = rowId;
	}
	
	public Long getShowId() {
		return showId;
	}

	public void setShowId(Long showId) {
		this.showId = showId;
	}

	public Long getRowId() {
		return rowId;
	}

	public void setRowId(Long rowId) {
		this.rowId = rowId;
	}

}
