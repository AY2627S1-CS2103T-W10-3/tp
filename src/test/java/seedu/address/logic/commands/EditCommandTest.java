package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EDUCATION_LEVEL_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_GUARDIAN_EMAIL_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_GUARDIAN_NAME_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_GUARDIAN_PHONE_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_SUBJECT_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.EditCommand.EditPersonDescriptor;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.ResultViewScope;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.testutil.EditPersonDescriptorBuilder;
import seedu.address.testutil.PersonBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for EditCommand.
 */
public class EditCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_archivedView_throwsCommandException() {
        model.setPerson(ALICE, ALICE.withArchived(true));
        model.updateFilteredPersonList(person -> true, ResultViewScope.ARCHIVED);

        assertCommandFailure(new EditCommand(INDEX_FIRST_PERSON, DESC_AMY), model,
                EditCommand.MESSAGE_REQUIRES_ACTIVE_VIEW);
    }

    @Test
    public void execute_allFieldsSpecifiedUnfilteredList_success() {
        Person editedPerson = new PersonBuilder().build();
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder(editedPerson).build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_PERSON, descriptor);

        String expectedMessage = String.format("Edited student: %1$s", editedPerson.getName().fullName);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(model.getFilteredPersonList().get(0), editedPerson);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_someFieldsSpecifiedUnfilteredList_success() {
        Index indexLastPerson = Index.fromOneBased(model.getFilteredPersonList().size());
        Person lastPerson = model.getFilteredPersonList().get(indexLastPerson.getZeroBased());

        PersonBuilder personInList = new PersonBuilder(lastPerson);
        Person editedPerson = personInList.withName(VALID_NAME_BOB).withPhone(VALID_PHONE_BOB)
                .withTags(VALID_TAG_HUSBAND).build();

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB)
                .withPhone(VALID_PHONE_BOB).withTags(VALID_TAG_HUSBAND).build();
        EditCommand editCommand = new EditCommand(indexLastPerson, descriptor);

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS,
                editedPerson.getName().fullName);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(lastPerson, editedPerson);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_noFieldSpecifiedUnfilteredList_success() {
        EditCommand editCommand = new EditCommand(INDEX_FIRST_PERSON, new EditPersonDescriptor());
        Person editedPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS,
                editedPerson.getName().fullName);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_addPhoneToNameOnlyPerson_preservesAbsentDetails() {
        Person nameOnly = new PersonBuilder().withName("Name Only").withoutPhone().withoutEmail()
                .withoutAddress().withTags().build();
        AddressBook addressBook = new AddressBook();
        addressBook.addPerson(nameOnly);
        Model modelWithNameOnly = new ModelManager(addressBook, new UserPrefs());
        Person editedPerson = new PersonBuilder(nameOnly).withPhone(VALID_PHONE_BOB).build();
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withPhone(VALID_PHONE_BOB).build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_PERSON, descriptor);

        Model expectedModel = new ModelManager(new AddressBook(addressBook), new UserPrefs());
        expectedModel.setPerson(nameOnly, editedPerson);
        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS,
                editedPerson.getName().fullName);
        assertCommandSuccess(editCommand, modelWithNameOnly, expectedMessage, expectedModel);
    }

    @Test
    public void execute_editStudentField_preservesGuardian() {
        Person withGuardian = new PersonBuilder().withName("Guardian Student")
                .withGuardian("Mary Guardian", "81234567", "mary@example.com").build();
        AddressBook addressBook = new AddressBook();
        addressBook.addPerson(withGuardian);
        Model modelWithGuardian = new ModelManager(addressBook, new UserPrefs());
        Person editedPerson = new PersonBuilder(withGuardian).withPhone(VALID_PHONE_BOB).build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_PERSON,
                new EditPersonDescriptorBuilder().withPhone(VALID_PHONE_BOB).build());

        Model expectedModel = new ModelManager(new AddressBook(addressBook), new UserPrefs());
        expectedModel.setPerson(withGuardian, editedPerson);
        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS,
                editedPerson.getName().fullName);
        assertCommandSuccess(editCommand, modelWithGuardian, expectedMessage, expectedModel);
    }

    @Test
    public void execute_editStudentField_preservesTutoringDetails() {
        Person withTutoringDetails = new PersonBuilder().withName("Tutoring Student")
                .withSubject("H2 Mathematics").withEducationLevel("JC1").build();
        AddressBook addressBook = new AddressBook();
        addressBook.addPerson(withTutoringDetails);
        Model modelWithTutoringDetails = new ModelManager(addressBook, new UserPrefs());
        Person editedPerson = new PersonBuilder(withTutoringDetails).withPhone(VALID_PHONE_BOB).build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_PERSON,
                new EditPersonDescriptorBuilder().withPhone(VALID_PHONE_BOB).build());

        Model expectedModel = new ModelManager(new AddressBook(addressBook), new UserPrefs());
        expectedModel.setPerson(withTutoringDetails, editedPerson);
        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS,
                editedPerson.getName().fullName);
        assertCommandSuccess(editCommand, modelWithTutoringDetails, expectedMessage, expectedModel);
    }

    @Test
    public void execute_editStudentField_preservesLesson() {
        Person scheduledStudent = new PersonBuilder().withName("Scheduled Student")
                .withLesson("2026-10-30 16:00").build();
        AddressBook addressBook = new AddressBook();
        addressBook.addPerson(scheduledStudent);
        Model modelWithLesson = new ModelManager(addressBook, new UserPrefs());
        Person editedPerson = new PersonBuilder(scheduledStudent).withPhone(VALID_PHONE_BOB).build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_PERSON,
                new EditPersonDescriptorBuilder().withPhone(VALID_PHONE_BOB).build());

        Model expectedModel = new ModelManager(new AddressBook(addressBook), new UserPrefs());
        expectedModel.setPerson(scheduledStudent, editedPerson);
        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS,
                editedPerson.getName().fullName);
        assertCommandSuccess(editCommand, modelWithLesson, expectedMessage, expectedModel);
    }

    @Test
    public void execute_editLearningContext_replacesBothValues() {
        Person original = new PersonBuilder().withName("Learning Student")
                .withLearningFocus("Fractions").withNextAction("Worksheet 1").build();
        AddressBook addressBook = new AddressBook();
        addressBook.addPerson(original);
        Model actualModel = new ModelManager(addressBook, new UserPrefs());
        Person editedPerson = new PersonBuilder(original).withLearningFocus("Algebra")
                .withNextAction("Worksheet 2").build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_PERSON,
                new EditPersonDescriptorBuilder().withLearningFocus("Algebra")
                        .withNextAction("Worksheet 2").build());

        Model expectedModel = new ModelManager(new AddressBook(addressBook), new UserPrefs());
        expectedModel.setPerson(original, editedPerson);
        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS,
                editedPerson.getName().fullName);
        assertCommandSuccess(editCommand, actualModel, expectedMessage, expectedModel);
    }

    @Test
    public void execute_editGuardianAndTutoringFields_success() {
        Person person = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person editedPerson = new PersonBuilder(person)
                .withGuardian(VALID_GUARDIAN_NAME_AMY, VALID_GUARDIAN_PHONE_AMY, VALID_GUARDIAN_EMAIL_AMY)
                .withSubject(VALID_SUBJECT_AMY)
                .withEducationLevel(VALID_EDUCATION_LEVEL_AMY)
                .build();
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder()
                .withGuardianName(VALID_GUARDIAN_NAME_AMY)
                .withGuardianPhone(VALID_GUARDIAN_PHONE_AMY)
                .withGuardianEmail(VALID_GUARDIAN_EMAIL_AMY)
                .withSubject(VALID_SUBJECT_AMY)
                .withEducationLevel(VALID_EDUCATION_LEVEL_AMY)
                .build();

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(person, editedPerson);
        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS,
                editedPerson.getName().fullName);

        assertCommandSuccess(new EditCommand(INDEX_FIRST_PERSON, descriptor), model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_editExistingGuardianPhone_preservesOtherGuardianFields() {
        Person person = new PersonBuilder().withName("Guardian Student")
                .withGuardian("Existing Guardian", "81111111", "existing@example.com").build();
        AddressBook addressBook = new AddressBook();
        addressBook.addPerson(person);
        Model guardianModel = new ModelManager(addressBook, new UserPrefs());
        Person editedPerson = new PersonBuilder(person)
                .withGuardian("Existing Guardian", VALID_GUARDIAN_PHONE_AMY, "existing@example.com").build();
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder()
                .withGuardianPhone(VALID_GUARDIAN_PHONE_AMY).build();
        Model expectedModel = new ModelManager(new AddressBook(addressBook), new UserPrefs());
        expectedModel.setPerson(person, editedPerson);
        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS,
                editedPerson.getName().fullName);

        assertCommandSuccess(new EditCommand(INDEX_FIRST_PERSON, descriptor), guardianModel,
                expectedMessage, expectedModel);
    }

    @Test
    public void execute_guardianContactWithoutGuardianName_failure() {
        Person person = new PersonBuilder().withName("No Guardian").withoutGuardian().build();
        AddressBook addressBook = new AddressBook();
        addressBook.addPerson(person);
        Model guardianModel = new ModelManager(addressBook, new UserPrefs());
        EditCommand command = new EditCommand(INDEX_FIRST_PERSON,
                new EditPersonDescriptorBuilder().withGuardianPhone(VALID_GUARDIAN_PHONE_AMY).build());

        assertCommandFailure(command, guardianModel, EditCommand.MESSAGE_GUARDIAN_NAME_REQUIRED);
    }

    @Test
    public void execute_clearAllOptionalFields_success() {
        Person original = new PersonBuilder().withName("Complete Student")
                .withPhone("81234567").withEmail("student@example.com").withAddress("1 Main Street")
                .withGuardian("Guardian Name", "82345678", "guardian@example.com")
                .withSubject("Mathematics").withEducationLevel("S3")
                .withLearningFocus("Fractions").withNextAction("Worksheet 1").withTags("active").build();
        AddressBook addressBook = new AddressBook();
        addressBook.addPerson(original);
        Model actualModel = new ModelManager(addressBook, new UserPrefs());
        Person clearedPerson = new PersonBuilder(original).withoutPhone().withoutEmail().withoutAddress()
                .withoutGuardian().withoutSubject().withoutEducationLevel()
                .withoutLearningFocus().withoutNextAction().withTags().build();
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder()
                .withoutPhone().withoutEmail().withoutAddress().withoutGuardianName()
                .withoutSubject().withoutEducationLevel().withoutLearningFocus().withoutNextAction()
                .withTags().build();
        Model expectedModel = new ModelManager(new AddressBook(addressBook), new UserPrefs());
        expectedModel.setPerson(original, clearedPerson);

        assertCommandSuccess(new EditCommand(INDEX_FIRST_PERSON, descriptor), actualModel,
                String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS,
                        clearedPerson.getName().fullName), expectedModel);
    }

    @Test
    public void execute_clearGuardianName_clearsGuardianContacts() {
        Person original = new PersonBuilder().withName("Guardian Student")
                .withGuardian("Guardian Name", "82345678", "guardian@example.com").build();
        AddressBook addressBook = new AddressBook();
        addressBook.addPerson(original);
        Model actualModel = new ModelManager(addressBook, new UserPrefs());
        Person clearedPerson = new PersonBuilder(original).withoutGuardian().build();
        Model expectedModel = new ModelManager(new AddressBook(addressBook), new UserPrefs());
        expectedModel.setPerson(original, clearedPerson);

        assertCommandSuccess(new EditCommand(INDEX_FIRST_PERSON,
                        new EditPersonDescriptorBuilder().withoutGuardianName().build()), actualModel,
                String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS,
                        clearedPerson.getName().fullName), expectedModel);
    }

    @Test
    public void execute_clearGuardianNameAndSetContact_failure() {
        Person original = new PersonBuilder().withName("Guardian Student")
                .withGuardian("Guardian Name", "82345678", "guardian@example.com").build();
        AddressBook addressBook = new AddressBook();
        addressBook.addPerson(original);
        Model actualModel = new ModelManager(addressBook, new UserPrefs());
        EditCommand command = new EditCommand(INDEX_FIRST_PERSON, new EditPersonDescriptorBuilder()
                .withoutGuardianName().withGuardianPhone("83456789").build());

        assertCommandFailure(command, actualModel, EditCommand.MESSAGE_GUARDIAN_NAME_REQUIRED);
    }

    @Test
    public void execute_clearAlreadyAbsentField_success() {
        Person original = new PersonBuilder().withName("Name Only").withoutPhone().build();
        AddressBook addressBook = new AddressBook();
        addressBook.addPerson(original);
        Model actualModel = new ModelManager(addressBook, new UserPrefs());
        Model expectedModel = new ModelManager(new AddressBook(addressBook), new UserPrefs());

        assertCommandSuccess(new EditCommand(INDEX_FIRST_PERSON,
                        new EditPersonDescriptorBuilder().withoutPhone().build()), actualModel,
                String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS, original.getName().fullName), expectedModel);
    }

    @Test
    public void execute_filteredList_success() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Person personInFilteredList = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person editedPerson = new PersonBuilder(personInFilteredList).withName(VALID_NAME_BOB).build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_PERSON,
                new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB).build());

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS,
                editedPerson.getName().fullName);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(model.getFilteredPersonList().get(0), editedPerson);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_duplicatePersonUnfilteredList_failure() {
        Person firstPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder(firstPerson).build();
        EditCommand editCommand = new EditCommand(INDEX_SECOND_PERSON, descriptor);

        assertCommandFailure(editCommand, model, "This student already exists in TutorContacts.");
    }

    @Test
    public void execute_duplicatePersonFilteredList_failure() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        // edit person in filtered list into a duplicate in address book
        Person personInList = model.getAddressBook().getPersonList().get(INDEX_SECOND_PERSON.getZeroBased());
        EditCommand editCommand = new EditCommand(INDEX_FIRST_PERSON,
                new EditPersonDescriptorBuilder(personInList).build());

        assertCommandFailure(editCommand, model, "This student already exists in TutorContacts.");
    }

    @Test
    public void execute_duplicateArchivedPerson_showsArchiveGuidance() {
        Person activePerson = new PersonBuilder(ALICE).withName("Active Student").build();
        Person archivedPerson = new PersonBuilder().withName("Archived Student").withArchived(true).build();
        AddressBook addressBook = new AddressBook();
        addressBook.addPerson(activePerson);
        addressBook.addPerson(archivedPerson);
        Model modelWithArchivedDuplicate = new ModelManager(addressBook, new UserPrefs());
        EditCommand editCommand = new EditCommand(INDEX_FIRST_PERSON,
                new EditPersonDescriptorBuilder().withName("Archived Student").build());

        assertCommandFailure(editCommand, modelWithArchivedDuplicate,
                "An archived student with this name already exists. "
                        + "Use `list archived`, then `unarchive INDEX` to restore them.");
    }

    @Test
    public void execute_invalidPersonIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB).build();
        EditCommand editCommand = new EditCommand(outOfBoundIndex, descriptor);

        assertCommandFailure(editCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    /**
     * Edit filtered list where index is larger than size of filtered list,
     * but smaller than size of address book
     */
    @Test
    public void execute_invalidPersonIndexFilteredList_failure() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);
        Index outOfBoundIndex = INDEX_SECOND_PERSON;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getPersonList().size());

        EditCommand editCommand = new EditCommand(outOfBoundIndex,
                new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB).build());

        assertCommandFailure(editCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        final EditCommand standardCommand = new EditCommand(INDEX_FIRST_PERSON, DESC_AMY);

        // same values -> returns true
        EditPersonDescriptor copyDescriptor = new EditPersonDescriptor(DESC_AMY);
        EditCommand commandWithSameValues = new EditCommand(INDEX_FIRST_PERSON, copyDescriptor);
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different index -> returns false
        assertFalse(standardCommand.equals(new EditCommand(INDEX_SECOND_PERSON, DESC_AMY)));

        // different descriptor -> returns false
        assertFalse(standardCommand.equals(new EditCommand(INDEX_FIRST_PERSON, DESC_BOB)));
    }

    @Test
    public void toStringMethod() {
        Index index = Index.fromOneBased(1);
        EditPersonDescriptor editPersonDescriptor = new EditPersonDescriptor();
        EditCommand editCommand = new EditCommand(index, editPersonDescriptor);
        String expected = EditCommand.class.getCanonicalName() + "{index=" + index + ", editPersonDescriptor="
                + editPersonDescriptor + "}";
        assertEquals(expected, editCommand.toString());
    }

}
