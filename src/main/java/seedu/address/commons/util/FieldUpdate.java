package seedu.address.commons.util;

import static java.util.Objects.requireNonNull;

import java.util.Objects;
import java.util.Optional;

/**
 * An immutable request to change a single optional field. A {@code FieldUpdate} is in exactly one of three
 * states: {@code unchanged} (leave the existing value as it is), {@code set} (replace it with a new value), or
 * {@code clear} (remove the existing value). Representing the three states explicitly removes the need to pair a
 * nullable value with a separate "edited" flag.
 *
 * @param <T> the type of the field value
 */
public final class FieldUpdate<T> {

    private enum Kind { UNCHANGED, SET, CLEAR }

    private static final FieldUpdate<?> UNCHANGED = new FieldUpdate<>(Kind.UNCHANGED, null);
    private static final FieldUpdate<?> CLEAR = new FieldUpdate<>(Kind.CLEAR, null);

    private final Kind kind;
    private final T value;

    private FieldUpdate(Kind kind, T value) {
        this.kind = kind;
        this.value = value;
    }

    /** Returns an update that leaves the existing value unchanged. */
    @SuppressWarnings("unchecked")
    public static <T> FieldUpdate<T> unchanged() {
        return (FieldUpdate<T>) UNCHANGED;
    }

    /** Returns an update that replaces the existing value with the non-null {@code value}. */
    public static <T> FieldUpdate<T> set(T value) {
        requireNonNull(value);
        return new FieldUpdate<>(Kind.SET, value);
    }

    /** Returns an update that clears the existing value. */
    @SuppressWarnings("unchecked")
    public static <T> FieldUpdate<T> clear() {
        return (FieldUpdate<T>) CLEAR;
    }

    /** Returns true if this update changes the existing value, that is, if it is a set or a clear. */
    public boolean isEdited() {
        return kind != Kind.UNCHANGED;
    }

    /**
     * Returns the value this update would set, or {@code Optional.empty()} for a clear or an unchanged update.
     */
    public Optional<T> value() {
        return Optional.ofNullable(value);
    }

    /**
     * Applies this update to {@code existing} and returns the resulting value: the existing value when unchanged,
     * the new value when set, and empty when cleared.
     */
    public Optional<T> applyTo(Optional<T> existing) {
        requireNonNull(existing);
        return switch (kind) {
            case UNCHANGED -> existing;
            case SET -> Optional.of(value);
            case CLEAR -> Optional.empty();
        };
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof FieldUpdate<?> otherUpdate)) {
            return false;
        }

        return kind == otherUpdate.kind && Objects.equals(value, otherUpdate.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(kind, value);
    }

    @Override
    public String toString() {
        return switch (kind) {
            case UNCHANGED -> "unchanged";
            case SET -> "set(" + value + ")";
            case CLEAR -> "clear";
        };
    }
}
