package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.ResultViewScope;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.model.person.Remark;
import seedu.address.testutil.PersonBuilder;

public class RemarkCommandTest {

    private static final Remark REMARK = new Remark("Prefers evening lessons.");

    private final Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_addRemark_success() {
        Person target = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person remarked = new PersonBuilder(target).withRemark(REMARK.getValue()).build();
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setPerson(target, remarked);

        String expectedMessage = String.format(RemarkCommand.MESSAGE_ADD_REMARK_SUCCESS,
                Messages.format(remarked));

        assertCommandSuccess(new RemarkCommand(INDEX_FIRST_PERSON, Optional.of(REMARK)), model,
                expectedMessage, expectedModel);
    }

    @Test
    public void execute_removeRemark_success() {
        Person target = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person remarked = target.withRemark(Optional.of(REMARK));
        model.setPerson(target, remarked);
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setPerson(remarked, target);

        String expectedMessage = String.format(RemarkCommand.MESSAGE_DELETE_REMARK_SUCCESS,
                Messages.format(target));

        assertCommandSuccess(new RemarkCommand(INDEX_FIRST_PERSON, Optional.empty()), model,
                expectedMessage, expectedModel);
    }

    @Test
    public void execute_outOfRangeIndex_throwsCommandException() {
        Index outOfRange = Index.fromOneBased(model.getFilteredPersonList().size() + 1);

        assertCommandFailure(new RemarkCommand(outOfRange, Optional.of(REMARK)), model,
                Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_archivedView_throwsCommandException() {
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS, ResultViewScope.ARCHIVED);

        assertCommandFailure(new RemarkCommand(INDEX_FIRST_PERSON, Optional.of(REMARK)), model,
                RemarkCommand.MESSAGE_REQUIRES_ACTIVE_VIEW);
    }

    @Test
    public void equals() {
        RemarkCommand command = new RemarkCommand(INDEX_FIRST_PERSON, Optional.of(REMARK));

        assertEquals(command, command);
        assertEquals(command, new RemarkCommand(INDEX_FIRST_PERSON, Optional.of(new Remark(REMARK.getValue()))));
        assertNotEquals(command, null);
        assertNotEquals(command, new ClearCommand());
        assertNotEquals(command, new RemarkCommand(INDEX_SECOND_PERSON, Optional.of(REMARK)));
        assertNotEquals(command, new RemarkCommand(INDEX_FIRST_PERSON, Optional.empty()));
    }
}
