package seedu.address.ui;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Optional;

import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import seedu.address.logic.Messages;
import seedu.address.model.person.Lesson;
import seedu.address.model.person.Person;

/** Displays the complete read-only details of the selected student. */
public class PersonDetailsPanel extends UiPart<Region> {

    private static final String FXML = "PersonDetailsPanel.fxml";
    private static final String OVERDUE_SUFFIX = " (overdue)";
    private static final String OVERDUE_STYLE_CLASS = "details-overdue";

    @FXML
    private VBox emptyState;
    @FXML
    private ScrollPane detailsScroll;
    @FXML
    private Label name;
    @FXML
    private FlowPane tags;
    @FXML
    private Label phone;
    @FXML
    private Label email;
    @FXML
    private Label address;
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

    private Person selectedPerson;

    /** Creates a panel that follows the selected student. */
    public PersonDetailsPanel(ObservableValue<Person> selectedPerson) {
        super(FXML);
        selectedPerson.addListener((unused, oldPerson, newPerson) -> showPerson(newPerson));
        showPerson(selectedPerson.getValue());
    }

    private void showPerson(Person person) {
        selectedPerson = person;
        boolean hasPerson = person != null;
        emptyState.setVisible(!hasPerson);
        emptyState.setManaged(!hasPerson);
        detailsScroll.setVisible(hasPerson);
        detailsScroll.setManaged(hasPerson);
        if (!hasPerson) {
            return;
        }

        name.setText(person.getName().fullName);
        showTags(person);
        phone.setText(person.getPhone().map(value -> value.value).orElse(Messages.NOT_PROVIDED));
        email.setText(person.getEmail().map(value -> value.value).orElse(Messages.NOT_PROVIDED));
        address.setText(person.getAddress().map(value -> value.value).orElse(Messages.NOT_PROVIDED));
        subject.setText(person.getSubject().map(value -> value.getValue()).orElse(Messages.NOT_PROVIDED));
        educationLevel.setText(person.getEducationLevel().map(value -> value.getValue()).orElse(Messages.NOT_PROVIDED));
        learningFocus.setText(person.getLearningFocus().map(value -> value.getValue()).orElse(Messages.NOT_PROVIDED));
        nextAction.setText(person.getNextAction().map(value -> value.getValue()).orElse(Messages.NOT_PROVIDED));
        LocalDateTime now = LocalDateTime.now();
        lesson.setText(formatLesson(person.getLesson(), now));
        lesson.getStyleClass().remove(OVERDUE_STYLE_CLASS);
        if (person.getLesson().map(value -> value.isOverdue(now)).orElse(false)) {
            lesson.getStyleClass().add(OVERDUE_STYLE_CLASS);
        }
        guardianName.setText(person.getGuardian()
                .map(value -> value.getName().fullName).orElse(Messages.NOT_PROVIDED));
        guardianPhone.setText(person.getGuardian().flatMap(value -> value.getPhone())
                .map(value -> value.value).orElse(Messages.NOT_PROVIDED));
        guardianEmail.setText(person.getGuardian().flatMap(value -> value.getEmail())
                .map(value -> value.value).orElse(Messages.NOT_PROVIDED));
    }

    /** Refreshes time-dependent details for the selected student. */
    public void refresh() {
        showPerson(selectedPerson);
    }

    private void showTags(Person person) {
        tags.getChildren().clear();
        boolean hasTags = !person.getTags().isEmpty();
        tags.setVisible(hasTags);
        tags.setManaged(hasTags);
        person.getTags().stream()
                .sorted(Comparator.comparing(tag -> tag.tagName))
                .map(tag -> new Label(tag.tagName))
                .forEach(tags.getChildren()::add);
    }

    static String formatLesson(Optional<Lesson> lesson, LocalDateTime now) {
        if (lesson.isEmpty()) {
            return Messages.NOT_SCHEDULED;
        }
        return lesson.get().getValue() + (lesson.get().isOverdue(now) ? OVERDUE_SUFFIX : "");
    }
}
