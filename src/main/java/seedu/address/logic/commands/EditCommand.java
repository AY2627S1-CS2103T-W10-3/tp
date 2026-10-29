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

import java.util.List;
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
        Optional<Phone> updatedPhone = editPersonDescriptor.getPhone().applyTo(personToEdit.getPhone());
        Optional<Email> updatedEmail = editPersonDescriptor.getEmail().applyTo(personToEdit.getEmail());
        Optional<Address> updatedAddress = editPersonDescriptor.getAddress().applyTo(personToEdit.getAddress());
        Optional<Guardian> updatedGuardian = createUpdatedGuardian(personToEdit, editPersonDescriptor);
        Optional<Subject> updatedSubject = editPersonDescriptor.getSubject().applyTo(personToEdit.getSubject());
        Optional<EducationLevel> updatedEducationLevel =
                editPersonDescriptor.getEducationLevel().applyTo(personToEdit.getEducationLevel());
        Optional<LearningFocus> updatedLearningFocus =
                editPersonDescriptor.getLearningFocus().applyTo(personToEdit.getLearningFocus());
        Optional<NextAction> updatedNextAction =
                editPersonDescriptor.getNextAction().applyTo(personToEdit.getNextAction());
        Set<Tag> updatedTags = editPersonDescriptor.getTags().orElse(personToEdit.getTags());

        return new Person(updatedName, updatedPhone, updatedEmail, updatedAddress,
                updatedGuardian, updatedSubject, updatedEducationLevel, updatedLearningFocus,
                updatedNextAction, personToEdit.getLesson(), personToEdit.isArchived(), updatedTags);
    }

    /**
     * Builds the updated guardian for the edited student. Guardian field updates are applied to the student's
     * existing guardian. Clearing the guardian name also clears its phone and email, and supplying a guardian
     * phone or email while no guardian name remains is rejected.
     */
    private static Optional<Guardian> createUpdatedGuardian(Person personToEdit, EditPersonDescriptor descriptor)
            throws CommandException {
        Optional<Guardian> existingGuardian = personToEdit.getGuardian();
        Optional<Name> updatedName = descriptor.getGuardianName().applyTo(existingGuardian.map(Guardian::getName));

        boolean suppliesContactWithoutName = updatedName.isEmpty()
                && (descriptor.getGuardianPhone().value().isPresent()
                        || descriptor.getGuardianEmail().value().isPresent());
        if (suppliesContactWithoutName) {
            throw new CommandException(MESSAGE_GUARDIAN_NAME_REQUIRED);
        }

        boolean guardianRemoved = descriptor.getGuardianName().isEdited() && updatedName.isEmpty();
        Optional<Phone> updatedPhone = guardianRemoved ? Optional.empty()
                : descriptor.getGuardianPhone().applyTo(existingGuardian.flatMap(Guardian::getPhone));
        Optional<Email> updatedEmail = guardianRemoved ? Optional.empty()
                : descriptor.getGuardianEmail().applyTo(existingGuardian.flatMap(Guardian::getEmail));

        return updatedName.map(name -> new Guardian(name, updatedPhone, updatedEmail));
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
}
