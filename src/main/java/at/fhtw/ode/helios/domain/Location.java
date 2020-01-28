package at.fhtw.ode.helios.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import com.google.gson.JsonElement;
import org.vaadin.addon.leaflet.shared.Point;

import javax.persistence.*;


@Entity
@Table(name = "locations")
public class Location extends AbstractEntity {

    //@Column(columnDefinition = "POINT")
    private Point location;

    //@Temporal(TemporalType.DATE)
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
