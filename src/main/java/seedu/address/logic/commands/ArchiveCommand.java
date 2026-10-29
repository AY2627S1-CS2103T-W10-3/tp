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

/** Archives a student identified by the displayed active-view index. */
public class ArchiveCommand extends Command {

    public static final String COMMAND_WORD = "archive";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Archives the student identified by the index in the displayed active student list.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";
    public static final String MESSAGE_ARCHIVE_PERSON_SUCCESS = "Archived student: %1$s";
    public static final String MESSAGE_REQUIRES_ACTIVE_VIEW =
            "Archive can only be used from an active student view. Run `list` first.";

    private final Index targetIndex;

    public ArchiveCommand(Index targetIndex) {
        this.targetIndex = requireNonNull(targetIndex);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        if (model.getResultViewScope() != ResultViewScope.ACTIVE) {
            throw new CommandException(MESSAGE_REQUIRES_ACTIVE_VIEW);
        }

        List<Person> displayedStudents = model.getFilteredPersonList();
        if (targetIndex.getZeroBased() >= displayedStudents.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person student = displayedStudents.get(targetIndex.getZeroBased());
        model.setPerson(student, student.withArchived(true));
        return new CommandResult(String.format(MESSAGE_ARCHIVE_PERSON_SUCCESS, student.getName().fullName));
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || other instanceof ArchiveCommand otherCommand
                && targetIndex.equals(otherCommand.targetIndex);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("targetIndex", targetIndex).toString();
    }
}
