package at.fhtw.ode.helios;

import com.vaadin.server.*;
import org.jsoup.nodes.Element;

public class HeliosSessionInitListener implements SessionInitListener {

    @Override
    public final void sessionInit(final SessionInitEvent event) {

        event.getSession().addBootstrapListener(new BootstrapListener() {

            @Override
            public void modifyBootstrapPage(final BootstrapPageResponse response) {
                final Element head = response.getDocument().head();
                head.appendElement("meta")
                        .attr("name", "viewport")
                        .attr("content", "width=device-width, initial-scale=1, maximum-scale=1.0, user-scalable=no");
                head.appendElement("meta")
                        .attr("name", "apple-mobile-web-app-capable")
                        .attr("content", "yes");
                head.appendElement("meta")
                        .attr("name", "apple-mobile-web-app-status-bar-style")
                        .attr("content", "black-translucent");
            }

            @Override
            public void modifyBootstrapFragment(
                    final BootstrapFragmentResponse response) {
            }
        });
    }
}
