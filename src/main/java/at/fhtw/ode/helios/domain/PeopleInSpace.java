package at.fhtw.ode.helios.domain;

import com.google.gson.JsonElement;
import elemental.json.Json;

import java.util.ArrayList;

public final class PeopleInSpace {

    private int numberOfPeople;
    private ArrayList<JsonElement> namesOfPeople = new ArrayList<JsonElement>();

    public int getNumberOfPeople() {
        return numberOfPeople;
    }

    public void setNumberOfPeople(int numberOfPeople) {
        this.numberOfPeople = numberOfPeople;
    }

    public ArrayList<JsonElement> getNamesOfPeople() {
        return namesOfPeople;
    }

    public void setNamesOfPeople(ArrayList<JsonElement> namesOfPeople) {
        this.namesOfPeople = namesOfPeople;
    }
}
