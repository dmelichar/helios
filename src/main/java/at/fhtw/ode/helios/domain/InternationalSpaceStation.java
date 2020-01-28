package at.fhtw.ode.helios.domain;

import java.util.ArrayList;
import java.util.LinkedList;

public class InternationalSpaceStation extends AbstractEntity{

    private ArrayList<String> people = new ArrayList<String>();
    private LinkedList<Location> locations = new LinkedList<Location>();


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
}
