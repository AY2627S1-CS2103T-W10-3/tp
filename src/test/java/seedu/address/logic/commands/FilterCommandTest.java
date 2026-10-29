package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.logic.Messages.MESSAGE_PERSONS_LISTED_OVERVIEW;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.ResultViewScope;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.EducationLevel;
import seedu.address.model.person.Person;
import seedu.address.model.person.PersonMatchesFiltersPredicate;
import seedu.address.model.person.Subject;
import seedu.address.testutil.PersonBuilder;

public class FilterCommandTest {
    private static final Person ALICE = new PersonBuilder().withName("Alice")
            .withSubject("H2 Mathematics").withEducationLevel("JC1").build();
    private static final Person BOB = new PersonBuilder().withName("Bob")
            .withSubject("H2 Mathematics").withEducationLevel("S3").build();
    private static final Person CARLA = new PersonBuilder().withName("Carla")
            .withSubject("English").withoutEducationLevel().build();
    private static final Person DORA = new PersonBuilder().withName("Dora")
            .withoutSubject().withEducationLevel("JC1").build();

    private Model model;

    @BeforeEach
    public void setUp() {
        AddressBook addressBook = new AddressBook();
        List.of(ALICE, BOB, CARLA, DORA).forEach(addressBook::addPerson);
        model = new ModelManager(addressBook, new UserPrefs());
    }

    @Test
    public void execute_subjectOnly_preservesOriginalOrder() {
        PersonMatchesFiltersPredicate predicate = predicate("h2 mathematics", null);
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.updateFilteredPersonList(predicate);

        assertCommandSuccess(new FilterCommand(predicate), model,
                String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 2), expectedModel);
        assertEquals(List.of(ALICE, BOB), model.getFilteredPersonList());
    }

    @Test
    public void execute_educationLevelOnly_excludesMissingValues() {
        PersonMatchesFiltersPredicate predicate = predicate(null, "jc1");
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.updateFilteredPersonList(predicate);

        assertCommandSuccess(new FilterCommand(predicate), model,
                String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 2), expectedModel);
        assertEquals(List.of(ALICE, DORA), model.getFilteredPersonList());
    }

    @Test
    public void execute_bothFilters_usesAnd() {
        PersonMatchesFiltersPredicate predicate = predicate("H2 MATHEMATICS", "s3");
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.updateFilteredPersonList(predicate);

        assertCommandSuccess(new FilterCommand(predicate), model,
                String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 1), expectedModel);
        assertEquals(List.of(BOB), model.getFilteredPersonList());
    }

    @Test
    public void execute_noMatches_showsClearFeedback() {
        PersonMatchesFiltersPredicate predicate = predicate("Physics", null);
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.updateFilteredPersonList(predicate);

        assertCommandSuccess(new FilterCommand(predicate), model,
                FilterCommand.MESSAGE_NO_MATCHES, expectedModel);
        assertEquals(List.of(), model.getFilteredPersonList());
    }

    @Test
    public void execute_matchingArchivedStudent_excludesArchivedStudent() {
        model.setPerson(ALICE, ALICE.withArchived(true));
        PersonMatchesFiltersPredicate predicate = predicate("H2 Mathematics", "JC1");
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.updateFilteredPersonList(predicate);

        assertCommandSuccess(new FilterCommand(predicate), model,
                FilterCommand.MESSAGE_NO_MATCHES, expectedModel);
        assertEquals(List.of(), model.getFilteredPersonList());
    }

    @Test
    public void execute_fromArchivedView_showsActiveMatchesOnly() {
        model.setPerson(BOB, BOB.withArchived(true));
        model.updateFilteredPersonList(person -> true, ResultViewScope.ARCHIVED);

        new FilterCommand(predicate("H2 Mathematics", null)).execute(model);

        assertEquals(ResultViewScope.ACTIVE, model.getResultViewScope());
        assertEquals(List.of(ALICE), model.getFilteredPersonList());
    }

    @Test
    public void execute_filterThenList_restoresAllStudents() {
        new FilterCommand(predicate("English", null)).execute(model);
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());

        assertCommandSuccess(new ListCommand(), model, ListCommand.MESSAGE_SUCCESS, expectedModel);
        assertEquals(List.of(ALICE, BOB, CARLA, DORA), model.getFilteredPersonList());
    }

    private static PersonMatchesFiltersPredicate predicate(String subject, String educationLevel) {
        Optional<Subject> optionalSubject = subject == null
                ? Optional.empty() : Optional.of(new Subject(subject));
        Optional<EducationLevel> optionalLevel = educationLevel == null
                ? Optional.empty() : Optional.of(new EducationLevel(educationLevel));
        return new PersonMatchesFiltersPredicate(optionalSubject, optionalLevel);
    }
}
