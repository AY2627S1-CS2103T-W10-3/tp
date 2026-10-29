package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.ui.MainWindow.NavigationSelection.ACTIVE;
import static seedu.address.ui.MainWindow.NavigationSelection.ARCHIVED;
import static seedu.address.ui.MainWindow.NavigationSelection.UNCHANGED;
import static seedu.address.ui.MainWindow.NavigationSelection.UPCOMING;

import org.junit.jupiter.api.Test;

public class MainWindowTest {

    @Test
    public void navigationSelectionFor_navigationCommands_returnsMatchingSelection() {
        assertEquals(ACTIVE, MainWindow.navigationSelectionFor("list"));
        assertEquals(UPCOMING, MainWindow.navigationSelectionFor("upcoming"));
        assertEquals(ARCHIVED, MainWindow.navigationSelectionFor("list archived"));
        assertEquals(ARCHIVED, MainWindow.navigationSelectionFor("  list   archived  "));
    }

    @Test
    public void navigationSelectionFor_queryCommands_selectsActiveStudents() {
        assertEquals(ACTIVE, MainWindow.navigationSelectionFor("find Alex"));
        assertEquals(ACTIVE, MainWindow.navigationSelectionFor("  find   Alex Bernice "));
        assertEquals(ACTIVE, MainWindow.navigationSelectionFor("filter s/Mathematics"));
        assertEquals(ACTIVE, MainWindow.navigationSelectionFor("filter l/JC1 s/English"));
    }

    @Test
    public void navigationSelectionFor_editAndOtherCommands_returnsExpectedSelection() {
        assertEquals(ACTIVE, MainWindow.navigationSelectionFor("edit 1 p/91234567"));
        assertEquals(UNCHANGED, MainWindow.navigationSelectionFor("schedule 1 at/2099-10-30 16:00"));
        assertEquals(UNCHANGED, MainWindow.navigationSelectionFor("archive 1"));
    }
}
