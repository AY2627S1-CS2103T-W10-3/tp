package seedu.address.logic;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

/** Tests how person details appear in command feedback. */
public class MessagesTest {

    @Test
    public void userFacingMessages_useStudentTerminology() {
        assertEquals("The student index provided is invalid.", Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        assertEquals("%1$d student(s) listed!", Messages.MESSAGE_PERSONS_LISTED_OVERVIEW);
    }

    @Test
    public void format_nameOnly_showsMissingDetailsForDisplay() {
        Person person = new PersonBuilder().withName("Only Name").withoutPhone().withoutEmail()
                .withoutAddress().withTags().build();

        assertEquals("Only Name; Phone: Not provided; Email: Not provided; Address: Not provided; "
                        + "Subject: Not provided; Education level: Not provided; "
                        + "Learning focus: Not provided; Next action: Not provided; Lesson: Not scheduled; "
                        + "Guardian: Not provided; Guardian phone: Not provided; Guardian email: Not provided; Tags: ",
                Messages.format(person));
    }

    @Test
    public void format_guardianDetails_showsAllGuardianFields() {
        Person person = new PersonBuilder().withName("Student Name").withoutPhone().withoutEmail()
                .withoutAddress().withTags()
                .withSubject("H2 Mathematics").withEducationLevel("jc1")
                .withGuardian("Guardian Name", "81234567", "guardian@example.com").build();

        assertEquals("Student Name; Phone: Not provided; Email: Not provided; Address: Not provided; "
                        + "Subject: H2 Mathematics; Education level: JC1; "
                        + "Learning focus: Not provided; Next action: Not provided; Lesson: Not scheduled; "
                        + "Guardian: Guardian Name; Guardian phone: 81234567; "
                        + "Guardian email: guardian@example.com; Tags: ", Messages.format(person));
    }

    @Test
    public void format_learningContext_showsBothValues() {
        Person person = new PersonBuilder().withName("Student Name").withoutPhone().withoutEmail()
                .withoutAddress().withTags().withLearningFocus("Fractions and ratios")
                .withNextAction("Complete worksheet 2").build();

        assertEquals("Student Name; Phone: Not provided; Email: Not provided; Address: Not provided; "
                        + "Subject: Not provided; Education level: Not provided; "
                        + "Learning focus: Fractions and ratios; Next action: Complete worksheet 2; "
                        + "Lesson: Not scheduled; "
                        + "Guardian: Not provided; Guardian phone: Not provided; Guardian email: Not provided; Tags: ",
                Messages.format(person));
    }

    @Test
    public void format_scheduledLesson_showsLessonTime() {
        Person person = new PersonBuilder().withName("Student Name").withoutPhone().withoutEmail()
                .withoutAddress().withTags().withLesson("2026-10-30 16:00").build();

        assertEquals("Student Name; Phone: Not provided; Email: Not provided; Address: Not provided; "
                        + "Subject: Not provided; Education level: Not provided; "
                        + "Learning focus: Not provided; Next action: Not provided; Lesson: 2026-10-30 16:00; "
                        + "Guardian: Not provided; Guardian phone: Not provided; Guardian email: Not provided; Tags: ",
                Messages.format(person));
    }
}
