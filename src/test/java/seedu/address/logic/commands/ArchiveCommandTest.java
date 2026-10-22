package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
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
        assertTrue(archived.isArchived());
        assertFalse(model.getFilteredPersonList().contains(archived));
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
    public void equals_sameIndex_returnsTrue() {
        assertEquals(new ArchiveCommand(INDEX_FIRST_PERSON), new ArchiveCommand(INDEX_FIRST_PERSON));
    }
}
