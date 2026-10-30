package seedu.address.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.logging.Logger;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
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
    private static final String EMPTY_UPCOMING_VIEW_MESSAGE =
            "No upcoming lessons. Use schedule INDEX at/DATE_TIME to schedule one.";
    private final Logger logger = LogsCenter.getLogger(PersonListPanel.class);

    @FXML
    private ListView<Person> personListView;

    private Person pendingSelection;
    private boolean hasPendingSelection;
    private final SimpleBooleanProperty isUpcomingView = new SimpleBooleanProperty(false);

    /**
     * Creates a {@code PersonListPanel} with the displayed and complete student lists.
     */
    public PersonListPanel(ObservableList<Person> personList, ObservableList<Person> allPersons) {
        super(FXML);
        // Registered before the list is given to the ListView, so that it sees the selection before the
        // ListView's selection model adjusts it for the change.
        personList.addListener(this::rememberSelection);
        personListView.setItems(personList);
        Label emptyListPlaceholder = new Label();
        emptyListPlaceholder.textProperty().bind(Bindings.createStringBinding(() ->
                getEmptyListMessage(allPersons.size(), isUpcomingView.get()), allPersons, isUpcomingView));
        personListView.setPlaceholder(emptyListPlaceholder);
        personListView.setCellFactory(listView -> new PersonListViewCell());
        personList.addListener(this::refreshIfReordered);
        selectFirstStudent();
    }

    /** Returns the student currently selected in the displayed list. */
    public ReadOnlyObjectProperty<Person> selectedPersonProperty() {
        return personListView.getSelectionModel().selectedItemProperty();
    }

    /** Sets whether the displayed list represents the Upcoming Lessons view. */
    public void setUpcomingView(boolean isUpcomingView) {
        this.isUpcomingView.set(isUpcomingView);
    }

    /** Selects the first displayed student, or clears selection when the list is empty. */
    private void selectFirstStudent() {
        if (personListView.getItems().isEmpty()) {
            personListView.getSelectionModel().clearSelection();
        } else {
            personListView.getSelectionModel().selectFirst();
        }
    }

    /**
     * Remembers which student should stay selected after {@code change}, and reselects that student once the
     * change has been processed. When a student moves, {@code ListView} can highlight one row while reporting
     * another student as selected, so the student is always selected again by position.
     */
    private void rememberSelection(ListChangeListener.Change<? extends Person> change) {
        Person selectedPerson = hasPendingSelection
                ? pendingSelection
                : personListView.getSelectionModel().getSelectedItem();
        pendingSelection = findPersonToSelect(selectedPerson, change, Person::isSamePerson);
        if (!hasPendingSelection) {
            hasPendingSelection = true;
            Platform.runLater(this::applyPendingSelection);
        }
    }

    private void applyPendingSelection() {
        hasPendingSelection = false;
        int index = pendingSelection == null ? -1 : personListView.getItems().indexOf(pendingSelection);
        pendingSelection = null;
        if (index == -1) {
            selectFirstStudent();
        } else {
            personListView.getSelectionModel().clearAndSelect(index);
        }
    }

    /**
     * Returns the item in the changed list that represents {@code selected}, or null if there is none.
     * An edited student is a new object, so it is matched with {@code isSame}. A student whose identity changed
     * is matched when the change only replaces that one student.
     */
    static <T> T findPersonToSelect(T selected, ListChangeListener.Change<? extends T> change,
                                    BiPredicate<T, T> isSame) {
        if (selected == null) {
            return null;
        }
        List<T> added = new ArrayList<>();
        List<T> removed = new ArrayList<>();
        while (change.next()) {
            added.addAll(change.getAddedSubList());
            removed.addAll(change.getRemoved());
        }
        change.reset();

        List<? extends T> items = change.getList();
        if (items.contains(selected)) {
            return selected;
        }
        for (T item : items) {
            if (isSame.test(item, selected)) {
                return item;
            }
        }
        boolean isOnlyReplacement = removed.equals(List.of(selected)) && added.size() == 1;
        return isOnlyReplacement ? added.get(0) : null;
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

    static String getEmptyListMessage(int totalStudentCount, boolean isUpcomingView) {
        if (totalStudentCount == 0) {
            return NO_STUDENTS_MESSAGE;
        }
        return isUpcomingView ? EMPTY_UPCOMING_VIEW_MESSAGE : EMPTY_VIEW_MESSAGE;
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
