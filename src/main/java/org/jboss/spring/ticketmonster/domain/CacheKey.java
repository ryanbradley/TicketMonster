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
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((rowId == null) ? 0 : rowId.hashCode());
		result = prime * result + ((showId == null) ? 0 : showId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CacheKey other = (CacheKey) obj;
		if (rowId == null) {
			if (other.rowId != null)
				return false;
		} else if (!rowId.equals(other.rowId))
			return false;
		if (showId == null) {
			if (other.showId != null)
				return false;
		} else if (!showId.equals(other.showId))
			return false;
		return true;
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
