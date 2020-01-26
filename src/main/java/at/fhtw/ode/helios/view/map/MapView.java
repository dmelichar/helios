package at.fhtw.ode.helios.view.map;

import java.awt.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import at.fhtw.ode.helios.HeliosUI;
import at.fhtw.ode.helios.domain.Location;
import at.fhtw.ode.helios.domain.PeopleInSpace;
import at.fhtw.ode.helios.domain.WeatherData;
import com.google.gson.JsonElement;
import com.vaadin.navigator.View;
import com.vaadin.server.BrowserWindowOpener;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.*;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

import org.vaadin.addon.leaflet.LCircle;
import org.vaadin.addon.leaflet.LCircleMarker;
import org.vaadin.addon.leaflet.LMap;
import org.vaadin.addon.leaflet.LMarker;
import org.vaadin.addon.leaflet.LOpenStreetMapLayer;
import org.vaadin.addon.leaflet.shared.Point;

import javax.swing.*;


@SuppressWarnings("serial")
public class MapView extends VerticalLayout implements View {

    private LMap map;

    public MapView() {
        setSizeFull();
        addStyleName("map");
        setMargin(false);
        setSpacing(false);

        addComponent(buildHeader());

        map = new LMap();
        map.addStyleName("leafletmap");
        map.setZoomLevel(2);
        map.setSizeFull();
        final LOpenStreetMapLayer osm = new LOpenStreetMapLayer();
        map.addLayer(osm);
        addComponent(map);
        setExpandRatio(map, 1);

        configureMap();
    }
    public Component buildHeader() {
        HorizontalLayout buttonHeader = new HorizontalLayout();
        buttonHeader.addStyleName("viewheader");
        Responsive.makeResponsive(buttonHeader);

        Label titleLabel = new Label("Map operations");
        titleLabel.setSizeUndefined();
        titleLabel.addStyleName(ValoTheme.LABEL_H1);
        titleLabel.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        buttonHeader.addComponent(titleLabel);

        Button locate = new Button("Locate Me");
        locate.addClickListener((Button.ClickListener) event -> map.locate());
        buttonHeader.addComponent(locate);

        Button locateISS = new Button("Locate ISS");
        locateISS.addClickListener((Button.ClickListener) event -> locateISSListener());
        buttonHeader.addComponent(locateISS);

        Button saveState = new Button("Save State");
        saveState.addClickListener((Button.ClickListener) event -> saveStateListener());
        buttonHeader.addComponent(saveState);

        Button peopleInSpace = new Button("People in Space");
        peopleInSpace.addClickListener((Button.ClickListener) event -> peopleInSpaceListener());
        buttonHeader.addComponent(peopleInSpace);

        return buttonHeader;
    }

    public void locateISSListener() {
        final LMarker markerISS = new LMarker();

        Location locationISS = HeliosUI.getDataProvider().getISSLocation();

        markerISS.setIcon(FontAwesome.SPACE_SHUTTLE);
        markerISS.setPopup("ISS Location in the coordinates " + locationISS.getLocation().toString() + " at timestamp: " + locationISS.getDate().toString());
        markerISS.setPoint(locationISS.getLocation());
        map.addComponents(markerISS);
    }

    public void saveStateListener() {

    }

    public void peopleInSpaceListener() {
        final Label numberPeople = new Label();
        final Label namesPeople = new Label();
        ArrayList<String> nameList = new ArrayList<>();

        //TODO: Print only one time or do a popup or something
        PeopleInSpace people = HeliosUI.getDataProvider().getNumberOfPeopleInSpace();
        numberPeople.setValue("There are " + people.getNumberOfPeople() + " people in space at the ISS craft right now, these are: ");

        int i=0;
        int iend;
        for (JsonElement element : people.getNamesOfPeople()) {
            String str = element.toString().substring(element.toString().indexOf(":") + 2);
            iend = str.indexOf("\"");
            if (iend != -1) {
                str = str.substring(0, iend);
            }
            nameList.add(i, str);
            i++;
        }

        namesPeople.setValue(nameList.toString().substring(1, nameList.toString().length()-1));
        addComponents(numberPeople, namesPeople);
    }

    public void configureMap() {

        final LMarker m = new LMarker();
        m.setIcon(FontAwesome.MALE);
        final LCircleMarker cm = new LCircleMarker();
        final LCircle c = new LCircle();
        Location myLocation = new Location();
        final Label passTimes = new Label();
        final Label weatherData = new Label();

        map.addLocateListener(event -> {
            if (m.getParent() == null) {
                m.setPopup("Your Location is in the coordinates " + event.getPoint());
                m.setPoint(event.getPoint());
                cm.setPoint(event.getPoint());
                cm.setColor("red");
                cm.setRadius(1);
                c.setPoint(event.getPoint());
                c.setColor("yellow");
                c.setStroke(false);
                c.setRadius(event.getAccuracy());
                map.addComponents(m, cm, c);
                map.setLayersToUpdateOnLocate(m, cm, c);

                // ISS Pass Time at users location
                myLocation.setLocation(event.getPoint());
                Location risetime = HeliosUI.getDataProvider().getISSPassTimes(myLocation);
                String myTime = risetime.getRisetimes(0).toString().substring(risetime.getRisetimes(0).toString().lastIndexOf(":") + 1);
                myTime = myTime.substring(0, myTime.length() - 1);
                Instant time = Instant.ofEpochSecond(Long.parseLong(myTime));
                risetime.setDate(Date.from(time));
                passTimes.setValue("The next pass time of the ISS at your location is at: " + risetime.getDate().toString());

                // Weather Data at users location
                WeatherData weather = HeliosUI.getDataProvider().getCloudCover(myLocation, myTime);
                if (weather.getCloudCover() < 0.6) {
                    weatherData.setValue("Weather at pass time: " + weather.getSummary() + ", clear sky! You should be able to see the International Space Station!");
                }
                else {
                    weatherData.setValue("Weather at pass time: " + weather.getSummary() + ", the sky will be covered with clouds. Detecting the International Space Station will be difficult!");
                }
            }
        });
        addComponent(passTimes);
        addComponent(weatherData);

        // ToDo: save to locations list
    }
}
