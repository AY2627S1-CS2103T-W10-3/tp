package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import java.time.Clock;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Lesson;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

public class UpcomingCommandTest {

    private static final ZoneId ZONE = ZoneId.of("Asia/Singapore");
    /** The fixed current time used by these tests: 2026-10-22 16:00:30 local time. */
    private static final Clock CLOCK = Clock.fixed(
            LocalDateTime.of(2026, 10, 22, 16, 0, 30).atZone(ZONE).toInstant(), ZONE);

    private static final Person PAST = new PersonBuilder().withName("Past Student")
            .withLesson("2026-10-21 10:00").build();
    private static final Person CURRENT_MINUTE = new PersonBuilder().withName("Current Student")
            .withLesson("2026-10-22 16:00").build();
    private static final Person LATER = new PersonBuilder().withName("Later Student")
            .withLesson("2026-11-05 09:00").build();
    private static final Person SOONEST = new PersonBuilder().withName("Soonest Student")
            .withLesson("2026-10-22 16:01").build();
    private static final Person TIE_ZED = new PersonBuilder().withName("zed Tan")
            .withLesson("2026-10-30 16:00").build();
    private static final Person TIE_AMY = new PersonBuilder().withName("Amy Lim")
            .withLesson("2026-10-30 16:00").build();
    private static final Person NO_LESSON = new PersonBuilder().withName("Unscheduled Student").build();

    private Model model;

    @BeforeEach
    public void setUp() {
        AddressBook addressBook = new AddressBook();
        List.of(PAST, CURRENT_MINUTE, LATER, SOONEST, TIE_ZED, TIE_AMY, NO_LESSON).forEach(addressBook::addPerson);
        model = new ModelManager(addressBook, new UserPrefs());
    }

    @Test
    public void execute_mixedLessons_listsOnlyFutureLessonsInTimeThenNameOrder() {
        CommandResult result = new UpcomingCommand(CLOCK).execute(model);

        assertEquals(String.format(UpcomingCommand.MESSAGE_SUCCESS, 4), result.getFeedbackToUser());
        assertEquals(List.of(SOONEST, TIE_AMY, TIE_ZED, LATER), model.getFilteredPersonList());
    }

    @Test
    public void execute_sameTimeDifferentCase_ordersNamesCaseInsensitively() {
        Person lowerCaseBob = new PersonBuilder().withName("bob").withLesson("2026-10-30 16:00").build();
        model.addPerson(lowerCaseBob);

        new UpcomingCommand(CLOCK).execute(model);

        assertEquals(List.of(SOONEST, TIE_AMY, lowerCaseBob, TIE_ZED, LATER), model.getFilteredPersonList());
    }

    @Test
    public void execute_clockAdvances_excludesLessonsThatHaveStarted() {
        Clock eightDaysLater = Clock.offset(CLOCK, Duration.ofDays(8));

        new UpcomingCommand(eightDaysLater).execute(model);

        assertEquals(List.of(LATER), model.getFilteredPersonList());
    }

    @Test
    public void execute_noUpcomingLessons_showsClearFeedback() {
        AddressBook addressBook = new AddressBook();
        List.of(PAST, CURRENT_MINUTE, NO_LESSON).forEach(addressBook::addPerson);
        Model modelWithoutUpcoming = new ModelManager(addressBook, new UserPrefs());

        CommandResult result = new UpcomingCommand(CLOCK).execute(modelWithoutUpcoming);

        assertEquals(UpcomingCommand.MESSAGE_NO_UPCOMING_LESSONS, result.getFeedbackToUser());
        assertTrue(modelWithoutUpcoming.getFilteredPersonList().isEmpty());
    }

    @Test
    public void execute_upcoming_doesNotModifyRecords() {
        AddressBook before = new AddressBook(model.getAddressBook());

        new UpcomingCommand(CLOCK).execute(model);

        assertEquals(before, model.getAddressBook());
    }

    @Test
    public void execute_listAfterUpcoming_restoresSavedOrder() {
        new UpcomingCommand(CLOCK).execute(model);
        new ListCommand().execute(model);

        assertEquals(List.of(PAST, CURRENT_MINUTE, LATER, SOONEST, TIE_ZED, TIE_AMY, NO_LESSON),
                model.getFilteredPersonList());
    }

    @Test
    public void execute_indexCommandsAfterUpcoming_targetDisplayedStudent() throws CommandException {
        new UpcomingCommand(CLOCK).execute(model);

        new UnscheduleCommand(INDEX_FIRST_PERSON).execute(model);

        assertEquals(Optional.empty(), model.getAddressBook().getPersonList().stream()
                .filter(person -> person.isSamePerson(SOONEST)).findFirst().orElseThrow().getLesson());
        assertEquals(List.of(TIE_AMY, TIE_ZED, LATER), model.getFilteredPersonList());
    }

    @Test
    public void execute_rescheduleAfterUpcoming_reordersDisplayedList() throws CommandException {
        new UpcomingCommand(CLOCK).execute(model);

        new ScheduleCommand(INDEX_FIRST_PERSON, new Lesson("2026-12-01 10:00"), CLOCK).execute(model);

        Person rescheduled = SOONEST.withLesson(Optional.of(new Lesson("2026-12-01 10:00")));
        assertEquals(List.of(TIE_AMY, TIE_ZED, LATER, rescheduled), model.getFilteredPersonList());
    }

    @Test
    public void execute_deleteAfterUpcoming_deletesDisplayedStudent() throws CommandException {
        new UpcomingCommand(CLOCK).execute(model);

        new DeleteCommand(INDEX_FIRST_PERSON).execute(model);

        assertFalse(model.hasPerson(SOONEST));
        assertTrue(model.hasPerson(PAST));
    }

    @Test
    public void equals() {
        UpcomingCommand upcomingCommand = new UpcomingCommand(CLOCK);

        assertTrue(upcomingCommand.equals(upcomingCommand));
        assertTrue(upcomingCommand.equals(new UpcomingCommand()));
        assertFalse(upcomingCommand.equals(null));
        assertFalse(upcomingCommand.equals(new ListCommand()));
    }
}
