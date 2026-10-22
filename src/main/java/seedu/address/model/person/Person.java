package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
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
    private final Optional<LearningFocus> learningFocus;
    private final Optional<NextAction> nextAction;

    // Data fields
    private final Optional<Lesson> lesson;
    private final Optional<Address> address;
    private final Set<Tag> tags = new HashSet<>();

    /**
     * Constructs a person with a required name, optional contact details, and no scheduled lesson.
     * Optional wrappers and tags must not be null; use empty values for omitted details.
     */
    public Person(Name name, Optional<Phone> phone, Optional<Email> email, Optional<Address> address,
            Optional<Guardian> guardian, Optional<Subject> subject, Optional<EducationLevel> educationLevel,
            Optional<LearningFocus> learningFocus, Optional<NextAction> nextAction, Set<Tag> tags) {
        this(name, phone, email, address, guardian, subject, educationLevel, learningFocus, nextAction,
                Optional.empty(), tags);
    }

    /**
     * Constructs a person with a required name, optional contact details, and an optional scheduled lesson.
     * Optional wrappers and tags must not be null; use empty values for omitted details.
     */
    public Person(Name name, Optional<Phone> phone, Optional<Email> email, Optional<Address> address,
            Optional<Guardian> guardian, Optional<Subject> subject, Optional<EducationLevel> educationLevel,
            Optional<LearningFocus> learningFocus, Optional<NextAction> nextAction, Optional<Lesson> lesson,
            Set<Tag> tags) {
        requireAllNonNull(name, phone, email, address, guardian, subject, educationLevel, learningFocus,
                nextAction, lesson, tags);
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.guardian = guardian;
        this.subject = subject;
        this.educationLevel = educationLevel;
        this.learningFocus = learningFocus;
        this.nextAction = nextAction;
        this.lesson = lesson;
        this.tags.addAll(tags);
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

    public Optional<LearningFocus> getLearningFocus() {
        return learningFocus;
    }

    public Optional<NextAction> getNextAction() {
        return nextAction;
    }

    public Optional<Lesson> getLesson() {
        return lesson;
    }

    /** Returns a copy of this student with {@code lesson} as the scheduled lesson, replacing any existing one. */
    public Person withLesson(Optional<Lesson> lesson) {
        requireNonNull(lesson);
        return new Person(name, phone, email, address, guardian, subject, educationLevel, learningFocus,
                nextAction, lesson, tags);
    }

    /**
     * Returns an immutable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    /** Returns true if both students have the same name after case and whitespace normalization. */
    public boolean isSamePerson(Person otherPerson) {
        if (otherPerson == this) {
            return true;
        }

        return otherPerson != null
                && normalizedName(otherPerson.getName()).equals(normalizedName(getName()));
    }

    private static String normalizedName(Name name) {
        return name.fullName.strip().replaceAll("\\s+", " ").toLowerCase(Locale.ROOT);
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
                && learningFocus.equals(otherPerson.learningFocus)
                && nextAction.equals(otherPerson.nextAction)
                && lesson.equals(otherPerson.lesson)
                && tags.equals(otherPerson.tags);
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, phone, email, address, guardian, subject, educationLevel, learningFocus,
                nextAction, lesson, tags);
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
                .add("learningFocus", learningFocus)
                .add("nextAction", nextAction)
                .add("lesson", lesson)
                .add("tags", tags)
                .toString();
    }

}
