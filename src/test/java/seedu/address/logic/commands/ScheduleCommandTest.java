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

import java.time.Clock;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
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
 * Contains integration tests (interaction with the Model) and unit tests for {@code ScheduleCommand}.
 */
public class ScheduleCommandTest {

    private static final ZoneId ZONE = ZoneId.of("Asia/Singapore");
    /** The fixed current time used by these tests: 2026-10-22 16:00:30 local time. */
    private static final Clock CLOCK = Clock.fixed(
            LocalDateTime.of(2026, 10, 22, 16, 0, 30).atZone(ZONE).toInstant(), ZONE);
    private static final Lesson FUTURE_LESSON = new Lesson("2026-10-30 16:00");
    private static final Lesson OTHER_FUTURE_LESSON = new Lesson("2026-11-02 09:30");

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_noExistingLesson_schedulesLesson() {
        Person student = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        ScheduleCommand command = new ScheduleCommand(INDEX_FIRST_PERSON, FUTURE_LESSON, CLOCK);

        String expectedMessage = String.format(ScheduleCommand.MESSAGE_SCHEDULE_SUCCESS,
                student.getName().fullName, FUTURE_LESSON);
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setPerson(student, student.withLesson(Optional.of(FUTURE_LESSON)));

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_existingLesson_replacesLesson() {
        Person student = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person scheduledStudent = student.withLesson(Optional.of(FUTURE_LESSON));
        model.setPerson(student, scheduledStudent);
        ScheduleCommand command = new ScheduleCommand(INDEX_FIRST_PERSON, OTHER_FUTURE_LESSON, CLOCK);

        String expectedMessage = String.format(ScheduleCommand.MESSAGE_RESCHEDULE_SUCCESS,
                student.getName().fullName, OTHER_FUTURE_LESSON, FUTURE_LESSON);
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setPerson(scheduledStudent, student.withLesson(Optional.of(OTHER_FUTURE_LESSON)));

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_lessonAtStartOfNextMinute_success() {
        Lesson nextMinute = new Lesson("2026-10-22 16:01");
        Person student = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());

        String expectedMessage = String.format(ScheduleCommand.MESSAGE_SCHEDULE_SUCCESS,
                student.getName().fullName, nextMinute);
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setPerson(student, student.withLesson(Optional.of(nextMinute)));

        assertCommandSuccess(new ScheduleCommand(INDEX_FIRST_PERSON, nextMinute, CLOCK), model,
                expectedMessage, expectedModel);
    }

    @Test
    public void execute_lessonInCurrentMinute_throwsCommandException() {
        assertCommandFailure(new ScheduleCommand(INDEX_FIRST_PERSON, new Lesson("2026-10-22 16:00"), CLOCK),
                model, ScheduleCommand.MESSAGE_LESSON_NOT_IN_FUTURE);
    }

    @Test
    public void execute_pastLesson_throwsCommandException() {
        assertCommandFailure(new ScheduleCommand(INDEX_FIRST_PERSON, new Lesson("2026-10-21 16:00"), CLOCK),
                model, ScheduleCommand.MESSAGE_LESSON_NOT_IN_FUTURE);
    }

    @Test
    public void execute_pastLessonWithExistingLesson_keepsExistingLesson() {
        Person student = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        model.setPerson(student, student.withLesson(Optional.of(FUTURE_LESSON)));

        assertCommandFailure(new ScheduleCommand(INDEX_FIRST_PERSON, new Lesson("2026-10-01 16:00"), CLOCK),
                model, ScheduleCommand.MESSAGE_LESSON_NOT_IN_FUTURE);
        assertEquals(Optional.of(FUTURE_LESSON),
                model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased()).getLesson());
    }

    @Test
    public void execute_laterClock_rejectsLessonThatHasStarted() {
        Clock laterClock = Clock.offset(CLOCK, Duration.ofDays(8));

        assertCommandFailure(new ScheduleCommand(INDEX_FIRST_PERSON, FUTURE_LESSON, laterClock),
                model, ScheduleCommand.MESSAGE_LESSON_NOT_IN_FUTURE);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);

        assertCommandFailure(new ScheduleCommand(outOfBoundIndex, FUTURE_LESSON, CLOCK), model,
                Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validIndexFilteredList_schedulesDisplayedStudent() {
        showPersonAtIndex(model, INDEX_SECOND_PERSON);
        Person student = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());

        String expectedMessage = String.format(ScheduleCommand.MESSAGE_SCHEDULE_SUCCESS,
                student.getName().fullName, FUTURE_LESSON);
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        showPersonAtIndex(expectedModel, INDEX_SECOND_PERSON);
        expectedModel.setPerson(student, student.withLesson(Optional.of(FUTURE_LESSON)));

        assertCommandSuccess(new ScheduleCommand(INDEX_FIRST_PERSON, FUTURE_LESSON, CLOCK), model,
                expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexFilteredList_throwsCommandException() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        assertCommandFailure(new ScheduleCommand(INDEX_SECOND_PERSON, FUTURE_LESSON, CLOCK), model,
                Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_archivedView_throwsCommandException() {
        Person student = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        model.setPerson(student, student.withArchived(true));
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS, ResultViewScope.ARCHIVED);

        assertCommandFailure(new ScheduleCommand(INDEX_FIRST_PERSON, FUTURE_LESSON, CLOCK), model,
                ScheduleCommand.MESSAGE_REQUIRES_ACTIVE_VIEW);
    }

    @Test
    public void execute_rescheduleReordersUpcomingList_addsListUpdateNotice() throws Exception {
        showUpcomingFirstThenSecond();
        Person first = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Lesson laterLesson = new Lesson("2026-12-01 10:00");

        CommandResult result = new ScheduleCommand(INDEX_FIRST_PERSON, laterLesson, CLOCK).execute(model);

        assertEquals(String.format(ScheduleCommand.MESSAGE_RESCHEDULE_SUCCESS, first.getName().fullName,
                laterLesson, FUTURE_LESSON) + ". " + Messages.MESSAGE_DISPLAYED_LIST_UPDATED,
                result.getFeedbackToUser());
        assertEquals(first.getName(), model.getFilteredPersonList().get(INDEX_SECOND_PERSON.getZeroBased()).getName());
    }

    @Test
    public void execute_rescheduleKeepsUpcomingOrder_noListUpdateNotice() throws Exception {
        showUpcomingFirstThenSecond();
        Person first = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Lesson stillSoonest = new Lesson("2026-10-31 10:00");

        CommandResult result = new ScheduleCommand(INDEX_FIRST_PERSON, stillSoonest, CLOCK).execute(model);

        assertEquals(String.format(ScheduleCommand.MESSAGE_RESCHEDULE_SUCCESS, first.getName().fullName,
                stillSoonest, FUTURE_LESSON), result.getFeedbackToUser());
    }

    /** Schedules the first two typical students and shows the upcoming list, in which they keep that order. */
    private void showUpcomingFirstThenSecond() throws Exception {
        new ScheduleCommand(INDEX_FIRST_PERSON, FUTURE_LESSON, CLOCK).execute(model);
        new ScheduleCommand(INDEX_SECOND_PERSON, OTHER_FUTURE_LESSON, CLOCK).execute(model);
        new UpcomingCommand(CLOCK).execute(model);
    }

    @Test
    public void execute_archiveThenRestoreScheduledStudent_keepsLesson() throws Exception {
        new ScheduleCommand(INDEX_FIRST_PERSON, FUTURE_LESSON, CLOCK).execute(model);
        Person scheduledStudent = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());

        new ArchiveCommand(INDEX_FIRST_PERSON).execute(model);
        ListCommand.archived().execute(model);
        Person archivedStudent = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        assertEquals(scheduledStudent.withArchived(true), archivedStudent);
        assertEquals(Optional.of(FUTURE_LESSON), archivedStudent.getLesson());

        new UnarchiveCommand(INDEX_FIRST_PERSON).execute(model);
        new ListCommand().execute(model);
        assertTrue(model.getFilteredPersonList().contains(scheduledStudent));
    }

    @Test
    public void equals() {
        ScheduleCommand scheduleFirst = new ScheduleCommand(INDEX_FIRST_PERSON, FUTURE_LESSON, CLOCK);

        // same object -> returns true
        assertTrue(scheduleFirst.equals(scheduleFirst));

        // same values -> returns true
        assertTrue(scheduleFirst.equals(new ScheduleCommand(INDEX_FIRST_PERSON, FUTURE_LESSON)));

        // different types -> returns false
        assertFalse(scheduleFirst.equals(1));

        // null -> returns false
        assertFalse(scheduleFirst.equals(null));

        // different index -> returns false
        assertFalse(scheduleFirst.equals(new ScheduleCommand(INDEX_SECOND_PERSON, FUTURE_LESSON, CLOCK)));

        // different lesson -> returns false
        assertFalse(scheduleFirst.equals(new ScheduleCommand(INDEX_FIRST_PERSON, OTHER_FUTURE_LESSON, CLOCK)));
    }

    @Test
    public void toStringMethod() {
        ScheduleCommand command = new ScheduleCommand(INDEX_FIRST_PERSON, FUTURE_LESSON, CLOCK);
        String expected = ScheduleCommand.class.getCanonicalName() + "{targetIndex=" + INDEX_FIRST_PERSON
                + ", lesson=" + FUTURE_LESSON + "}";
        assertEquals(expected, command.toString());
    }
}
