package at.fhtw.ode.helios.view.map;

import at.fhtw.ode.helios.HeliosUI;
import at.fhtw.ode.helios.domain.Location;
import com.vaadin.event.ShortcutAction;
import com.vaadin.navigator.View;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.addon.leaflet.*;
import org.vaadin.addon.leaflet.shared.Point;

import static at.fhtw.ode.helios.data.DataProvider.timestamp;

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
        map.setZoomLevel(3);
        map.setSizeFull();
        final LOpenStreetMapLayer osm = new LOpenStreetMapLayer();
        map.addLayer(osm);
        addComponent(map);
        setExpandRatio(map, 1);

        configureMap();
        createWindow();
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

        Button astronatusCheck = new Button("Astronauts check");
        astronatusCheck.addClickListener((Button.ClickListener) event -> peopleInSpace());
        buttonHeader.addComponent(astronatusCheck);

        return buttonHeader;
    }

    public void locateISSListener() {
        final LMarker markerISS = new LMarker();

        Location locationISS = HeliosUI.getDataProvider().pollCurrentISSLocation();

        markerISS.setIcon(FontAwesome.SPACE_SHUTTLE);
        markerISS.setPopup("ISS Location in the coordinates " + locationISS.getLocation() + " at timestamp: " + locationISS.getDate());
        markerISS.setPoint(locationISS.getLocation());
        map.addComponents(markerISS);
    }

    public void peopleInSpace() {
        Label number = new Label("There are " + HeliosUI.getDataProvider().getNumberOfPeopleInSpace() + " people at the ISS craft in space right now.");
        Label people = new Label("These are: " + HeliosUI.getDataProvider().getPeopleinSpace().substring(1, HeliosUI.getDataProvider().getPeopleinSpace().length()-1));

        // Create a sub-window and set the content
        Window subWindow = new Window("Astronauts");
        VerticalLayout subContent = new VerticalLayout();
        subWindow.setContent(subContent);
        subWindow.center();
        subWindow.setModal(true);
        subWindow.setClosable(false);
        subWindow.setResizable(false);
        Button cancel = new Button("Close");
        cancel.addClickListener(event -> subWindow.close());
        cancel.setClickShortcut(ShortcutAction.KeyCode.ESCAPE, null);
        subContent.addComponents(number, people, cancel);
        subContent.setComponentAlignment(cancel, Alignment.TOP_RIGHT);
        getUI().addWindow(subWindow);
    }

    public void saveStateListener(Point point) {
        Location location = new Location();
        location.setDate(timestamp());
        location.setLocation(point);
        //HeliosUI.getHeliosRepository().save(location);
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
                saveStateListener(event.getPoint());
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
}
