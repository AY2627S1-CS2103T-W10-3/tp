package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;

import seedu.address.MainApp;

public class MainWindowFxmlTest {

    @Test
    public void mainWindowFxml_usesTutorContactsShell() throws IOException, URISyntaxException {
        String fxml = readResource("/view/MainWindow.fxml");

        assertTrue(fxml.contains("@TutorContactsTheme.css"));
        assertTrue(fxml.contains("text=\"TutorContacts\""));
        assertTrue(fxml.contains("styleClass=\"sidebar\""));
        assertTrue(fxml.contains("text=\"All Students\""));
        assertTrue(fxml.contains("text=\"Upcoming Lessons\""));
        assertTrue(fxml.contains("text=\"Archived Students\""));
        assertTrue(countOccurrences(fxml, "styleClass=\"view-icon\"") == 3);
        assertFalse(fxml.contains("text=\"VIEWS\""));
        assertFalse(fxml.contains("DarkTheme.css"));
    }

    @Test
    public void mainWindowFxml_commandBoxFollowsStudentContent() throws IOException, URISyntaxException {
        String fxml = readResource("/view/MainWindow.fxml");

        int studentListPosition = fxml.indexOf("fx:id=\"personListPanelPlaceholder\"");
        int commandBoxPosition = fxml.indexOf("fx:id=\"commandBoxPlaceholder\"");
        assertTrue(studentListPosition >= 0);
        assertTrue(commandBoxPosition > studentListPosition);
    }

    @Test
    public void tutorContactsTheme_usesNativeSystemFont() throws IOException, URISyntaxException {
        String stylesheet = readResource("/view/TutorContactsTheme.css");

        assertTrue(stylesheet.contains("-fx-font-family: \"System\";"));
        assertFalse(stylesheet.contains("Segoe UI"));
    }

    private static String readResource(String resourcePath) throws IOException, URISyntaxException {
        Path path = Path.of(MainApp.class.getResource(resourcePath).toURI());
        return Files.readString(path);
    }

    private static int countOccurrences(String text, String value) {
        return text.split(value, -1).length - 1;
    }
}
