package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class EducationLevelTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new EducationLevel(null));
    }

    @Test
    public void constructor_invalidEducationLevel_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new EducationLevel(""));
        assertThrows(IllegalArgumentException.class, () -> new EducationLevel("   "));
        assertThrows(IllegalArgumentException.class, () -> new EducationLevel("P0"));
        assertThrows(IllegalArgumentException.class, () -> new EducationLevel("P7"));
        assertThrows(IllegalArgumentException.class, () -> new EducationLevel("S0"));
        assertThrows(IllegalArgumentException.class, () -> new EducationLevel("S6"));
        assertThrows(IllegalArgumentException.class, () -> new EducationLevel("JC0"));
        assertThrows(IllegalArgumentException.class, () -> new EducationLevel("JC3"));
        assertThrows(IllegalArgumentException.class, () -> new EducationLevel(" P6 "));
    }

    @Test
    public void isValidEducationLevel() {
        assertFalse(EducationLevel.isValidEducationLevel("Primary 1"));
        assertFalse(EducationLevel.isValidEducationLevel("J1"));

        assertTrue(EducationLevel.isValidEducationLevel("P1"));
        assertTrue(EducationLevel.isValidEducationLevel("P6"));
        assertTrue(EducationLevel.isValidEducationLevel("S1"));
        assertTrue(EducationLevel.isValidEducationLevel("S5"));
        assertTrue(EducationLevel.isValidEducationLevel("JC1"));
        assertTrue(EducationLevel.isValidEducationLevel("JC2"));
        assertTrue(EducationLevel.isValidEducationLevel("p3"));
        assertTrue(EducationLevel.isValidEducationLevel("jC2"));
    }

    @Test
    public void constructor_lowerCase_normalizesToUpperCase() {
        EducationLevel educationLevel = new EducationLevel("jc1");

        assertEquals("JC1", educationLevel.getValue());
        assertEquals("JC1", educationLevel.toString());
    }

    @Test
    public void equals() {
        EducationLevel educationLevel = new EducationLevel("s3");
        EducationLevel educationLevelCopy = new EducationLevel("S3");

        assertTrue(educationLevel.equals(educationLevel));
        assertTrue(educationLevel.equals(educationLevelCopy));
        assertEquals(educationLevel.hashCode(), educationLevelCopy.hashCode());
        assertFalse(educationLevel.equals(null));
        assertFalse(educationLevel.equals("S3"));
        assertFalse(educationLevel.equals(new EducationLevel("S4")));
    }
}
