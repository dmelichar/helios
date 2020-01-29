package at.fhtw.ode.helios.event;

import at.fhtw.ode.helios.view.HeliosViewType;

public abstract class HeliosEvent {

    public static class BrowserResizeEvent {
    }

    public static class CloseOpenWindowsEvent {
    }

    public static final class PostViewChangeEvent {
        private final HeliosViewType view;

        public PostViewChangeEvent(final HeliosViewType view) {
            this.view = view;
        }

        public HeliosViewType getView() {
            return view;
        }
    }
}
