package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class NameTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Name(null));
    }

    @Test
    public void constructor_invalidName_throwsIllegalArgumentException() {
        String invalidName = "";
        assertThrows(IllegalArgumentException.class, () -> new Name(invalidName));
    }

    @Test
    public void isValidName() {
        // null name
        assertThrows(NullPointerException.class, () -> Name.isValidName(null));

        // invalid name
        assertFalse(Name.isValidName("")); // empty string
        assertFalse(Name.isValidName(" ")); // spaces only
        assertFalse(Name.isValidName("^")); // only non-alphanumeric characters
        assertFalse(Name.isValidName("peter*")); // contains non-alphanumeric characters
        assertFalse(Name.isValidName("-Jean")); // starts with a hyphen
        assertFalse(Name.isValidName("'Liam")); // starts with an apostrophe
        assertFalse(Name.isValidName("/o Kumar")); // starts with a slash
        assertFalse(Name.isValidName("Tan, Ah Kow")); // contains a comma

        // valid name
        assertTrue(Name.isValidName("peter jack")); // alphabets only
        assertTrue(Name.isValidName("12345")); // numbers only
        assertTrue(Name.isValidName("peter the 2nd")); // alphanumeric characters
        assertTrue(Name.isValidName("Capital Tan")); // with capital letters
        assertTrue(Name.isValidName("David Roger Jackson Ray Jr 2nd")); // long names
        assertTrue(Name.isValidName("Jean-Luc Tan")); // hyphen
        assertTrue(Name.isValidName("Liam O'Brien")); // apostrophe
        assertTrue(Name.isValidName("Liam O’Brien")); // typographic apostrophe
        assertTrue(Name.isValidName("José Tan")); // accented letter
        assertTrue(Name.isValidName("José Tan")); // letter followed by a combining accent
        assertTrue(Name.isValidName("Raj s/o Kumar")); // son of
        assertTrue(Name.isValidName("Priya d/o Raju")); // daughter of
        assertTrue(Name.isValidName("Martin Luther King Jr.")); // full stop
        assertTrue(Name.isValidName("陈小明")); // non-Latin letters
    }

    @Test
    public void equals() {
        Name name = new Name("Valid Name");

        // same values -> returns true
        assertTrue(name.equals(new Name("Valid Name")));

        // same object -> returns true
        assertTrue(name.equals(name));

        // null -> returns false
        assertFalse(name.equals(null));

        // different types -> returns false
        assertFalse(name.equals(5.0f));

        // different values -> returns false
        assertFalse(name.equals(new Name("Other Valid Name")));
    }
}
