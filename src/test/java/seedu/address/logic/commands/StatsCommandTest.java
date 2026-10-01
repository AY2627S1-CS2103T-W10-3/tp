package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;

/**
 * Contains integration tests (interaction with the Model) and unit tests for StatsCommand.
 */
public class StatsCommandTest {

    private Model model;
    private Model expectedModel;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
    }

    @Test
    public void execute_populatedAddressBook_showsCount() {
        int count = model.getFilteredPersonList().size();
        String expectedMessage = String.format(StatsCommand.MESSAGE_SUCCESS, count);
        assertCommandSuccess(new StatsCommand(), model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_emptyAddressBook_showsZero() {
        Model emptyModel = new ModelManager(new AddressBook(), new UserPrefs());
        Model expectedEmptyModel = new ModelManager(new AddressBook(), new UserPrefs());
        String expectedMessage = String.format(StatsCommand.MESSAGE_SUCCESS, 0);
        assertCommandSuccess(new StatsCommand(), emptyModel, expectedMessage, expectedEmptyModel);
    }

    @Test
    public void execute_filteredList_countsOnlyShownPersons() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);
        showPersonAtIndex(expectedModel, INDEX_FIRST_PERSON);
        String expectedMessage = String.format(StatsCommand.MESSAGE_SUCCESS, 1);
        assertCommandSuccess(new StatsCommand(), model, expectedMessage, expectedModel);
    }
}
