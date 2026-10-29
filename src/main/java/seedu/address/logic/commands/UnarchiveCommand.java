package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ResultViewScope;
import seedu.address.model.person.Person;

/** Restores an archived student identified by the displayed archived-view index. */
public class UnarchiveCommand extends Command {

    public static final String COMMAND_WORD = "unarchive";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Restores the student identified by the index in the archived student list.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: list archived\n" + COMMAND_WORD + " 1";
    public static final String MESSAGE_UNARCHIVE_PERSON_SUCCESS = "Restored student: %1$s";
    public static final String MESSAGE_REQUIRES_ARCHIVED_VIEW =
            "Unarchive can only be used from the archived student view. Run `list archived` first.";

    private final Index targetIndex;

    public UnarchiveCommand(Index targetIndex) {
        this.targetIndex = requireNonNull(targetIndex);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        if (model.getResultViewScope() != ResultViewScope.ARCHIVED) {
            throw new CommandException(MESSAGE_REQUIRES_ARCHIVED_VIEW);
        }

        List<Person> displayedStudents = model.getFilteredPersonList();
        if (targetIndex.getZeroBased() >= displayedStudents.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person student = displayedStudents.get(targetIndex.getZeroBased());
        model.setPerson(student, student.withArchived(false));
        return new CommandResult(String.format(MESSAGE_UNARCHIVE_PERSON_SUCCESS, student.getName().fullName));
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || other instanceof UnarchiveCommand otherCommand
                && targetIndex.equals(otherCommand.targetIndex);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("targetIndex", targetIndex).toString();
    }
}
