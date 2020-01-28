package at.fhtw.ode.helios.view.map;

import java.time.Instant;
import java.util.Date;

import com.vaadin.client.ui.Icon;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

import org.vaadin.addon.leaflet.shared.Point;

import at.fhtw.ode.helios.HeliosUI;
import at.fhtw.ode.helios.domain.Location;

@SuppressWarnings("serial")
public class InfoPanel extends Window {

    public InfoPanel(Point pos) {
        setCaption("Information");
        setModal(true);
        setClosable(false);
        setResizable(false);
        setWidth(600.0f, Unit.PIXELS);

        setContent(buildContent(pos));
    }

    private Component buildContent(Point pos) {
        Location location = new Location();
        location.setDate(null);
        location.setLocation(pos);

        VerticalLayout components = new VerticalLayout();

        HorizontalLayout mainContent = new HorizontalLayout();
        mainContent.addComponent(buildWeatherInfo());
        mainContent.addComponent(buildLocationResult(location));

        components.addComponent(mainContent);
        components.addComponent(buildFooter());

        return components;
    }

    private Component buildWeatherInfo() {
        HorizontalLayout weatherComponents = new HorizontalLayout();
        Label image = new Label();
        image.setContentMode(ContentMode.HTML);
        image.setValue(VaadinIcons.EYE.getHtml());
        image.addStyleName("big-icon");
        weatherComponents.addComponent(image);

        VerticalLayout weatherResult = new VerticalLayout();
        Label test = new Label("test");
        weatherResult.addComponent(test);
        weatherComponents.addComponent(weatherResult);

        return weatherComponents;
    }

    private Component buildLocationResult(Location location) {
        VerticalLayout locationResult = new VerticalLayout();

        Label locationTitle = new Label("My location");
        locationTitle.addStyleName(ValoTheme.LABEL_H3);
        locationTitle.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        locationResult.addComponent(locationTitle);

        Label locationLabel = new Label(location.getLocation().toString());
        locationResult.addComponent(locationLabel);


        Label passTimeTitel = new Label("Next ISS pass");
        passTimeTitel.addStyleName(ValoTheme.LABEL_H3);
        passTimeTitel.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        locationResult.addComponent(passTimeTitel);

        Date time = HeliosUI.getDataProvider().getISSPassTime(location);
        Label passTime = new Label(time.toString());
        locationResult.addComponent(passTime);
        return locationResult;
    }

    private Component buildFooter() {
        HorizontalLayout footer = new HorizontalLayout();
        footer.addStyleName(ValoTheme.WINDOW_BOTTOM_TOOLBAR);
        footer.setWidth(100.0f, Unit.PERCENTAGE);

        Button cancel = new Button("Close");
        cancel.addClickListener(event -> close());
        cancel.setClickShortcut(KeyCode.ESCAPE, null);

        footer.addComponents(cancel);
        footer.setExpandRatio(cancel, 1);
        footer.setComponentAlignment(cancel, Alignment.TOP_RIGHT);
        return footer;
    }

}
