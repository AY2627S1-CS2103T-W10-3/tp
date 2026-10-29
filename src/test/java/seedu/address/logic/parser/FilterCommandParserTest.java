package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EDUCATION_LEVEL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SUBJECT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.FilterCommand;
import seedu.address.model.person.EducationLevel;
import seedu.address.model.person.PersonMatchesFiltersPredicate;
import seedu.address.model.person.Subject;

public class FilterCommandParserTest {
    private final FilterCommandParser parser = new FilterCommandParser();

    @Test
    public void parse_noFilters_failure() {
        assertParseFailure(parser, "   ", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                FilterCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_subjectOnly_success() {
        FilterCommand expected = command("H2 Mathematics", null);
        assertParseSuccess(parser, " s/  H2   Mathematics  ", expected);
    }

    @Test
    public void parse_prefixLikeTextInsideSubject_success() {
        assertParseSuccess(parser, " s/A Level/Mathematics", command("A Level/Mathematics", null));
        assertParseSuccess(parser, " s/\"A l/Mathematics x/Extension\"",
                command("A l/Mathematics x/Extension", null));
        assertParseSuccess(parser, " s/English \"l/Literature\"",
                command("English \"l/Literature\"", null));
    }

    @Test
    public void parse_educationLevelOnly_success() {
        assertParseSuccess(parser, " l/jc1", command(null, "JC1"));
    }

    @Test
    public void parse_bothFiltersInAnyOrder_success() {
        FilterCommand expected = command("Mathematics", "S3");
        assertParseSuccess(parser, " s/Mathematics l/s3", expected);
        assertParseSuccess(parser, " l/S3 s/Mathematics", expected);
    }

    @Test
    public void parse_repeatedPrefix_failure() {
        assertParseFailure(parser, " s/Math s/Physics",
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_SUBJECT));
        assertParseFailure(parser, " l/S3 l/JC1",
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_EDUCATION_LEVEL));
    }

    @Test
    public void parse_invalidOrUnsupportedValue_failure() {
        assertParseFailure(parser, " s/", Subject.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, " s/Math\tPhysics", Subject.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, " l/University", EducationLevel.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, " s/Math x/value", "Unsupported prefix in filter command: x/");
        assertParseFailure(parser, "unexpected s/Math", String.format(
                MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
    }

    private static FilterCommand command(String subject, String educationLevel) {
        Optional<Subject> optionalSubject = subject == null
                ? Optional.empty() : Optional.of(new Subject(subject));
        Optional<EducationLevel> optionalLevel = educationLevel == null
                ? Optional.empty() : Optional.of(new EducationLevel(educationLevel));
        return new FilterCommand(new PersonMatchesFiltersPredicate(optionalSubject, optionalLevel));
    }
}
