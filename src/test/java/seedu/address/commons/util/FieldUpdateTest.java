package seedu.address.commons.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;

public class FieldUpdateTest {

    @Test
    public void set_nullValue_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> FieldUpdate.set(null));
    }

    @Test
    public void isEdited() {
        assertFalse(FieldUpdate.unchanged().isEdited());
        assertTrue(FieldUpdate.set("value").isEdited());
        assertTrue(FieldUpdate.clear().isEdited());
    }

    @Test
    public void value() {
        assertEquals(Optional.empty(), FieldUpdate.unchanged().value());
        assertEquals(Optional.of("value"), FieldUpdate.set("value").value());
        assertEquals(Optional.empty(), FieldUpdate.clear().value());
    }

    @Test
    public void applyTo_unchanged_keepsExistingValue() {
        assertEquals(Optional.of("old"), FieldUpdate.<String>unchanged().applyTo(Optional.of("old")));
        assertEquals(Optional.empty(), FieldUpdate.<String>unchanged().applyTo(Optional.empty()));
    }

    @Test
    public void applyTo_set_replacesExistingValue() {
        assertEquals(Optional.of("new"), FieldUpdate.set("new").applyTo(Optional.of("old")));
        assertEquals(Optional.of("new"), FieldUpdate.set("new").applyTo(Optional.empty()));
    }

    @Test
    public void applyTo_clear_removesExistingValue() {
        assertEquals(Optional.empty(), FieldUpdate.<String>clear().applyTo(Optional.of("old")));
        assertEquals(Optional.empty(), FieldUpdate.<String>clear().applyTo(Optional.empty()));
    }

    @Test
    public void applyTo_nullExisting_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> FieldUpdate.set("new").applyTo(null));
    }

    @Test
    public void equals() {
        FieldUpdate<String> set = FieldUpdate.set("value");

        // same values -> returns true
        assertTrue(set.equals(FieldUpdate.set("value")));
        assertTrue(FieldUpdate.unchanged().equals(FieldUpdate.unchanged()));
        assertTrue(FieldUpdate.clear().equals(FieldUpdate.clear()));

        // same object -> returns true
        assertTrue(set.equals(set));

        // null -> returns false
        assertFalse(set.equals(null));

        // different type -> returns false
        assertFalse(set.equals("value"));

        // different set value -> returns false
        assertFalse(set.equals(FieldUpdate.set("other")));

        // different kind -> returns false
        assertFalse(set.equals(FieldUpdate.clear()));
        assertFalse(FieldUpdate.unchanged().equals(FieldUpdate.clear()));
    }

    @Test
    public void hashCode_consistentWithEquals() {
        assertEquals(FieldUpdate.set("value").hashCode(), FieldUpdate.set("value").hashCode());
        assertEquals(FieldUpdate.unchanged().hashCode(), FieldUpdate.unchanged().hashCode());
    }

    @Test
    public void toStringMethod() {
        assertEquals("unchanged", FieldUpdate.unchanged().toString());
        assertEquals("clear", FieldUpdate.clear().toString());
        assertEquals("set(value)", FieldUpdate.set("value").toString());
    }
}
