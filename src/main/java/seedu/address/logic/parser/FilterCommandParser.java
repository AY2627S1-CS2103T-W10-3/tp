package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EDUCATION_LEVEL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SUBJECT;

import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.address.logic.commands.FilterCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.EducationLevel;
import seedu.address.model.person.PersonMatchesFiltersPredicate;
import seedu.address.model.person.Subject;

/** Parses arguments for {@link FilterCommand}. */
public class FilterCommandParser implements Parser<FilterCommand> {
    private static final Pattern PREFIX_TOKEN = Pattern.compile("(?<!\\S)([A-Za-z][A-Za-z0-9]*/)");
    private static final Set<String> SUPPORTED_PREFIXES = Set.of(
            PREFIX_SUBJECT.toString(), PREFIX_EDUCATION_LEVEL.toString());

    @Override
    public FilterCommand parse(String args) throws ParseException {
        requireNonNull(args);
        rejectUnsupportedPrefixes(args);
        ArgumentMultimap arguments = ArgumentTokenizer.tokenize(args, PREFIX_SUBJECT, PREFIX_EDUCATION_LEVEL);
        if (!arguments.getPreamble().isBlank()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
        }
        arguments.verifyNoDuplicatePrefixesFor(PREFIX_SUBJECT, PREFIX_EDUCATION_LEVEL);

        Optional<Subject> subject = arguments.getValue(PREFIX_SUBJECT).isPresent()
                ? Optional.of(ParserUtil.parseSubject(normalizeFreeText(
                        arguments.getValue(PREFIX_SUBJECT).get())))
                : Optional.empty();
        Optional<EducationLevel> educationLevel = arguments.getValue(PREFIX_EDUCATION_LEVEL).isPresent()
                ? Optional.of(ParserUtil.parseEducationLevel(arguments.getValue(PREFIX_EDUCATION_LEVEL).get()))
                : Optional.empty();
        if (subject.isEmpty() && educationLevel.isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
        }
        return new FilterCommand(new PersonMatchesFiltersPredicate(subject, educationLevel));
    }

    private static void rejectUnsupportedPrefixes(String args) throws ParseException {
        Matcher matcher = PREFIX_TOKEN.matcher(args);
        while (matcher.find()) {
            if (!SUPPORTED_PREFIXES.contains(matcher.group(1))) {
                throw new ParseException("Unsupported prefix in filter command: " + matcher.group(1));
            }
        }
    }

    private static String normalizeFreeText(String value) {
        return value.strip().replaceAll("\\s+", " ");
    }
}
