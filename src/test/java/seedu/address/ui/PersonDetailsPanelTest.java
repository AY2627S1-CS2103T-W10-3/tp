package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import seedu.address.MainApp;
import seedu.address.model.person.Lesson;

public class PersonDetailsPanelTest {

    private static final LocalDateTime NOW = LocalDateTime.of(2026, 10, 22, 16, 0, 30);

    @Test
    public void personDetailsPanelFxml_containsAllReadOnlyDetails() throws IOException, URISyntaxException {
        String fxml = readResource("/view/PersonDetailsPanel.fxml");
        String[] fields = {"name", "tags", "phone", "email", "address", "subject", "educationLevel",
            "learningFocus", "nextAction", "lesson", "guardianName", "guardianPhone", "guardianEmail"};

        for (String field : fields) {
            assertTrue(fxml.contains("fx:id=\"" + field + "\""), () -> "Missing details field: " + field);
        }
        assertTrue(fxml.contains("fx:id=\"emptyState\""));
    }

    @Test
    public void formatLesson_missingLesson_showsNotScheduled() {
        assertEquals("Not scheduled", PersonDetailsPanel.formatLesson(Optional.empty(), NOW));
    }

    @Test
    public void formatLesson_overdueLesson_marksStatusInText() {
        Lesson lesson = new Lesson("2026-10-22 16:00");

        assertEquals("2026-10-22 16:00 (overdue)",
                PersonDetailsPanel.formatLesson(Optional.of(lesson), NOW));
    }

    private static String readResource(String resourcePath) throws IOException, URISyntaxException {
        Path path = Path.of(MainApp.class.getResource(resourcePath).toURI());
        return Files.readString(path);
    }
}
