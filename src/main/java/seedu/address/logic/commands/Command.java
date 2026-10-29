package seedu.address.logic.commands;

import java.util.ArrayList;
import java.util.List;

import seedu.address.logic.Messages;
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
     * Returns whether replacing the student at {@code targetIndex} of {@code shownBefore} with {@code replacement}
     * changed the displayed list beyond that row, for example by re-sorting it or removing the student from it.
     * Other students may then be shown at different indices.
     */
    private static boolean hasDisplayedListChanged(List<Person> shownBefore, int targetIndex, Person replacement,
            List<Person> shownAfter) {
        List<Person> unchangedOrder = new ArrayList<>(shownBefore);
        unchangedOrder.set(targetIndex, replacement);
        return !unchangedOrder.equals(shownAfter);
    }

    /**
     * Returns {@code feedback} followed by a notice that the displayed list was updated, if
     * {@link #hasDisplayedListChanged} is true for the given lists.
     */
    protected static String withListUpdateNotice(String feedback, List<Person> shownBefore, int targetIndex,
            Person replacement, List<Person> shownAfter) {
        if (!hasDisplayedListChanged(shownBefore, targetIndex, replacement, shownAfter)) {
            return feedback;
        }
        return feedback + ". " + Messages.MESSAGE_DISPLAYED_LIST_UPDATED;
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
