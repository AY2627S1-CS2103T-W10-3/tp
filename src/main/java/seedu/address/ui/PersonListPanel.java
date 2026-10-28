package seedu.address.ui;

import java.util.logging.Logger;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.collections.ListChangeListener;
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
        personList.addListener(this::refreshIfReordered);
        personList.addListener((ListChangeListener<Person>) change -> keepSelectionValid());
        selectFirstStudent();
    }

    /** Returns the student currently selected in the displayed list. */
    public ReadOnlyObjectProperty<Person> selectedPersonProperty() {
        return personListView.getSelectionModel().selectedItemProperty();
    }

    /** Selects the first displayed student, or clears selection when the list is empty. */
    private void selectFirstStudent() {
        if (personListView.getItems().isEmpty()) {
            personListView.getSelectionModel().clearSelection();
        } else {
            personListView.getSelectionModel().selectFirst();
        }
    }

    private void keepSelectionValid() {
        Person selectedPerson = personListView.getSelectionModel().getSelectedItem();
        Platform.runLater(() -> {
            if (selectedPerson == null || !personListView.getItems().contains(selectedPerson)) {
                selectFirstStudent();
            }
        });
    }

    /**
     * Redraws the displayed cards after the current change has been processed, so that time-dependent details,
     * such as whether a lesson is overdue, reflect the current time.
     */
    public void refreshCards() {
        Platform.runLater(personListView::refresh);
    }

    /**
     * Refreshes the displayed cards when {@code change} is one that {@code ListView} does not redraw by itself.
     * The refresh runs after {@code ListView} has processed the change, so it never redraws with an outdated count.
     */
    private void refreshIfReordered(ListChangeListener.Change<? extends Person> change) {
        if (needsRefresh(change)) {
            logger.fine("Refreshing student list after the displayed order changed");
            Platform.runLater(personListView::refresh);
        }
    }

    /**
     * Returns true if {@code change} may leave stale cards in a {@code ListView}.
     * {@code ListView} redraws cells for additions and removals that change the number of items, and for the first
     * replaced range only. Reorders, item updates, several replaced ranges, and moves (a removal and an addition at
     * different positions) keep the same number of items, so the existing cells are only re-laid out.
     */
    static boolean needsRefresh(ListChangeListener.Change<?> change) {
        int replacedRanges = 0;
        boolean hasAddition = false;
        boolean hasRemoval = false;
        while (change.next()) {
            if (change.wasPermutated() || change.wasUpdated()) {
                return true;
            } else if (change.wasReplaced()) {
                replacedRanges++;
            } else {
                hasAddition |= change.wasAdded();
                hasRemoval |= change.wasRemoved();
            }
        }
        return replacedRanges > 1 || hasAddition && hasRemoval;
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
                Region card = new PersonCard(person, getIndex() + 1).getRoot();
                card.prefWidthProperty().bind(personListView.widthProperty().subtract(22));
                setGraphic(card);
            }
        }
    }

}
