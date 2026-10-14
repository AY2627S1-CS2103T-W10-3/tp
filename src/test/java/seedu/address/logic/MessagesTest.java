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

        assertEquals("Only Name; Phone: Not provided; Email: Not provided; Address: Not provided; "
                        + "Guardian: Not provided; Guardian phone: Not provided; Guardian email: Not provided; Tags: ",
                Messages.format(person));
    }

    @Test
    public void format_guardianDetails_showsAllGuardianFields() {
        Person person = new PersonBuilder().withName("Student Name").withoutPhone().withoutEmail()
                .withoutAddress().withTags()
                .withGuardian("Guardian Name", "81234567", "guardian@example.com").build();

        assertEquals("Student Name; Phone: Not provided; Email: Not provided; Address: Not provided; "
                        + "Guardian: Guardian Name; Guardian phone: 81234567; "
                        + "Guardian email: guardian@example.com; Tags: ", Messages.format(person));
    }
}
