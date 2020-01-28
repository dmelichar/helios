package at.fhtw.ode.helios.view.map;

import at.fhtw.ode.helios.HeliosUI;
import at.fhtw.ode.helios.domain.Location;
import at.fhtw.ode.helios.domain.Weather;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import java.util.Date;

@SuppressWarnings("serial")
public class InfoPanel extends Window {

    public InfoPanel(Location location) {
        setCaption("Your location information");
        setModal(true);
        setClosable(false);
        setResizable(false);
        //setWidth(1200.0f, Unit.PIXELS);

        setContent(buildContent(location));
    }

    private Component buildContent(Location location) {
        Location myLocation = new Location();
        myLocation.setDate(null);
        myLocation.setLocation(location.getLocation());

        VerticalLayout components = new VerticalLayout();
        //components.setMargin(true);

        HorizontalLayout mainContent = new HorizontalLayout();
        mainContent.addComponent(buildLocationResult(myLocation));
        mainContent.addComponent(buildWeatherInfo(location));

        components.addComponent(mainContent);
        components.addComponent(buildFooter());

        return components;
    }

    private Component buildWeatherInfo(Location location) {
        HorizontalLayout weatherComponents = new HorizontalLayout();
        Label image = new Label();
        image.setContentMode(ContentMode.HTML);
        image.setValue(VaadinIcons.EYE.getHtml());
        image.addStyleName("big-icon");
        weatherComponents.addComponent(image);

        VerticalLayout weatherResult = new VerticalLayout();

        Label weatherTitle = new Label("Weather data at pass time");
        weatherTitle.addStyleName(ValoTheme.LABEL_H3);
        weatherTitle.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        weatherResult.addComponent(weatherTitle);

        Label weatherInfo = new Label();
        Weather weather = HeliosUI.getDataProvider().pollWeatherData(location, HeliosUI.getDataProvider().getISSPassTimeAsString(location));

        if (weather.getCloudCover() < 0.5) {
            weatherInfo.setValue(weather.getSummary() + ". Not many clouds cover the sky! You should be able to spot the International Space Station!");
        }
        else {
            weatherInfo.setValue(weather.getSummary() + ". Many clouds cover the sky! Detecting the International Space Station will be very difficult!");
        }

        weatherResult.addComponent(weatherInfo);
        //weatherComponents.addComponent(weatherResult);

        return weatherResult;
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
