package at.fhtw.ode.helios.event;

import at.fhtw.ode.helios.view.HeliosViewType;

public abstract class HeliosEvent {

    public static class BrowserResizeEvent {

    }

    public static class UserLoggedOutEvent {

    }

    public static class NotificationsCountUpdatedEvent {
    }

    public static final class ReportsCountUpdatedEvent {
        private final int count;

        public ReportsCountUpdatedEvent(final int count) {
            this.count = count;
        }

        public int getCount() {
            return count;
        }

    }

    public static class CloseOpenWindowsEvent {
    }

    public static class ProfileUpdatedEvent {
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
