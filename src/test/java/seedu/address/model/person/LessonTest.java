package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

public class LessonTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Lesson(null));
    }

    @Test
    public void constructor_invalidLesson_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new Lesson(""));
        assertThrows(IllegalArgumentException.class, () -> new Lesson("2026-02-30 10:00"));
    }

    @Test
    public void isValidLesson() {
        // null lesson
        assertThrows(NullPointerException.class, () -> Lesson.isValidLesson(null));

        // invalid format
        assertFalse(Lesson.isValidLesson(""));
        assertFalse(Lesson.isValidLesson("2026-10-30"));
        assertFalse(Lesson.isValidLesson("16:00"));
        assertFalse(Lesson.isValidLesson("30-10-2026 16:00"));
        assertFalse(Lesson.isValidLesson("2026/10/30 16:00"));
        assertFalse(Lesson.isValidLesson("2026-10-30T16:00"));
        assertFalse(Lesson.isValidLesson("2026-10-30 4pm"));
        assertFalse(Lesson.isValidLesson("2026-1-30 16:00"));
        assertFalse(Lesson.isValidLesson("2026-10-30 6:00"));
        assertFalse(Lesson.isValidLesson("2026-10-30 16:00:00"));
        assertFalse(Lesson.isValidLesson("+12345-01-01 10:00")); // signed five-digit year
        assertFalse(Lesson.isValidLesson("12345-01-01 10:00")); // five-digit year
        assertFalse(Lesson.isValidLesson("+2099-01-01 10:00")); // signed year
        assertFalse(Lesson.isValidLesson("-2099-01-01 10:00")); // negative year

        // non-existent dates and times
        assertFalse(Lesson.isValidLesson("2026-02-29 10:00"));
        assertFalse(Lesson.isValidLesson("2026-13-01 10:00"));
        assertFalse(Lesson.isValidLesson("2026-04-31 10:00"));
        assertFalse(Lesson.isValidLesson("2026-10-30 24:00"));
        assertFalse(Lesson.isValidLesson("2026-10-30 16:60"));

        // boundary values
        assertTrue(Lesson.isValidLesson("2028-02-29 10:00"));
        assertTrue(Lesson.isValidLesson("2026-10-30 00:00"));
        assertTrue(Lesson.isValidLesson("2026-10-30 23:59"));
        assertTrue(Lesson.isValidLesson("2026-12-31 16:00"));
    }

    @Test
    public void isAfter() {
        Lesson lesson = new Lesson("2026-10-30 16:00");

        assertTrue(lesson.isAfter(LocalDateTime.of(2026, 10, 30, 15, 59)));
        assertFalse(lesson.isAfter(LocalDateTime.of(2026, 10, 30, 16, 0)));
        assertFalse(lesson.isAfter(LocalDateTime.of(2026, 10, 30, 16, 0, 1)));
        assertFalse(lesson.isAfter(LocalDateTime.of(2026, 10, 30, 16, 1)));
    }

    @Test
    public void isOverdue() {
        Lesson lesson = new Lesson("2026-10-30 16:00");

        assertFalse(lesson.isOverdue(LocalDateTime.of(2026, 10, 30, 15, 59)));
        assertTrue(lesson.isOverdue(LocalDateTime.of(2026, 10, 30, 16, 0)));
        assertTrue(lesson.isOverdue(LocalDateTime.of(2026, 10, 31, 9, 0)));
    }

    @Test
    public void getValueAndToString_validLesson_returnsInputFormat() {
        Lesson lesson = new Lesson("2026-10-30 09:05");

        assertEquals("2026-10-30 09:05", lesson.getValue());
        assertEquals("2026-10-30 09:05", lesson.toString());
        assertEquals(LocalDateTime.of(2026, 10, 30, 9, 5), lesson.getDateTime());
    }

    @Test
    public void equals() {
        Lesson lesson = new Lesson("2026-10-30 16:00");
        Lesson lessonCopy = new Lesson("2026-10-30 16:00");

        assertTrue(lesson.equals(lesson));
        assertTrue(lesson.equals(lessonCopy));
        assertEquals(lesson.hashCode(), lessonCopy.hashCode());
        assertFalse(lesson.equals(null));
        assertFalse(lesson.equals("2026-10-30 16:00"));
        assertFalse(lesson.equals(new Lesson("2026-10-30 16:01")));
    }
}
