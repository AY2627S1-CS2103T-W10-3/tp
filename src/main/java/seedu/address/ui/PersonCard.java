package seedu.address.ui;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Optional;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
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
    private Label phone;
    @FXML
    private Label address;
    @FXML
    private Label email;
    @FXML
    private Label subject;
    @FXML
    private Label educationLevel;
    @FXML
    private Label learningFocus;
    @FXML
    private Label nextAction;
    @FXML
    private Label lesson;
    @FXML
    private Label guardianName;
    @FXML
    private Label guardianPhone;
    @FXML
    private Label guardianEmail;
    @FXML
    private FlowPane tags;

    /**
     * Creates a {@code PersonCard} with the given {@code Person} and index to display.
     */
    public PersonCard(Person person, int displayedIndex) {
        super(FXML);
        this.person = person;
        id.setText(displayedIndex + ". ");
        name.setText(person.getName().fullName);
        phone.setText(person.getPhone().map(value -> value.value).orElse(Messages.NOT_PROVIDED));
        address.setText(person.getAddress().map(value -> value.value).orElse(Messages.NOT_PROVIDED));
        email.setText(person.getEmail().map(value -> value.value).orElse(Messages.NOT_PROVIDED));
        subject.setText("Subject: "
                + person.getSubject().map(value -> value.getValue()).orElse(Messages.NOT_PROVIDED));
        educationLevel.setText("Education level: "
                + person.getEducationLevel().map(value -> value.getValue()).orElse(Messages.NOT_PROVIDED));
        learningFocus.setText("Learning focus: "
                + person.getLearningFocus().map(value -> value.getValue()).orElse(Messages.NOT_PROVIDED));
        nextAction.setText("Next action: "
                + person.getNextAction().map(value -> value.getValue()).orElse(Messages.NOT_PROVIDED));
        LocalDateTime now = LocalDateTime.now();
        lesson.setText(formatLesson(person.getLesson(), now));
        if (person.getLesson().map(value -> value.isOverdue(now)).orElse(false)) {
            lesson.getStyleClass().add(OVERDUE_STYLE_CLASS);
        }
        guardianName.setText("Guardian: " + person.getGuardian()
                .map(value -> value.getName().fullName).orElse(Messages.NOT_PROVIDED));
        guardianPhone.setText("Guardian phone: " + person.getGuardian()
                .flatMap(value -> value.getPhone()).map(value -> value.value).orElse(Messages.NOT_PROVIDED));
        guardianEmail.setText("Guardian email: " + person.getGuardian()
                .flatMap(value -> value.getEmail()).map(value -> value.value).orElse(Messages.NOT_PROVIDED));
        person.getTags().stream()
                .sorted(Comparator.comparing(tag -> tag.tagName))
                .forEach(tag -> tags.getChildren().add(new Label(tag.tagName)));
    }

    /**
     * Returns the lesson line shown on a student card, marking a lesson that has started by {@code now} as overdue.
     */
    static String formatLesson(Optional<Lesson> lesson, LocalDateTime now) {
        if (lesson.isEmpty()) {
            return "Lesson: " + Messages.NOT_SCHEDULED;
        }
        String suffix = lesson.get().isOverdue(now) ? OVERDUE_SUFFIX : "";
        return "Lesson: " + lesson.get().getValue() + suffix;
    }
}
