package at.fhtw.ode.helios.domain;

import org.vaadin.addon.leaflet.shared.Point;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;

public class InternationalSpaceStation extends AbstractEntity{

    private ArrayList<String> people = new ArrayList<>();
    private int numberOfPeopleInSpace;
    private LinkedList<Location> locations = new LinkedList<>();

    public LinkedList<Location> getLocations() {
        return locations;
    }

    public ArrayList<String> getPeople() {
        return people;
    }

    public void setPeople(ArrayList<String> people) {
        this.people = people;
    }

    public void setLocations(LinkedList<Location> locations) {
        this.locations = locations;
    }

    public void addLocation(Location location) {
        this.locations.add(location);
    }

    public String getPeopleAsStrings() {
        return people.toString();
    }

    public int getNumberOfPeopleInSpace() {
        return numberOfPeopleInSpace;
    }

    public void setNumberOfPeopleInSpace(int numberOfPeopleInSpace) {
        this.numberOfPeopleInSpace = numberOfPeopleInSpace;
    }
}
