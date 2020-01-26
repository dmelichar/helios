package at.fhtw.ode.helios.data;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.sql.Time;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import at.fhtw.ode.helios.domain.PeopleInSpace;
import at.fhtw.ode.helios.domain.WeatherData;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import com.vaadin.navigator.View;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import org.vaadin.addon.leaflet.shared.Point;

import at.fhtw.ode.helios.domain.Location;

public class DataProvider extends VerticalLayout implements View {

    private static final String ISS_API = "http://api.open-notify.org/iss-now.json";
    private static final String PEOPLE_IN_SPACE_API = "http://api.open-notify.org/astros.json";

    private static Multimap<Long, Location> locations;
    private static Date lastDataUpdate;

    public DataProvider() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, -1);
        if (lastDataUpdate == null || lastDataUpdate.before(cal.getTime())) {
            refreshStaticData();
            lastDataUpdate = new Date();
        }
    }

    private void refreshStaticData() {
        locations = generateLocationsData();

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
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is,
                    StandardCharsets.UTF_8));
            String jsonText = readAll(rd);
            JsonElement jelement = new JsonParser().parse(jsonText);
            JsonObject jobject = jelement.getAsJsonObject();
            return jobject;
        } finally {
            is.close();
        }
    }

    /* Time utility method */
    public static Date timestamp() {
        return new Date(ThreadLocalRandom.current().nextInt() * 1000L);
    }

    private Multimap<Long, Location> generateLocationsData() {
        Multimap<Long, Location> result = MultimapBuilder.hashKeys().arrayListValues().build();

        // between -90 and 90 and -180 and 180
        for (long i = 0; i <= 100; i++) {
            Location location = new Location();

            Date random = DataProvider.timestamp();
            location.setDate(random);

            double lon = ThreadLocalRandom.current().nextDouble(-90, 90);
            double lat = ThreadLocalRandom.current().nextDouble(-180, 180);
            location.setLocation(new Point(lat, lon));

            result.put(i, location);
        }


        return result;
    }


    public Collection<Location> getRecentLocations(int count) {
        List<Location> orderedLocations = Lists.newArrayList(locations.values());
        Collections.sort(orderedLocations, (o1, o2) -> o2.getDate().compareTo((o1.getDate())));

        return orderedLocations.subList(0,
            Math.min(count, locations.values().size() -1));
    }

    public Location getISSLocation() {
        Location location = null;

        try {
            location = new Location();
            JsonObject response = readJsonFromUrl(ISS_API);

            Instant time = Instant.ofEpochSecond(response.get("timestamp").getAsLong());
            location.setDate(Date.from(time));

            JsonObject pos = response.get("iss_position").getAsJsonObject();
            location.setLocation(new Point(pos.get("latitude").getAsDouble(), pos.get("longitude").getAsDouble()));

            return location;

        } catch(IOException e) {

        }

        return location;
    }

    public PeopleInSpace getNumberOfPeopleInSpace() {

        PeopleInSpace people = null;

        try {
            people = new PeopleInSpace();
            JsonObject response = readJsonFromUrl(PEOPLE_IN_SPACE_API);

            int number = response.get("number").getAsInt();
            people.setNumberOfPeople(number);

            ArrayList<JsonElement> namesOfPeople = new ArrayList<>();
            JsonArray names = response.get("people").getAsJsonArray();

            for (JsonElement jsonElement : names) {
                namesOfPeople.add(jsonElement);
            }

            people.setNamesOfPeople(namesOfPeople);
            return people;

        } catch(IOException e) {

        }

        return people;
    }

    public WeatherData getCloudCover(Location myLocation, String timestamp) {

        WeatherData weather = null;

        try {
            weather = new WeatherData();

            String latitude = "";
            String longitude = "";

            Pattern pattern = Pattern.compile(", *");
            Matcher matcher = pattern.matcher(myLocation.getLocation().toString());
            if (matcher.find()) {
                latitude = myLocation.getLocation().toString().substring(0, matcher.start());
                longitude = myLocation.getLocation().toString().substring(matcher.end());
            }

            String DARKSKY_API = "https://api.darksky.net/forecast/1a8c4d19947f4c72b82a5b76a742aecb/" + latitude + "," + longitude + "," + timestamp;
            JsonObject response = readJsonFromUrl(DARKSKY_API);

            JsonObject cur = response.get("currently").getAsJsonObject();
            weather.setSummary(cur.get("summary").getAsString());
            weather.setCloudCover(cur.get("cloudCover").getAsFloat());

            return weather;

        } catch(IOException e) {

        }
        return weather;
    }

       public Location getISSPassTimes(Location myLocation) {

           Location location = null;

           try {
               String latitude = "";
               String longitude = "";

               Pattern pattern = Pattern.compile(", *");
               Matcher matcher = pattern.matcher(myLocation.getLocation().toString());
               if (matcher.find()) {
                   latitude = myLocation.getLocation().toString().substring(0, matcher.start());
                   longitude = myLocation.getLocation().toString().substring(matcher.end());
               }

               String ISS_PASS_TIMES_API = "http://api.open-notify.org/iss-pass.json?lat=" + latitude + "&lon=" + longitude;

               location = new Location();
               JsonObject response = readJsonFromUrl(ISS_PASS_TIMES_API);

               ArrayList<JsonElement> risetimes = new ArrayList<>();
               JsonArray times = response.get("response").getAsJsonArray();

               for (JsonElement jsonElement : times) {
                   risetimes.add(jsonElement);
               }

               location.setRisetimes(risetimes);
               return location;

           } catch(IOException e) {

           }

           return location;
       }

    public String getWeatherData() {
        String weather = null;
        return weather;
    }
}
