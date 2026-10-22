package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EDUCATION_LEVEL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GUARDIAN_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GUARDIAN_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GUARDIAN_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_LEARNING_FOCUS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NEXT_ACTION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SUBJECT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Adds a person to the address book.
 */
public class AddCommand extends Command {

    public static final String COMMAND_WORD = "add";

    /** Canonical add syntax shared by usage errors and offline help. */
    public static final String FORMAT = COMMAND_WORD + " "
            + PREFIX_NAME + "NAME "
            + "[" + PREFIX_PHONE + "PHONE] "
            + "[" + PREFIX_EMAIL + "EMAIL] "
            + "[" + PREFIX_ADDRESS + "ADDRESS] "
            + "[" + PREFIX_SUBJECT + "SUBJECT] "
            + "[" + PREFIX_EDUCATION_LEVEL + "EDUCATION_LEVEL] "
            + "[" + PREFIX_LEARNING_FOCUS + "LEARNING_FOCUS] "
            + "[" + PREFIX_NEXT_ACTION + "NEXT_ACTION] "
            + "[" + PREFIX_GUARDIAN_NAME + "GUARDIAN_NAME] "
            + "[" + PREFIX_GUARDIAN_PHONE + "GUARDIAN_PHONE] "
            + "[" + PREFIX_GUARDIAN_EMAIL + "GUARDIAN_EMAIL] "
            + "[" + PREFIX_TAG + "TAG]";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a student to TutorContacts. "
            + "Parameters: " + FORMAT + "\n"
            + "Prefixes may appear in any order, but each can appear only once. "
            + "Guardian phone or email requires gn/.\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_NAME + "John Doe "
            + PREFIX_PHONE + "98765432 "
            + PREFIX_EMAIL + "johnd@example.com "
            + PREFIX_SUBJECT + "H2 Mathematics "
            + PREFIX_EDUCATION_LEVEL + "JC2 "
            + PREFIX_LEARNING_FOCUS + "Algebraic equations "
            + PREFIX_NEXT_ACTION + "Complete worksheet 2 "
            + PREFIX_GUARDIAN_NAME + "Jane Doe "
            + PREFIX_GUARDIAN_PHONE + "91234567 "
            + PREFIX_ADDRESS + "311, Clementi Ave 2, #02-25 "
            + PREFIX_TAG + "friends";

    public static final String MESSAGE_SUCCESS = "New person added: %1$s";
    public static final String MESSAGE_DUPLICATE_PERSON = "This student already exists in TutorContacts.";

    private final Person toAdd;

    /**
     * Creates an AddCommand to add the specified {@code Person}
     */
    public AddCommand(Person person) {
        requireNonNull(person);
        toAdd = person;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (model.hasPerson(toAdd)) {
            if (hasArchivedDuplicate(model, toAdd)) {
                throw new CommandException(Messages.MESSAGE_DUPLICATE_ARCHIVED_PERSON);
            }
            throw new CommandException(MESSAGE_DUPLICATE_PERSON);
        }

        model.addPerson(toAdd);
        return new CommandResult(String.format(MESSAGE_SUCCESS, Messages.format(toAdd)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AddCommand otherAddCommand)) {
            return false;
        }

        return toAdd.equals(otherAddCommand.toAdd);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("toAdd", toAdd)
                .toString();
    }
}
