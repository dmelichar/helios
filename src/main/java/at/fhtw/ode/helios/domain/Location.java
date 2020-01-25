package at.fhtw.ode.helios.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import com.google.gson.JsonElement;
import org.vaadin.addon.leaflet.shared.Point;

public final class Location {

    private Point location;
    private Date date;
    private ArrayList<JsonElement> risetimes = new ArrayList<JsonElement>();

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

    public JsonElement getRisetimes(int index) {
        return risetimes.get(index);
    }

    public void setRisetimes(ArrayList<JsonElement> risetimes) {
        this.risetimes = risetimes;
    }

    @Override
    public String toString() {
        return String.format("%s @ %s", Arrays.toString(location.getLatLonPair()), date.toString());
    }
}
