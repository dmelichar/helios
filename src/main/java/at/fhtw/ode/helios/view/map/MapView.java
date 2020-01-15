package at.fhtw.ode.helios.view.map;

import java.util.Date;

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
import org.vaadin.addon.leaflet.LeafletLocateEvent;
import org.vaadin.addon.leaflet.LeafletLocateListener;


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

        Button locate = new Button("Locate");
        locate.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                map.locate();
            }
        });
        buttonHeader.addComponent(locate);


        // Todo: new button for locate ISS
        // ToDo: new button for "check if visible", no new map overlay too much

        // Button saveLocation = new Button("Save location");
        // saveLocation.addClickListener(new Button.ClickListener() {
        //     @Override
        //     public void buttonClick(Button.ClickEvent event) {

        //     }
        // });
        // buttonHeader.addComponent(saveLocation);

        // Button stop = new Button("Stop");
        // stop.addClickListener(new Button.ClickListener() {
        //     @Override
        //     public void buttonClick(Button.ClickEvent event) {
        //         map.stopLocate();
        //     }
        // });
        // buttonHeader.addComponent(stop);

        return buttonHeader;
    }


    public void configureMap() {
        final Label pos = new Label();

        final LMarker m = new LMarker();
        m.setPopup("That's you");
        final LCircleMarker cm = new LCircleMarker();
        final LCircle c = new LCircle();

        map.addLocateListener(new LeafletLocateListener() {
            @Override
            public void onLocate(LeafletLocateEvent event) {
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
            }
        });

        // ToDo: save to locations list
        addComponent(pos);
    }

}
