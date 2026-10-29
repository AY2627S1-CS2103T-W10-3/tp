package seedu.address.ui;

import java.time.LocalDateTime;
import java.util.List;

import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import seedu.address.model.person.Person;
import seedu.address.ui.StudentDisplayFormatter.StudentDetails;

/** Displays the complete read-only details of the selected student. */
public class PersonDetailsPanel extends UiPart<Region> {

    private static final String FXML = "PersonDetailsPanel.fxml";
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

        StudentDetails details = StudentDisplayFormatter.formatDetails(person, LocalDateTime.now());
        name.setText(details.name());
        showTags(details.tags());
        phone.setText(details.phone());
        email.setText(details.email());
        address.setText(details.address());
        subject.setText(details.subject());
        educationLevel.setText(details.educationLevel());
        learningFocus.setText(details.learningFocus());
        nextAction.setText(details.nextAction());
        lesson.setText(details.lesson().text());
        lesson.getStyleClass().remove(OVERDUE_STYLE_CLASS);
        if (details.lesson().isOverdue()) {
            lesson.getStyleClass().add(OVERDUE_STYLE_CLASS);
        }
        guardianName.setText(details.guardianName());
        guardianPhone.setText(details.guardianPhone());
        guardianEmail.setText(details.guardianEmail());
    }

    /** Refreshes time-dependent details for the selected student. */
    public void refresh() {
        showPerson(selectedPerson);
    }

    private void showTags(List<String> tagNames) {
        tags.getChildren().clear();
        boolean hasTags = !tagNames.isEmpty();
        tags.setVisible(hasTags);
        tags.setManaged(hasTags);
        tagNames.stream()
                .map(Label::new)
                .forEach(tags.getChildren()::add);
    }
}
