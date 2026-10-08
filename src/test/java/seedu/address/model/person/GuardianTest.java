package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import java.util.Optional;

import org.junit.jupiter.api.Test;

public class GuardianTest {

    private static final Name VALID_NAME = new Name("Jane Doe");
    private static final Name OTHER_NAME = new Name("John Doe");
    private static final Phone VALID_PHONE = new Phone("91234567");
    private static final Phone OTHER_PHONE = new Phone("98765432");
    private static final Email VALID_EMAIL = new Email("jane@example.com");
    private static final Email OTHER_EMAIL = new Email("john@example.com");

    @Test
    public void constructor_nullName_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () ->
                new Guardian(null, Optional.of(VALID_PHONE), Optional.of(VALID_EMAIL)));
    }

    @Test
    public void constructor_nullPhoneOptional_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () ->
                new Guardian(VALID_NAME, null, Optional.of(VALID_EMAIL)));
    }

    @Test
    public void constructor_nullEmailOptional_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () ->
                new Guardian(VALID_NAME, Optional.of(VALID_PHONE), null));
    }

    @Test
    public void constructor_validContactCombinations_success() {
        Guardian guardianWithoutContacts = new Guardian(VALID_NAME, Optional.empty(), Optional.empty());
        Guardian guardianWithPhone = new Guardian(VALID_NAME, Optional.of(VALID_PHONE), Optional.empty());
        Guardian guardianWithEmail = new Guardian(VALID_NAME, Optional.empty(), Optional.of(VALID_EMAIL));
        Guardian guardianWithAllContacts = new Guardian(
                VALID_NAME, Optional.of(VALID_PHONE), Optional.of(VALID_EMAIL));

        assertEquals(Optional.empty(), guardianWithoutContacts.getPhone());
        assertEquals(Optional.empty(), guardianWithoutContacts.getEmail());
        assertEquals(Optional.of(VALID_PHONE), guardianWithPhone.getPhone());
        assertEquals(Optional.of(VALID_EMAIL), guardianWithEmail.getEmail());
        assertEquals(VALID_NAME, guardianWithAllContacts.getName());
        assertEquals(Optional.of(VALID_PHONE), guardianWithAllContacts.getPhone());
        assertEquals(Optional.of(VALID_EMAIL), guardianWithAllContacts.getEmail());
    }

    @Test
    public void constructor_sharedContactValues_success() {
        Guardian firstGuardian = new Guardian(VALID_NAME, Optional.of(VALID_PHONE), Optional.of(VALID_EMAIL));
        Guardian secondGuardian = new Guardian(OTHER_NAME, Optional.of(VALID_PHONE), Optional.of(VALID_EMAIL));

        assertEquals(firstGuardian.getPhone(), secondGuardian.getPhone());
        assertEquals(firstGuardian.getEmail(), secondGuardian.getEmail());
        assertFalse(firstGuardian.equals(secondGuardian));
    }

    @Test
    public void equals() {
        Guardian guardian = new Guardian(VALID_NAME, Optional.of(VALID_PHONE), Optional.of(VALID_EMAIL));

        // same values -> returns true
        Guardian guardianCopy = new Guardian(VALID_NAME, Optional.of(VALID_PHONE), Optional.of(VALID_EMAIL));
        assertTrue(guardian.equals(guardianCopy));
        assertEquals(guardian.hashCode(), guardianCopy.hashCode());

        // same object -> returns true
        assertTrue(guardian.equals(guardian));

        // null -> returns false
        assertFalse(guardian.equals(null));

        // different type -> returns false
        assertFalse(guardian.equals(5));

        // different name -> returns false
        Guardian editedGuardian = new Guardian(OTHER_NAME, Optional.of(VALID_PHONE), Optional.of(VALID_EMAIL));
        assertFalse(guardian.equals(editedGuardian));

        // different phone -> returns false
        editedGuardian = new Guardian(VALID_NAME, Optional.of(OTHER_PHONE), Optional.of(VALID_EMAIL));
        assertFalse(guardian.equals(editedGuardian));

        // different email -> returns false
        editedGuardian = new Guardian(VALID_NAME, Optional.of(VALID_PHONE), Optional.of(OTHER_EMAIL));
        assertFalse(guardian.equals(editedGuardian));
    }

    @Test
    public void toStringMethod() {
        Guardian guardian = new Guardian(VALID_NAME, Optional.of(VALID_PHONE), Optional.empty());
        String expected = Guardian.class.getCanonicalName() + "{name=" + VALID_NAME
                + ", phone=Optional[" + VALID_PHONE + "], email=Optional.empty}";

        assertEquals(expected, guardian.toString());
    }
}
