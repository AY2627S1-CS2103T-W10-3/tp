package seedu.address.logic;

import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Stream;

import seedu.address.logic.parser.Prefix;
import seedu.address.model.person.Person;

/**
 * Container for user visible messages.
 */
public class Messages {

    public static final String MESSAGE_UNKNOWN_COMMAND = "Unknown command.";
    public static final String MESSAGE_INVALID_COMMAND_FORMAT = "Invalid command format!\n%1$s";
    public static final String MESSAGE_INVALID_PERSON_DISPLAYED_INDEX = "The person index provided is invalid.";
    public static final String MESSAGE_PERSONS_LISTED_OVERVIEW = "%1$d person(s) listed!";
    public static final String MESSAGE_DUPLICATE_FIELDS =
                "Multiple values specified for the following single-valued field(s): ";
    public static final String MESSAGE_DUPLICATE_ARCHIVED_PERSON =
            "An archived student with this name already exists. "
                    + "Use `list archived`, then `unarchive INDEX` to restore them.";
    public static final String NOT_PROVIDED = "Not provided";

    /**
     * Returns an error message indicating the duplicate prefixes.
     */
    public static String getErrorMessageForDuplicatePrefixes(Prefix... duplicatePrefixes) {
        assert duplicatePrefixes.length > 0;

        Set<String> duplicateFields = new TreeSet<>();
        Stream.of(duplicatePrefixes).map(Prefix::toString).forEach(duplicateFields::add);

        return MESSAGE_DUPLICATE_FIELDS + String.join(" ", duplicateFields);
    }

    /**
     * Formats the {@code person} for display to the user.
     */
    public static String format(Person person) {
        final StringBuilder builder = new StringBuilder();
        builder.append(person.getName())
                .append("; Phone: ")
                .append(person.getPhone().map(phone -> phone.value).orElse(NOT_PROVIDED))
                .append("; Email: ")
                .append(person.getEmail().map(email -> email.value).orElse(NOT_PROVIDED))
                .append("; Address: ")
                .append(person.getAddress().map(address -> address.value).orElse(NOT_PROVIDED))
                .append("; Subject: ")
                .append(person.getSubject().map(subject -> subject.getValue()).orElse(NOT_PROVIDED))
                .append("; Education level: ")
                .append(person.getEducationLevel().map(level -> level.getValue()).orElse(NOT_PROVIDED))
                .append("; Learning focus: ")
                .append(person.getLearningFocus().map(focus -> focus.getValue()).orElse(NOT_PROVIDED))
                .append("; Next action: ")
                .append(person.getNextAction().map(action -> action.getValue()).orElse(NOT_PROVIDED))
                .append("; Guardian: ")
                .append(person.getGuardian().map(guardian -> guardian.getName().fullName).orElse(NOT_PROVIDED))
                .append("; Guardian phone: ")
                .append(person.getGuardian().flatMap(guardian -> guardian.getPhone())
                        .map(phone -> phone.value).orElse(NOT_PROVIDED))
                .append("; Guardian email: ")
                .append(person.getGuardian().flatMap(guardian -> guardian.getEmail())
                        .map(email -> email.value).orElse(NOT_PROVIDED))
                .append("; Tags: ");
        person.getTags().forEach(builder::append);
        return builder.toString();
    }

}
