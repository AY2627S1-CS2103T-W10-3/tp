package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;

import seedu.address.MainApp;
public class PersonDetailsPanelTest {

    @Test
    public void personDetailsPanelFxml_containsAllReadOnlyDetails() throws IOException, URISyntaxException {
        String fxml = readResource("/view/PersonDetailsPanel.fxml");
        String[] fields = {"name", "tags", "phone", "email", "address", "subject", "educationLevel",
            "learningFocus", "nextAction", "lesson", "guardianName", "guardianPhone", "guardianEmail"};

        for (String field : fields) {
            assertTrue(fxml.contains("fx:id=\"" + field + "\""), () -> "Missing details field: " + field);
        }
        assertTrue(fxml.contains("fx:id=\"emptyState\""));
        assertFalse(fxml.contains("alignment=\"TOP_LEFT\""));
    }

    private static String readResource(String resourcePath) throws IOException, URISyntaxException {
        Path path = Path.of(MainApp.class.getResource(resourcePath).toURI());
        return Files.readString(path);
    }
}
