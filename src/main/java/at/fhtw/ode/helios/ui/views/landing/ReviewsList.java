package at.fhtw.ode.helios.ui.views.landing;

import at.fhtw.ode.helios.backend.Review;
import at.fhtw.ode.helios.backend.ReviewService;
import at.fhtw.ode.helios.ui.MainLayout;
import at.fhtw.ode.helios.ui.common.AbstractEditorDialog;
import at.fhtw.ode.helios.ui.encoders.LocalDateToStringEncoder;
import at.fhtw.ode.helios.ui.encoders.LongToStringEncoder;
import at.fhtw.ode.helios.ui.views.landing.ReviewsList.ReviewsModel;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyModifier;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.polymertemplate.EventHandler;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.ModelItem;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.templatemodel.Encode;
import com.vaadin.flow.templatemodel.TemplateModel;

import java.util.List;


@Route(value = "", layout = MainLayout.class)
@PageTitle("Review List")
@Tag("reviews-list")
@JsModule("./src/views/reviewslist/reviews-list.js")
public class ReviewsList extends PolymerTemplate<ReviewsModel> {

    public interface ReviewsModel extends TemplateModel {
        @Encode(value = LongToStringEncoder.class, path = "id")
        @Encode(value = LocalDateToStringEncoder.class, path = "date")
        @Encode(value = LongToStringEncoder.class, path = "category.id")
        void setReviews(List<Review> reviews);
    }

    @Id("search")
    private TextField search;
    @Id("newReview")
    private Button addReview;
    @Id("header")
    private H2 header;

    private ReviewEditorDialog reviewForm = new ReviewEditorDialog(
            this::saveUpdate, this::deleteUpdate);

    public ReviewsList() {
        search.setPlaceholder("Search reviews");
        search.addValueChangeListener(e -> updateList());
        search.setValueChangeMode(ValueChangeMode.EAGER);
        search.addFocusShortcut(Key.KEY_F, KeyModifier.CONTROL);

        addReview.addClickListener(e -> openForm(new Review(),
                AbstractEditorDialog.Operation.ADD));
        /*
            This is a fall-back method:
            '+' is not a event.code (DOM events), so as a fall-back shortcuts
            will perform a character-based comparison. Since Key.ADD changes
            locations frequently based on the keyboard layout's language, we
            opted to use a character instead.
         */
        addReview.addClickShortcut(Key.of("+"));


        // Set review button and edit button text from Java
        getElement().setProperty("reviewButtonText", "New review");
        getElement().setProperty("editButtonText", "Edit");

        updateList();

    }

    public void saveUpdate(Review review,
                           AbstractEditorDialog.Operation operation) {
        ReviewService.getInstance().saveReview(review);
        updateList();
        Notification.show(
                "Beverage successfully " + operation.getNameInText() + "ed.",
                3000, Position.BOTTOM_START)
                .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
    }

    public void deleteUpdate(Review review) {
        ReviewService.getInstance().deleteReview(review);
        updateList();
        Notification.show("Beverage successfully deleted.", 3000,
                Position.BOTTOM_START)
                .addThemeVariants(NotificationVariant.LUMO_CONTRAST);
    }

    private void updateList() {
        List<Review> reviews = ReviewService.getInstance()
                .findReviews(search.getValue());
        if (search.isEmpty()) {
            header.setText("Reviews");
            header.add(new Span(reviews.size() + " in total"));
        } else {
            header.setText("Search for “" + search.getValue() + "”");
            if (!reviews.isEmpty()) {
                header.add(new Span(reviews.size() + " results"));
            }
        }
        getModel().setReviews(reviews);
    }

    @EventHandler
    private void edit(@ModelItem Review review) {
        openForm(review, AbstractEditorDialog.Operation.EDIT);
    }

    private void openForm(Review review,
                          AbstractEditorDialog.Operation operation) {
        // Add the form lazily as the UI is not yet initialized when
        // this view is constructed
        if (reviewForm.getElement().getParent() == null) {
            getUI().ifPresent(ui -> ui.add(reviewForm));
        }
        reviewForm.open(review, operation);
    }
}
