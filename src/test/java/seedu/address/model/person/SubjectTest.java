package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class SubjectTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Subject(null));
    }

    @Test
    public void constructor_invalidSubject_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new Subject(""));
        assertThrows(IllegalArgumentException.class, () -> new Subject("   "));
        assertThrows(IllegalArgumentException.class, () -> new Subject("Mathematics\nH2"));
        assertThrows(IllegalArgumentException.class, () -> new Subject("a".repeat(Subject.MAX_LENGTH + 1)));
    }

    @Test
    public void isValidSubject() {
        assertFalse(Subject.isValidSubject(""));
        assertFalse(Subject.isValidSubject("\t"));
        assertFalse(Subject.isValidSubject("Science\u0000"));
        assertFalse(Subject.isValidSubject("a".repeat(Subject.MAX_LENGTH + 1)));

        assertTrue(Subject.isValidSubject("H2 Mathematics"));
        assertTrue(Subject.isValidSubject("English Language & Literature"));
        assertTrue(Subject.isValidSubject("Chinese (Higher)"));
        assertTrue(Subject.isValidSubject("A-Math / E-Math"));
        assertTrue(Subject.isValidSubject("a".repeat(Subject.MAX_LENGTH)));
    }

    @Test
    public void getValueAndToString_validSubject_returnsStoredValue() {
        Subject subject = new Subject("H2 Mathematics");

        assertEquals("H2 Mathematics", subject.getValue());
        assertEquals("H2 Mathematics", subject.toString());
    }

    @Test
    public void equals() {
        Subject subject = new Subject("H2 Mathematics");
        Subject subjectCopy = new Subject("H2 Mathematics");

        assertTrue(subject.equals(subject));
        assertTrue(subject.equals(subjectCopy));
        assertEquals(subject.hashCode(), subjectCopy.hashCode());
        assertFalse(subject.equals(null));
        assertFalse(subject.equals("H2 Mathematics"));
        assertFalse(subject.equals(new Subject("H2 Physics")));
    }
}
