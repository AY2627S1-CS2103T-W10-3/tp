package seedu.address.logic.commands;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Represents a command with hidden internal logic and the ability to be executed.
 */
public abstract class Command {

    /** Returns whether an archived student has the same identity as {@code person}. */
    protected static boolean hasArchivedDuplicate(Model model, Person person) {
        return model.getAddressBook().getPersonList().stream()
                .anyMatch(existing -> existing.isArchived() && existing.isSamePerson(person));
    }

    /**
     * Executes the command and returns the result message.
     *
     * @param model {@code Model} which the command should operate on.
     * @return feedback message of the operation result for display
     * @throws CommandException If an error occurs during command execution.
     */
    public abstract CommandResult execute(Model model) throws CommandException;

}
