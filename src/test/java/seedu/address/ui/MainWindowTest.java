package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.ui.MainWindow.NavigationSelection.ACTIVE;
import static seedu.address.ui.MainWindow.NavigationSelection.ARCHIVED;
import static seedu.address.ui.MainWindow.NavigationSelection.UNCHANGED;
import static seedu.address.ui.MainWindow.NavigationSelection.UPCOMING;

import org.junit.jupiter.api.Test;

public class MainWindowTest {

    @Test
    public void fitWithin_positionInsideBounds_keepsPosition() {
        assertEquals(100, MainWindow.fitWithin(100, 800, 0, 1920));
        assertEquals(1120, MainWindow.fitWithin(1120, 800, 0, 1920));
    }

    @Test
    public void fitWithin_positionOutsideBounds_movesInsideBounds() {
        assertEquals(1120, MainWindow.fitWithin(3000, 800, 0, 1920));
        assertEquals(0, MainWindow.fitWithin(-500, 800, 0, 1920));
        assertEquals(3040, MainWindow.fitWithin(5000, 800, 1920, 3840));
    }

    @Test
    public void fitWithin_sizeLargerThanBounds_startsAtBoundsStart() {
        assertEquals(0, MainWindow.fitWithin(50, 2000, 0, 1920));
    }

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
    public void navigationSelectionFor_addCommand_selectsActiveStudents() {
        assertEquals(ACTIVE, MainWindow.navigationSelectionFor("add n/Sam Lee"));
        assertEquals(ACTIVE, MainWindow.navigationSelectionFor("  add   n/Sam Lee "));
        assertEquals(UNCHANGED, MainWindow.navigationSelectionFor("additional n/Sam Lee"));
    }

    @Test
    public void navigationSelectionFor_editAndOtherCommands_returnsExpectedSelection() {
        // edit keeps the current result view, including the upcoming view
        assertEquals(UNCHANGED, MainWindow.navigationSelectionFor("edit 1 p/91234567"));
        assertEquals(UNCHANGED, MainWindow.navigationSelectionFor("schedule 1 at/2099-10-30 16:00"));
        assertEquals(UNCHANGED, MainWindow.navigationSelectionFor("archive 1"));
    }
}
