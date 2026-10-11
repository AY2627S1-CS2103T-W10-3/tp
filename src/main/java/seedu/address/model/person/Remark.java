package seedu.address.model.person;

import static java.util.Objects.requireNonNull;

/**
 * An optional note attached to a person. An empty value means no remark.
 */
public class Remark {
    public final String value;

    /**
     * Creates a remark from the user's note.
     */
    public Remark(String value) {
        this.value = requireNonNull(value);
    }

    @Override
    public boolean equals(Object other) {
        return other == this || other instanceof Remark remark && value.equals(remark.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return value;
    }
}
