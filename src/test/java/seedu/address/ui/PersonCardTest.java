package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;

import seedu.address.MainApp;
public class PersonCardTest {

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
        assertEquals(1, countOccurrences(fxml, "fx:id=\"id\" minWidth=\"26\""));
        assertEquals(1, countOccurrences(fxml, "textOverrun=\"ELLIPSIS\""));
        assertEquals(1, countOccurrences(fxml, "styleClass=\"summary-grid\""));
        assertEquals(3, countOccurrences(fxml, "<ColumnConstraints"));
        assertEquals(3, countOccurrences(fxml, "styleClass=\"summary-label\""));
    }

    @Test
    public void personListCardFxml_wrapsTagsBelowName() throws IOException, URISyntaxException {
        Path fxmlPath = Path.of(MainApp.class.getResource("/view/PersonListCard.fxml").toURI());
        String fxml = Files.readString(fxmlPath);

        int namePosition = fxml.indexOf("fx:id=\"name\"");
        int nameRowEnd = fxml.indexOf("</HBox>");
        int tagsPosition = fxml.indexOf("<FlowPane fx:id=\"tags\" minWidth=\"0\"");
        assertTrue(namePosition >= 0 && namePosition < nameRowEnd);
        assertTrue(tagsPosition > nameRowEnd);
    }

    private static int countOccurrences(String text, String value) {
        return text.split(value, -1).length - 1;
    }
}
