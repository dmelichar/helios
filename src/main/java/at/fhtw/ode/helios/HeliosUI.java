package at.fhtw.ode.helios;

import at.fhtw.ode.helios.data.DataProvider;
import at.fhtw.ode.helios.event.HeliosEvent;
import at.fhtw.ode.helios.event.HeliosEventBus;
import at.fhtw.ode.helios.view.MainView;
import com.google.common.eventbus.Subscribe;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinSession;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

import java.util.Locale;

@Theme("dashboard")
@Widgetset("HeliosWidgetSet")
@Title("Helios")
@SuppressWarnings("serial")
@SpringUI
public final class HeliosUI extends UI {

    private final DataProvider dataProvider = new DataProvider();
    private final HeliosEventBus heliosEventbus = new HeliosEventBus();

    @Override
    protected void init(final VaadinRequest request) {
        setLocale(Locale.UK);

        Responsive.makeResponsive(this);
        addStyleName(ValoTheme.UI_WITH_MENU);

        updateContent();
    }


    private void updateContent() {
        setContent(new MainView());
        removeStyleName("loginview");
        getNavigator().navigateTo(getNavigator().getState());

        // Code below for when I get the stupid sessioning to work
//        User user = (User) VaadinSession.getCurrent().getAttribute(User.class.getName());
//        if (user != null && "admin".equals(user.getRole())) {
//            System.out.println("authenticated user");
//            // Authenticated user
//            setContent(new MainView());
//            removeStyleName("loginview");
//            getNavigator().navigateTo(getNavigator().getState());
//        } else {
//            System.out.println("login required");
//            setContent(new LoginView());
//            addStyleName("loginview");
//        }
    }

    @Subscribe
    public void closeOpenWindows(final HeliosEvent.CloseOpenWindowsEvent event) {
        for (Window window : getWindows()) {
            window.close();
        }
    }

    public static DataProvider getDataProvider() {
        return ((HeliosUI) getCurrent()).dataProvider;
    }


    public static HeliosEventBus getHeliosEventbus() {
        return ((HeliosUI) getCurrent()).heliosEventbus;
    }

}
