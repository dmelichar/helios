package at.fhtw.ode.helios.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.time.Duration;
import java.time.Instant;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.vaadin.addon.leaflet.shared.Point;

import at.fhtw.ode.helios.domain.Location;

public class DataProvider {

    private static final String ISS_API = "http://api.open-notify.org/iss-now.json";
    private static final String DARKSKY_API = "https://api.darksky.net/forecast/1a8c4d19947f4c72b82a5b76a742aecb/%s,%s";

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
                    Charset.forName("UTF-8")));
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

    public String getWeatherData() {
        String weather = null;

        return weather;

    }
}
