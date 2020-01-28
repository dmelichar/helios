package at.fhtw.ode.helios.view.map;

import java.util.Objects;

public class Coord {
    private double lon;
    private double lat;
    private String time;

    public Coord() {
        // Must have a public no-argument constructor
    }

    public Coord(double lon, double lat, String time) {
        this.lon = lon;
        this.lat = lat;
        this.time = time;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Coord{" +
                "lon=" + lon +
                ", lat=" + lat +
                ", time='" + time + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coord coord = (Coord) o;
        return lon == coord.lon &&
                lat == coord.lat &&
                Objects.equals(time, coord.time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lon, lat, time);
    }
}
