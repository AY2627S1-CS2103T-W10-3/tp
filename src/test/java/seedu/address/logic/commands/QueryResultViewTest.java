package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.NameContainsKeywordsPredicate;
import seedu.address.model.person.Person;
import seedu.address.model.person.PersonMatchesFiltersPredicate;
import seedu.address.model.person.Subject;
import seedu.address.testutil.EditPersonDescriptorBuilder;
import seedu.address.testutil.PersonBuilder;

/** Cross-feature tests for commands that act on the currently displayed active result view. */
public class QueryResultViewTest {
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
    public void execute_editAfterFind_targetsDisplayedIndex() throws Exception {
        new FindCommand(new NameContainsKeywordsPredicate(List.of("ali")))
                .execute(model);
        assertEquals(List.of(ALICE), model.getFilteredPersonList());

        new EditCommand(INDEX_FIRST_PERSON,
                new EditPersonDescriptorBuilder().withPhone("91234567").build()).execute(model);

        Person editedAlice = new PersonBuilder(ALICE).withPhone("91234567").build();
        assertEquals(List.of(BOB, editedAlice, CARLA), model.getAddressBook().getPersonList());
        assertEquals(List.of(BOB, editedAlice, CARLA), model.getFilteredPersonList());
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
    public void execute_listAfterQuery_restoresCompleteActiveView() throws Exception {
        new FindCommand(new NameContainsKeywordsPredicate(List.of("ali")))
                .execute(model);

        new ListCommand().execute(model);

        assertEquals(List.of(BOB, ALICE, CARLA), model.getFilteredPersonList());
    }
}
