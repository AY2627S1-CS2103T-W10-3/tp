package seedu.address.logic;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

/** Tests how person details appear in command feedback. */
public class MessagesTest {

    @Test
    public void format_nameOnly_showsMissingDetailsForDisplay() {
        Person person = new PersonBuilder().withName("Only Name").withoutPhone().withoutEmail()
                .withoutAddress().withTags().build();

        assertEquals("Only Name; Phone: Not provided; Email: Not provided; Address: Not provided; Tags: ",
                Messages.format(person));
    }
}
