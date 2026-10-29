package seedu.address.logic;

import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Stream;

import seedu.address.logic.parser.Prefix;

/**
 * Container for user visible messages.
 */
public class Messages {

    public static final String MESSAGE_UNKNOWN_COMMAND = "Unknown command.";
    public static final String MESSAGE_INVALID_COMMAND_FORMAT = "Invalid command format!\n%1$s";
    public static final String MESSAGE_INVALID_PERSON_DISPLAYED_INDEX = "The student index provided is invalid.";
    public static final String MESSAGE_PERSONS_LISTED_OVERVIEW = "%1$d student(s) listed!";
    public static final String MESSAGE_DUPLICATE_FIELDS =
                "Multiple values specified for the following single-valued field(s): ";
    public static final String MESSAGE_DUPLICATE_ARCHIVED_PERSON =
            "An archived student with this name already exists. "
                    + "Use `list archived`, then `unarchive INDEX` to restore them.";
    public static final String MESSAGE_DISPLAYED_LIST_UPDATED =
            "The displayed list has been updated, so check the indices before your next command.";
    public static final String NOT_PROVIDED = "Not provided";
    public static final String NOT_SCHEDULED = "Not scheduled";

    /**
     * Returns an error message indicating the duplicate prefixes.
     */
    public static String getErrorMessageForDuplicatePrefixes(Prefix... duplicatePrefixes) {
        assert duplicatePrefixes.length > 0;

        Set<String> duplicateFields = new TreeSet<>();
        Stream.of(duplicatePrefixes).map(Prefix::toString).forEach(duplicateFields::add);

        return MESSAGE_DUPLICATE_FIELDS + String.join(" ", duplicateFields);
    }

}
