package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BOB;

import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.PersonBuilder;

public class PersonTest {

    @Test
    public void constructor_nameOnly_hasEmptyOptionalDetails() {
        Person person = new Person(new Name("Only Name"), Optional.empty(), Optional.empty(), Optional.empty(),
                Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), false,
                Set.of());
        assertEquals(Optional.empty(), person.getPhone());
        assertEquals(Optional.empty(), person.getEmail());
        assertEquals(Optional.empty(), person.getAddress());
        assertEquals(Optional.empty(), person.getGuardian());
        assertEquals(Optional.empty(), person.getSubject());
        assertEquals(Optional.empty(), person.getEducationLevel());
        assertEquals(Optional.empty(), person.getLearningFocus());
        assertEquals(Optional.empty(), person.getNextAction());
        assertTrue(person.getTags().isEmpty());
    }

    @Test
    public void constructor_nullName_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () ->
                new Person(null, Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(),
                        Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), false, Set.of()));
    }

    @Test
    public void optionalDetails_partiallyComplete_preservedByCopyBuilder() {
        Person person = new PersonBuilder().withName("Partial Student").withoutEmail().withoutAddress().build();
        assertEquals(Optional.of(new Phone(PersonBuilder.DEFAULT_PHONE)), person.getPhone());
        assertEquals(Optional.empty(), person.getEmail());
        assertEquals(Optional.empty(), person.getAddress());
        assertEquals(person, new PersonBuilder(person).build());
    }

    @Test
    public void equals_optionalDetailsDiffer_returnsFalse() {
        Person nameOnly = new PersonBuilder().withoutPhone().withoutEmail().withoutAddress().build();
        Person withPhone = new PersonBuilder(nameOnly).withPhone(PersonBuilder.DEFAULT_PHONE).build();
        assertNotEquals(nameOnly, withPhone);
        assertTrue(nameOnly.isSamePerson(withPhone));
    }

    @Test
    public void guardian_preservedByCopyBuilderAndIncludedInEquality() {
        Person withGuardian = new PersonBuilder().withGuardian(
                "Jane Bee", "81234567", "jane@example.com").build();
        Person copied = new PersonBuilder(withGuardian).build();
        Person withoutGuardian = new PersonBuilder(withGuardian).withoutGuardian().build();

        assertEquals(withGuardian, copied);
        assertTrue(withGuardian.getGuardian().isPresent());
        assertNotEquals(withGuardian, withoutGuardian);
        assertTrue(withGuardian.isSamePerson(withoutGuardian));
    }

    @Test
    public void tutoringDetails_preservedByCopyBuilderAndIncludedInEquality() {
        Person withTutoringDetails = new PersonBuilder().withSubject("H2 Mathematics")
                .withEducationLevel("jc1").build();
        Person copied = new PersonBuilder(withTutoringDetails).build();

        assertEquals(withTutoringDetails, copied);
        assertEquals("H2 Mathematics", withTutoringDetails.getSubject().orElseThrow().toString());
        assertEquals("JC1", withTutoringDetails.getEducationLevel().orElseThrow().toString());
        assertNotEquals(withTutoringDetails, new PersonBuilder(withTutoringDetails).withoutSubject().build());
        assertNotEquals(withTutoringDetails,
                new PersonBuilder(withTutoringDetails).withoutEducationLevel().build());
    }

    @Test
    public void learningContext_preservedByCopyBuilderAndIncludedInEquality() {
        Person withLearningContext = new PersonBuilder().withLearningFocus("Fractions and ratios")
                .withNextAction("Complete worksheet 2").build();
        Person copied = new PersonBuilder(withLearningContext).build();

        assertEquals(withLearningContext, copied);
        assertEquals("Fractions and ratios",
                withLearningContext.getLearningFocus().orElseThrow().getValue());
        assertEquals("Complete worksheet 2",
                withLearningContext.getNextAction().orElseThrow().getValue());
        assertNotEquals(withLearningContext,
                new PersonBuilder(withLearningContext).withoutLearningFocus().build());
        assertNotEquals(withLearningContext,
                new PersonBuilder(withLearningContext).withoutNextAction().build());
        assertTrue(withLearningContext.isSamePerson(
                new PersonBuilder(withLearningContext).withoutLearningFocus().withoutNextAction().build()));
    }

    @Test
    public void withArchived_preservesFieldsAndChangesOnlyArchiveState() {
        Person active = new PersonBuilder(ALICE).withLearningFocus("Fractions")
                .withNextAction("Complete worksheet 2").build();

        Person archived = active.withArchived(true);

        assertTrue(archived.isArchived());
        assertEquals(active.getName(), archived.getName());
        assertEquals(active.getPhone(), archived.getPhone());
        assertEquals(active.getEmail(), archived.getEmail());
        assertEquals(active.getAddress(), archived.getAddress());
        assertEquals(active.getGuardian(), archived.getGuardian());
        assertEquals(active.getSubject(), archived.getSubject());
        assertEquals(active.getEducationLevel(), archived.getEducationLevel());
        assertEquals(active.getLearningFocus(), archived.getLearningFocus());
        assertEquals(active.getNextAction(), archived.getNextAction());
        assertEquals(active.getTags(), archived.getTags());
        assertFalse(archived.withArchived(false).isArchived());
        assertNotEquals(active, archived);
    }

    @Test
    public void asObservableList_modifyList_throwsUnsupportedOperationException() {
        Person person = new PersonBuilder().build();
        assertThrows(UnsupportedOperationException.class, () -> person.getTags().remove(0));
    }

    @Test
    public void isSamePerson() {
        // same object -> returns true
        assertTrue(ALICE.isSamePerson(ALICE));

        // null -> returns false
        assertFalse(ALICE.isSamePerson(null));

        // same name, all other attributes different -> returns true
        Person editedAlice = new PersonBuilder(ALICE).withPhone(VALID_PHONE_BOB).withEmail(VALID_EMAIL_BOB)
                .withAddress(VALID_ADDRESS_BOB).withTags(VALID_TAG_HUSBAND).build();
        assertTrue(ALICE.isSamePerson(editedAlice));

        // different name, all other attributes same -> returns false
        editedAlice = new PersonBuilder(ALICE).withName(VALID_NAME_BOB).build();
        assertFalse(ALICE.isSamePerson(editedAlice));

        // Duplicate student names are case-insensitive.
        Person editedBob = new PersonBuilder(BOB).withName(VALID_NAME_BOB.toLowerCase()).build();
        assertTrue(BOB.isSamePerson(editedBob));

        // Surrounding and repeated whitespace does not make a new student.
        String nameWithTrailingSpaces = VALID_NAME_BOB + " ";
        editedBob = new PersonBuilder(BOB).withName(nameWithTrailingSpaces).build();
        assertTrue(BOB.isSamePerson(editedBob));
        editedBob = new PersonBuilder(BOB).withName(VALID_NAME_BOB.replace(" ", "   ")).build();
        assertTrue(BOB.isSamePerson(editedBob));
    }

    @Test
    public void equals() {
        // same values -> returns true
        Person aliceCopy = new PersonBuilder(ALICE).build();
        assertTrue(ALICE.equals(aliceCopy));

        // same object -> returns true
        assertTrue(ALICE.equals(ALICE));

        // null -> returns false
        assertFalse(ALICE.equals(null));

        // different type -> returns false
        assertFalse(ALICE.equals(5));

        // different person -> returns false
        assertFalse(ALICE.equals(BOB));

        // different name -> returns false
        Person editedAlice = new PersonBuilder(ALICE).withName(VALID_NAME_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different phone -> returns false
        editedAlice = new PersonBuilder(ALICE).withPhone(VALID_PHONE_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different email -> returns false
        editedAlice = new PersonBuilder(ALICE).withEmail(VALID_EMAIL_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different address -> returns false
        editedAlice = new PersonBuilder(ALICE).withAddress(VALID_ADDRESS_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different tags -> returns false
        editedAlice = new PersonBuilder(ALICE).withTags(VALID_TAG_HUSBAND).build();
        assertFalse(ALICE.equals(editedAlice));

        // different lesson -> returns false
        editedAlice = new PersonBuilder(ALICE).withLesson("2026-10-30 16:00").build();
        assertFalse(ALICE.equals(editedAlice));
    }

    @Test
    public void withLesson_replacesOnlyLesson() {
        Lesson lesson = new Lesson("2026-10-30 16:00");
        Person scheduledAlice = ALICE.withLesson(Optional.of(lesson));

        assertEquals(Optional.of(lesson), scheduledAlice.getLesson());
        assertEquals(new PersonBuilder(ALICE).withLesson("2026-10-30 16:00").build(), scheduledAlice);
        assertTrue(scheduledAlice.isSamePerson(ALICE));
        assertEquals(ALICE, scheduledAlice.withLesson(Optional.empty()));
        assertThrows(NullPointerException.class, () -> ALICE.withLesson(null));
    }

    @Test
    public void withLesson_archivedStudent_keepsArchiveState() {
        Person archivedAlice = ALICE.withArchived(true);
        Person scheduledAlice = archivedAlice.withLesson(Optional.of(new Lesson("2026-10-30 16:00")));

        assertTrue(scheduledAlice.isArchived());
        assertTrue(scheduledAlice.withLesson(Optional.empty()).isArchived());
        assertEquals(archivedAlice, scheduledAlice.withLesson(Optional.empty()));
    }

    @Test
    public void withArchived_scheduledStudent_keepsLesson() {
        Optional<Lesson> lesson = Optional.of(new Lesson("2026-10-30 16:00"));
        Person scheduledAlice = ALICE.withLesson(lesson);

        assertEquals(lesson, scheduledAlice.withArchived(true).getLesson());
        assertEquals(scheduledAlice, scheduledAlice.withArchived(true).withArchived(false));
    }

    @Test
    public void toStringMethod() {
        String expected = Person.class.getCanonicalName() + "{name=" + ALICE.getName() + ", phone=" + ALICE.getPhone()
                + ", email=" + ALICE.getEmail() + ", address=" + ALICE.getAddress() + ", guardian="
                + ALICE.getGuardian() + ", subject=" + ALICE.getSubject() + ", educationLevel="
                + ALICE.getEducationLevel() + ", learningFocus=" + ALICE.getLearningFocus() + ", nextAction="
                + ALICE.getNextAction() + ", lesson=" + ALICE.getLesson() + ", archived=" + ALICE.isArchived()
                + ", tags=" + ALICE.getTags() + "}";
        assertEquals(expected, ALICE.toString());
    }
}
