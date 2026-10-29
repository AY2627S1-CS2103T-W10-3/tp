package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.FindCommand;
import seedu.address.model.person.NameContainsKeywordsPredicate;

public class FindCommandParserTest {

    private FindCommandParser parser = new FindCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ", String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validArgs_returnsFindCommand() {
        // no leading and trailing whitespaces
        FindCommand expectedFindCommand =
                new FindCommand(new NameContainsKeywordsPredicate(List.of("Alice", "Bob")));
        assertParseSuccess(parser, "Alice Bob", expectedFindCommand);

        // multiple whitespaces between keywords
        assertParseSuccess(parser, " \n Alice \n \t Bob  \t", expectedFindCommand);

        // repeated keywords are accepted and remain harmless during matching
        expectedFindCommand = new FindCommand(new NameContainsKeywordsPredicate(List.of("Ali", "Ali")));
        assertParseSuccess(parser, "Ali Ali", expectedFindCommand);
    }

    @Test
    public void parse_prefixLikeKeyword_returnsFindCommandWithHint() {
        assertParseSuccess(parser, "n/alex",
                new FindCommand(new NameContainsKeywordsPredicate(List.of("n/alex")), true));

        // prefix detection ignores case
        assertParseSuccess(parser, "S/Mathematics",
                new FindCommand(new NameContainsKeywordsPredicate(List.of("S/Mathematics")), true));

        // one prefix-like keyword among plain keywords is enough
        assertParseSuccess(parser, "alex gn/mary",
                new FindCommand(new NameContainsKeywordsPredicate(List.of("alex", "gn/mary")), true));
    }

    @Test
    public void parse_slashInsideKeyword_returnsFindCommandWithoutHint() {
        assertParseSuccess(parser, "raj/kumar",
                new FindCommand(new NameContainsKeywordsPredicate(List.of("raj/kumar")), false));
    }

}
