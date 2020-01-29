package at.fhtw.ode.helios.view.map;

import at.fhtw.ode.helios.HeliosUI;
import at.fhtw.ode.helios.domain.Location;
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

        Button astronatusCheck = new Button("Astronaut check");
        astronatusCheck.setIcon(VaadinIcons.ROCKET);
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

        String basepath = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();
        FileResource astronautResource = new FileResource(new File(basepath + "/WEB-INF/images/Astronaut.png"));
        Image astronautImage = new Image(null, astronautResource);
        astronautImage.setWidth(72.0f, Unit.PIXELS);

        // Create a sub-window and set the content
        Window subWindow = new Window("Astronauts");
        VerticalLayout subContent = new VerticalLayout();

        HorizontalLayout footer = new HorizontalLayout();
        footer.addStyleName(ValoTheme.WINDOW_BOTTOM_TOOLBAR);
        footer.setWidth(100.0f, Unit.PERCENTAGE);

        subWindow.setContent(subContent);
        subWindow.center();
        subWindow.setModal(true);
        subWindow.setClosable(false);
        subWindow.setResizable(false);

        Button cancel = new Button("Close");
        cancel.addClickListener(event -> subWindow.close());
        cancel.setClickShortcut(ShortcutAction.KeyCode.ESCAPE, null);

        footer.addComponents(cancel);
        footer.setExpandRatio(cancel, 1);
        footer.setComponentAlignment(cancel, Alignment.TOP_RIGHT);

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
}
