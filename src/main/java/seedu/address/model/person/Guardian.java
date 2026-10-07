package seedu.address.model.person;

import static java.util.Objects.requireNonNull;

import java.util.Objects;
import java.util.Optional;

import seedu.address.commons.util.ToStringBuilder;

/**
 * Represents a student's guardian in the address book.
 * Guarantees: immutable; name is present and contact fields are optional.
 */
public class Guardian {

    private final Name name;
    private final Optional<Phone> phone;
    private final Optional<Email> email;

    /**
     * Constructs a {@code Guardian}.
     *
     * @param name The guardian's name.
     * @param phone The guardian's optional phone number.
     * @param email The guardian's optional email address.
     */
    public Guardian(Name name, Optional<Phone> phone, Optional<Email> email) {
        this.name = requireNonNull(name);
        this.phone = requireNonNull(phone);
        this.email = requireNonNull(email);
    }

    public Name getName() {
        return name;
    }

    public Optional<Phone> getPhone() {
        return phone;
    }

    public Optional<Email> getEmail() {
        return email;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Guardian otherGuardian)) {
            return false;
        }

        return name.equals(otherGuardian.name)
                && phone.equals(otherGuardian.phone)
                && email.equals(otherGuardian.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, phone, email);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("name", name)
                .add("phone", phone)
                .add("email", email)
                .toString();
    }
}
