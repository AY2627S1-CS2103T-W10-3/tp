package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.util.Locale;

/**
 * Represents a student's education level.
 * Guarantees: immutable; is valid as declared in {@link #isValidEducationLevel(String)}.
 * An absent education level should be represented outside this class instead of using a blank value.
 */
public class EducationLevel {

    public static final String MESSAGE_CONSTRAINTS =
            "Education levels should be P1-P6, S1-S5, or JC1-JC2";
    public static final String VALIDATION_REGEX = "(?i)(P[1-6]|S[1-5]|JC[1-2])";

    private final String value;

    /**
     * Constructs an {@code EducationLevel}.
     *
     * @param educationLevel A valid education level.
     */
    public EducationLevel(String educationLevel) {
        requireNonNull(educationLevel);
        checkArgument(isValidEducationLevel(educationLevel), MESSAGE_CONSTRAINTS);
        value = educationLevel.toUpperCase(Locale.ROOT);
    }

    /**
     * Returns true if the given string is a valid education level.
     */
    public static boolean isValidEducationLevel(String test) {
        return test.matches(VALIDATION_REGEX);
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
        if (!(other instanceof EducationLevel otherEducationLevel)) {
            return false;
        }

        return value.equals(otherEducationLevel.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
