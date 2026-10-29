package seedu.address.ui;

import java.time.LocalDateTime;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import seedu.address.model.person.Person;
import seedu.address.ui.StudentDisplayFormatter.StudentDetails;

/**
 * A UI component that displays information of a {@code Person}.
 */
public class PersonCard extends UiPart<Region> {

    private static final String FXML = "PersonListCard.fxml";
    private static final String OVERDUE_STYLE_CLASS = "lesson-overdue";

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on AddressBook level 4</a>
     */

    public final Person person;

    @FXML
    private VBox cardPane;
    @FXML
    private Label name;
    @FXML
    private Label id;
    @FXML
    private Label subject;
    @FXML
    private Label educationLevel;
    @FXML
    private Label lesson;
    @FXML
    private HBox tags;

    /**
     * Creates a {@code PersonCard} with the given {@code Person} and index to display.
     */
    public PersonCard(Person person, int displayedIndex) {
        super(FXML);
        this.person = person;
        StudentDetails details = StudentDisplayFormatter.formatDetails(person, LocalDateTime.now());
        id.setText(displayedIndex + ". ");
        name.setText(details.name());
        subject.setText(details.subject());
        educationLevel.setText(details.educationLevel());
        lesson.setText(details.lesson().text());
        if (details.lesson().isOverdue()) {
            lesson.getStyleClass().add(OVERDUE_STYLE_CLASS);
        }
        details.tags().forEach(tag -> tags.getChildren().add(new Label(tag)));
    }
}
