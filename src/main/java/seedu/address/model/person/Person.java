package seedu.address.model.person;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.tag.Tag;

/**
 * Represents a Person in the address book.
 * Guarantees: name is present, optional details are explicit, field values are validated, immutable.
 */
public class Person {

    // Identity fields
    private final Name name;
    private final Optional<Phone> phone;
    private final Optional<Email> email;
    private final Optional<Guardian> guardian;
    private final Optional<Subject> subject;
    private final Optional<EducationLevel> educationLevel;

    // Data fields
    private final Optional<Address> address;
    private final Set<Tag> tags = new HashSet<>();

    /**
     * Constructs a person with a required name and optional contact details.
     * Optional wrappers and tags must not be null; use empty values for omitted details.
     */
    public Person(Name name, Optional<Phone> phone, Optional<Email> email, Optional<Address> address,
            Optional<Guardian> guardian, Optional<Subject> subject, Optional<EducationLevel> educationLevel,
            Set<Tag> tags) {
        requireAllNonNull(name, phone, email, address, guardian, subject, educationLevel, tags);
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.guardian = guardian;
        this.subject = subject;
        this.educationLevel = educationLevel;
        this.tags.addAll(tags);
    }

    /**
     * Constructs a person without tutoring details.
     */
    public Person(Name name, Optional<Phone> phone, Optional<Email> email, Optional<Address> address,
            Optional<Guardian> guardian, Set<Tag> tags) {
        this(name, phone, email, address, guardian, Optional.empty(), Optional.empty(), tags);
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

    public Optional<Address> getAddress() {
        return address;
    }

    public Optional<Guardian> getGuardian() {
        return guardian;
    }

    public Optional<Subject> getSubject() {
        return subject;
    }

    public Optional<EducationLevel> getEducationLevel() {
        return educationLevel;
    }

    /**
     * Returns an immutable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    /**
     * Returns true if both persons have the same name.
     * This defines a weaker notion of equality between two persons.
     */
    public boolean isSamePerson(Person otherPerson) {
        if (otherPerson == this) {
            return true;
        }

        return otherPerson != null
                && otherPerson.getName().equals(getName());
    }

    /**
     * Returns true if both persons have the same identity and data fields.
     * This defines a stronger notion of equality between two persons.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Person otherPerson)) {
            return false;
        }

        return name.equals(otherPerson.name)
                && phone.equals(otherPerson.phone)
                && email.equals(otherPerson.email)
                && address.equals(otherPerson.address)
                && guardian.equals(otherPerson.guardian)
                && subject.equals(otherPerson.subject)
                && educationLevel.equals(otherPerson.educationLevel)
                && tags.equals(otherPerson.tags);
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, phone, email, address, guardian, subject, educationLevel, tags);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("name", name)
                .add("phone", phone)
                .add("email", email)
                .add("address", address)
                .add("guardian", guardian)
                .add("subject", subject)
                .add("educationLevel", educationLevel)
                .add("tags", tags)
                .toString();
    }

}
