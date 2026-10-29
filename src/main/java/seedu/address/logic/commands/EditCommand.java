package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EDUCATION_LEVEL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GUARDIAN_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GUARDIAN_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GUARDIAN_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_LEARNING_FOCUS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NEXT_ACTION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SUBJECT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ResultViewScope;
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
 * Edits the details of an existing person in the address book.
 */
public class EditCommand extends Command {

    public static final String COMMAND_WORD = "edit";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the details of the student identified "
            + "by the index number used in the displayed student list. "
            + "Existing values will be overwritten by the input values.\n"
            + "Leave an optional field empty to clear it. Clearing the guardian name also clears "
            + "the guardian phone and email.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "[" + PREFIX_NAME + "NAME] "
            + "[" + PREFIX_PHONE + "PHONE] "
            + "[" + PREFIX_EMAIL + "EMAIL] "
            + "[" + PREFIX_ADDRESS + "ADDRESS] "
            + "[" + PREFIX_GUARDIAN_NAME + "GUARDIAN_NAME] "
            + "[" + PREFIX_GUARDIAN_PHONE + "GUARDIAN_PHONE] "
            + "[" + PREFIX_GUARDIAN_EMAIL + "GUARDIAN_EMAIL] "
            + "[" + PREFIX_SUBJECT + "SUBJECT] "
            + "[" + PREFIX_EDUCATION_LEVEL + "EDUCATION_LEVEL] "
            + "[" + PREFIX_LEARNING_FOCUS + "LEARNING_FOCUS] "
            + "[" + PREFIX_NEXT_ACTION + "NEXT_ACTION] "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_PHONE + "91234567 "
            + PREFIX_EMAIL + "johndoe@example.com";

    public static final String MESSAGE_EDIT_PERSON_SUCCESS = "Edited student: %1$s";
    public static final String MESSAGE_NO_CHANGES = "No changes made to %1$s.";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_DUPLICATE_PERSON = "This student already exists in TutorContacts.";
    public static final String MESSAGE_REQUIRES_ACTIVE_VIEW =
            "Edit can only be used from an active student view. Run `list` first.";
    public static final String MESSAGE_GUARDIAN_NAME_REQUIRED =
            "Guardian name is required when guardian phone or email is provided.";

    private final Index index;
    private final EditPersonDescriptor editPersonDescriptor;

    /**
     * @param index of the person in the filtered person list to edit
     * @param editPersonDescriptor details to edit the person with
     */
    public EditCommand(Index index, EditPersonDescriptor editPersonDescriptor) {
        requireNonNull(index);
        requireNonNull(editPersonDescriptor);

        this.index = index;
        this.editPersonDescriptor = new EditPersonDescriptor(editPersonDescriptor);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        if (model.getResultViewScope() != ResultViewScope.ACTIVE) {
            throw new CommandException(MESSAGE_REQUIRES_ACTIVE_VIEW);
        }
        List<Person> lastShownList = model.getFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToEdit = lastShownList.get(index.getZeroBased());
        Person editedPerson = createEditedPerson(personToEdit, editPersonDescriptor);

        if (personToEdit.equals(editedPerson)) {
            return new CommandResult(String.format(MESSAGE_NO_CHANGES, personToEdit.getName()));
        }

        if (!personToEdit.isSamePerson(editedPerson) && model.hasPerson(editedPerson)) {
            if (hasArchivedDuplicate(model, editedPerson)) {
                throw new CommandException(Messages.MESSAGE_DUPLICATE_ARCHIVED_PERSON);
            }
            throw new CommandException(MESSAGE_DUPLICATE_PERSON);
        }

        // The current result view is kept, so the edited student leaves it if they no longer match its query.
        List<Person> shownBefore = List.copyOf(lastShownList);
        model.setPerson(personToEdit, editedPerson);
        String feedback = String.format(MESSAGE_EDIT_PERSON_SUCCESS, Messages.format(editedPerson));
        return new CommandResult(withListUpdateNotice(feedback, shownBefore, index.getZeroBased(), editedPerson,
                model.getFilteredPersonList()));
    }

    /**
     * Creates and returns a {@code Person} with the details of {@code personToEdit}
     * edited with {@code editPersonDescriptor}.
     */
    private static Person createEditedPerson(Person personToEdit, EditPersonDescriptor editPersonDescriptor)
            throws CommandException {
        assert personToEdit != null;

        Name updatedName = editPersonDescriptor.getName().orElse(personToEdit.getName());
        Optional<Phone> updatedPhone = editPersonDescriptor.isPhoneEdited()
                ? editPersonDescriptor.getPhone() : personToEdit.getPhone();
        Optional<Email> updatedEmail = editPersonDescriptor.isEmailEdited()
                ? editPersonDescriptor.getEmail() : personToEdit.getEmail();
        Optional<Address> updatedAddress = editPersonDescriptor.isAddressEdited()
                ? editPersonDescriptor.getAddress() : personToEdit.getAddress();
        Optional<Name> updatedGuardianName = editPersonDescriptor.isGuardianNameEdited()
                ? editPersonDescriptor.getGuardianName() : personToEdit.getGuardian().map(Guardian::getName);
        Optional<Phone> updatedGuardianPhone = editPersonDescriptor.isGuardianNameEdited()
                && updatedGuardianName.isEmpty() ? Optional.empty()
                : editPersonDescriptor.isGuardianPhoneEdited()
                        ? editPersonDescriptor.getGuardianPhone()
                        : personToEdit.getGuardian().flatMap(Guardian::getPhone);
        Optional<Email> updatedGuardianEmail = editPersonDescriptor.isGuardianNameEdited()
                && updatedGuardianName.isEmpty() ? Optional.empty()
                : editPersonDescriptor.isGuardianEmailEdited()
                        ? editPersonDescriptor.getGuardianEmail()
                        : personToEdit.getGuardian().flatMap(Guardian::getEmail);
        boolean suppliesGuardianContactWithoutName = updatedGuardianName.isEmpty()
                && (editPersonDescriptor.getGuardianPhone().isPresent()
                || editPersonDescriptor.getGuardianEmail().isPresent());
        if (suppliesGuardianContactWithoutName) {
            throw new CommandException(MESSAGE_GUARDIAN_NAME_REQUIRED);
        }
        Optional<Guardian> updatedGuardian = updatedGuardianName.map(name ->
                new Guardian(name, updatedGuardianPhone, updatedGuardianEmail));
        Optional<Subject> updatedSubject = editPersonDescriptor.isSubjectEdited()
                ? editPersonDescriptor.getSubject() : personToEdit.getSubject();
        Optional<EducationLevel> updatedEducationLevel = editPersonDescriptor.isEducationLevelEdited()
                ? editPersonDescriptor.getEducationLevel() : personToEdit.getEducationLevel();
        Optional<LearningFocus> updatedLearningFocus = editPersonDescriptor.isLearningFocusEdited()
                ? editPersonDescriptor.getLearningFocus() : personToEdit.getLearningFocus();
        Optional<NextAction> updatedNextAction = editPersonDescriptor.isNextActionEdited()
                ? editPersonDescriptor.getNextAction() : personToEdit.getNextAction();
        Set<Tag> updatedTags = editPersonDescriptor.getTags().orElse(personToEdit.getTags());

        return new Person(updatedName, updatedPhone, updatedEmail, updatedAddress,
                updatedGuardian, updatedSubject, updatedEducationLevel, updatedLearningFocus,
                updatedNextAction, personToEdit.getLesson(), personToEdit.isArchived(), updatedTags);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EditCommand otherEditCommand)) {
            return false;
        }

        return index.equals(otherEditCommand.index)
                && editPersonDescriptor.equals(otherEditCommand.editPersonDescriptor);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("index", index)
                .add("editPersonDescriptor", editPersonDescriptor)
                .toString();
    }

    /**
     * Stores the details to edit the person with. Each non-empty field value will replace the
     * corresponding field value of the person.
     */
    public static class EditPersonDescriptor {
        private Name name;
        private Phone phone;
        private boolean phoneEdited;
        private Email email;
        private boolean emailEdited;
        private Address address;
        private boolean addressEdited;
        private Name guardianName;
        private boolean guardianNameEdited;
        private Phone guardianPhone;
        private boolean guardianPhoneEdited;
        private Email guardianEmail;
        private boolean guardianEmailEdited;
        private Subject subject;
        private boolean subjectEdited;
        private EducationLevel educationLevel;
        private boolean educationLevelEdited;
        private LearningFocus learningFocus;
        private boolean learningFocusEdited;
        private NextAction nextAction;
        private boolean nextActionEdited;
        private Set<Tag> tags;

        public EditPersonDescriptor() {}

        /**
         * Copy constructor.
         * A defensive copy of {@code tags} is used internally.
         */
        public EditPersonDescriptor(EditPersonDescriptor toCopy) {
            name = toCopy.name;
            phone = toCopy.phone;
            phoneEdited = toCopy.phoneEdited;
            email = toCopy.email;
            emailEdited = toCopy.emailEdited;
            address = toCopy.address;
            addressEdited = toCopy.addressEdited;
            guardianName = toCopy.guardianName;
            guardianNameEdited = toCopy.guardianNameEdited;
            guardianPhone = toCopy.guardianPhone;
            guardianPhoneEdited = toCopy.guardianPhoneEdited;
            guardianEmail = toCopy.guardianEmail;
            guardianEmailEdited = toCopy.guardianEmailEdited;
            subject = toCopy.subject;
            subjectEdited = toCopy.subjectEdited;
            educationLevel = toCopy.educationLevel;
            educationLevelEdited = toCopy.educationLevelEdited;
            learningFocus = toCopy.learningFocus;
            learningFocusEdited = toCopy.learningFocusEdited;
            nextAction = toCopy.nextAction;
            nextActionEdited = toCopy.nextActionEdited;
            tags = toCopy.tags == null ? null : new HashSet<>(toCopy.tags);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return name != null || phoneEdited || emailEdited || addressEdited || guardianNameEdited
                    || guardianPhoneEdited || guardianEmailEdited || subjectEdited || educationLevelEdited
                    || learningFocusEdited || nextActionEdited || tags != null;
        }

        public void setName(Name name) {
            this.name = name;
        }

        public Optional<Name> getName() {
            return Optional.ofNullable(name);
        }

        public void setPhone(Phone phone) {
            this.phone = phone;
            phoneEdited = true;
        }

        /** Records a request to clear the phone. */
        public void clearPhone() {
            phone = null;
            phoneEdited = true;
        }

        public boolean isPhoneEdited() {
            return phoneEdited;
        }

        public Optional<Phone> getPhone() {
            return Optional.ofNullable(phone);
        }

        public void setEmail(Email email) {
            this.email = email;
            emailEdited = true;
        }

        /** Records a request to clear the email. */
        public void clearEmail() {
            email = null;
            emailEdited = true;
        }

        public boolean isEmailEdited() {
            return emailEdited;
        }

        public Optional<Email> getEmail() {
            return Optional.ofNullable(email);
        }

        public void setAddress(Address address) {
            this.address = address;
            addressEdited = true;
        }

        /** Records a request to clear the address. */
        public void clearAddress() {
            address = null;
            addressEdited = true;
        }

        public boolean isAddressEdited() {
            return addressEdited;
        }

        public Optional<Address> getAddress() {
            return Optional.ofNullable(address);
        }

        public void setGuardianName(Name guardianName) {
            this.guardianName = guardianName;
            guardianNameEdited = true;
        }

        /** Records a request to clear the guardian and its contact details. */
        public void clearGuardianName() {
            guardianName = null;
            guardianNameEdited = true;
        }

        public boolean isGuardianNameEdited() {
            return guardianNameEdited;
        }

        public Optional<Name> getGuardianName() {
            return Optional.ofNullable(guardianName);
        }

        public void setGuardianPhone(Phone guardianPhone) {
            this.guardianPhone = guardianPhone;
            guardianPhoneEdited = true;
        }

        /** Records a request to clear the guardian phone. */
        public void clearGuardianPhone() {
            guardianPhone = null;
            guardianPhoneEdited = true;
        }

        public boolean isGuardianPhoneEdited() {
            return guardianPhoneEdited;
        }

        public Optional<Phone> getGuardianPhone() {
            return Optional.ofNullable(guardianPhone);
        }

        public void setGuardianEmail(Email guardianEmail) {
            this.guardianEmail = guardianEmail;
            guardianEmailEdited = true;
        }

        /** Records a request to clear the guardian email. */
        public void clearGuardianEmail() {
            guardianEmail = null;
            guardianEmailEdited = true;
        }

        public boolean isGuardianEmailEdited() {
            return guardianEmailEdited;
        }

        public Optional<Email> getGuardianEmail() {
            return Optional.ofNullable(guardianEmail);
        }

        public void setSubject(Subject subject) {
            this.subject = subject;
            subjectEdited = true;
        }

        /** Records a request to clear the subject. */
        public void clearSubject() {
            subject = null;
            subjectEdited = true;
        }

        public boolean isSubjectEdited() {
            return subjectEdited;
        }

        public Optional<Subject> getSubject() {
            return Optional.ofNullable(subject);
        }

        public void setEducationLevel(EducationLevel educationLevel) {
            this.educationLevel = educationLevel;
            educationLevelEdited = true;
        }

        /** Records a request to clear the education level. */
        public void clearEducationLevel() {
            educationLevel = null;
            educationLevelEdited = true;
        }

        public boolean isEducationLevelEdited() {
            return educationLevelEdited;
        }

        public Optional<EducationLevel> getEducationLevel() {
            return Optional.ofNullable(educationLevel);
        }

        public void setLearningFocus(LearningFocus learningFocus) {
            this.learningFocus = learningFocus;
            learningFocusEdited = true;
        }

        /** Records a request to clear the learning focus. */
        public void clearLearningFocus() {
            learningFocus = null;
            learningFocusEdited = true;
        }

        public boolean isLearningFocusEdited() {
            return learningFocusEdited;
        }

        public Optional<LearningFocus> getLearningFocus() {
            return Optional.ofNullable(learningFocus);
        }

        public void setNextAction(NextAction nextAction) {
            this.nextAction = nextAction;
            nextActionEdited = true;
        }

        /** Records a request to clear the next action. */
        public void clearNextAction() {
            nextAction = null;
            nextActionEdited = true;
        }

        public boolean isNextActionEdited() {
            return nextActionEdited;
        }

        public Optional<NextAction> getNextAction() {
            return Optional.ofNullable(nextAction);
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
                    && Objects.equals(phone, otherEditPersonDescriptor.phone)
                    && phoneEdited == otherEditPersonDescriptor.phoneEdited
                    && Objects.equals(email, otherEditPersonDescriptor.email)
                    && emailEdited == otherEditPersonDescriptor.emailEdited
                    && Objects.equals(address, otherEditPersonDescriptor.address)
                    && addressEdited == otherEditPersonDescriptor.addressEdited
                    && Objects.equals(guardianName, otherEditPersonDescriptor.guardianName)
                    && guardianNameEdited == otherEditPersonDescriptor.guardianNameEdited
                    && Objects.equals(guardianPhone, otherEditPersonDescriptor.guardianPhone)
                    && guardianPhoneEdited == otherEditPersonDescriptor.guardianPhoneEdited
                    && Objects.equals(guardianEmail, otherEditPersonDescriptor.guardianEmail)
                    && guardianEmailEdited == otherEditPersonDescriptor.guardianEmailEdited
                    && Objects.equals(subject, otherEditPersonDescriptor.subject)
                    && subjectEdited == otherEditPersonDescriptor.subjectEdited
                    && Objects.equals(educationLevel, otherEditPersonDescriptor.educationLevel)
                    && educationLevelEdited == otherEditPersonDescriptor.educationLevelEdited
                    && Objects.equals(learningFocus, otherEditPersonDescriptor.learningFocus)
                    && learningFocusEdited == otherEditPersonDescriptor.learningFocusEdited
                    && Objects.equals(nextAction, otherEditPersonDescriptor.nextAction)
                    && nextActionEdited == otherEditPersonDescriptor.nextActionEdited
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
}
