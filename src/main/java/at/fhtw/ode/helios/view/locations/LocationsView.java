package at.fhtw.ode.helios.view.locations;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutionException;

import at.fhtw.ode.helios.view.map.Coord;
import at.fhtw.ode.helios.view.map.InfoPanel;
import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.*;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Responsive;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Grid.Column;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.ui.VerticalLayout;

import at.fhtw.ode.helios.HeliosUI;
import at.fhtw.ode.helios.domain.Location;
import at.fhtw.ode.helios.event.HeliosEventBus;

@SuppressWarnings("serial")
public class LocationsView extends VerticalLayout implements View {

    public LocationsView() throws IOException, ExecutionException, InterruptedException {
        setSizeFull();
        addStyleName("locations");
        setMargin(false);
        setSpacing(false);
        HeliosEventBus.register(this);

        addComponent(buildToolbar());

        Grid<Location> grid = buildLocationTable();
        addComponent(grid);
        setExpandRatio(grid, 1);
    }

    @Override
    public void detach() {
        super.detach();
        HeliosEventBus.unregister(this);
    }

    public Component buildToolbar() {
        HorizontalLayout header = new HorizontalLayout();
        header.addStyleName("viewheader");
        Responsive.makeResponsive(header);

        Label title = new Label("Previous locations");
        title.setSizeUndefined();
        title.addStyleName(ValoTheme.LABEL_H1);
        title.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        header.addComponent(title);

        return header;
    }

    public Grid<Location> buildLocationTable() throws IOException, ExecutionException, InterruptedException {
        final DateFormat DATEFORMAT = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
        final Set<Column<Location, ?>> collapsibleColumns = new LinkedHashSet<>();

        Grid<Location> grid = new Grid<>();
        grid.setSelectionMode(SelectionMode.SINGLE);
        grid.setSizeFull();

        Column<Location, String> date = grid.addColumn(location -> DATEFORMAT.format(location.getDate()));
        date.setId("Time").setHidable(true);

        collapsibleColumns.add(grid.addColumn(Location::getLocation).setId("Location"));

        grid.setColumnReorderingAllowed(false);

        ListDataProvider<Location> dataProvider = com.vaadin.data.provider.DataProvider.ofCollection(HeliosUI.getDataProvider().getRecentLocations(20));

        grid.setDataProvider(dataProvider);

        return grid;
    }

    @Override
    public void enter(final ViewChangeEvent event) {
    }

}
