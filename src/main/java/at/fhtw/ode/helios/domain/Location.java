package at.fhtw.ode.helios.domain;

import java.util.Date;

import org.vaadin.addon.leaflet.shared.Point;

public final class Location {

    private Point location;
    private Date date;

    public Point getLocation() {
        return location;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setLocation(Point location) {
        this.location = location;
    }

}
