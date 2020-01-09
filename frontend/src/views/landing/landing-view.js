import {html, PolymerElement} from '@polymer/polymer/polymer-element.js';
import '@vaadin/vaadin-ordered-layout/src/vaadin-vertical-layout.js';
import '@vaadin/vaadin-ordered-layout/src/vaadin-horizontal-layout.js';
import '@vaadin/vaadin-text-field/src/vaadin-text-field.js';
import '@vaadin/vaadin-tabs/src/vaadin-tabs.js';
import '@vaadin/vaadin-tabs/src/vaadin-tab.js';
import '@polymer/iron-icon/iron-icon.js';

class LandingView extends PolymerElement {

    static get template() {
        return html`
<style include="shared-styles">
                :host {
                    display: block;
                    height: 100%;
                }
            </style>
<vaadin-vertical-layout style="width: 100%; height: 100%;">
 <vaadin-horizontal-layout theme="margin" style="align-self: stretch; justify-content: space-evenly;">
  <vaadin-tabs theme="equal-width-tabs" style="align-self: center;">
   <vaadin-tab>
    <vaadin-text-field placeholder="Helios"></vaadin-text-field>
   </vaadin-tab>
   <vaadin-tab>
    <iron-icon icon="VaadinIcon.MAP_MARKER"></iron-icon>
   </vaadin-tab>
  </vaadin-tabs>
 </vaadin-horizontal-layout>
</vaadin-vertical-layout>
`;
    }

    static get is() {
        return 'landing-view';
    }

    static get properties() {
        return {
            // Declare your properties here.
        };
    }
}

customElements.define(LandingView.is, LandingView);
