package org.jboss.spring.ticketmonster.domain;

/**
 * Key object for look-ups in the cache, formed by a Show id and a User id.
 * 
 * @author Ryan Bradley
 *
 */

public class CacheKey {
	
	private Long showId;
	
	private Long userId;

	public CacheKey(Long showId, Long userId) {
		this.showId = showId;
		this.userId = userId;
	}
	
	public Long getShowId() {
		return showId;
	}

	public void setShowId(Long showId) {
		this.showId = showId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

}
