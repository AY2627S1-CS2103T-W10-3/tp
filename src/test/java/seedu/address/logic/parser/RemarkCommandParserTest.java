package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.RemarkCommand;
import seedu.address.model.person.Remark;

public class RemarkCommandParserTest {

    private static final String MESSAGE_INVALID_FORMAT =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, RemarkCommand.MESSAGE_USAGE);

    private final RemarkCommandParser parser = new RemarkCommandParser();

    @Test
    public void parse_indexAndRemark_returnsRemarkCommand() {
        assertParseSuccess(parser, "1 r/Prefers evening lessons.",
                new RemarkCommand(INDEX_FIRST_PERSON, Optional.of(new Remark("Prefers evening lessons."))));
    }

    @Test
    public void parse_emptyRemark_returnsRemoveRemarkCommand() {
        assertParseSuccess(parser, "1 r/", new RemarkCommand(INDEX_FIRST_PERSON, Optional.empty()));
        assertParseSuccess(parser, "1 r/   ", new RemarkCommand(INDEX_FIRST_PERSON, Optional.empty()));
    }

    @Test
    public void parse_missingFields_throwsParseException() {
        assertParseFailure(parser, "", MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser, "1", MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser, "r/Prefers evening lessons.", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidIndex_throwsParseException() {
        assertParseFailure(parser, "0 r/Prefers evening lessons.", MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser, "one r/Prefers evening lessons.", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_duplicateRemark_throwsParseException() {
        assertParseFailure(parser, "1 r/first r/second",
                Messages.getErrorMessageForDuplicatePrefixes(CliSyntax.PREFIX_REMARK));
    }

    @Test
    public void parse_tooLongRemark_throwsParseException() {
        assertParseFailure(parser, "1 r/" + "a".repeat(Remark.MAX_LENGTH + 1), Remark.MESSAGE_CONSTRAINTS);
    }
}
