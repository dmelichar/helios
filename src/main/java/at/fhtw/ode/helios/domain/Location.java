package at.fhtw.ode.helios.domain;

import java.util.Arrays;
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

    @Override
    public String toString() {
        return String.format("%s @ %s", Arrays.toString(location.getLatLonPair()), date.toString());
    }
}
