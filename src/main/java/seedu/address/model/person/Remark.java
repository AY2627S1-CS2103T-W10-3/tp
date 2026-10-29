package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a free-form remark about a student.
 * Guarantees: immutable; is valid as declared in {@link #isValidRemark(String)}.
 */
public final class Remark {

    public static final int MAX_LENGTH = 200;
    public static final String MESSAGE_CONSTRAINTS = "Remark should not be blank, exceed "
            + MAX_LENGTH + " characters, or contain control characters";

    private final String value;

    /**
     * Constructs a {@code Remark}.
     *
     * @param remark A valid remark.
     */
    public Remark(String remark) {
        requireNonNull(remark);
        checkArgument(isValidRemark(remark), MESSAGE_CONSTRAINTS);
        value = remark;
    }

    /** Returns true if the given string is a valid remark. */
    public static boolean isValidRemark(String test) {
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

        if (!(other instanceof Remark otherRemark)) {
            return false;
        }

        return value.equals(otherRemark.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
