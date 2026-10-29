package seedu.address.logic;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;

/** Tests user-facing message constants. */
public class MessagesTest {

    @Test
    public void userFacingMessages_useStudentTerminology() {
        assertEquals("The student index provided is invalid.", Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        assertEquals("%1$d student(s) listed!", Messages.MESSAGE_PERSONS_LISTED_OVERVIEW);
    }

    @Test
    public void dataLoadFailedMessage_namesFileAndAdvisesBackup() {
        String message = String.format(Messages.MESSAGE_DATA_LOAD_FAILED, Path.of("data", "addressbook.json"));

        assertEquals("Warning: The data file " + Path.of("data", "addressbook.json") + " could not be loaded, so "
                + "TutorContacts started with no students. Back up or repair that file before adding or changing "
                + "students, because the first change replaces it.", message);
    }
}
