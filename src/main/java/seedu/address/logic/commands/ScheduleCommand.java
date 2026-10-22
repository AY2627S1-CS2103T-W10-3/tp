package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_LESSON;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Lesson;
import seedu.address.model.person.Person;

/**
 * Schedules the next lesson for a student identified by the displayed index, replacing any existing lesson.
 */
public class ScheduleCommand extends Command {

    public static final String COMMAND_WORD = "schedule";

    public static final String FORMAT = COMMAND_WORD + " INDEX " + PREFIX_LESSON + Lesson.FORMAT;

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Schedules the next lesson for the student identified by the index number used in the displayed "
            + "student list, replacing any existing lesson. The time uses this computer's local time.\n"
            + "Parameters: INDEX (must be a positive integer) " + PREFIX_LESSON + Lesson.FORMAT + "\n"
            + "Example: " + COMMAND_WORD + " 1 " + PREFIX_LESSON + "2099-10-30 16:00";

    public static final String MESSAGE_SCHEDULE_SUCCESS = "Scheduled lesson for %1$s: %2$s";
    public static final String MESSAGE_RESCHEDULE_SUCCESS = "Rescheduled lesson for %1$s: %2$s (previously %3$s)";
    public static final String MESSAGE_LESSON_NOT_IN_FUTURE = "Lesson time must be in the future.";

    private static final Logger logger = LogsCenter.getLogger(ScheduleCommand.class);

    private final Index targetIndex;
    private final Lesson lesson;
    private final Clock clock;

    /**
     * Creates a ScheduleCommand that compares the lesson against the computer's current local time.
     */
    public ScheduleCommand(Index targetIndex, Lesson lesson) {
        this(targetIndex, lesson, Clock.systemDefaultZone());
    }

    /**
     * Creates a ScheduleCommand that compares the lesson against the time given by {@code clock}.
     */
    public ScheduleCommand(Index targetIndex, Lesson lesson, Clock clock) {
        requireNonNull(targetIndex);
        requireNonNull(lesson);
        requireNonNull(clock);
        this.targetIndex = targetIndex;
        this.lesson = lesson;
        this.clock = clock;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }
        if (!lesson.isAfter(LocalDateTime.now(clock))) {
            throw new CommandException(MESSAGE_LESSON_NOT_IN_FUTURE);
        }

        Person student = lastShownList.get(targetIndex.getZeroBased());
        Optional<Lesson> previousLesson = student.getLesson();
        Person scheduledStudent = student.withLesson(Optional.of(lesson));
        model.setPerson(student, scheduledStudent);
        assert scheduledStudent.getLesson().equals(Optional.of(lesson));
        logger.fine("Scheduled lesson " + lesson + " for " + student.getName());

        String studentName = student.getName().fullName;
        if (previousLesson.isPresent()) {
            return new CommandResult(String.format(MESSAGE_RESCHEDULE_SUCCESS, studentName, lesson,
                    previousLesson.get()));
        }
        return new CommandResult(String.format(MESSAGE_SCHEDULE_SUCCESS, studentName, lesson));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ScheduleCommand otherScheduleCommand)) {
            return false;
        }

        return targetIndex.equals(otherScheduleCommand.targetIndex)
                && lesson.equals(otherScheduleCommand.lesson);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndex", targetIndex)
                .add("lesson", lesson)
                .toString();
    }
}
