package at.fhtw.ode.helios.domain;

import org.vaadin.addon.leaflet.shared.Point;

import javax.persistence.Entity;
import java.util.Arrays;
import java.util.Date;

@Entity
public class Location extends AbstractEntity{

    private Point location;
    private Date date;
    private String timestamp;

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

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return String.format("%s @ %s", Arrays.toString(location.getLatLonPair()), date.toString());
    }
}
