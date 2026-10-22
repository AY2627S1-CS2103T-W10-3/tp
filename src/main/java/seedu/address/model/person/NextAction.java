package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents the next tutoring action planned for a student.
 * Guarantees: immutable; is valid as declared in {@link #isValidNextAction(String)}.
 */
public final class NextAction {

    public static final int MAX_LENGTH = 200;
    public static final String MESSAGE_CONSTRAINTS = "Next action should not be blank, exceed "
            + MAX_LENGTH + " characters, or contain control characters";

    private final String value;

    /**
     * Constructs a {@code NextAction}.
     *
     * @param nextAction A valid next action.
     */
    public NextAction(String nextAction) {
        requireNonNull(nextAction);
        checkArgument(isValidNextAction(nextAction), MESSAGE_CONSTRAINTS);
        value = nextAction;
    }

    /** Returns true if the given string is a valid next action. */
    public static boolean isValidNextAction(String test) {
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

        if (!(other instanceof NextAction otherNextAction)) {
            return false;
        }

        return value.equals(otherNextAction.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
