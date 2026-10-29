package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EDUCATION_LEVEL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SUBJECT;

import java.util.Optional;

import seedu.address.logic.commands.FilterCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.EducationLevel;
import seedu.address.model.person.PersonMatchesFiltersPredicate;
import seedu.address.model.person.Subject;

/** Parses arguments for {@link FilterCommand}. */
public class FilterCommandParser implements Parser<FilterCommand> {
    @Override
    public FilterCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap arguments = CommandArgumentTokenizer.tokenize(args, FilterCommand.COMMAND_WORD,
                PREFIX_SUBJECT, PREFIX_EDUCATION_LEVEL);
        if (!arguments.getPreamble().isBlank()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
        }
        arguments.verifyNoDuplicatePrefixesFor(PREFIX_SUBJECT, PREFIX_EDUCATION_LEVEL);

        Optional<Subject> subject = arguments.getValue(PREFIX_SUBJECT).isPresent()
                ? Optional.of(ParserUtil.parseSubject(arguments.getValue(PREFIX_SUBJECT).get()))
                : Optional.empty();
        Optional<EducationLevel> educationLevel = arguments.getValue(PREFIX_EDUCATION_LEVEL).isPresent()
                ? Optional.of(ParserUtil.parseEducationLevel(arguments.getValue(PREFIX_EDUCATION_LEVEL).get()))
                : Optional.empty();
        if (subject.isEmpty() && educationLevel.isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
        }
        return new FilterCommand(new PersonMatchesFiltersPredicate(subject, educationLevel));
    }
}
