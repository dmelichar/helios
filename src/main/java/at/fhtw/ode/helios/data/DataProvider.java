package at.fhtw.ode.helios.data;

import at.fhtw.ode.helios.domain.InternationalSpaceStation;
import at.fhtw.ode.helios.domain.Location;
import at.fhtw.ode.helios.domain.Weather;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.vaadin.addon.leaflet.shared.Point;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.*;

public class DataProvider {

    private static final String LOCATION_API = "http://api.open-notify.org/iss-now.json";
    private static final String PEOPLE_IN_SPACE_API = "http://api.open-notify.org/astros.json";
    private static final String PASSTIME_API = "http://api.open-notify.org/iss-pass.json?lat=%s&lon=%s";
    private static final String DARKSKY_API = "https://api.darksky.net/forecast/1a8c4d19947f4c72b82a5b76a742aecb/";

    private static Multimap<Long, Location> locations;
    private static InternationalSpaceStation issData;

    private static Date lastDataUpdate;

    public DataProvider() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, -1);
        if (lastDataUpdate == null || lastDataUpdate.before(cal.getTime())) {
            refreshISSData();
            lastDataUpdate = new Date();
        }
    }

    private void refreshISSData() {
        issData = initISS();
    }

    public InternationalSpaceStation initISS() {
        InternationalSpaceStation builder = new InternationalSpaceStation();
        builder.setPeople(pollPeopleInSpace());
        builder.setNumberOfPeopleInSpace(pollPeopleInSpace().size());
        return builder;
    }

    public void generateLocationsMap (Multimap<Long, Location> locationMultimap) {
        locations = locationMultimap;
    }

    public Collection<Location> getRecentLocations(int count) {
        List<Location> orderedLocations = Lists.newArrayList(locations.values());
        orderedLocations.sort((o1, o2) -> o2.getDate().compareTo((o1.getDate())));
        return orderedLocations.subList(0, Math.min(count, locations.values().size()));
    }

    public Location pollCurrentISSLocation() {
        Location location = null;

        try {
            location = new Location();
            JsonObject response = readJsonFromUrl(LOCATION_API);

            Instant time = Instant.ofEpochSecond(response.get("timestamp").getAsLong());
            location.setDate(Date.from(time));

            JsonObject pos = response.get("iss_position").getAsJsonObject();
            location.setLocation(new Point(pos.get("latitude").getAsDouble(), pos.get("longitude").getAsDouble()));

            return location;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return location;
    }

    public Location pollISSPassTime(Location myLocation) {
        Location result = new Location();

        try {
            JsonObject response = readJsonFromUrl(String.format(PASSTIME_API,
                    myLocation.getLocation().getLat().toString(), myLocation.getLocation().getLon()));

            JsonArray times = response.get("response").getAsJsonArray();

            String time = times.get(0).getAsJsonObject().get("risetime").getAsString();
            Instant instant = Instant.ofEpochSecond(Long.parseLong(time));

            result.setDate(Date.from(instant));
            result.setTimestamp(time);

            return result;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public String getPeopleinSpace() {
        return DataProvider.issData.getPeopleAsStrings();
    }

    public int getNumberOfPeopleInSpace () {
        return DataProvider.issData.getNumberOfPeopleInSpace();
    }

    private ArrayList<String> pollPeopleInSpace() {
        ArrayList<String> nameList = null;

        try {
            nameList = new ArrayList<>();
            JsonObject response = readJsonFromUrl(PEOPLE_IN_SPACE_API);
            JsonArray peopleList = response.get("people").getAsJsonArray();

            for (JsonElement peopleObjects : peopleList) {
                JsonObject people = peopleObjects.getAsJsonObject();
                nameList.add(people.get("name").getAsString());
            }

            return nameList;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return nameList;
    }

    public Weather pollWeatherData(Location myLocation, String timestamp) {
        Weather weather = null;

        try {
            weather = new Weather();

            JsonObject response = readJsonFromUrl(DARKSKY_API
                    + myLocation.getLocation().getLat().toString() + "," + myLocation.getLocation().getLon().toString() + "," + timestamp);

            JsonObject cur = response.get("currently").getAsJsonObject();
            weather.setSummary(cur.get("summary").getAsString());
            weather.setCloudCover(cur.get("cloudCover").getAsFloat());
            weather.setIcon(cur.get("icon").getAsString());
            weather.setTemperature(cur.get("temperature").getAsFloat());
            weather.setVisibility(cur.get("visibility").getAsFloat());

            return weather;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return weather;
    }

    /* JSON utility method */
    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    /* JSON utility method */
    private static JsonObject readJsonFromUrl(String url) throws IOException {
        try (InputStream is = new URL(url).openStream()) {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            String jsonText = readAll(rd);
            JsonElement jelement = new JsonParser().parse(jsonText);
            return jelement.getAsJsonObject();
        }
    }
}