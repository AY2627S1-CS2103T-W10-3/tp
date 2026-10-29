package seedu.address.logic;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/** Tests user-facing message constants. */
public class MessagesTest {

    @Test
    public void userFacingMessages_useStudentTerminology() {
        assertEquals("The student index provided is invalid.", Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        assertEquals("%1$d student(s) listed!", Messages.MESSAGE_PERSONS_LISTED_OVERVIEW);
    }
}
