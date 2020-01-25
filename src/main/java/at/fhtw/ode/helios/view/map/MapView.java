package at.fhtw.ode.helios.view.map;

import java.awt.*;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import at.fhtw.ode.helios.HeliosUI;
import at.fhtw.ode.helios.domain.Location;
import at.fhtw.ode.helios.domain.PeopleInSpace;
import com.vaadin.navigator.View;
import com.vaadin.server.Responsive;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import org.vaadin.addon.leaflet.LCircle;
import org.vaadin.addon.leaflet.LCircleMarker;
import org.vaadin.addon.leaflet.LMap;
import org.vaadin.addon.leaflet.LMarker;
import org.vaadin.addon.leaflet.LOpenStreetMapLayer;
import org.vaadin.addon.leaflet.shared.Point;


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
        final Label iss_pos = new Label();
        //final Label passTimes = new Label();
        final LMarker markerISS = new LMarker();

        //TODO: Icon for ISS Lmarker
        markerISS.setIcon("ISS");

        Location location = HeliosUI.getDataProvider().getISSLocation();
        iss_pos.setValue("ISS location: " + new Date().toString() + ": " + location.getLocation().toString());
        markerISS.setPopup("ISS Location\nTimestamp: " + location.getDate().toString() + "\nCoordinates: " + location.getLocation().toString());
        markerISS.setPoint(location.getLocation());
        map.addComponents(markerISS);

        addComponent(iss_pos);

        //TODO: Pass times in DataProvider - handle response or do request right
        //Location risetime = HeliosUI.getDataProvider().getISSPassTimes(location, 1);
        //passTimes.setValue("The next pass time of the ISS at your location is at: " + risetime.getDate().toString());
        //addComponent(passTimes);
    }

    public void saveStateListener() {

    }

    public void peopleInSpaceListener() {
        final Label numberPeople = new Label();

        PeopleInSpace people = HeliosUI.getDataProvider().getNumberOfPeopleInSpace();
        numberPeople.setValue("There are " + people.getNumberOfPeople() + " people in space right now");
        addComponent(numberPeople);
    }

    public void configureMap() {
        final Label pos = new Label();

        final LMarker m = new LMarker();
        m.setPopup("That's you");
        final LCircleMarker cm = new LCircleMarker();
        final LCircle c = new LCircle();

        map.addLocateListener(event -> {
            pos.setValue("Your location: " + new Date().toString() + ": " + event.getPoint().toString());
            if (m.getParent() == null) {
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
            }
        });

        // ToDo: save to locations list
        addComponent(pos);
    }
}
