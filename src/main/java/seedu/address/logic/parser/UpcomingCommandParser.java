package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.logic.commands.UpcomingCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/** Parses input for {@link UpcomingCommand}, which takes no arguments. */
public class UpcomingCommandParser implements Parser<UpcomingCommand> {

    @Override
    public UpcomingCommand parse(String args) throws ParseException {
        if (!args.trim().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, UpcomingCommand.MESSAGE_USAGE));
        }
        return new UpcomingCommand();
    }
}
