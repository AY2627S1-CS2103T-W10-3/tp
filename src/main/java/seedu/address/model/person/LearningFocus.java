package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents the academic area a tutor is currently working on with a student.
 * Guarantees: immutable; is valid as declared in {@link #isValidLearningFocus(String)}.
 */
public final class LearningFocus {

    public static final int MAX_LENGTH = 200;
    public static final String MESSAGE_CONSTRAINTS = "Learning focus should not be blank, exceed "
            + MAX_LENGTH + " characters, or contain control characters";

    private final String value;

    /**
     * Constructs a {@code LearningFocus}.
     *
     * @param learningFocus A valid learning focus.
     */
    public LearningFocus(String learningFocus) {
        requireNonNull(learningFocus);
        checkArgument(isValidLearningFocus(learningFocus), MESSAGE_CONSTRAINTS);
        value = learningFocus;
    }

    /** Returns true if the given string is a valid learning focus. */
    public static boolean isValidLearningFocus(String test) {
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

        if (!(other instanceof LearningFocus otherLearningFocus)) {
            return false;
        }

        return value.equals(otherLearningFocus.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
