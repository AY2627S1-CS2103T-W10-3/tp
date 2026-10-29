package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_REMARK;

import java.util.List;
import java.util.Optional;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ResultViewScope;
import seedu.address.model.person.Person;
import seedu.address.model.person.Remark;

/**
 * Adds, replaces or removes the remark of a student identified by the displayed index.
 */
public class RemarkCommand extends Command {

    public static final String COMMAND_WORD = "remark";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Edits the remark of the student identified by the index number used in the displayed "
            + "student list, replacing any existing remark. Leave the remark empty to remove it.\n"
            + "Parameters: INDEX (must be a positive integer) " + PREFIX_REMARK + "[REMARK]\n"
            + "Example: " + COMMAND_WORD + " 1 " + PREFIX_REMARK + "Prefers evening lessons.";

    public static final String MESSAGE_ADD_REMARK_SUCCESS = "Added remark to student: %1$s";
    public static final String MESSAGE_DELETE_REMARK_SUCCESS = "Removed remark from student: %1$s";
    public static final String MESSAGE_REQUIRES_ACTIVE_VIEW =
            "Remark can only be used from an active student view. Run `list` first.";

    private final Index targetIndex;
    private final Optional<Remark> remark;

    /**
     * Creates a RemarkCommand that sets the remark of the student at {@code targetIndex},
     * or removes it when {@code remark} is empty.
     */
    public RemarkCommand(Index targetIndex, Optional<Remark> remark) {
        requireNonNull(targetIndex);
        requireNonNull(remark);
        this.targetIndex = targetIndex;
        this.remark = remark;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        if (model.getResultViewScope() != ResultViewScope.ACTIVE) {
            throw new CommandException(MESSAGE_REQUIRES_ACTIVE_VIEW);
        }
        List<Person> lastShownList = model.getFilteredPersonList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person student = lastShownList.get(targetIndex.getZeroBased());
        Person remarkedStudent = student.withRemark(remark);
        model.setPerson(student, remarkedStudent);

        String message = remark.isPresent() ? MESSAGE_ADD_REMARK_SUCCESS : MESSAGE_DELETE_REMARK_SUCCESS;
        return new CommandResult(String.format(message, Messages.format(remarkedStudent)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof RemarkCommand otherRemarkCommand)) {
            return false;
        }

        return targetIndex.equals(otherRemarkCommand.targetIndex)
                && remark.equals(otherRemarkCommand.remark);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndex", targetIndex)
                .add("remark", remark)
                .toString();
    }
}
