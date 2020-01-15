package at.fhtw.ode.helios.event;

import at.fhtw.ode.helios.HeliosUI;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.SubscriberExceptionContext;
import com.google.common.eventbus.SubscriberExceptionHandler;

public class HeliosEventBus implements SubscriberExceptionHandler {

    private final EventBus eventBus = new EventBus(this);

    public static void post(final Object event) {
        HeliosUI.getHeliosEventbus().eventBus.post(event);
    }

    public static void register(final Object object) {
        HeliosUI.getHeliosEventbus().eventBus.register(object);
    }

    public static void unregister(final Object object) {
        HeliosUI.getHeliosEventbus().eventBus.unregister(object);
    }

    @Override
    public final void handleException(final Throwable exception,
                                      final SubscriberExceptionContext context) {
        exception.printStackTrace();
    }
}
