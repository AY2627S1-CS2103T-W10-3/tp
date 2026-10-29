package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;

/**
 * Tests the empty-list message, which list changes make {@code PersonListPanel} refresh its cards, and which
 * student it keeps selected after a change.
 */
public class PersonListPanelTest {

    private ObservableList<String> source;
    private SortedList<String> displayed;
    private FilteredList<String> filtered;
    private final List<Boolean> refreshDecisions = new ArrayList<>();
    private ObservableList<String> lessonSource;
    private FilteredList<String> lessonFiltered;
    private SortedList<String> lessonDisplayed;

    @BeforeEach
    public void setUp() {
        source = FXCollections.observableArrayList("Carla", "Alex", "Bob");
        filtered = new FilteredList<>(source);
        displayed = new SortedList<>(filtered);
        displayed.addListener((ListChangeListener<String>) change ->
                refreshDecisions.add(PersonListPanel.needsRefresh(change)));
    }

    @Test
    public void getEmptyListMessage_usesRecordedStudentCount() throws Exception {
        Optional<Method> messageMethod = Arrays.stream(PersonListPanel.class.getDeclaredMethods())
                .filter(method -> method.getName().equals("getEmptyListMessage"))
                .findFirst();
        assertTrue(messageMethod.isPresent(), "PersonListPanel should select its message from the total record count");

        Method method = messageMethod.get();
        method.setAccessible(true);
        assertEquals("No students recorded. Use add to create your first student.", method.invoke(null, 0));
        assertEquals("No students in this view. Use list or list archived.", method.invoke(null, 1));
    }

    @Test
    public void needsRefresh_reorder_returnsTrue() {
        displayed.setComparator(Comparator.naturalOrder());
        displayed.setComparator(null);

        assertEquals(List.of(true, true), refreshDecisions);
    }

    @Test
    public void needsRefresh_moveWithinSortedList_returnsTrue() {
        displayed.setComparator(Comparator.naturalOrder());
        refreshDecisions.clear();

        source.set(source.indexOf("Alex"), "Dora");

        assertEquals(List.of("Bob", "Carla", "Dora"), displayed);
        assertEquals(List.of(true), refreshDecisions);
    }

    @Test
    public void needsRefresh_singleReplacement_returnsFalse() {
        source.set(0, "Cara");

        assertEquals(List.of(false), refreshDecisions);
    }

    @Test
    public void needsRefresh_additionOrRemoval_returnsFalse() {
        source.add("Dora");
        source.remove("Alex");
        filtered.setPredicate(name -> name.startsWith("B"));

        assertFalse(refreshDecisions.contains(true));
    }

    @Test
    public void needsRefresh_filterWithSameSize_returnsFalse() {
        filtered.setPredicate(name -> true);

        assertTrue(refreshDecisions.stream().noneMatch(decision -> decision));
    }

    @Test
    public void findPersonToSelect_editedStudentMoves_returnsEditedStudent() {
        // Mirrors rescheduling the first student in the upcoming view, which moves them to the end.
        List<String> selections = trackSelection("Bernice:10-30",
                "Alex:11-02", "Bernice:10-30", "Charlotte:10-31");

        lessonSource.set(1, "Bernice:12-01");

        assertEquals(List.of("Charlotte:10-31", "Alex:11-02", "Bernice:12-01"), lessonDisplayed);
        assertEquals(List.of("Bernice:12-01"), selections);
    }

    @Test
    public void findPersonToSelect_selectedStudentUnchanged_returnsSameStudent() {
        List<String> selections = trackSelection("Alex:11-02",
                "Alex:11-02", "Bernice:10-30", "Charlotte:10-31");

        lessonSource.set(1, "Bernice:12-01");
        lessonSource.add("Dora:10-01");

        assertEquals(List.of("Alex:11-02", "Alex:11-02"), selections);
    }

    @Test
    public void findPersonToSelect_onlySelectedStudentRenamed_returnsRenamedStudent() {
        List<String> selections = trackSelection("Bernice:10-30",
                "Alex:11-02", "Bernice:10-30", "Charlotte:10-31");

        lessonSource.set(1, "Bea:10-30");

        assertEquals(List.of("Bea:10-30"), selections);
    }

    @Test
    public void findPersonToSelect_selectedStudentFilteredOut_returnsNull() {
        List<String> selections = trackSelection("Bernice:10-30",
                "Alex:11-02", "Bernice:10-30", "Charlotte:10-31");

        lessonFiltered.setPredicate(student -> !student.startsWith("Bernice"));

        assertEquals(Arrays.asList((String) null), selections);
    }

    @Test
    public void findPersonToSelect_nothingSelected_returnsNull() {
        List<String> selections = trackSelection(null, "Alex:11-02");

        lessonSource.add("Bernice:10-30");

        assertEquals(Arrays.asList((String) null), selections);
    }

    /**
     * Displays {@code students}, written as {@code name:lesson}, in lesson order, and returns the student to select
     * after each later change, starting from {@code selected}. Students with the same name are the same student.
     */
    private List<String> trackSelection(String selected, String... students) {
        lessonSource = FXCollections.observableArrayList(students);
        lessonFiltered = new FilteredList<>(lessonSource);
        lessonDisplayed = new SortedList<>(lessonFiltered, Comparator.comparing(student -> student.split(":")[1]));
        List<String> selections = new ArrayList<>();
        lessonDisplayed.addListener((ListChangeListener<String>) change -> {
            String previous = selections.isEmpty() ? selected : selections.getLast();
            selections.add(PersonListPanel.findPersonToSelect(previous, change, (first, second) ->
                    first.split(":")[0].equals(second.split(":")[0])));
        });
        return selections;
    }
}
