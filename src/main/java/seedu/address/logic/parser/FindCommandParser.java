package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.NameContainsKeywordsPredicate;

/**
 * Parses input arguments and creates a new FindCommand object
 */
public class FindCommandParser implements Parser<FindCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the FindCommand
     * and returns a FindCommand object for execution.
     * @throws ParseException if the user input does not conform to the expected format
     */
    public FindCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }

        String[] nameKeywords = trimmedArgs.split("\\s+");
        boolean hasPrefixLikeKeyword = Stream.of(nameKeywords).anyMatch(FindCommandParser::startsWithPrefix);

        return new FindCommand(new NameContainsKeywordsPredicate(List.of(nameKeywords)), hasPrefixLikeKeyword);
    }

    /**
     * Returns true if {@code keyword} starts with a command prefix such as {@code n/}, ignoring case.
     */
    private static boolean startsWithPrefix(String keyword) {
        String lowerCaseKeyword = keyword.toLowerCase(Locale.ROOT);
        return CliSyntax.ALL_PREFIXES.stream().anyMatch(prefix -> lowerCaseKeyword.startsWith(prefix.getPrefix()));
    }

}
