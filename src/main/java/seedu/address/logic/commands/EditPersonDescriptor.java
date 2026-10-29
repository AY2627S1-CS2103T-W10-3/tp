package seedu.address.logic.commands;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.util.FieldUpdate;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.person.Address;
import seedu.address.model.person.EducationLevel;
import seedu.address.model.person.Email;
import seedu.address.model.person.LearningFocus;
import seedu.address.model.person.Name;
import seedu.address.model.person.NextAction;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Subject;
import seedu.address.model.tag.Tag;

/**
 * Stores the details to edit the person with. Each optional field is a {@link FieldUpdate} that records whether the
 * field is left unchanged, set to a new value, or cleared. The name is required and therefore cannot be cleared, and
 * the tag set replaces the existing tags wholesale when present.
 */
public class EditPersonDescriptor {
    private Name name;
    private FieldUpdate<Phone> phone = FieldUpdate.unchanged();
    private FieldUpdate<Email> email = FieldUpdate.unchanged();
    private FieldUpdate<Address> address = FieldUpdate.unchanged();
    private FieldUpdate<Name> guardianName = FieldUpdate.unchanged();
    private FieldUpdate<Phone> guardianPhone = FieldUpdate.unchanged();
    private FieldUpdate<Email> guardianEmail = FieldUpdate.unchanged();
    private FieldUpdate<Subject> subject = FieldUpdate.unchanged();
    private FieldUpdate<EducationLevel> educationLevel = FieldUpdate.unchanged();
    private FieldUpdate<LearningFocus> learningFocus = FieldUpdate.unchanged();
    private FieldUpdate<NextAction> nextAction = FieldUpdate.unchanged();
    private Set<Tag> tags;

    public EditPersonDescriptor() {}

    /**
     * Copy constructor.
     * A defensive copy of {@code tags} is used internally. Each {@link FieldUpdate} is immutable and can be shared.
     */
    public EditPersonDescriptor(EditPersonDescriptor toCopy) {
        name = toCopy.name;
        phone = toCopy.phone;
        email = toCopy.email;
        address = toCopy.address;
        guardianName = toCopy.guardianName;
        guardianPhone = toCopy.guardianPhone;
        guardianEmail = toCopy.guardianEmail;
        subject = toCopy.subject;
        educationLevel = toCopy.educationLevel;
        learningFocus = toCopy.learningFocus;
        nextAction = toCopy.nextAction;
        tags = toCopy.tags == null ? null : new HashSet<>(toCopy.tags);
    }

    /**
     * Returns true if at least one field is edited.
     */
    public boolean isAnyFieldEdited() {
        return name != null || phone.isEdited() || email.isEdited() || address.isEdited()
                || guardianName.isEdited() || guardianPhone.isEdited() || guardianEmail.isEdited()
                || subject.isEdited() || educationLevel.isEdited() || learningFocus.isEdited()
                || nextAction.isEdited() || tags != null;
    }

    public void setName(Name name) {
        this.name = name;
    }

    public Optional<Name> getName() {
        return Optional.ofNullable(name);
    }

    public void setPhone(Phone phone) {
        this.phone = FieldUpdate.set(phone);
    }

    /** Records a request to clear the phone. */
    public void clearPhone() {
        phone = FieldUpdate.clear();
    }

    public FieldUpdate<Phone> getPhone() {
        return phone;
    }

    public void setEmail(Email email) {
        this.email = FieldUpdate.set(email);
    }

    /** Records a request to clear the email. */
    public void clearEmail() {
        email = FieldUpdate.clear();
    }

    public FieldUpdate<Email> getEmail() {
        return email;
    }

    public void setAddress(Address address) {
        this.address = FieldUpdate.set(address);
    }

    /** Records a request to clear the address. */
    public void clearAddress() {
        address = FieldUpdate.clear();
    }

    public FieldUpdate<Address> getAddress() {
        return address;
    }

    public void setGuardianName(Name guardianName) {
        this.guardianName = FieldUpdate.set(guardianName);
    }

    /** Records a request to clear the guardian and its contact details. */
    public void clearGuardianName() {
        guardianName = FieldUpdate.clear();
    }

    public FieldUpdate<Name> getGuardianName() {
        return guardianName;
    }

    public void setGuardianPhone(Phone guardianPhone) {
        this.guardianPhone = FieldUpdate.set(guardianPhone);
    }

    /** Records a request to clear the guardian phone. */
    public void clearGuardianPhone() {
        guardianPhone = FieldUpdate.clear();
    }

    public FieldUpdate<Phone> getGuardianPhone() {
        return guardianPhone;
    }

    public void setGuardianEmail(Email guardianEmail) {
        this.guardianEmail = FieldUpdate.set(guardianEmail);
    }

    /** Records a request to clear the guardian email. */
    public void clearGuardianEmail() {
        guardianEmail = FieldUpdate.clear();
    }

    public FieldUpdate<Email> getGuardianEmail() {
        return guardianEmail;
    }

    public void setSubject(Subject subject) {
        this.subject = FieldUpdate.set(subject);
    }

    /** Records a request to clear the subject. */
    public void clearSubject() {
        subject = FieldUpdate.clear();
    }

    public FieldUpdate<Subject> getSubject() {
        return subject;
    }

    public void setEducationLevel(EducationLevel educationLevel) {
        this.educationLevel = FieldUpdate.set(educationLevel);
    }

    /** Records a request to clear the education level. */
    public void clearEducationLevel() {
        educationLevel = FieldUpdate.clear();
    }

    public FieldUpdate<EducationLevel> getEducationLevel() {
        return educationLevel;
    }

    public void setLearningFocus(LearningFocus learningFocus) {
        this.learningFocus = FieldUpdate.set(learningFocus);
    }

    /** Records a request to clear the learning focus. */
    public void clearLearningFocus() {
        learningFocus = FieldUpdate.clear();
    }

    public FieldUpdate<LearningFocus> getLearningFocus() {
        return learningFocus;
    }

    public void setNextAction(NextAction nextAction) {
        this.nextAction = FieldUpdate.set(nextAction);
    }

    /** Records a request to clear the next action. */
    public void clearNextAction() {
        nextAction = FieldUpdate.clear();
    }

    public FieldUpdate<NextAction> getNextAction() {
        return nextAction;
    }

    /**
     * Sets {@code tags} to this object's {@code tags}.
     * A defensive copy of {@code tags} is used internally.
     */
    public void setTags(Set<Tag> tags) {
        this.tags = (tags != null) ? new HashSet<>(tags) : null;
    }

    /**
     * Returns an unmodifiable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     * Returns {@code Optional#empty()} if {@code tags} is null.
     */
    public Optional<Set<Tag>> getTags() {
        return (tags != null) ? Optional.of(Collections.unmodifiableSet(tags)) : Optional.empty();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EditPersonDescriptor otherEditPersonDescriptor)) {
            return false;
        }

        return Objects.equals(name, otherEditPersonDescriptor.name)
                && phone.equals(otherEditPersonDescriptor.phone)
                && email.equals(otherEditPersonDescriptor.email)
                && address.equals(otherEditPersonDescriptor.address)
                && guardianName.equals(otherEditPersonDescriptor.guardianName)
                && guardianPhone.equals(otherEditPersonDescriptor.guardianPhone)
                && guardianEmail.equals(otherEditPersonDescriptor.guardianEmail)
                && subject.equals(otherEditPersonDescriptor.subject)
                && educationLevel.equals(otherEditPersonDescriptor.educationLevel)
                && learningFocus.equals(otherEditPersonDescriptor.learningFocus)
                && nextAction.equals(otherEditPersonDescriptor.nextAction)
                && Objects.equals(tags, otherEditPersonDescriptor.tags);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("name", name)
                .add("phone", phone)
                .add("email", email)
                .add("address", address)
                .add("guardianName", guardianName)
                .add("guardianPhone", guardianPhone)
                .add("guardianEmail", guardianEmail)
                .add("subject", subject)
                .add("educationLevel", educationLevel)
                .add("learningFocus", learningFocus)
                .add("nextAction", nextAction)
                .add("tags", tags)
                .toString();
    }
}
