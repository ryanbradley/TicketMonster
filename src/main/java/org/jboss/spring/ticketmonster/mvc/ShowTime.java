package org.jboss.spring.ticketmonster.mvc;

import java.util.Date;

/**
 * @author Marius Bogoevici
 */
public class ShowTime {

    private long showId;

    private Date date;

    public long getShowId() {
        return showId;
    }

    public void setShowId(long showId) {
        this.showId = showId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
