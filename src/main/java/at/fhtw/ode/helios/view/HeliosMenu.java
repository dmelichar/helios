package at.fhtw.ode.helios.view;

import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings({"serial"})
public final class HeliosMenu extends CustomComponent {

    public static final String ID = "helios-menu";
    private static final String STYLE_VISIBLE = "valo-menu-visible";

    public HeliosMenu() {
        setPrimaryStyleName("valo-menu");
        setId(ID);
        setSizeUndefined();

        setCompositionRoot(buildContent());
    }

    private Component buildContent() {
        final CssLayout menuContent = new CssLayout();
        menuContent.addStyleName("sidebar");
        menuContent.addStyleName(ValoTheme.MENU_PART);
        menuContent.addStyleName("no-vertical-drag-hints");
        menuContent.addStyleName("no-horizontal-drag-hints");
        menuContent.setWidth(null);
        menuContent.setHeight("100%");

        menuContent.addComponent(buildTitle());
        menuContent.addComponent(buildMenuItems());

        return menuContent;
    }

    private Component buildTitle() {
        Label logo = new Label("<h2><strong>Helios</strong></h2>", ContentMode.HTML);
        logo.setSizeUndefined();
        HorizontalLayout logoWrapper = new HorizontalLayout(logo);
        logoWrapper.setComponentAlignment(logo, Alignment.MIDDLE_CENTER);
        logoWrapper.addStyleName("valo-menu-title");
        logoWrapper.setSpacing(false);
        return logoWrapper;
    }


    private Component buildMenuItems() {
        CssLayout menuItemsLayout = new CssLayout();
        menuItemsLayout.addStyleName("valo-menuitems");

        for (final HeliosViewType view : HeliosViewType.values()) {
            Component menuItemComponent = new ValoMenuItemButton(view);
            menuItemsLayout.addComponent(menuItemComponent);
        }
        return menuItemsLayout;
    }

    @Override
    public void attach() {
        super.attach();
    }


    public final class ValoMenuItemButton extends Button {

        private static final String STYLE_SELECTED = "selected";

        private final HeliosViewType view;

        public ValoMenuItemButton(final HeliosViewType view) {
            this.view = view;
            setPrimaryStyleName("valo-menu-item");
            setIcon(view.getIcon());
            setCaption(view.getViewName().substring(0, 1).toUpperCase() + view.getViewName().substring(1));
            addClickListener((ClickListener) event -> UI.getCurrent().getNavigator()
                    .navigateTo(view.getViewName()));
        }
    }
}
