package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Locale;
import java.util.function.Predicate;

import seedu.address.model.Model;
import seedu.address.model.ResultViewScope;
import seedu.address.model.person.Lesson;
import seedu.address.model.person.Person;

/**
 * Lists students with lessons strictly in the future, ordered by lesson time and then by name.
 * The view is read-only and does not change any lesson.
 */
public class UpcomingCommand extends Command {

    public static final String COMMAND_WORD = "upcoming";

    public static final String MESSAGE_SUCCESS = "Listed %1$d student(s) with upcoming lessons.";
    public static final String MESSAGE_NO_UPCOMING_LESSONS = "No upcoming lessons. Use `schedule INDEX at/"
            + Lesson.FORMAT + "` to schedule one.";

    /**
     * Orders students by lesson time, then by case-insensitive name, then by exact name.
     * Students without a lesson are placed last, although the upcoming view never displays them.
     */
    static final Comparator<Person> LESSON_ORDER = Comparator
            .comparing((Person person) -> person.getLesson().map(Lesson::getDateTime).orElse(null),
                    Comparator.nullsLast(Comparator.naturalOrder()))
            .thenComparing(person -> person.getName().fullName.toLowerCase(Locale.ROOT))
            .thenComparing(person -> person.getName().fullName);

    private final Clock clock;

    /**
     * Creates an UpcomingCommand that compares lessons against the computer's current local time.
     */
    public UpcomingCommand() {
        this(Clock.systemDefaultZone());
    }

    /**
     * Creates an UpcomingCommand that compares lessons against the time given by {@code clock}.
     */
    public UpcomingCommand(Clock clock) {
        requireNonNull(clock);
        this.clock = clock;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        LocalDateTime now = LocalDateTime.now(clock);
        Predicate<Person> hasUpcomingLesson = person -> person.getLesson()
                .map(lesson -> lesson.isAfter(now))
                .orElse(false);

        model.updateFilteredPersonList(hasUpcomingLesson, ResultViewScope.ACTIVE);
        model.updateSortedPersonList(LESSON_ORDER);
        assert model.getFilteredPersonList().stream().allMatch(person -> hasUpcomingLesson.test(person)
                && !person.isArchived());

        int count = model.getFilteredPersonList().size();
        if (count == 0) {
            return new CommandResult(MESSAGE_NO_UPCOMING_LESSONS);
        }
        return new CommandResult(String.format(MESSAGE_SUCCESS, count));
    }

    @Override
    public boolean equals(Object other) {
        return other == this || other instanceof UpcomingCommand;
    }

    @Override
    public int hashCode() {
        return UpcomingCommand.class.hashCode();
    }
}
