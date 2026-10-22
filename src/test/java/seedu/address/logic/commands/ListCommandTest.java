package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.ResultViewScope;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for ListCommand.
 */
public class ListCommandTest {

    private Model model;
    private Model expectedModel;

    @Test
    public void listSuccessMessage_identifiesStudents() {
        assertEquals("Listed all students.", ListCommand.MESSAGE_SUCCESS);
    }

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
    }

    @Test
    public void execute_listIsNotFiltered_showsSameList() {
        assertCommandSuccess(new ListCommand(), model, ListCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void execute_listIsFiltered_showsEverything() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);
        assertCommandSuccess(new ListCommand(), model, ListCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void execute_archivedStudents_showsArchivedOnly() {
        Person archived = new PersonBuilder(model.getFilteredPersonList().get(0)).withArchived(true).build();
        model.setPerson(model.getFilteredPersonList().get(0), archived);
        expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.updateFilteredPersonList(person -> true, ResultViewScope.ARCHIVED);

        assertCommandSuccess(ListCommand.archived(), model,
                ListCommand.MESSAGE_ARCHIVED_SUCCESS, expectedModel);
    }

    @Test
    public void execute_noArchivedStudents_showsActionableEmptyMessage() {
        expectedModel.updateFilteredPersonList(person -> true, ResultViewScope.ARCHIVED);

        assertCommandSuccess(ListCommand.archived(), model,
                ListCommand.MESSAGE_NO_ARCHIVED_STUDENTS, expectedModel);
    }

    @Test
    public void equals_sameScope_returnsTrue() {
        assertEquals(new ListCommand(), new ListCommand());
        assertEquals(ListCommand.archived(), ListCommand.archived());
    }
}
