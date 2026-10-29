package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import seedu.address.MainApp;
import seedu.address.model.person.Lesson;

public class PersonCardTest {

    private static final LocalDateTime NOW = LocalDateTime.of(2026, 10, 22, 16, 0, 30);

    @Test
    public void personListCardFxml_keepsDetailsInSeparatePanel() throws IOException, URISyntaxException {
        Path fxmlPath = Path.of(MainApp.class.getResource("/view/PersonListCard.fxml").toURI());
        String fxml = Files.readString(fxmlPath);

        assertEquals(0, countOccurrences(fxml, "fx:id=\"learningFocus\""));
        assertEquals(0, countOccurrences(fxml, "fx:id=\"nextAction\""));
        assertEquals(0, countOccurrences(fxml, "fx:id=\"phone\""));
        assertEquals(0, countOccurrences(fxml, "fx:id=\"guardianName\""));
    }

    @Test
    public void personListCardFxml_containsLessonLabel() throws IOException, URISyntaxException {
        Path fxmlPath = Path.of(MainApp.class.getResource("/view/PersonListCard.fxml").toURI());
        String fxml = Files.readString(fxmlPath);

        assertEquals(1, countOccurrences(fxml, "fx:id=\"lesson\""));
    }

    @Test
    public void personListCardFxml_containsCompactSummary()
            throws IOException, URISyntaxException {
        Path fxmlPath = Path.of(MainApp.class.getResource("/view/PersonListCard.fxml").toURI());
        String fxml = Files.readString(fxmlPath);

        assertEquals(1, countOccurrences(fxml, "styleClass=\"card-name\""));
        assertEquals(1, countOccurrences(fxml, "fx:id=\"id\" minWidth=\"38\""));
        assertEquals(1, countOccurrences(fxml, "textOverrun=\"ELLIPSIS\""));
        assertEquals(1, countOccurrences(fxml, "styleClass=\"summary-grid\""));
        assertEquals(3, countOccurrences(fxml, "<ColumnConstraints"));
        assertEquals(3, countOccurrences(fxml, "styleClass=\"summary-label\""));
    }

    @Test
    public void formatLesson_noLesson_showsNotScheduled() {
        assertEquals("Not scheduled", PersonCard.formatLesson(Optional.empty(), NOW));
    }

    @Test
    public void formatLesson_futureLesson_showsTimeOnly() {
        assertEquals("2026-10-22 16:01",
                PersonCard.formatLesson(Optional.of(new Lesson("2026-10-22 16:01")), NOW));
    }

    @Test
    public void formatLesson_startedLesson_marksOverdue() {
        assertEquals("2026-10-22 16:00 (overdue)",
                PersonCard.formatLesson(Optional.of(new Lesson("2026-10-22 16:00")), NOW));
        assertEquals("2026-10-01 09:00 (overdue)",
                PersonCard.formatLesson(Optional.of(new Lesson("2026-10-01 09:00")), NOW));
    }

    private static int countOccurrences(String text, String value) {
        return text.split(value, -1).length - 1;
    }
}
