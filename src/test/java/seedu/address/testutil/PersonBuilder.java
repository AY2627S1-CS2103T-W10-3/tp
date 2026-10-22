package seedu.address.testutil;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import seedu.address.model.person.Address;
import seedu.address.model.person.EducationLevel;
import seedu.address.model.person.Email;
import seedu.address.model.person.Guardian;
import seedu.address.model.person.LearningFocus;
import seedu.address.model.person.Name;
import seedu.address.model.person.NextAction;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Subject;
import seedu.address.model.tag.Tag;
import seedu.address.model.util.SampleDataUtil;

/**
 * A utility class to help with building Person objects.
 */
public class PersonBuilder {

    public static final String DEFAULT_NAME = "Amy Bee";
    public static final String DEFAULT_PHONE = "85355255";
    public static final String DEFAULT_EMAIL = "amy@gmail.com";
    public static final String DEFAULT_ADDRESS = "123, Jurong West Ave 6, #08-111";
    public static final String DEFAULT_SUBJECT = "Mathematics";
    public static final String DEFAULT_EDUCATION_LEVEL = "S3";

    private Name name;
    private Optional<Phone> phone;
    private Optional<Email> email;
    private Optional<Address> address;
    private Optional<Guardian> guardian;
    private Optional<Subject> subject;
    private Optional<EducationLevel> educationLevel;
    private Optional<LearningFocus> learningFocus;
    private Optional<NextAction> nextAction;
    private boolean archived;
    private Set<Tag> tags;

    /**
     * Creates a {@code PersonBuilder} with the default details.
     */
    public PersonBuilder() {
        name = new Name(DEFAULT_NAME);
        phone = Optional.of(new Phone(DEFAULT_PHONE));
        email = Optional.of(new Email(DEFAULT_EMAIL));
        address = Optional.of(new Address(DEFAULT_ADDRESS));
        guardian = Optional.empty();
        subject = Optional.empty();
        educationLevel = Optional.empty();
        learningFocus = Optional.empty();
        nextAction = Optional.empty();
        archived = false;
        tags = new HashSet<>();
    }

    /**
     * Initializes the PersonBuilder with the data of {@code personToCopy}.
     */
    public PersonBuilder(Person personToCopy) {
        name = personToCopy.getName();
        phone = personToCopy.getPhone();
        email = personToCopy.getEmail();
        address = personToCopy.getAddress();
        guardian = personToCopy.getGuardian();
        subject = personToCopy.getSubject();
        educationLevel = personToCopy.getEducationLevel();
        learningFocus = personToCopy.getLearningFocus();
        nextAction = personToCopy.getNextAction();
        archived = personToCopy.isArchived();
        tags = new HashSet<>(personToCopy.getTags());
    }

    /**
     * Sets the {@code Name} of the {@code Person} that we are building.
     */
    public PersonBuilder withName(String name) {
        this.name = new Name(name);
        return this;
    }

    /**
     * Parses the {@code tags} into a {@code Set<Tag>} and sets it to the {@code Person} that we are building.
     */
    public PersonBuilder withTags(String ... tags) {
        this.tags = SampleDataUtil.getTagSet(tags);
        return this;
    }

    /**
     * Sets the {@code Address} of the {@code Person} that we are building.
     */
    public PersonBuilder withAddress(String address) {
        this.address = Optional.of(new Address(address));
        return this;
    }

    /** Omits the person's address. */
    public PersonBuilder withoutAddress() {
        this.address = Optional.empty();
        return this;
    }

    /**
     * Sets the {@code Phone} of the {@code Person} that we are building.
     */
    public PersonBuilder withPhone(String phone) {
        this.phone = Optional.of(new Phone(phone));
        return this;
    }

    /** Omits the person's phone number. */
    public PersonBuilder withoutPhone() {
        this.phone = Optional.empty();
        return this;
    }

    /**
     * Sets the {@code Email} of the {@code Person} that we are building.
     */
    public PersonBuilder withEmail(String email) {
        this.email = Optional.of(new Email(email));
        return this;
    }

    /** Omits the person's email address. */
    public PersonBuilder withoutEmail() {
        this.email = Optional.empty();
        return this;
    }

    /** Sets a guardian with all contact fields. */
    public PersonBuilder withGuardian(String name, String phone, String email) {
        guardian = Optional.of(new Guardian(new Name(name), Optional.of(new Phone(phone)),
                Optional.of(new Email(email))));
        return this;
    }

    /** Sets a guardian without contact details. */
    public PersonBuilder withGuardianName(String name) {
        guardian = Optional.of(new Guardian(new Name(name), Optional.empty(), Optional.empty()));
        return this;
    }

    /** Omits the person's guardian. */
    public PersonBuilder withoutGuardian() {
        guardian = Optional.empty();
        return this;
    }

    /** Sets the person's tutoring subject. */
    public PersonBuilder withSubject(String subject) {
        this.subject = Optional.of(new Subject(subject));
        return this;
    }

    /** Omits the person's tutoring subject. */
    public PersonBuilder withoutSubject() {
        subject = Optional.empty();
        return this;
    }

    /** Sets the person's education level. */
    public PersonBuilder withEducationLevel(String educationLevel) {
        this.educationLevel = Optional.of(new EducationLevel(educationLevel));
        return this;
    }

    /** Omits the person's education level. */
    public PersonBuilder withoutEducationLevel() {
        educationLevel = Optional.empty();
        return this;
    }

    /** Sets the student's current learning focus. */
    public PersonBuilder withLearningFocus(String learningFocus) {
        this.learningFocus = Optional.of(new LearningFocus(learningFocus));
        return this;
    }

    /** Omits the student's current learning focus. */
    public PersonBuilder withoutLearningFocus() {
        learningFocus = Optional.empty();
        return this;
    }

    /** Sets the student's next tutoring action. */
    public PersonBuilder withNextAction(String nextAction) {
        this.nextAction = Optional.of(new NextAction(nextAction));
        return this;
    }

    /** Omits the student's next tutoring action. */
    public PersonBuilder withoutNextAction() {
        nextAction = Optional.empty();
        return this;
    }

    /** Sets whether the student is archived. */
    public PersonBuilder withArchived(boolean archived) {
        this.archived = archived;
        return this;
    }

    /** Builds a {@code Person} with the configured fields. */
    public Person build() {
        return new Person(name, phone, email, address, guardian, subject, educationLevel, learningFocus,
                nextAction, archived, tags);
    }

}
