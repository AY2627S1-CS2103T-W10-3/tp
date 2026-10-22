package seedu.address.ui;

import java.util.logging.Logger;

import javafx.application.Platform;
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
    private static final String EMPTY_LIST_MESSAGE = "No students recorded. Use add to create your first student.";
    private final Logger logger = LogsCenter.getLogger(PersonListPanel.class);

    @FXML
    private ListView<Person> personListView;

    /**
     * Creates a {@code PersonListPanel} with the given {@code ObservableList}.
     */
    public PersonListPanel(ObservableList<Person> personList) {
        super(FXML);
        personListView.setItems(personList);
        personListView.setPlaceholder(new Label(EMPTY_LIST_MESSAGE));
        personListView.setCellFactory(listView -> new PersonListViewCell());
        personList.addListener(this::refreshIfReordered);
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
