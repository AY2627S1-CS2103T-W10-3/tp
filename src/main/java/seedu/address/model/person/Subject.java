package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents the subject in which a student is being tutored.
 * Guarantees: immutable; is valid as declared in {@link #isValidSubject(String)}.
 * An absent subject should be represented outside this class instead of using a blank value.
 */
public class Subject {

    public static final int MAX_LENGTH = 100;
    public static final String MESSAGE_CONSTRAINTS = "Subjects should not be blank, exceed "
            + MAX_LENGTH + " characters, or contain control characters";

    private final String value;

    /**
     * Constructs a {@code Subject}.
     *
     * @param subject A valid subject.
     */
    public Subject(String subject) {
        requireNonNull(subject);
        checkArgument(isValidSubject(subject), MESSAGE_CONSTRAINTS);
        value = subject;
    }

    /**
     * Returns true if the given string is a valid subject.
     */
    public static boolean isValidSubject(String test) {
        return !test.isBlank()
                && test.length() <= MAX_LENGTH
                && test.codePoints().noneMatch(Character::isISOControl);
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Subject otherSubject)) {
            return false;
        }

        return value.equals(otherSubject.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
