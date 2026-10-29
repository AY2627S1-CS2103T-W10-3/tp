package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EDUCATION_LEVEL_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_GUARDIAN_EMAIL_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_GUARDIAN_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_GUARDIAN_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_SUBJECT_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.model.tag.Tag;
import seedu.address.testutil.EditPersonDescriptorBuilder;

public class EditPersonDescriptorTest {

    @Test
    public void isAnyFieldEdited() {
        // no field edited -> returns false
        assertFalse(new EditPersonDescriptor().isAnyFieldEdited());

        // a field set -> returns true
        assertTrue(new EditPersonDescriptorBuilder().withPhone(VALID_PHONE_BOB).build().isAnyFieldEdited());

        // a field cleared -> returns true
        assertTrue(new EditPersonDescriptorBuilder().withoutPhone().build().isAnyFieldEdited());
    }

    @Test
    public void getPhone_unchanged_isNotEditedAndHasNoValue() {
        EditPersonDescriptor descriptor = new EditPersonDescriptor();
        assertFalse(descriptor.getPhone().isEdited());
        assertTrue(descriptor.getPhone().value().isEmpty());
    }

    @Test
    public void getPhone_set_isEditedWithValue() {
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withPhone(VALID_PHONE_BOB).build();
        assertTrue(descriptor.getPhone().isEdited());
        assertEquals(VALID_PHONE_BOB, descriptor.getPhone().value().orElseThrow().value);
    }

    @Test
    public void getPhone_cleared_isEditedWithoutValue() {
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withoutPhone().build();
        assertTrue(descriptor.getPhone().isEdited());
        assertTrue(descriptor.getPhone().value().isEmpty());
    }

    @Test
    public void setTags_modifyingSourceSet_doesNotAffectDescriptor() {
        Set<Tag> tags = new HashSet<>();
        tags.add(new Tag("friend"));
        EditPersonDescriptor descriptor = new EditPersonDescriptor();
        descriptor.setTags(tags);

        tags.add(new Tag("colleague"));

        assertEquals(Set.of(new Tag("friend")), descriptor.getTags().orElseThrow());
    }

    @Test
    public void getTags_modifyingReturnedSet_throwsUnsupportedOperationException() {
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withTags("friend").build();
        Set<Tag> tags = descriptor.getTags().orElseThrow();
        assertThrows(UnsupportedOperationException.class, () -> tags.add(new Tag("colleague")));
    }

    @Test
    public void copyConstructor_tagsAreDefensivelyCopied() {
        EditPersonDescriptor original = new EditPersonDescriptorBuilder().withTags("friend").build();
        EditPersonDescriptor copy = new EditPersonDescriptor(original);

        original.setTags(Set.of(new Tag("friend"), new Tag("colleague")));

        assertEquals(Set.of(new Tag("friend")), copy.getTags().orElseThrow());
    }

    @Test
    public void equals() {
        // same values -> returns true
        EditPersonDescriptor descriptorWithSameValues = new EditPersonDescriptor(DESC_AMY);
        assertTrue(DESC_AMY.equals(descriptorWithSameValues));

        // same object -> returns true
        assertTrue(DESC_AMY.equals(DESC_AMY));

        // null -> returns false
        assertFalse(DESC_AMY.equals(null));

        // different types -> returns false
        assertFalse(DESC_AMY.equals(5));

        // different values -> returns false
        assertFalse(DESC_AMY.equals(DESC_BOB));

        // different name -> returns false
        EditPersonDescriptor editedAmy = new EditPersonDescriptorBuilder(DESC_AMY).withName(VALID_NAME_BOB).build();
        assertFalse(DESC_AMY.equals(editedAmy));

        // different phone -> returns false
        editedAmy = new EditPersonDescriptorBuilder(DESC_AMY).withPhone(VALID_PHONE_BOB).build();
        assertFalse(DESC_AMY.equals(editedAmy));

        // different email -> returns false
        editedAmy = new EditPersonDescriptorBuilder(DESC_AMY).withEmail(VALID_EMAIL_BOB).build();
        assertFalse(DESC_AMY.equals(editedAmy));

        // different address -> returns false
        editedAmy = new EditPersonDescriptorBuilder(DESC_AMY).withAddress(VALID_ADDRESS_BOB).build();
        assertFalse(DESC_AMY.equals(editedAmy));

        // different guardian fields -> returns false
        editedAmy = new EditPersonDescriptorBuilder(DESC_AMY).withGuardianName(VALID_GUARDIAN_NAME_BOB).build();
        assertFalse(DESC_AMY.equals(editedAmy));
        editedAmy = new EditPersonDescriptorBuilder(DESC_AMY).withGuardianPhone(VALID_GUARDIAN_PHONE_BOB).build();
        assertFalse(DESC_AMY.equals(editedAmy));
        editedAmy = new EditPersonDescriptorBuilder(DESC_AMY).withGuardianEmail(VALID_GUARDIAN_EMAIL_BOB).build();
        assertFalse(DESC_AMY.equals(editedAmy));

        // different tutoring fields -> returns false
        editedAmy = new EditPersonDescriptorBuilder(DESC_AMY).withSubject(VALID_SUBJECT_BOB).build();
        assertFalse(DESC_AMY.equals(editedAmy));
        editedAmy = new EditPersonDescriptorBuilder(DESC_AMY)
                .withEducationLevel(VALID_EDUCATION_LEVEL_BOB).build();
        assertFalse(DESC_AMY.equals(editedAmy));

        // different tags -> returns false
        editedAmy = new EditPersonDescriptorBuilder(DESC_AMY).withTags(VALID_TAG_HUSBAND).build();
        assertFalse(DESC_AMY.equals(editedAmy));
    }

    @Test
    public void toStringMethod() {
        EditPersonDescriptor editPersonDescriptor = new EditPersonDescriptor();
        String expected = EditPersonDescriptor.class.getCanonicalName() + "{name="
                + editPersonDescriptor.getName().orElse(null) + ", phone="
                + editPersonDescriptor.getPhone().value().orElse(null) + ", email="
                + editPersonDescriptor.getEmail().value().orElse(null) + ", address="
                + editPersonDescriptor.getAddress().value().orElse(null) + ", guardianName="
                + editPersonDescriptor.getGuardianName().value().orElse(null) + ", guardianPhone="
                + editPersonDescriptor.getGuardianPhone().value().orElse(null) + ", guardianEmail="
                + editPersonDescriptor.getGuardianEmail().value().orElse(null) + ", subject="
                + editPersonDescriptor.getSubject().value().orElse(null) + ", educationLevel="
                + editPersonDescriptor.getEducationLevel().value().orElse(null) + ", learningFocus="
                + editPersonDescriptor.getLearningFocus().value().orElse(null) + ", nextAction="
                + editPersonDescriptor.getNextAction().value().orElse(null) + ", tags="
                + editPersonDescriptor.getTags().orElse(null) + "}";
        assertEquals(expected, editPersonDescriptor.toString());
    }
}
