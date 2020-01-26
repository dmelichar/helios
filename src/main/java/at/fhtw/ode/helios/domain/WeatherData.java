package at.fhtw.ode.helios.domain;

public final class WeatherData {

    private String summary;
    private float cloudCover;

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public float getCloudCover() {
        return cloudCover;
    }

    public void setCloudCover(float cloudCover) {
        this.cloudCover = cloudCover;
    }
}
