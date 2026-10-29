package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import seedu.address.model.Model;
import seedu.address.model.ResultViewScope;

/**
 * Lists all active students, or all archived students, in saved order.
 */
public class ListCommand extends Command {

    public static final String COMMAND_WORD = "list";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Lists active students, or archived students when followed by `archived`.\n"
            + "Parameters: [archived]\n"
            + "Example: " + COMMAND_WORD + " archived";
    public static final String MESSAGE_SUCCESS = "Listed all active students.";
    public static final String MESSAGE_NO_STUDENTS =
            "No students recorded. Use `add` to create your first student.";
    public static final String MESSAGE_NO_ACTIVE_STUDENTS =
            "No active students. Use `list archived` to see archived students.";
    public static final String MESSAGE_ARCHIVED_SUCCESS = "Listed archived students.";
    public static final String MESSAGE_NO_ARCHIVED_STUDENTS =
            "No archived students. Use `archive INDEX` from an active view to archive a student.";

    private final ResultViewScope scope;

    public ListCommand() {
        this(ResultViewScope.ACTIVE);
    }

    private ListCommand(ResultViewScope scope) {
        this.scope = scope;
    }

    /** Returns a command that lists archived students. */
    public static ListCommand archived() {
        return new ListCommand(ResultViewScope.ARCHIVED);
    }


    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS, scope);
        if (scope == ResultViewScope.ARCHIVED) {
            return new CommandResult(model.getFilteredPersonList().isEmpty()
                    ? MESSAGE_NO_ARCHIVED_STUDENTS : MESSAGE_ARCHIVED_SUCCESS);
        }
        if (model.getFilteredPersonList().isEmpty()) {
            return new CommandResult(model.getAddressBook().getPersonList().isEmpty()
                    ? MESSAGE_NO_STUDENTS : MESSAGE_NO_ACTIVE_STUDENTS);
        }
        return new CommandResult(MESSAGE_SUCCESS);
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || other instanceof ListCommand otherCommand && scope == otherCommand.scope;
    }
}
