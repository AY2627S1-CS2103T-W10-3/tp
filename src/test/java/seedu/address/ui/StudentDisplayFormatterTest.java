package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import seedu.address.model.person.Lesson;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;
import seedu.address.ui.StudentDisplayFormatter.LessonDisplay;
import seedu.address.ui.StudentDisplayFormatter.StudentDetails;

public class StudentDisplayFormatterTest {

    private static final LocalDateTime NOW = LocalDateTime.of(2026, 10, 22, 16, 0, 30);

    @Test
    public void formatDetails_completeStudent_returnsEveryDisplayValue() {
        Person person = new PersonBuilder().withName("Alex Tan").withPhone("81234567")
                .withEmail("alex@example.com").withAddress("21 Dover Road")
                .withSubject("H2 Mathematics").withEducationLevel("JC2")
                .withLearningFocus("Integration").withNextAction("Complete worksheet 4")
                .withLesson("2026-10-22 16:01")
                .withGuardian("Mary Tan", "92345678", "mary@example.com")
                .withTags("online", "examPrep").build();

        StudentDetails details = StudentDisplayFormatter.formatDetails(person, NOW);

        assertEquals("Alex Tan", details.name());
        assertEquals(List.of("examPrep", "online"), details.tags());
        assertEquals("81234567", details.phone());
        assertEquals("alex@example.com", details.email());
        assertEquals("21 Dover Road", details.address());
        assertEquals("H2 Mathematics", details.subject());
        assertEquals("JC2", details.educationLevel());
        assertEquals("Integration", details.learningFocus());
        assertEquals("Complete worksheet 4", details.nextAction());
        assertEquals(new LessonDisplay("2026-10-22 16:01", false), details.lesson());
        assertEquals("Mary Tan", details.guardianName());
        assertEquals("92345678", details.guardianPhone());
        assertEquals("mary@example.com", details.guardianEmail());
    }

    @Test
    public void formatDetails_missingOptionalValues_returnsPlaceholders() {
        Person person = new PersonBuilder().withName("Name Only").withoutPhone().withoutEmail()
                .withoutAddress().withoutSubject().withoutEducationLevel().withoutLearningFocus()
                .withoutNextAction().withoutLesson().withoutGuardian().withTags().build();

        StudentDetails details = StudentDisplayFormatter.formatDetails(person, NOW);

        assertEquals(List.of(), details.tags());
        assertEquals("Not provided", details.phone());
        assertEquals("Not provided", details.email());
        assertEquals("Not provided", details.address());
        assertEquals("Not provided", details.subject());
        assertEquals("Not provided", details.educationLevel());
        assertEquals("Not provided", details.learningFocus());
        assertEquals("Not provided", details.nextAction());
        assertEquals(new LessonDisplay("Not scheduled", false), details.lesson());
        assertEquals("Not provided", details.guardianName());
        assertEquals("Not provided", details.guardianPhone());
        assertEquals("Not provided", details.guardianEmail());
    }

    @Test
    public void formatDetails_guardianWithoutContacts_returnsContactPlaceholders() {
        Person person = new PersonBuilder().withGuardianName("Mary Tan").build();

        StudentDetails details = StudentDisplayFormatter.formatDetails(person, NOW);

        assertEquals("Mary Tan", details.guardianName());
        assertEquals("Not provided", details.guardianPhone());
        assertEquals("Not provided", details.guardianEmail());
    }

    @Test
    public void formatLesson_futureLesson_returnsTimeWithoutOverdueState() {
        LessonDisplay lesson = StudentDisplayFormatter.formatLesson(
                Optional.of(new Lesson("2026-10-22 16:01")), NOW);

        assertEquals("2026-10-22 16:01", lesson.text());
        assertFalse(lesson.isOverdue());
    }

    @Test
    public void formatLesson_startedLesson_returnsOverdueState() {
        LessonDisplay lesson = StudentDisplayFormatter.formatLesson(
                Optional.of(new Lesson("2026-10-22 16:00")), NOW);

        assertEquals("2026-10-22 16:00 (overdue)", lesson.text());
        assertTrue(lesson.isOverdue());
    }

    @Test
    public void formatLesson_nullArguments_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> StudentDisplayFormatter.formatLesson(null, NOW));
        assertThrows(NullPointerException.class, () ->
                StudentDisplayFormatter.formatLesson(Optional.empty(), null));
    }
}
