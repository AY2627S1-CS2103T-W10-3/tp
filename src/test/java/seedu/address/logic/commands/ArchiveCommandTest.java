package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.DANIEL;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.ResultViewScope;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;

public class ArchiveCommandTest {

    private final Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_validDisplayedIndex_archivesStudentAndPreservesFields() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);
        Person target = model.getFilteredPersonList().get(0);
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        showPersonAtIndex(expectedModel, INDEX_FIRST_PERSON);
        expectedModel.setPerson(target, target.withArchived(true));

        String expectedMessage = String.format(ArchiveCommand.MESSAGE_ARCHIVE_PERSON_SUCCESS,
                Messages.format(target));

        assertCommandSuccess(new ArchiveCommand(INDEX_FIRST_PERSON), model, expectedMessage, expectedModel);
        Person archived = model.getAddressBook().getPersonList().stream()
                .filter(person -> person.isSamePerson(target)).findFirst().orElseThrow();
        assertEquals(target.withArchived(true), archived);
        assertFalse(model.getFilteredPersonList().contains(archived));
    }

    @Test
    public void execute_filteredView_archivesStudentAtDisplayedIndex() {
        model.updateFilteredPersonList(person -> person.isSamePerson(DANIEL));
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.updateFilteredPersonList(person -> person.isSamePerson(DANIEL));
        expectedModel.setPerson(DANIEL, DANIEL.withArchived(true));

        assertCommandSuccess(new ArchiveCommand(INDEX_FIRST_PERSON), model,
                String.format(ArchiveCommand.MESSAGE_ARCHIVE_PERSON_SUCCESS, Messages.format(DANIEL)), expectedModel);
        assertFalse(findStoredPerson(ALICE).isArchived());
        assertTrue(findStoredPerson(DANIEL).isArchived());
    }

    @Test
    public void execute_outOfRangeIndex_throwsCommandException() {
        Index outOfRange = Index.fromOneBased(model.getFilteredPersonList().size() + 1);

        assertCommandFailure(new ArchiveCommand(outOfRange), model,
                Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_archivedView_throwsWrongViewBeforeIndexError() {
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS, ResultViewScope.ARCHIVED);
        assertTrue(model.getFilteredPersonList().isEmpty());

        assertCommandFailure(new ArchiveCommand(INDEX_FIRST_PERSON), model,
                ArchiveCommand.MESSAGE_REQUIRES_ACTIVE_VIEW);
    }

    @Test
    public void equals() {
        assertEquals(new ArchiveCommand(INDEX_FIRST_PERSON), new ArchiveCommand(INDEX_FIRST_PERSON));
        assertFalse(new ArchiveCommand(INDEX_FIRST_PERSON).equals(new ArchiveCommand(INDEX_SECOND_PERSON)));
        assertFalse(new ArchiveCommand(INDEX_FIRST_PERSON).equals(null));
        assertFalse(new ArchiveCommand(INDEX_FIRST_PERSON).equals(new ListCommand()));
    }

    @Test
    public void toStringMethod() {
        assertEquals(ArchiveCommand.class.getCanonicalName() + "{targetIndex=" + INDEX_FIRST_PERSON + "}",
                new ArchiveCommand(INDEX_FIRST_PERSON).toString());
    }

    private Person findStoredPerson(Person target) {
        return model.getAddressBook().getPersonList().stream()
                .filter(person -> person.isSamePerson(target))
                .findFirst()
                .orElseThrow();
    }
}
