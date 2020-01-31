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
import com.vaadin.server.FileResource;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
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
        Label weatherInfo = new Label();
        Label cloudInfo = new Label();
        VerticalLayout weatherResult = new VerticalLayout();

        // Get weather data
        Weather weather = HeliosUI.getDataProvider().pollWeatherData(location, HeliosUI.getDataProvider().pollISSPassTime(location).getTimestamp());

        // Set title
        Label weatherTitle = new Label("Weather data at pass time");
        weatherTitle.addStyleName(ValoTheme.LABEL_H3);
        weatherTitle.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        weatherResult.addComponents(weatherTitle);

        // Set weather data labels
        float celsius = ((weather.getTemperature()-32)*5/9);
        celsius = round(celsius);
        float visibility = weather.getVisibility();
        visibility = round(visibility);

        if (visibility >= 10) {
            weatherInfo.setValue(weather.getSummary() + ". " + celsius + " degrees celsius and the average visibility is more than " + visibility + " miles.");
        }
        else {
            weatherInfo.setValue(weather.getSummary() + ". " + celsius + " degrees celsius and the average visibility is " + visibility + " miles.");
        }
        if (weather.getCloudCover() < 0.55) {
            cloudInfo.setValue("Not many clouds cover the sky! You should be able to spot the International Space Station!");
        }
        else {
            cloudInfo.setValue("Many clouds cover the sky! Detecting the International Space Station will be very difficult!");
        }
        weatherResult.addComponent(weatherInfo);
        weatherResult.addComponent(cloudInfo);

        // Find the application directory
        String basepath = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();

        // Image as a file resource
        FileResource resourceClearDay = new FileResource(new File(basepath + "/WEB-INF/images/ClearDay.png"));
        FileResource resourceClearNight = new FileResource(new File(basepath + "/WEB-INF/images/ClearNight.png"));
        FileResource resourceCloudy = new FileResource(new File(basepath + "/WEB-INF/images/Cloudy.png"));
        FileResource resourceRain = new FileResource(new File(basepath + "/WEB-INF/images/Rain.png"));
        FileResource resourceWind = new FileResource(new File(basepath + "/WEB-INF/images/Wind.png"));
        FileResource resourceSleet = new FileResource(new File(basepath + "/WEB-INF/images/Sleet.png"));
        FileResource resourceSnow = new FileResource(new File(basepath + "/WEB-INF/images/Snow.png"));
        FileResource resourceFog = new FileResource(new File(basepath + "/WEB-INF/images/Fog.png"));
        FileResource resourcePartlyCloudyDay = new FileResource(new File(basepath + "/WEB-INF/images/partlyCloudyDay.png"));
        FileResource resourcePartlyCloudyNight = new FileResource(new File(basepath + "/WEB-INF/images/partlyCloudyNight.png"));

        // Show the image in the application
        Image clearDayImage = new Image(null, resourceClearDay);
        Image clearNightImage = new Image(null, resourceClearNight);
        Image cloudyImage = new Image(null, resourceCloudy);
        Image rainImage = new Image(null, resourceRain);
        Image windImage = new Image(null, resourceWind);
        Image sleetImage = new Image(null, resourceSleet);
        Image snowImage = new Image(null, resourceSnow);
        Image fogImage = new Image(null, resourceFog);
        Image partlyCloudyDayImage = new Image(null, resourcePartlyCloudyDay);
        Image partlyCloudyNightImage = new Image(null, resourcePartlyCloudyNight);

        // Set the correct icon
        switch (weather.getIcon()) {
            case "clear-day":
                weatherResult.addComponent(clearDayImage);
                weatherResult.setComponentAlignment(clearDayImage, Alignment.BOTTOM_CENTER);
                break;
            case "clear-night":
                weatherResult.addComponent(clearNightImage);
                weatherResult.setComponentAlignment(clearNightImage, Alignment.BOTTOM_CENTER);
                break;
            case "rain":
                weatherResult.addComponent(rainImage);
                weatherResult.setComponentAlignment(rainImage, Alignment.BOTTOM_CENTER);
                break;
            case "snow":
                weatherResult.addComponent(snowImage);
                weatherResult.setComponentAlignment(snowImage, Alignment.BOTTOM_CENTER);
                break;
            case "sleet":
                weatherResult.addComponent(sleetImage);
                weatherResult.setComponentAlignment(sleetImage, Alignment.BOTTOM_CENTER);
                break;
            case "wind":
                weatherResult.addComponent(windImage);
                weatherResult.setComponentAlignment(windImage, Alignment.BOTTOM_CENTER);
                break;
            case "fog":
                weatherResult.addComponent(fogImage);
                weatherResult.setComponentAlignment(fogImage, Alignment.BOTTOM_CENTER);
                break;
            case "cloudy":
                weatherResult.addComponent(cloudyImage);
                weatherResult.setComponentAlignment(cloudyImage, Alignment.BOTTOM_CENTER);
                break;
            case "partly-cloudy-day":
                weatherResult.addComponent(partlyCloudyDayImage);
                weatherResult.setComponentAlignment(partlyCloudyDayImage, Alignment.BOTTOM_CENTER);
                break;
            case "partly-cloudy-night":
                weatherResult.addComponent(partlyCloudyNightImage);
                weatherResult.setComponentAlignment(partlyCloudyNightImage, Alignment.BOTTOM_CENTER);
                break;
            default:
                break;
        }

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

        Date time = HeliosUI.getDataProvider().pollISSPassTime(location).getDate();
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

    public static float round (float input) {
        BigDecimal roundedCelsius = new BigDecimal(Float.toString(input));
        roundedCelsius = roundedCelsius.setScale(2, BigDecimal.ROUND_HALF_UP);
        return roundedCelsius.floatValue();
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
