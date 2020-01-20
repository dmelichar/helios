package at.fhtw.ode.helios.view.map;

import java.util.Date;

import at.fhtw.ode.helios.HeliosUI;
import at.fhtw.ode.helios.domain.Location;
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

        return buttonHeader;
    }

    public void locateISSListener() {
        final Label iss_pos = new Label();
        Location location = HeliosUI.getDataProvider().getISSLocation();

        iss_pos.setValue(location.toString());
        addComponent(iss_pos);
    }

    public void saveStateListener() {

    }

    public void configureMap() {
        final Label pos = new Label();

        final LMarker m = new LMarker();
        m.setPopup("That's you");
        final LCircleMarker cm = new LCircleMarker();
        final LCircle c = new LCircle();

        map.addLocateListener(event -> {
            pos.setValue(new Date().toString() + ": " + event.toString());
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
