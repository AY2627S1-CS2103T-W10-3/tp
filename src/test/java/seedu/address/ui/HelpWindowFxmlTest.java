package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;

import seedu.address.MainApp;

public class HelpWindowFxmlTest {

    @Test
    public void helpWindowFxml_containsEverySupportedCommand() throws IOException, URISyntaxException {
        String fxml = readResource("/view/HelpWindow.fxml");
        String[] commands = {"add n/NAME", "edit INDEX", "list", "find KEYWORD", "filter [s/SUBJECT]",
            "archive INDEX", "list archived", "unarchive INDEX", "delete INDEX", "schedule INDEX",
            "unschedule INDEX", "upcoming", "clear", "help", "exit"};

        for (String command : commands) {
            assertTrue(fxml.contains(command), () -> "Missing Help command: " + command);
        }
    }

    @Test
    public void helpWindowFxml_usesStructuredOfflineLayout() throws IOException, URISyntaxException {
        String fxml = readResource("/view/HelpWindow.fxml");

        assertTrue(fxml.contains("TutorContacts Help"));
        assertTrue(fxml.contains("Offline command reference"));
        assertTrue(fxml.contains("fx:id=\"overviewPage\""));
        assertTrue(fxml.contains("fx:id=\"studentsPage\""));
        assertTrue(fxml.contains("fx:id=\"searchPage\""));
        assertTrue(fxml.contains("fx:id=\"schedulingPage\""));
        assertTrue(fxml.contains("fx:id=\"archivingPage\""));
        assertTrue(fxml.contains("Copy User Guide URL"));
        assertFalse(fxml.contains("TextArea"));
        assertFalse(fxml.contains("WebView"));
        assertFalse(fxml.contains("autocomplete"));
        assertFalse(fxml.contains("Statistics"));
    }

    private static String readResource(String resourcePath) throws IOException, URISyntaxException {
        Path path = Path.of(MainApp.class.getResource(resourcePath).toURI());
        return Files.readString(path);
    }
}
