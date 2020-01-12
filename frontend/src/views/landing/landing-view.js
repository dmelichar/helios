import {html, PolymerElement} from '@polymer/polymer/polymer-element.js';

class LandingView extends PolymerElement {

    static get template() {
        return html`
<style include="shared-styles">
                :host {
                    display: block;
                    height: 100%;
                }
            </style>

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
