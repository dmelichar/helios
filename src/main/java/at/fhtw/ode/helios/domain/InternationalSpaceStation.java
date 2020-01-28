package at.fhtw.ode.helios.domain;

import java.util.ArrayList;

public class InternationalSpaceStation extends AbstractEntity{

    private ArrayList<String> people = new ArrayList<>();
    private int numberOfPeopleInSpace;

    public ArrayList<String> getPeople() {
        return people;
    }

    public void setPeople(ArrayList<String> people) {
        this.people = people;
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
