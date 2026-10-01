package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.model.Model;

/**
 * Reports the number of persons currently shown in the address book.
 */
public class StatsCommand extends Command {

    public static final String COMMAND_WORD = "stats";

    public static final String MESSAGE_SUCCESS = "You have %1$d contact(s) in the address book.";

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        int count = model.getFilteredPersonList().size();
        return new CommandResult(String.format(MESSAGE_SUCCESS, count));
    }
}
