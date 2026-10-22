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
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.CollectionUtil;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
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

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the details of the person identified "
            + "by the index number used in the displayed person list. "
            + "Existing values will be overwritten by the input values.\n"
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

    public static final String MESSAGE_EDIT_PERSON_SUCCESS = "Edited person: %1$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_DUPLICATE_PERSON = "This person already exists in the address book.";
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
        List<Person> lastShownList = model.getFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToEdit = lastShownList.get(index.getZeroBased());
        Person editedPerson = createEditedPerson(personToEdit, editPersonDescriptor);

        if (!personToEdit.isSamePerson(editedPerson) && model.hasPerson(editedPerson)) {
            throw new CommandException(MESSAGE_DUPLICATE_PERSON);
        }

        model.setPerson(personToEdit, editedPerson);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        return new CommandResult(String.format(MESSAGE_EDIT_PERSON_SUCCESS, Messages.format(editedPerson)));
    }

    /**
     * Creates and returns a {@code Person} with the details of {@code personToEdit}
     * edited with {@code editPersonDescriptor}.
     */
    private static Person createEditedPerson(Person personToEdit, EditPersonDescriptor editPersonDescriptor)
            throws CommandException {
        assert personToEdit != null;

        Name updatedName = editPersonDescriptor.getName().orElse(personToEdit.getName());
        Optional<Phone> updatedPhone = editPersonDescriptor.getPhone().or(personToEdit::getPhone);
        Optional<Email> updatedEmail = editPersonDescriptor.getEmail().or(personToEdit::getEmail);
        Optional<Address> updatedAddress = editPersonDescriptor.getAddress().or(personToEdit::getAddress);
        Optional<Name> updatedGuardianName = editPersonDescriptor.getGuardianName()
                .or(() -> personToEdit.getGuardian().map(Guardian::getName));
        Optional<Phone> updatedGuardianPhone = editPersonDescriptor.getGuardianPhone()
                .or(() -> personToEdit.getGuardian().flatMap(Guardian::getPhone));
        Optional<Email> updatedGuardianEmail = editPersonDescriptor.getGuardianEmail()
                .or(() -> personToEdit.getGuardian().flatMap(Guardian::getEmail));
        if (updatedGuardianName.isEmpty() && (updatedGuardianPhone.isPresent() || updatedGuardianEmail.isPresent())) {
            throw new CommandException(MESSAGE_GUARDIAN_NAME_REQUIRED);
        }
        Optional<Guardian> updatedGuardian = updatedGuardianName.map(name ->
                new Guardian(name, updatedGuardianPhone, updatedGuardianEmail));
        Optional<Subject> updatedSubject = editPersonDescriptor.getSubject().or(personToEdit::getSubject);
        Optional<EducationLevel> updatedEducationLevel = editPersonDescriptor.getEducationLevel()
                .or(personToEdit::getEducationLevel);
        Optional<LearningFocus> updatedLearningFocus = editPersonDescriptor.getLearningFocus()
                .or(personToEdit::getLearningFocus);
        Optional<NextAction> updatedNextAction = editPersonDescriptor.getNextAction()
                .or(personToEdit::getNextAction);
        Set<Tag> updatedTags = editPersonDescriptor.getTags().orElse(personToEdit.getTags());

        return new Person(updatedName, updatedPhone, updatedEmail, updatedAddress,
                updatedGuardian, updatedSubject, updatedEducationLevel, updatedLearningFocus,
                updatedNextAction, personToEdit.getLesson(), updatedTags);
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
        private Email email;
        private Address address;
        private Name guardianName;
        private Phone guardianPhone;
        private Email guardianEmail;
        private Subject subject;
        private EducationLevel educationLevel;
        private LearningFocus learningFocus;
        private NextAction nextAction;
        private Set<Tag> tags;

        public EditPersonDescriptor() {}

        /**
         * Copy constructor.
         * A defensive copy of {@code tags} is used internally.
         */
        public EditPersonDescriptor(EditPersonDescriptor toCopy) {
            setName(toCopy.name);
            setPhone(toCopy.phone);
            setEmail(toCopy.email);
            setAddress(toCopy.address);
            setGuardianName(toCopy.guardianName);
            setGuardianPhone(toCopy.guardianPhone);
            setGuardianEmail(toCopy.guardianEmail);
            setSubject(toCopy.subject);
            setEducationLevel(toCopy.educationLevel);
            setLearningFocus(toCopy.learningFocus);
            setNextAction(toCopy.nextAction);
            setTags(toCopy.tags);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(name, phone, email, address, guardianName,
                    guardianPhone, guardianEmail, subject, educationLevel, learningFocus, nextAction, tags);
        }

        public void setName(Name name) {
            this.name = name;
        }

        public Optional<Name> getName() {
            return Optional.ofNullable(name);
        }

        public void setPhone(Phone phone) {
            this.phone = phone;
        }

        public Optional<Phone> getPhone() {
            return Optional.ofNullable(phone);
        }

        public void setEmail(Email email) {
            this.email = email;
        }

        public Optional<Email> getEmail() {
            return Optional.ofNullable(email);
        }

        public void setAddress(Address address) {
            this.address = address;
        }

        public Optional<Address> getAddress() {
            return Optional.ofNullable(address);
        }

        public void setGuardianName(Name guardianName) {
            this.guardianName = guardianName;
        }

        public Optional<Name> getGuardianName() {
            return Optional.ofNullable(guardianName);
        }

        public void setGuardianPhone(Phone guardianPhone) {
            this.guardianPhone = guardianPhone;
        }

        public Optional<Phone> getGuardianPhone() {
            return Optional.ofNullable(guardianPhone);
        }

        public void setGuardianEmail(Email guardianEmail) {
            this.guardianEmail = guardianEmail;
        }

        public Optional<Email> getGuardianEmail() {
            return Optional.ofNullable(guardianEmail);
        }

        public void setSubject(Subject subject) {
            this.subject = subject;
        }

        public Optional<Subject> getSubject() {
            return Optional.ofNullable(subject);
        }

        public void setEducationLevel(EducationLevel educationLevel) {
            this.educationLevel = educationLevel;
        }

        public Optional<EducationLevel> getEducationLevel() {
            return Optional.ofNullable(educationLevel);
        }

        public void setLearningFocus(LearningFocus learningFocus) {
            this.learningFocus = learningFocus;
        }

        public Optional<LearningFocus> getLearningFocus() {
            return Optional.ofNullable(learningFocus);
        }

        public void setNextAction(NextAction nextAction) {
            this.nextAction = nextAction;
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
                    && Objects.equals(email, otherEditPersonDescriptor.email)
                    && Objects.equals(address, otherEditPersonDescriptor.address)
                    && Objects.equals(guardianName, otherEditPersonDescriptor.guardianName)
                    && Objects.equals(guardianPhone, otherEditPersonDescriptor.guardianPhone)
                    && Objects.equals(guardianEmail, otherEditPersonDescriptor.guardianEmail)
                    && Objects.equals(subject, otherEditPersonDescriptor.subject)
                    && Objects.equals(educationLevel, otherEditPersonDescriptor.educationLevel)
                    && Objects.equals(learningFocus, otherEditPersonDescriptor.learningFocus)
                    && Objects.equals(nextAction, otherEditPersonDescriptor.nextAction)
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
