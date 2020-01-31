package at.fhtw.ode.helios.view.map;

import at.fhtw.ode.helios.HeliosUI;
import at.fhtw.ode.helios.domain.Location;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import com.vaadin.event.ShortcutAction;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.server.FileResource;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.addon.leaflet.*;

import java.io.File;
import java.util.Date;

@SuppressWarnings("serial")
public class MapView extends VerticalLayout implements View {

    private LMap map;
    private static long locationMapCounter = 0;

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
        createWindow();
        saveLocation();
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
        locate.setIcon(VaadinIcons.LOCATION_ARROW);
        locate.addClickListener((Button.ClickListener) event -> map.locate());
        buttonHeader.addComponent(locate);

        Button locateISS = new Button("Locate ISS");
        locateISS.setIcon(VaadinIcons.LOCATION_ARROW);
        locateISS.addClickListener((Button.ClickListener) event -> locateISSListener());
        buttonHeader.addComponent(locateISS);

        Button astronautsCheck = new Button("Astronaut check");
        astronautsCheck.setIcon(VaadinIcons.ROCKET);
        astronautsCheck.addClickListener((Button.ClickListener) event -> peopleInSpace());
        buttonHeader.addComponent(astronautsCheck);

        return buttonHeader;
    }

    public void locateISSListener() {
        final LMarker markerISS = new LMarker();

        String basepath = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();
        FileResource resourceSatellite = new FileResource(new File(basepath + "/WEB-INF/images/Satellite.png"));

        Location locationISS = HeliosUI.getDataProvider().pollCurrentISSLocation();

        markerISS.setIcon(resourceSatellite);
        markerISS.setPopup("ISS Location in the coordinates " + locationISS.getLocation() + " at timestamp: " + locationISS.getDate());
        markerISS.setPoint(locationISS.getLocation());
        map.addComponents(markerISS);
    }

    public void peopleInSpace() {
        Label number = new Label("There are " + HeliosUI.getDataProvider().getNumberOfPeopleInSpace() + " people at the ISS craft in space right now.");
        Label people = new Label("These are: " + HeliosUI.getDataProvider().getPeopleinSpace().substring(1, HeliosUI.getDataProvider().getPeopleinSpace().length()-1));

        String basepath = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();
        FileResource astronautResource = new FileResource(new File(basepath + "/WEB-INF/images/Astronaut.png"));
        Image astronautImage = new Image(null, astronautResource);

        // Create a sub-window and set the content
        Window subWindow = new Window("Astronauts");
        VerticalLayout subContent = new VerticalLayout();

        subWindow.setContent(subContent);
        subWindow.center();
        subWindow.setModal(true);
        subWindow.setClosable(false);
        subWindow.setResizable(false);

        // Footer
        HorizontalLayout footer = new HorizontalLayout();
        footer.addStyleName(ValoTheme.WINDOW_BOTTOM_TOOLBAR);
        footer.setWidth(100.0f, Unit.PERCENTAGE);

        // Close Button
        Button cancel = new Button("Close");
        cancel.addClickListener(event -> subWindow.close());
        cancel.setClickShortcut(ShortcutAction.KeyCode.ESCAPE, null);

        // Set components
        footer.addComponents(cancel);
        footer.setExpandRatio(cancel, 1);
        footer.setComponentAlignment(cancel, Alignment.BOTTOM_RIGHT);

        subContent.addComponents(number, people, astronautImage, footer);
        subContent.setComponentAlignment(astronautImage, Alignment.MIDDLE_CENTER);

        getUI().addWindow(subWindow);
    }

    public void configureMap() {

        LMarker m = new LMarker();
        m.setIcon(FontAwesome.MALE);
        LCircleMarker cm = new LCircleMarker();
        LCircle c = new LCircle();

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
            }
        });
    }

    public void createWindow () {

        map.addLocateListener(event -> {
            Location myLocation = new Location();
            myLocation.setLocation(event.getPoint());
            getUI().addWindow(new InfoPanel(myLocation));
        });
    }

    public void saveLocation () {
        Multimap<Long, Location> locationMultimap = MultimapBuilder.hashKeys().arrayListValues().build();

        map.addLocateListener(event -> {
            Location myLocation = new Location();
            myLocation.setLocation(event.getPoint());
            myLocation.setDate(new Date());
            locationMultimap.put(locationMapCounter, myLocation);
            locationMapCounter++;
        });
        HeliosUI.getDataProvider().generateLocationsMap(locationMultimap);
    }
}