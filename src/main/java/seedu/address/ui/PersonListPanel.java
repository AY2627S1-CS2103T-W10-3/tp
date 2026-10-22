package seedu.address.ui;

import java.util.logging.Logger;

import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.person.Person;

/**
 * Panel containing the list of persons.
 */
public class PersonListPanel extends UiPart<Region> {
    private static final String FXML = "PersonListPanel.fxml";
    private static final String NO_STUDENTS_MESSAGE =
            "No students recorded. Use add to create your first student.";
    private static final String EMPTY_VIEW_MESSAGE =
            "No students in this view. Use list or list archived.";
    private final Logger logger = LogsCenter.getLogger(PersonListPanel.class);

    @FXML
    private ListView<Person> personListView;

    /**
     * Creates a {@code PersonListPanel} with the displayed and complete student lists.
     */
    public PersonListPanel(ObservableList<Person> personList, ObservableList<Person> allPersons) {
        super(FXML);
        personListView.setItems(personList);
        Label emptyListPlaceholder = new Label();
        emptyListPlaceholder.textProperty().bind(Bindings.createStringBinding(() ->
                getEmptyListMessage(allPersons.size()), allPersons));
        personListView.setPlaceholder(emptyListPlaceholder);
        personListView.setCellFactory(listView -> new PersonListViewCell());
    }

    private static String getEmptyListMessage(int totalStudentCount) {
        return totalStudentCount == 0 ? NO_STUDENTS_MESSAGE : EMPTY_VIEW_MESSAGE;
    }

    /**
     * Custom {@code ListCell} that displays the graphics of a {@code Person} using a {@code PersonCard}.
     */
    class PersonListViewCell extends ListCell<Person> {
        @Override
        protected void updateItem(Person person, boolean empty) {
            super.updateItem(person, empty);

            if (empty || person == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(new PersonCard(person, getIndex() + 1).getRoot());
            }
        }
    }

}
