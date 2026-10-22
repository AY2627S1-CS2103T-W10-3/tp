package seedu.address.testutil;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import seedu.address.logic.commands.EditCommand.EditPersonDescriptor;
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

/**
 * A utility class to help with building EditPersonDescriptor objects.
 */
public class EditPersonDescriptorBuilder {

    private EditPersonDescriptor descriptor;

    public EditPersonDescriptorBuilder() {
        descriptor = new EditPersonDescriptor();
    }

    public EditPersonDescriptorBuilder(EditPersonDescriptor descriptor) {
        this.descriptor = new EditPersonDescriptor(descriptor);
    }

    /**
     * Returns an {@code EditPersonDescriptor} with fields containing {@code person}'s details
     */
    public EditPersonDescriptorBuilder(Person person) {
        descriptor = new EditPersonDescriptor();
        descriptor.setName(person.getName());
        person.getPhone().ifPresent(descriptor::setPhone);
        person.getEmail().ifPresent(descriptor::setEmail);
        person.getAddress().ifPresent(descriptor::setAddress);
        person.getGuardian().ifPresent(guardian -> {
            descriptor.setGuardianName(guardian.getName());
            guardian.getPhone().ifPresent(descriptor::setGuardianPhone);
            guardian.getEmail().ifPresent(descriptor::setGuardianEmail);
        });
        person.getSubject().ifPresent(descriptor::setSubject);
        person.getEducationLevel().ifPresent(descriptor::setEducationLevel);
        person.getLearningFocus().ifPresent(descriptor::setLearningFocus);
        person.getNextAction().ifPresent(descriptor::setNextAction);
        descriptor.setTags(person.getTags());
    }

    /**
     * Sets the {@code Name} of the {@code EditPersonDescriptor} that we are building.
     */
    public EditPersonDescriptorBuilder withName(String name) {
        descriptor.setName(new Name(name));
        return this;
    }

    /**
     * Sets the {@code Phone} of the {@code EditPersonDescriptor} that we are building.
     */
    public EditPersonDescriptorBuilder withPhone(String phone) {
        descriptor.setPhone(new Phone(phone));
        return this;
    }

    /** Sets a request to clear the phone. */
    public EditPersonDescriptorBuilder withoutPhone() {
        descriptor.clearPhone();
        return this;
    }

    /**
     * Sets the {@code Email} of the {@code EditPersonDescriptor} that we are building.
     */
    public EditPersonDescriptorBuilder withEmail(String email) {
        descriptor.setEmail(new Email(email));
        return this;
    }

    /** Sets a request to clear the email. */
    public EditPersonDescriptorBuilder withoutEmail() {
        descriptor.clearEmail();
        return this;
    }

    /**
     * Sets the {@code Address} of the {@code EditPersonDescriptor} that we are building.
     */
    public EditPersonDescriptorBuilder withAddress(String address) {
        descriptor.setAddress(new Address(address));
        return this;
    }

    /** Sets a request to clear the address. */
    public EditPersonDescriptorBuilder withoutAddress() {
        descriptor.clearAddress();
        return this;
    }

    /** Sets the guardian name to edit. */
    public EditPersonDescriptorBuilder withGuardianName(String name) {
        descriptor.setGuardianName(new Name(name));
        return this;
    }

    /** Sets a request to clear the guardian. */
    public EditPersonDescriptorBuilder withoutGuardianName() {
        descriptor.clearGuardianName();
        return this;
    }

    /** Sets the guardian phone to edit. */
    public EditPersonDescriptorBuilder withGuardianPhone(String phone) {
        descriptor.setGuardianPhone(new Phone(phone));
        return this;
    }

    /** Sets a request to clear the guardian phone. */
    public EditPersonDescriptorBuilder withoutGuardianPhone() {
        descriptor.clearGuardianPhone();
        return this;
    }

    /** Sets the guardian email to edit. */
    public EditPersonDescriptorBuilder withGuardianEmail(String email) {
        descriptor.setGuardianEmail(new Email(email));
        return this;
    }

    /** Sets a request to clear the guardian email. */
    public EditPersonDescriptorBuilder withoutGuardianEmail() {
        descriptor.clearGuardianEmail();
        return this;
    }

    /** Sets all present fields from the guardian to edit. */
    public EditPersonDescriptorBuilder withGuardian(Guardian guardian) {
        descriptor.setGuardianName(guardian.getName());
        guardian.getPhone().ifPresent(descriptor::setGuardianPhone);
        guardian.getEmail().ifPresent(descriptor::setGuardianEmail);
        return this;
    }

    /** Sets the subject to edit. */
    public EditPersonDescriptorBuilder withSubject(String subject) {
        descriptor.setSubject(new Subject(subject));
        return this;
    }

    /** Sets a request to clear the subject. */
    public EditPersonDescriptorBuilder withoutSubject() {
        descriptor.clearSubject();
        return this;
    }

    /** Sets the education level to edit. */
    public EditPersonDescriptorBuilder withEducationLevel(String educationLevel) {
        descriptor.setEducationLevel(new EducationLevel(educationLevel));
        return this;
    }

    /** Sets a request to clear the education level. */
    public EditPersonDescriptorBuilder withoutEducationLevel() {
        descriptor.clearEducationLevel();
        return this;
    }

    /** Sets the {@code LearningFocus} in the descriptor. */
    public EditPersonDescriptorBuilder withLearningFocus(String learningFocus) {
        descriptor.setLearningFocus(new LearningFocus(learningFocus));
        return this;
    }

    /** Sets a request to clear the learning focus. */
    public EditPersonDescriptorBuilder withoutLearningFocus() {
        descriptor.clearLearningFocus();
        return this;
    }

    /** Sets the {@code NextAction} in the descriptor. */
    public EditPersonDescriptorBuilder withNextAction(String nextAction) {
        descriptor.setNextAction(new NextAction(nextAction));
        return this;
    }

    /** Sets a request to clear the next action. */
    public EditPersonDescriptorBuilder withoutNextAction() {
        descriptor.clearNextAction();
        return this;
    }

    /**
     * Parses the {@code tags} into a {@code Set<Tag>} and sets it to the {@code EditPersonDescriptor}
     * that we are building.
     */
    public EditPersonDescriptorBuilder withTags(String... tags) {
        Set<Tag> tagSet = Stream.of(tags).map(Tag::new).collect(Collectors.toSet());
        descriptor.setTags(tagSet);
        return this;
    }

    public EditPersonDescriptor build() {
        return descriptor;
    }
}
