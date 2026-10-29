package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Lesson;
import seedu.address.model.person.NameContainsKeywordsPredicate;
import seedu.address.model.person.Person;
import seedu.address.model.person.PersonMatchesFiltersPredicate;
import seedu.address.model.person.Subject;
import seedu.address.testutil.EditPersonDescriptorBuilder;
import seedu.address.testutil.PersonBuilder;

/** Cross-feature tests for commands that act on the currently displayed active result view. */
public class QueryResultViewTest {
    private static final ZoneId ZONE = ZoneId.of("Asia/Singapore");
    private static final Clock CLOCK = Clock.fixed(
            LocalDateTime.of(2026, 10, 22, 16, 0).atZone(ZONE).toInstant(), ZONE);
    private static final Lesson FUTURE_LESSON = new Lesson("2099-10-30 16:00");
    private static final Lesson LATER_LESSON = new Lesson("2099-11-02 09:30");
    private static final Person BOB = new PersonBuilder().withName("Bob")
            .withSubject("English").build();
    private static final Person ALICE = new PersonBuilder().withName("Alice")
            .withSubject("Mathematics").build();
    private static final Person CARLA = new PersonBuilder().withName("Carla")
            .withSubject("Mathematics").build();

    private Model model;

    @BeforeEach
    public void setUp() {
        AddressBook addressBook = new AddressBook();
        List.of(BOB, ALICE, CARLA).forEach(addressBook::addPerson);
        model = new ModelManager(addressBook, new UserPrefs());
    }

    @Test
    public void execute_editAfterFind_targetsDisplayedIndexAndKeepsQuery() throws Exception {
        new FindCommand(new NameContainsKeywordsPredicate(List.of("ali")))
                .execute(model);
        assertEquals(List.of(ALICE), model.getFilteredPersonList());

        new EditCommand(INDEX_FIRST_PERSON,
                new EditPersonDescriptorBuilder().withPhone("91234567").build()).execute(model);

        Person editedAlice = new PersonBuilder(ALICE).withPhone("91234567").build();
        assertEquals(List.of(BOB, editedAlice, CARLA), model.getAddressBook().getPersonList());
        assertEquals(List.of(editedAlice), model.getFilteredPersonList());
    }

    @Test
    public void execute_editAfterFilterStudentStillMatches_keepsQueryWithoutNotice() throws Exception {
        new FilterCommand(new PersonMatchesFiltersPredicate(Optional.of(new Subject("Mathematics")),
                Optional.empty())).execute(model);
        assertEquals(List.of(ALICE, CARLA), model.getFilteredPersonList());

        CommandResult result = new EditCommand(INDEX_FIRST_PERSON,
                new EditPersonDescriptorBuilder().withLearningFocus("Differentiation").build()).execute(model);

        Person editedAlice = new PersonBuilder(ALICE).withLearningFocus("Differentiation").build();
        assertEquals(List.of(editedAlice, CARLA), model.getFilteredPersonList());
        assertFalse(result.getFeedbackToUser().contains(Messages.MESSAGE_DISPLAYED_LIST_UPDATED));
    }

    @Test
    public void execute_unchangedEditAfterFilter_keepsQuery() throws Exception {
        new FilterCommand(new PersonMatchesFiltersPredicate(Optional.of(new Subject("Mathematics")),
                Optional.empty())).execute(model);

        CommandResult result = new EditCommand(INDEX_FIRST_PERSON,
                new EditPersonDescriptorBuilder().withSubject("Mathematics").build()).execute(model);

        assertEquals(String.format(EditCommand.MESSAGE_NO_CHANGES, ALICE.getName()), result.getFeedbackToUser());
        assertEquals(List.of(ALICE, CARLA), model.getFilteredPersonList());
    }

    @Test
    public void execute_editAfterFilterStudentNoLongerMatches_removesStudentWithNotice() throws Exception {
        new FilterCommand(new PersonMatchesFiltersPredicate(Optional.of(new Subject("Mathematics")),
                Optional.empty())).execute(model);

        CommandResult result = new EditCommand(INDEX_FIRST_PERSON,
                new EditPersonDescriptorBuilder().withSubject("English").build()).execute(model);

        Person editedAlice = new PersonBuilder(ALICE).withSubject("English").build();
        assertEquals(List.of(BOB, editedAlice, CARLA), model.getAddressBook().getPersonList());
        assertEquals(List.of(CARLA), model.getFilteredPersonList());
        assertTrue(result.getFeedbackToUser().endsWith(Messages.MESSAGE_DISPLAYED_LIST_UPDATED));
    }

    @Test
    public void execute_deleteAfterFilter_targetsDisplayedIndex() throws Exception {
        PersonMatchesFiltersPredicate mathematics = new PersonMatchesFiltersPredicate(
                Optional.of(new Subject("mathematics")), Optional.empty());
        new FilterCommand(mathematics).execute(model);
        assertEquals(List.of(ALICE, CARLA), model.getFilteredPersonList());

        new DeleteCommand(INDEX_SECOND_PERSON).execute(model);

        assertEquals(List.of(BOB, ALICE), model.getAddressBook().getPersonList());
    }

    @Test
    public void execute_archiveAfterFilter_targetsDisplayedIndex() throws Exception {
        PersonMatchesFiltersPredicate mathematics = new PersonMatchesFiltersPredicate(
                Optional.of(new Subject("mathematics")), Optional.empty());
        new FilterCommand(mathematics).execute(model);
        assertEquals(List.of(ALICE, CARLA), model.getFilteredPersonList());

        new ArchiveCommand(INDEX_SECOND_PERSON).execute(model);

        Person archivedCarla = CARLA.withArchived(true);
        assertEquals(List.of(BOB, ALICE, archivedCarla), model.getAddressBook().getPersonList());
        assertEquals(List.of(ALICE), model.getFilteredPersonList());
    }

    @Test
    public void execute_deleteFromArchivedView_targetsDisplayedIndex() throws Exception {
        Person archivedBob = BOB.withArchived(true);
        Person archivedCarla = CARLA.withArchived(true);
        model.setPerson(BOB, archivedBob);
        model.setPerson(CARLA, archivedCarla);
        ListCommand.archived().execute(model);
        assertEquals(List.of(archivedBob, archivedCarla), model.getFilteredPersonList());

        new DeleteCommand(INDEX_SECOND_PERSON).execute(model);

        assertEquals(List.of(archivedBob, ALICE), model.getAddressBook().getPersonList());
        assertEquals(List.of(archivedBob), model.getFilteredPersonList());
    }

    @Test
    public void execute_scheduleAfterFind_targetsDisplayedIndex() throws Exception {
        new FindCommand(new NameContainsKeywordsPredicate(List.of("car")))
                .execute(model);
        assertEquals(List.of(CARLA), model.getFilteredPersonList());

        new ScheduleCommand(INDEX_FIRST_PERSON, FUTURE_LESSON, CLOCK).execute(model);

        Person scheduledCarla = CARLA.withLesson(Optional.of(FUTURE_LESSON));
        assertEquals(List.of(BOB, ALICE, scheduledCarla), model.getAddressBook().getPersonList());
        assertEquals(List.of(scheduledCarla), model.getFilteredPersonList());
    }

    @Test
    public void execute_unscheduleAfterFilter_targetsDisplayedIndex() throws Exception {
        Person scheduledCarla = CARLA.withLesson(Optional.of(FUTURE_LESSON));
        model.setPerson(CARLA, scheduledCarla);
        PersonMatchesFiltersPredicate mathematics = new PersonMatchesFiltersPredicate(
                Optional.of(new Subject("mathematics")), Optional.empty());
        new FilterCommand(mathematics).execute(model);
        assertEquals(List.of(ALICE, scheduledCarla), model.getFilteredPersonList());

        new UnscheduleCommand(INDEX_SECOND_PERSON).execute(model);

        assertEquals(List.of(BOB, ALICE, CARLA), model.getAddressBook().getPersonList());
        assertEquals(List.of(ALICE, CARLA), model.getFilteredPersonList());
    }

    @Test
    public void execute_editAfterUpcoming_targetsDisplayedIndexAndKeepsUpcomingView() throws Exception {
        Person scheduledBob = BOB.withLesson(Optional.of(LATER_LESSON));
        Person scheduledAlice = ALICE.withLesson(Optional.of(FUTURE_LESSON));
        model.setPerson(BOB, scheduledBob);
        model.setPerson(ALICE, scheduledAlice);
        new UpcomingCommand(CLOCK).execute(model);
        assertEquals(List.of(scheduledAlice, scheduledBob), model.getFilteredPersonList());

        new EditCommand(INDEX_FIRST_PERSON,
                new EditPersonDescriptorBuilder().withPhone("91234567").build()).execute(model);

        Person editedAlice = new PersonBuilder(scheduledAlice).withPhone("91234567").build();
        assertEquals(List.of(scheduledBob, editedAlice, CARLA), model.getAddressBook().getPersonList());
        assertEquals(List.of(editedAlice, scheduledBob), model.getFilteredPersonList());
    }

    @Test
    public void execute_renameAfterUpcoming_reordersSameTimeLessonsWithNotice() throws Exception {
        Person scheduledBob = BOB.withLesson(Optional.of(FUTURE_LESSON));
        Person scheduledAlice = ALICE.withLesson(Optional.of(FUTURE_LESSON));
        model.setPerson(BOB, scheduledBob);
        model.setPerson(ALICE, scheduledAlice);
        new UpcomingCommand(CLOCK).execute(model);
        assertEquals(List.of(scheduledAlice, scheduledBob), model.getFilteredPersonList());

        CommandResult result = new EditCommand(INDEX_FIRST_PERSON,
                new EditPersonDescriptorBuilder().withName("Zara").build()).execute(model);

        Person renamedAlice = new PersonBuilder(scheduledAlice).withName("Zara").build();
        assertEquals(List.of(scheduledBob, renamedAlice), model.getFilteredPersonList());
        assertTrue(result.getFeedbackToUser().endsWith(Messages.MESSAGE_DISPLAYED_LIST_UPDATED));
    }

    @Test
    public void execute_upcomingAfterFind_replacesPreviousQueryAndOrdersLessons() throws Exception {
        Person scheduledBob = BOB.withLesson(Optional.of(LATER_LESSON));
        Person scheduledAlice = ALICE.withLesson(Optional.of(FUTURE_LESSON));
        model.setPerson(BOB, scheduledBob);
        model.setPerson(ALICE, scheduledAlice);
        new FindCommand(new NameContainsKeywordsPredicate(List.of("bob"))).execute(model);
        assertEquals(List.of(scheduledBob), model.getFilteredPersonList());

        new UpcomingCommand(CLOCK).execute(model);

        assertEquals(List.of(scheduledAlice, scheduledBob), model.getFilteredPersonList());
    }

    @Test
    public void execute_filterAfterUpcoming_restoresSavedOrderForFilteredStudents() throws Exception {
        Person scheduledBob = BOB.withLesson(Optional.of(LATER_LESSON));
        Person scheduledAlice = ALICE.withLesson(Optional.of(FUTURE_LESSON));
        model.setPerson(BOB, scheduledBob);
        model.setPerson(ALICE, scheduledAlice);
        new UpcomingCommand(CLOCK).execute(model);
        assertEquals(List.of(scheduledAlice, scheduledBob), model.getFilteredPersonList());

        PersonMatchesFiltersPredicate mathematics = new PersonMatchesFiltersPredicate(
                Optional.of(new Subject("mathematics")), Optional.empty());
        new FilterCommand(mathematics).execute(model);

        assertEquals(List.of(scheduledAlice, CARLA), model.getFilteredPersonList());
    }

    @Test
    public void execute_listAfterQuery_restoresCompleteActiveView() throws Exception {
        new FindCommand(new NameContainsKeywordsPredicate(List.of("ali")))
                .execute(model);

        new ListCommand().execute(model);

        assertEquals(List.of(BOB, ALICE, CARLA), model.getFilteredPersonList());
    }
}
