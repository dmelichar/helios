package at.fhtw.ode.helios.ui.views.landing;

import at.fhtw.ode.helios.backend.Category;
import at.fhtw.ode.helios.backend.CategoryService;
import at.fhtw.ode.helios.backend.Review;
import at.fhtw.ode.helios.ui.common.AbstractEditorDialog;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.data.validator.DateRangeValidator;
import com.vaadin.flow.data.validator.IntegerRangeValidator;
import com.vaadin.flow.data.validator.StringLengthValidator;

import java.time.LocalDate;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;


public class ReviewEditorDialog extends AbstractEditorDialog<Review> {

    private transient CategoryService categoryService = CategoryService
            .getInstance();

    private ComboBox<Category> categoryBox = new ComboBox<>();
    private ComboBox<String> scoreBox = new ComboBox<>();
    private DatePicker lastTasted = new DatePicker();
    private TextField beverageName = new TextField();
    private TextField timesTasted = new TextField();

    public ReviewEditorDialog(BiConsumer<Review, Operation> saveHandler,
                              Consumer<Review> deleteHandler) {
        super("review", saveHandler, deleteHandler);

        createNameField();
        createCategoryBox();
        createDatePicker();
        createTimesField();
        createScoreBox();
    }

    private void createScoreBox() {
        scoreBox.setLabel("Rating");
        scoreBox.setRequired(true);
        scoreBox.setAllowCustomValue(false);
        scoreBox.setItems("1", "2", "3", "4", "5");
        getFormLayout().add(scoreBox);

        getBinder().forField(scoreBox)
                .withConverter(new StringToIntegerConverter(0,
                        "The score should be a number."))
                .withValidator(new IntegerRangeValidator(
                        "The tasting count must be between 1 and 5.", 1, 5))
                .bind(Review::getScore, Review::setScore);
    }

    private void createDatePicker() {
        lastTasted.setLabel("Last tasted");
        lastTasted.setRequired(true);
        lastTasted.setMax(LocalDate.now());
        lastTasted.setMin(LocalDate.of(1, 1, 1));
        lastTasted.setValue(LocalDate.now());
        getFormLayout().add(lastTasted);

        getBinder().forField(lastTasted)
                .withValidator(Objects::nonNull,
                        "The date should be in MM/dd/yyyy format.")
                .withValidator(new DateRangeValidator(
                        "The date should be neither Before Christ nor in the future.",
                        LocalDate.of(1, 1, 1), LocalDate.now()))
                .bind(Review::getDate, Review::setDate);

    }

    private void createCategoryBox() {
        categoryBox.setLabel("Category");
        categoryBox.setRequired(true);
        categoryBox.setItemLabelGenerator(Category::getName);
        categoryBox.setAllowCustomValue(false);
        categoryBox.setItems(categoryService.findCategories(""));
        getFormLayout().add(categoryBox);

        getBinder().forField(categoryBox)
                .withValidator(Objects::nonNull,
                        "The category should be defined.")
                .bind(Review::getCategory, Review::setCategory);
    }

    private void createTimesField() {
        timesTasted.setLabel("Times tasted");
        timesTasted.setRequired(true);
        timesTasted.setPattern("[0-9]*");
        timesTasted.setPreventInvalidInput(true);
        getFormLayout().add(timesTasted);

        getBinder().forField(timesTasted)
                .withConverter(
                        new StringToIntegerConverter(0, "Must enter a number."))
                .withValidator(new IntegerRangeValidator(
                        "The tasting count must be between 1 and 99.", 1, 99))
                .bind(Review::getCount, Review::setCount);
    }

    private void createNameField() {
        beverageName.setLabel("Beverage");
        beverageName.setRequired(true);
        getFormLayout().add(beverageName);

        getBinder().forField(beverageName)
                .withConverter(String::trim, String::trim)
                .withValidator(new StringLengthValidator(
                        "Beverage name must contain at least 3 printable characters",
                        3, null))
                .bind(Review::getName, Review::setName);
    }

    @Override
    protected void confirmDelete() {
        openConfirmationDialog("Delete review",
                "Are you sure you want to delete the review for “" + getCurrentItem().getName() + "”?", "");
    }

}
