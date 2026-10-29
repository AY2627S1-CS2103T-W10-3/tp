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
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.ResultViewScope;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Lesson;
import seedu.address.model.person.Person;

/**
 * Contains integration tests (interaction with the Model) and unit tests for {@code UnscheduleCommand}.
 */
public class UnscheduleCommandTest {

    private static final Lesson LESSON = new Lesson("2026-10-30 16:00");

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_scheduledStudent_clearsLesson() {
        Person student = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person scheduledStudent = student.withLesson(Optional.of(LESSON));
        model.setPerson(student, scheduledStudent);

        String expectedMessage = String.format(UnscheduleCommand.MESSAGE_UNSCHEDULE_SUCCESS,
                student.getName().fullName, LESSON);
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setPerson(scheduledStudent, student.withLesson(Optional.empty()));

        assertCommandSuccess(new UnscheduleCommand(INDEX_FIRST_PERSON), model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_upcomingView_addsListUpdateNotice() throws Exception {
        Person student = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person other = model.getFilteredPersonList().get(INDEX_SECOND_PERSON.getZeroBased());
        model.setPerson(student, student.withLesson(Optional.of(new Lesson("2099-10-30 16:00"))));
        model.setPerson(other, other.withLesson(Optional.of(new Lesson("2099-11-02 09:30"))));
        new UpcomingCommand().execute(model);

        CommandResult result = new UnscheduleCommand(INDEX_FIRST_PERSON).execute(model);

        assertEquals(String.format(UnscheduleCommand.MESSAGE_UNSCHEDULE_SUCCESS, student.getName().fullName,
                "2099-10-30 16:00") + ". " + Messages.MESSAGE_DISPLAYED_LIST_UPDATED, result.getFeedbackToUser());
        assertEquals(other.getName(), model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased()).getName());
    }

    @Test
    public void execute_studentWithoutLesson_throwsCommandException() {
        Person student = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());

        assertCommandFailure(new UnscheduleCommand(INDEX_FIRST_PERSON), model,
                String.format(UnscheduleCommand.MESSAGE_NO_LESSON, student.getName().fullName));
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);

        assertCommandFailure(new UnscheduleCommand(outOfBoundIndex), model,
                Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validIndexFilteredList_clearsDisplayedStudent() {
        Person student = model.getFilteredPersonList().get(INDEX_SECOND_PERSON.getZeroBased());
        Person scheduledStudent = student.withLesson(Optional.of(LESSON));
        model.setPerson(student, scheduledStudent);
        showPersonAtIndex(model, INDEX_SECOND_PERSON);

        String expectedMessage = String.format(UnscheduleCommand.MESSAGE_UNSCHEDULE_SUCCESS,
                student.getName().fullName, LESSON);
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        showPersonAtIndex(expectedModel, INDEX_SECOND_PERSON);
        expectedModel.setPerson(scheduledStudent, student.withLesson(Optional.empty()));

        assertCommandSuccess(new UnscheduleCommand(INDEX_FIRST_PERSON), model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexFilteredList_throwsCommandException() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        assertCommandFailure(new UnscheduleCommand(INDEX_SECOND_PERSON), model,
                Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_archivedView_throwsCommandException() {
        Person student = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        model.setPerson(student, student.withLesson(Optional.of(LESSON)).withArchived(true));
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS, ResultViewScope.ARCHIVED);

        assertCommandFailure(new UnscheduleCommand(INDEX_FIRST_PERSON), model,
                UnscheduleCommand.MESSAGE_REQUIRES_ACTIVE_VIEW);
    }

    @Test
    public void equals() {
        UnscheduleCommand unscheduleFirst = new UnscheduleCommand(INDEX_FIRST_PERSON);

        assertTrue(unscheduleFirst.equals(unscheduleFirst));
        assertTrue(unscheduleFirst.equals(new UnscheduleCommand(INDEX_FIRST_PERSON)));
        assertFalse(unscheduleFirst.equals(1));
        assertFalse(unscheduleFirst.equals(null));
        assertFalse(unscheduleFirst.equals(new UnscheduleCommand(INDEX_SECOND_PERSON)));
    }

    @Test
    public void toStringMethod() {
        UnscheduleCommand command = new UnscheduleCommand(INDEX_FIRST_PERSON);
        String expected = UnscheduleCommand.class.getCanonicalName() + "{targetIndex=" + INDEX_FIRST_PERSON + "}";
        assertEquals(expected, command.toString());
    }
}
