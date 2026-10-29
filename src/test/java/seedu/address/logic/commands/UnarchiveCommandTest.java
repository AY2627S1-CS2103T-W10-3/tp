package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.ResultViewScope;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;

public class UnarchiveCommandTest {

    private Model model;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        model.setPerson(ALICE, ALICE.withArchived(true));
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS, ResultViewScope.ARCHIVED);
    }

    @Test
    public void execute_validArchivedIndex_restoresStudentAndPreservesFields() {
        Person target = model.getFilteredPersonList().get(0);
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS, ResultViewScope.ARCHIVED);
        expectedModel.setPerson(target, target.withArchived(false));

        String expectedMessage = String.format(UnarchiveCommand.MESSAGE_UNARCHIVE_PERSON_SUCCESS,
                target.getName().fullName);

        assertCommandSuccess(new UnarchiveCommand(INDEX_FIRST_PERSON), model, expectedMessage, expectedModel);
        Person restored = model.getAddressBook().getPersonList().stream()
                .filter(person -> person.isSamePerson(target)).findFirst().orElseThrow();
        assertFalse(restored.isArchived());
    }

    @Test
    public void execute_outOfRangeIndex_throwsCommandException() {
        Index outOfRange = Index.fromOneBased(model.getFilteredPersonList().size() + 1);

        assertCommandFailure(new UnarchiveCommand(outOfRange), model,
                Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_activeView_throwsWrongViewBeforeIndexValidation() {
        model.updateFilteredPersonList(person -> false);

        assertCommandFailure(new UnarchiveCommand(INDEX_FIRST_PERSON), model,
                UnarchiveCommand.MESSAGE_REQUIRES_ARCHIVED_VIEW);
    }

    @Test
    public void equals_sameIndex_returnsTrue() {
        assertEquals(new UnarchiveCommand(INDEX_FIRST_PERSON), new UnarchiveCommand(INDEX_FIRST_PERSON));
    }
}
