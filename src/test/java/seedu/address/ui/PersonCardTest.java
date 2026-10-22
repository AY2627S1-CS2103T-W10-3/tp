package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;

import seedu.address.MainApp;

public class PersonCardTest {

    @Test
    public void personListCardFxml_containsLearningContextLabels() throws IOException, URISyntaxException {
        Path fxmlPath = Path.of(MainApp.class.getResource("/view/PersonListCard.fxml").toURI());
        String fxml = Files.readString(fxmlPath);

        assertEquals(1, countOccurrences(fxml, "fx:id=\"learningFocus\""));
        assertEquals(1, countOccurrences(fxml, "fx:id=\"nextAction\""));
    }

    private static int countOccurrences(String text, String value) {
        return text.split(value, -1).length - 1;
    }
}
