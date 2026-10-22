package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;

/** Tests which list changes make {@code PersonListPanel} refresh its cards. */
public class PersonListPanelTest {

    private ObservableList<String> source;
    private SortedList<String> displayed;
    private FilteredList<String> filtered;
    private final List<Boolean> refreshDecisions = new ArrayList<>();

    @BeforeEach
    public void setUp() {
        source = FXCollections.observableArrayList("Carla", "Alex", "Bob");
        filtered = new FilteredList<>(source);
        displayed = new SortedList<>(filtered);
        displayed.addListener((ListChangeListener<String>) change ->
                refreshDecisions.add(PersonListPanel.needsRefresh(change)));
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
}
