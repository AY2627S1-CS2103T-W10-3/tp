package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EDUCATION_LEVEL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SUBJECT;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.person.PersonMatchesFiltersPredicate;

/** Filters students by subject, education level, or both. */
public class FilterCommand extends Command {
    public static final String COMMAND_WORD = "filter";
    public static final String MESSAGE_NO_MATCHES = "No students match the filters.";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Filters students by subject, education level, or both.\n"
            + "Parameters: [" + PREFIX_SUBJECT + "SUBJECT] ["
            + PREFIX_EDUCATION_LEVEL + "EDUCATION_LEVEL]\n"
            + "Example: " + COMMAND_WORD + " " + PREFIX_SUBJECT + "H2 Mathematics "
            + PREFIX_EDUCATION_LEVEL + "JC1";

    private final PersonMatchesFiltersPredicate predicate;

    public FilterCommand(PersonMatchesFiltersPredicate predicate) {
        this.predicate = requireNonNull(predicate);
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredPersonList(predicate);
        if (model.getFilteredPersonList().isEmpty()) {
            return new CommandResult(MESSAGE_NO_MATCHES);
        }
        return new CommandResult(String.format(
                Messages.MESSAGE_PERSONS_LISTED_OVERVIEW, model.getFilteredPersonList().size()));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        return other instanceof FilterCommand otherCommand
                && predicate.equals(otherCommand.predicate);
    }

    @Override
    public int hashCode() {
        return predicate.hashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("predicate", predicate).toString();
    }
}
