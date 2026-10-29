package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.ScheduleCommand;
import seedu.address.model.person.Lesson;

public class ScheduleCommandParserTest {

    private static final String MESSAGE_INVALID_FORMAT =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, ScheduleCommand.MESSAGE_USAGE);
    private static final Lesson LESSON = new Lesson("2026-10-30 16:00");

    private final ScheduleCommandParser parser = new ScheduleCommandParser();

    @Test
    public void parse_validArgs_returnsScheduleCommand() {
        assertParseSuccess(parser, " 1 at/2026-10-30 16:00", new ScheduleCommand(INDEX_FIRST_PERSON, LESSON));
        assertParseSuccess(parser, " 2 at/2026-10-30 16:00", new ScheduleCommand(INDEX_SECOND_PERSON, LESSON));
    }

    @Test
    public void parse_extraWhitespace_returnsScheduleCommand() {
        assertParseSuccess(parser, "   1   at/  2026-10-30   16:00  ",
                new ScheduleCommand(INDEX_FIRST_PERSON, LESSON));
    }

    @Test
    public void parse_missingOrInvalidIndex_throwsParseException() {
        assertParseFailure(parser, " at/2026-10-30 16:00", MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser, " 0 at/2026-10-30 16:00", MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser, " -1 at/2026-10-30 16:00", MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser, " a at/2026-10-30 16:00", MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser, " 1 2 at/2026-10-30 16:00", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_missingLesson_throwsParseException() {
        assertParseFailure(parser, "", MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser, " 1", MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser, " 1 2026-10-30 16:00", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidLesson_throwsParseException() {
        assertParseFailure(parser, " 1 at/", Lesson.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, " 1 at/2026-10-30", Lesson.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, " 1 at/30-10-2026 16:00", Lesson.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, " 1 at/2026-02-30 16:00", Lesson.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, " 1 at/2026-10-30 24:00", Lesson.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, " 1 at/+12345-01-01 10:00", Lesson.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, " 1 at/2026-10-30 4pm", Lesson.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_repeatedLessonPrefix_throwsParseException() {
        assertParseFailure(parser, " 1 at/2026-10-30 16:00 at/2026-10-31 16:00",
                Messages.getErrorMessageForDuplicatePrefixes(CliSyntax.PREFIX_LESSON));
    }

    @Test
    public void parse_unsupportedPrefix_throwsParseException() {
        assertParseFailure(parser, " 1 at/2026-10-30 16:00 n/Alex",
                "Unsupported prefix in schedule command: n/");
    }
}
