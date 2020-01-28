package at.fhtw.ode.helios.view.map;

import at.fhtw.ode.helios.HeliosUI;
import at.fhtw.ode.helios.domain.Location;
import at.fhtw.ode.helios.domain.Weather;
import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        VerticalLayout components = new VerticalLayout();

        HorizontalLayout mainContent = new HorizontalLayout();
        mainContent.addComponent(buildLocationResult(location));
        mainContent.addComponent(buildWeatherInfo(location));

        components.addComponent(mainContent);
        components.addComponent(buildFooter());

        writeDB(location);

        return components;
    }

    private Component buildWeatherInfo(Location location) {
        //HorizontalLayout weatherComponents = new HorizontalLayout();
        Label image = new Label();
        Label weatherInfo = new Label();
        VerticalLayout weatherResult = new VerticalLayout();

        Weather weather = HeliosUI.getDataProvider().pollWeatherData(location, HeliosUI.getDataProvider().getISSPassTime(location).getTimestamp());

        Label weatherTitle = new Label("Weather data at pass time");
        weatherTitle.addStyleName(ValoTheme.LABEL_H3);
        weatherTitle.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        weatherResult.addComponent(weatherTitle);

        image.setContentMode(ContentMode.HTML);
        image.addStyleName("big-icon");
        image.setSizeFull();

        if (weather.getCloudCover() < 0.5) {
            weatherInfo.setValue(weather.getSummary() + ". Not many clouds cover the sky! You should be able to spot the International Space Station!");
        }
        else {
            weatherInfo.setValue(weather.getSummary() + ". Many clouds cover the sky! Detecting the International Space Station will be very difficult!");
        }

        switch (weather.getIcon()) {
            case "clear-day":
            case "clear-night":
                image.setValue(VaadinIcons.SUN_O.getHtml());
                break;
            case "rain":
            case "snow":
            case "sleet":
                image.setValue(VaadinIcons.UMBRELLA.getHtml());
                break;
            case "wind":
                image.setValue(VaadinIcons.ACADEMY_CAP.getHtml());
                break;
            case "fog":
            case "cloudy":
            case "partly-cloudy-day":
            case "partly-cloudy-night":
                image.setValue(VaadinIcons.CLOUD.getHtml());
                break;
            default:
                image.setValue(VaadinIcons.ARROWS_CROSS.getHtml());
                break;
        }

        weatherResult.addComponent(weatherInfo);
        weatherResult.addComponent(image);
        //weatherResult.setComponentAlignment(image, Alignment.MIDDLE_CENTER);
        //weatherComponents.addComponent(image);
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

        Date time = HeliosUI.getDataProvider().getISSPassTime(location).getDate();
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

    public void writeDB(Location location) {
        try {
            // Use a service account
            InputStream serviceAccount = new FileInputStream("test.json");
            GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount);
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(credentials)
                    .build();
            FirebaseApp.initializeApp(options);

            Firestore db = FirestoreClient.getFirestore();

            //asynchronously update doc, create the document if missing
            Map<String, Object> update = new HashMap<>();
            update.put("lat", location.getLocation().getLat());
            update.put("lon", location.getLocation().getLon());
            update.put("time", new Date().toString());
            ApiFuture<DocumentReference> writeResult =
                    db
                            .collection("coord")
                            .add(update);
// ...

//asynchronously retrieve all documents
            ApiFuture<QuerySnapshot> future = db.collection("coord").get();
// future.get() blocks on response
            List<QueryDocumentSnapshot> documents = future.get().getDocuments();
            for (QueryDocumentSnapshot document : documents) {
                System.out.println(document.getId() + " => " + document.toObject(Coord.class));
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
