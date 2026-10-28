package seedu.address.ui;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Optional;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import seedu.address.logic.Messages;
import seedu.address.model.person.Lesson;
import seedu.address.model.person.Person;

/**
 * A UI component that displays information of a {@code Person}.
 */
public class PersonCard extends UiPart<Region> {

    private static final String FXML = "PersonListCard.fxml";
    private static final String OVERDUE_STYLE_CLASS = "lesson-overdue";
    private static final String OVERDUE_SUFFIX = " (overdue)";

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
        id.setText(displayedIndex + ". ");
        name.setText(person.getName().fullName);
        subject.setText(person.getSubject().map(value -> value.getValue()).orElse(Messages.NOT_PROVIDED));
        educationLevel.setText(person.getEducationLevel()
                .map(value -> value.getValue()).orElse(Messages.NOT_PROVIDED));
        LocalDateTime now = LocalDateTime.now();
        lesson.setText(formatLesson(person.getLesson(), now));
        if (person.getLesson().map(value -> value.isOverdue(now)).orElse(false)) {
            lesson.getStyleClass().add(OVERDUE_STYLE_CLASS);
        }
        person.getTags().stream()
                .sorted(Comparator.comparing(tag -> tag.tagName))
                .forEach(tag -> tags.getChildren().add(new Label(tag.tagName)));
    }

    /**
     * Returns the lesson line shown on a student card, marking a lesson that has started by {@code now} as overdue.
     */
    static String formatLesson(Optional<Lesson> lesson, LocalDateTime now) {
        if (lesson.isEmpty()) {
            return Messages.NOT_SCHEDULED;
        }
        String suffix = lesson.get().isOverdue(now) ? OVERDUE_SUFFIX : "";
        return lesson.get().getValue() + suffix;
    }
}
