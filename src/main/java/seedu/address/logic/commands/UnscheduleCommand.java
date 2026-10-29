package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ResultViewScope;
import seedu.address.model.person.Lesson;
import seedu.address.model.person.Person;

/**
 * Clears the scheduled lesson of a student identified by the displayed index.
 */
public class UnscheduleCommand extends Command {

    public static final String COMMAND_WORD = "unschedule";

    public static final String FORMAT = COMMAND_WORD + " INDEX";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Clears the scheduled lesson of the student identified by the index number used in the displayed "
            + "student list.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_UNSCHEDULE_SUCCESS = "Cleared lesson for %1$s (was %2$s)";
    public static final String MESSAGE_NO_LESSON = "%1$s has no scheduled lesson.";

    public static final String MESSAGE_REQUIRES_ACTIVE_VIEW =
            "Unschedule can only be used from an active student view. Run `list` first.";

    private static final Logger logger = LogsCenter.getLogger(UnscheduleCommand.class);

    private final Index targetIndex;

    /**
     * Creates an UnscheduleCommand that clears the lesson of the student at {@code targetIndex}.
     */
    public UnscheduleCommand(Index targetIndex) {
        requireNonNull(targetIndex);
        this.targetIndex = targetIndex;
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

        List<Person> shownBefore = List.copyOf(lastShownList);
        Person student = lastShownList.get(targetIndex.getZeroBased());
        String studentName = student.getName().fullName;
        Optional<Lesson> previousLesson = student.getLesson();
        if (previousLesson.isEmpty()) {
            throw new CommandException(String.format(MESSAGE_NO_LESSON, studentName));
        }

        Person unscheduledStudent = student.withLesson(Optional.empty());
        model.setPerson(student, unscheduledStudent);
        assert unscheduledStudent.getLesson().isEmpty();
        logger.fine("Cleared lesson " + previousLesson.get() + " for " + student.getName());
        String feedback = String.format(MESSAGE_UNSCHEDULE_SUCCESS, studentName, previousLesson.get());
        return new CommandResult(withListUpdateNotice(feedback, shownBefore, targetIndex.getZeroBased(),
                unscheduledStudent, model.getFilteredPersonList()));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof UnscheduleCommand otherUnscheduleCommand)) {
            return false;
        }

        return targetIndex.equals(otherUnscheduleCommand.targetIndex);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndex", targetIndex)
                .toString();
    }
}
