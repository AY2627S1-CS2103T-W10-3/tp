package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.ADDRESS_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.ADDRESS_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.EDUCATION_LEVEL_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.EDUCATION_LEVEL_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.EMAIL_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.EMAIL_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.GUARDIAN_EMAIL_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.GUARDIAN_EMAIL_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.GUARDIAN_NAME_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.GUARDIAN_NAME_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.GUARDIAN_PHONE_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.GUARDIAN_PHONE_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_ADDRESS_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_EDUCATION_LEVEL_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_EMAIL_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_GUARDIAN_EMAIL_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_GUARDIAN_NAME_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_GUARDIAN_PHONE_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_NAME_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_PHONE_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_SUBJECT_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_TAG_DESC;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.PHONE_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.PHONE_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.PREAMBLE_NON_EMPTY;
import static seedu.address.logic.commands.CommandTestUtil.PREAMBLE_WHITESPACE;
import static seedu.address.logic.commands.CommandTestUtil.SUBJECT_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.SUBJECT_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_FRIEND;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_HUSBAND;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EDUCATION_LEVEL_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_GUARDIAN_EMAIL_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_GUARDIAN_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_GUARDIAN_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_SUBJECT_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_FRIEND;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EDUCATION_LEVEL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GUARDIAN_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GUARDIAN_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GUARDIAN_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SUBJECT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalPersons.AMY;
import static seedu.address.testutil.TypicalPersons.BOB;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.AddCommand;
import seedu.address.model.person.Address;
import seedu.address.model.person.EducationLevel;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Subject;
import seedu.address.model.tag.Tag;
import seedu.address.testutil.PersonBuilder;

public class AddCommandParserTest {
    private AddCommandParser parser = new AddCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        Person expectedPerson = new PersonBuilder(BOB).withTags(VALID_TAG_FRIEND).build();

        // whitespace only preamble
        assertParseSuccess(parser, PREAMBLE_WHITESPACE + NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB
                + ADDRESS_DESC_BOB + TAG_DESC_FRIEND, new AddCommand(expectedPerson));


        // Each prefix has one value, including the optional tag prefix.
        assertParseFailure(parser, NAME_DESC_BOB + TAG_DESC_HUSBAND + TAG_DESC_FRIEND,
                Messages.getErrorMessageForDuplicatePrefixes(CliSyntax.PREFIX_TAG));
    }

    @Test
    public void parse_allMvpPrefixesInAnyOrder_success() {
        Person expectedPerson = new PersonBuilder().withName("Bob Choo").withPhone(VALID_PHONE_BOB)
                .withEmail(VALID_EMAIL_BOB).withAddress(VALID_ADDRESS_BOB)
                .withSubject(VALID_SUBJECT_BOB).withEducationLevel(VALID_EDUCATION_LEVEL_BOB)
                .withGuardian(VALID_GUARDIAN_NAME_BOB, VALID_GUARDIAN_PHONE_BOB, VALID_GUARDIAN_EMAIL_BOB)
                .withTags(VALID_TAG_FRIEND).build();
        assertParseSuccess(parser, TAG_DESC_FRIEND + GUARDIAN_EMAIL_DESC_BOB + SUBJECT_DESC_BOB
                + ADDRESS_DESC_BOB + EDUCATION_LEVEL_DESC_BOB + PHONE_DESC_BOB + GUARDIAN_PHONE_DESC_BOB
                + EMAIL_DESC_BOB + GUARDIAN_NAME_DESC_BOB + NAME_DESC_BOB, new AddCommand(expectedPerson));
    }

    @Test
    public void parse_whitespaceAroundFields_normalizesFreeText() {
        Person expectedPerson = new PersonBuilder().withName("Bob Choo").withoutPhone().withoutEmail()
                .withoutAddress().withGuardianName("Jane Doe").withSubject("H2 Mathematics")
                .withEducationLevel("JC2").withTags().build();
        assertParseSuccess(parser, "\t s/ H2   Mathematics \t gn/ Jane   Doe \t l/ jc2 \t n/ Bob   Choo  ",
                new AddCommand(expectedPerson));
    }

    @Test
    public void parse_unsupportedPrefixLikeToken_failure() {
        String expectedMessage = "Unsupported prefix in add command: x/";
        assertParseFailure(parser, " n/Bob Choo x/extra", expectedMessage);
        assertParseFailure(parser, " x/extra n/Bob Choo", expectedMessage);
    }

    @Test
    public void parse_prefixLikeTextInsideFreeText_success() {
        Person withLongPrefixLikeText = new PersonBuilder().withName("Alice").withoutPhone().withoutEmail()
                .withoutAddress().withoutGuardian().withSubject("A Level/Mathematics")
                .withoutEducationLevel().withTags().build();
        assertParseSuccess(parser, " n/Alice s/A Level/Mathematics", new AddCommand(withLongPrefixLikeText));

        Person withQuotedPrefixLikeText = new PersonBuilder().withName("Alice").withoutPhone().withoutEmail()
                .withAddress("Block n/7 x/West").withoutGuardian().withSubject("A l/Mathematics")
                .withoutEducationLevel().withTags().build();
        assertParseSuccess(parser, " n/Alice a/\"Block n/7 x/West\" s/\"A l/Mathematics\"",
                new AddCommand(withQuotedPrefixLikeText));

        Person withLiteralQuotes = new PersonBuilder().withName("Alice").withoutPhone().withoutEmail()
                .withoutAddress().withoutGuardian().withSubject("English \"l/Literature\"")
                .withoutEducationLevel().withTags().build();
        assertParseSuccess(parser, " n/Alice s/English \"l/Literature\"",
                new AddCommand(withLiteralQuotes));
    }

    @Test
    public void parse_unclosedQuotedValue_failure() {
        assertParseFailure(parser, " n/Alice a/\"Block n/7",
                "Unclosed quoted value in add command.");
    }

    @Test
    public void parse_multipleErrors_reportsStructuralErrorBeforeValueError() {
        assertParseFailure(parser, "surprise n/Bob Choo p/abc p/123", String.format(
                MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
        assertParseFailure(parser, "n/Bob Choo e/invalid e/bob@example.com",
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_EMAIL));
        assertParseFailure(parser, "n/Bob Choo p/bad gp/bad", AddCommandParser.MESSAGE_GUARDIAN_NAME_REQUIRED);
        assertParseFailure(parser, "e/invalid n/Bob Choo p/bad", Phone.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "p/bad n/Bob Choo e/invalid", Phone.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "n/Bob Choo e/invalid p/bad p/123",
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE));
    }

    @Test
    public void parse_repeatedNonTagValue_failure() {
        String validExpectedPersonString = NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB
                + ADDRESS_DESC_BOB;

        // multiple names
        assertParseFailure(parser, NAME_DESC_AMY + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME));

        // multiple phones
        assertParseFailure(parser, PHONE_DESC_AMY + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE));

        // multiple emails
        assertParseFailure(parser, EMAIL_DESC_AMY + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_EMAIL));

        // multiple addresses
        assertParseFailure(parser, ADDRESS_DESC_AMY + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_ADDRESS));

        // multiple guardian fields
        assertParseFailure(parser, validExpectedPersonString + GUARDIAN_NAME_DESC_AMY + GUARDIAN_NAME_DESC_BOB,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_GUARDIAN_NAME));
        assertParseFailure(parser, validExpectedPersonString + GUARDIAN_PHONE_DESC_AMY + GUARDIAN_PHONE_DESC_BOB,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_GUARDIAN_PHONE));
        assertParseFailure(parser, validExpectedPersonString + GUARDIAN_EMAIL_DESC_AMY + GUARDIAN_EMAIL_DESC_BOB,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_GUARDIAN_EMAIL));

        assertParseFailure(parser, validExpectedPersonString + SUBJECT_DESC_AMY + SUBJECT_DESC_BOB,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_SUBJECT));
        assertParseFailure(parser, validExpectedPersonString + EDUCATION_LEVEL_DESC_AMY + EDUCATION_LEVEL_DESC_BOB,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_EDUCATION_LEVEL));

        // multiple fields repeated
        assertParseFailure(parser,
                validExpectedPersonString + PHONE_DESC_AMY + EMAIL_DESC_AMY + NAME_DESC_AMY + ADDRESS_DESC_AMY
                        + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME, PREFIX_ADDRESS, PREFIX_EMAIL, PREFIX_PHONE));

        // invalid value followed by valid value

        // invalid name
        assertParseFailure(parser, INVALID_NAME_DESC + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME));

        // invalid email
        assertParseFailure(parser, INVALID_EMAIL_DESC + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_EMAIL));

        // invalid phone
        assertParseFailure(parser, INVALID_PHONE_DESC + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE));

        // invalid address
        assertParseFailure(parser, INVALID_ADDRESS_DESC + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_ADDRESS));

        // valid value followed by invalid value

        // invalid name
        assertParseFailure(parser, validExpectedPersonString + INVALID_NAME_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME));

        // invalid email
        assertParseFailure(parser, validExpectedPersonString + INVALID_EMAIL_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_EMAIL));

        // invalid phone
        assertParseFailure(parser, validExpectedPersonString + INVALID_PHONE_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE));

        // invalid address
        assertParseFailure(parser, validExpectedPersonString + INVALID_ADDRESS_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_ADDRESS));
    }

    @Test
    public void parse_optionalFieldsMissing_success() {
        // zero tags
        Person expectedPerson = new PersonBuilder(AMY).withTags().build();
        assertParseSuccess(parser, NAME_DESC_AMY + PHONE_DESC_AMY + EMAIL_DESC_AMY + ADDRESS_DESC_AMY,
                new AddCommand(expectedPerson));

        Person nameOnly = new PersonBuilder().withName(VALID_NAME_BOB).withoutPhone().withoutEmail()
                .withoutAddress().withTags().build();
        assertParseSuccess(parser, NAME_DESC_BOB, new AddCommand(nameOnly));

        Person withPhone = new PersonBuilder(nameOnly).withPhone(VALID_PHONE_BOB).build();
        assertParseSuccess(parser, NAME_DESC_BOB + PHONE_DESC_BOB, new AddCommand(withPhone));

        Person withGuardianName = new PersonBuilder(nameOnly).withGuardianName(VALID_GUARDIAN_NAME_BOB).build();
        assertParseSuccess(parser, NAME_DESC_BOB + GUARDIAN_NAME_DESC_BOB, new AddCommand(withGuardianName));
    }

    @Test
    public void parse_guardianDetailsPresent_success() {
        Person expectedPerson = new PersonBuilder().withName(VALID_NAME_BOB).withoutPhone().withoutEmail()
                .withoutAddress().withTags()
                .withGuardian(VALID_GUARDIAN_NAME_BOB, VALID_GUARDIAN_PHONE_BOB, VALID_GUARDIAN_EMAIL_BOB)
                .build();

        assertParseSuccess(parser, GUARDIAN_EMAIL_DESC_BOB + NAME_DESC_BOB
                + GUARDIAN_NAME_DESC_BOB + GUARDIAN_PHONE_DESC_BOB, new AddCommand(expectedPerson));
    }

    @Test
    public void parse_tutoringDetailsPresent_success() {
        Person expectedPerson = new PersonBuilder().withName(VALID_NAME_BOB).withoutPhone().withoutEmail()
                .withoutAddress().withTags().withSubject(VALID_SUBJECT_BOB)
                .withEducationLevel(VALID_EDUCATION_LEVEL_BOB).build();

        assertParseSuccess(parser, EDUCATION_LEVEL_DESC_BOB + NAME_DESC_BOB + SUBJECT_DESC_BOB,
                new AddCommand(expectedPerson));
    }

    @Test
    public void parse_guardianContactWithoutName_failure() {
        assertParseFailure(parser, NAME_DESC_BOB + GUARDIAN_PHONE_DESC_BOB,
                AddCommandParser.MESSAGE_GUARDIAN_NAME_REQUIRED);
        assertParseFailure(parser, NAME_DESC_BOB + GUARDIAN_EMAIL_DESC_BOB,
                AddCommandParser.MESSAGE_GUARDIAN_NAME_REQUIRED);
        assertParseFailure(parser, NAME_DESC_BOB + GUARDIAN_PHONE_DESC_BOB + GUARDIAN_EMAIL_DESC_BOB,
                AddCommandParser.MESSAGE_GUARDIAN_NAME_REQUIRED);
    }

    @Test
    public void parse_compulsoryFieldMissing_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE);

        // missing name prefix
        assertParseFailure(parser, VALID_NAME_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB,
                expectedMessage);

        // all prefixes missing
        assertParseFailure(parser, VALID_NAME_BOB + VALID_PHONE_BOB + VALID_EMAIL_BOB + VALID_ADDRESS_BOB,
                expectedMessage);
    }

    @Test
    public void parse_invalidValue_failure() {
        // invalid name
        assertParseFailure(parser, INVALID_NAME_DESC + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
                + TAG_DESC_FRIEND, Name.MESSAGE_CONSTRAINTS);

        // invalid phone
        assertParseFailure(parser, NAME_DESC_BOB + INVALID_PHONE_DESC + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
                + TAG_DESC_FRIEND, Phone.MESSAGE_CONSTRAINTS);

        // invalid email
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + INVALID_EMAIL_DESC + ADDRESS_DESC_BOB
                + TAG_DESC_FRIEND, Email.MESSAGE_CONSTRAINTS);

        // invalid address
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + INVALID_ADDRESS_DESC
                + TAG_DESC_FRIEND, Address.MESSAGE_CONSTRAINTS);

        // invalid tag
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
                + INVALID_TAG_DESC, Tag.MESSAGE_CONSTRAINTS);

        // invalid guardian name
        assertParseFailure(parser, NAME_DESC_BOB + INVALID_GUARDIAN_NAME_DESC, Name.MESSAGE_CONSTRAINTS);

        // invalid guardian phone
        assertParseFailure(parser, NAME_DESC_BOB + GUARDIAN_NAME_DESC_BOB + INVALID_GUARDIAN_PHONE_DESC,
                Phone.MESSAGE_CONSTRAINTS);

        // invalid guardian email
        assertParseFailure(parser, NAME_DESC_BOB + GUARDIAN_NAME_DESC_BOB + INVALID_GUARDIAN_EMAIL_DESC,
                Email.MESSAGE_CONSTRAINTS);

        assertParseFailure(parser, NAME_DESC_BOB + INVALID_SUBJECT_DESC, Subject.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, NAME_DESC_BOB + INVALID_EDUCATION_LEVEL_DESC,
                EducationLevel.MESSAGE_CONSTRAINTS);

        // two invalid values, only first invalid value reported
        assertParseFailure(parser, INVALID_NAME_DESC + PHONE_DESC_BOB + EMAIL_DESC_BOB + INVALID_ADDRESS_DESC,
                Name.MESSAGE_CONSTRAINTS);

        // non-empty preamble
        assertParseFailure(parser, PREAMBLE_NON_EMPTY + NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB
                + ADDRESS_DESC_BOB + TAG_DESC_HUSBAND + TAG_DESC_FRIEND,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
    }
}
