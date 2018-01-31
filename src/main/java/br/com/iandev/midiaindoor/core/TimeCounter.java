package br.com.iandev.midiaindoor.core;

import java.util.Date;

/**
 * Created by Lucas on 20/03/2017.
 */

public final class TimeCounter {
    private Date date;
    private long elapsedTime;

    public TimeCounter() {
        this.setDate(new Date());
        this.setElapsedTime(new Date().getTime());
    }

    public TimeCounter(Date date) {
        if(date == null) {
            throw new IllegalArgumentException("date must not be null");
        }
        this.setDate(date);
        this.setElapsedTime(new Date().getTime());
    }

    public long getTime() {
        return new Date(getDate().getTime() + new Date().getTime() - this.getElapsedTime()).getTime();
    }

    public Date getDate() {
        return new Date(date.getTime() +  new Date().getTime() - this.getElapsedTime());
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public long getElapsedTime() {
        return elapsedTime;
    }

    public void setElapsedTime(long elapsedTime) {
        this.elapsedTime = elapsedTime;
    }
}
