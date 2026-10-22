package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.UpcomingCommand;

public class UpcomingCommandParserTest {

    private static final String MESSAGE_INVALID_FORMAT =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, UpcomingCommand.MESSAGE_USAGE);

    private final UpcomingCommandParser parser = new UpcomingCommandParser();

    @Test
    public void parse_noArgs_returnsUpcomingCommand() {
        assertParseSuccess(parser, "", new UpcomingCommand());
        assertParseSuccess(parser, "   ", new UpcomingCommand());
    }

    @Test
    public void parse_extraArgs_throwsParseException() {
        assertParseFailure(parser, "3", MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser, "today", MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser, " at/2099-10-30 16:00", MESSAGE_INVALID_FORMAT);
    }
}
