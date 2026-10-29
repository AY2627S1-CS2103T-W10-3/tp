package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.regex.Pattern;

/**
 * Represents the date and time of a student's next lesson, in the computer's local time.
 * Guarantees: immutable; is valid as declared in {@link #isValidLesson(String)}.
 */
public final class Lesson {

    public static final String FORMAT = "YYYY-MM-DD HH:mm";
    public static final String MESSAGE_CONSTRAINTS = "Lesson time should be a valid date and 24-hour time in the "
            + "format " + FORMAT + ", e.g. 2099-10-30 16:00";

    // The formatter alone also accepts signed years of any length, such as +12345, so check the shape first.
    private static final Pattern LESSON_SHAPE = Pattern.compile("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}");
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm")
            .withResolverStyle(ResolverStyle.STRICT);

    private final LocalDateTime dateTime;

    /**
     * Constructs a {@code Lesson}.
     *
     * @param lesson A valid lesson date and time.
     */
    public Lesson(String lesson) {
        requireNonNull(lesson);
        checkArgument(isValidLesson(lesson), MESSAGE_CONSTRAINTS);
        dateTime = LocalDateTime.parse(lesson, FORMATTER);
        assert dateTime.getSecond() == 0 && dateTime.getNano() == 0 : "Lessons are stored with minute precision";
    }

    /** Returns true if the given string is a valid lesson date and time. */
    public static boolean isValidLesson(String test) {
        requireNonNull(test);
        if (!LESSON_SHAPE.matcher(test).matches()) {
            return false;
        }
        try {
            LocalDateTime.parse(test, FORMATTER);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    /** Returns true if this lesson starts strictly after {@code now}. */
    public boolean isAfter(LocalDateTime now) {
        requireNonNull(now);
        return dateTime.isAfter(now);
    }

    /** Returns true if this lesson has started by {@code now}, so it is no longer upcoming. */
    public boolean isOverdue(LocalDateTime now) {
        return !isAfter(now);
    }

    /** Returns the lesson in the same format accepted by {@link #isValidLesson(String)}. */
    public String getValue() {
        return dateTime.format(FORMATTER);
    }

    @Override
    public String toString() {
        return getValue();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Lesson otherLesson)) {
            return false;
        }

        return dateTime.equals(otherLesson.dateTime);
    }

    @Override
    public int hashCode() {
        return dateTime.hashCode();
    }
}
