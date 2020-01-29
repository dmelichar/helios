package at.fhtw.ode.helios.view;

import at.fhtw.ode.helios.view.locations.LocationsView;
import at.fhtw.ode.helios.view.map.MapView;
import com.vaadin.navigator.View;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;

public enum HeliosViewType {
    MAP("map", MapView.class, FontAwesome.MAP, true),
    LOCATIONS("locations", LocationsView.class, FontAwesome.MAP_MARKER, false);

    private final String viewName;
    private final Class<? extends View> viewClass;
    private final Resource icon;
    private final boolean stateful;

    HeliosViewType(final String viewName,
                           final Class<? extends View> viewClass, final Resource icon,
                           final boolean stateful) {
        this.viewName = viewName;
        this.viewClass = viewClass;
        this.icon = icon;
        this.stateful = stateful;
    }

    public boolean isStateful() {
        return stateful;
    }

    public String getViewName() {
        return viewName;
    }

    public Class<? extends View> getViewClass() {
        return viewClass;
    }

    public Resource getIcon() {
        return icon;
    }

    public static HeliosViewType getByViewName(final String viewName) {
        HeliosViewType result = null;
        for (HeliosViewType viewType : values()) {
            if (viewType.getViewName().equals(viewName)) {
                result = viewType;
                break;
            }
        }
        return result;
    }

}
