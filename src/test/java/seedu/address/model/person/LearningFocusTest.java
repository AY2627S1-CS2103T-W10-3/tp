package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class LearningFocusTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new LearningFocus(null));
    }

    @Test
    public void constructor_invalidLearningFocus_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new LearningFocus(""));
        assertThrows(IllegalArgumentException.class, () -> new LearningFocus("   "));
        assertThrows(IllegalArgumentException.class, () -> new LearningFocus("review\nfractions"));
        assertThrows(IllegalArgumentException.class, () ->
                new LearningFocus("a".repeat(LearningFocus.MAX_LENGTH + 1)));
    }

    @Test
    public void isValidLearningFocus() {
        assertFalse(LearningFocus.isValidLearningFocus(""));
        assertFalse(LearningFocus.isValidLearningFocus("\t"));
        assertFalse(LearningFocus.isValidLearningFocus("Fractions\u0000"));
        assertFalse(LearningFocus.isValidLearningFocus("a".repeat(LearningFocus.MAX_LENGTH + 1)));

        assertTrue(LearningFocus.isValidLearningFocus("a"));
        assertTrue(LearningFocus.isValidLearningFocus("Fractions: 1/2 + 1/3?"));
        assertTrue(LearningFocus.isValidLearningFocus("Review (Ch. 4) — graphs & equations"));
        assertTrue(LearningFocus.isValidLearningFocus("a".repeat(LearningFocus.MAX_LENGTH)));
    }

    @Test
    public void getValueAndToString_validLearningFocus_returnsStoredValue() {
        LearningFocus learningFocus = new LearningFocus("Fractions: 1/2 + 1/3?");

        assertEquals("Fractions: 1/2 + 1/3?", learningFocus.getValue());
        assertEquals("Fractions: 1/2 + 1/3?", learningFocus.toString());
    }

    @Test
    public void equals() {
        LearningFocus learningFocus = new LearningFocus("Fractions");
        LearningFocus learningFocusCopy = new LearningFocus("Fractions");

        assertTrue(learningFocus.equals(learningFocus));
        assertTrue(learningFocus.equals(learningFocusCopy));
        assertEquals(learningFocus.hashCode(), learningFocusCopy.hashCode());
        assertFalse(learningFocus.equals(null));
        assertFalse(learningFocus.equals("Fractions"));
        assertFalse(learningFocus.equals(new LearningFocus("Algebra")));
    }
}
