package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EDUCATION_LEVEL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GUARDIAN_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GUARDIAN_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GUARDIAN_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_LEARNING_FOCUS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NEXT_ACTION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SUBJECT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.EditPersonDescriptor;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.tag.Tag;

/**
 * Parses input arguments and creates a new EditCommand object
 */
public class EditCommandParser implements Parser<EditCommand> {

    public static final String MESSAGE_CONFLICTING_TAG_EDIT =
            "An empty t/ clears all tags and cannot be combined with other t/ values.";

    /**
     * Parses the given {@code String} of arguments in the context of the EditCommand
     * and returns an EditCommand object for execution.
     * @throws ParseException if the user input does not conform to the expected format
     */
    public EditCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap =
                CommandArgumentTokenizer.tokenize(args, EditCommand.COMMAND_WORD,
                        PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL, PREFIX_ADDRESS,
                        PREFIX_GUARDIAN_NAME, PREFIX_GUARDIAN_PHONE, PREFIX_GUARDIAN_EMAIL, PREFIX_SUBJECT,
                        PREFIX_EDUCATION_LEVEL, PREFIX_LEARNING_FOCUS, PREFIX_NEXT_ACTION, PREFIX_TAG);

        Index index;

        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE), pe);
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL, PREFIX_ADDRESS,
                PREFIX_GUARDIAN_NAME, PREFIX_GUARDIAN_PHONE, PREFIX_GUARDIAN_EMAIL, PREFIX_SUBJECT,
                PREFIX_EDUCATION_LEVEL, PREFIX_LEARNING_FOCUS, PREFIX_NEXT_ACTION);

        EditPersonDescriptor editPersonDescriptor = new EditPersonDescriptor();

        if (argMultimap.getValue(PREFIX_NAME).isPresent()) {
            editPersonDescriptor.setName(ParserUtil.parseName(
                    normalizeFreeText(argMultimap.getValue(PREFIX_NAME).get())));
        }
        parseClearableField(argMultimap, PREFIX_PHONE, ParserUtil::parsePhone,
                editPersonDescriptor::setPhone, editPersonDescriptor::clearPhone);
        parseClearableField(argMultimap, PREFIX_EMAIL, ParserUtil::parseEmail,
                editPersonDescriptor::setEmail, editPersonDescriptor::clearEmail);
        parseClearableField(argMultimap, PREFIX_ADDRESS,
                value -> ParserUtil.parseAddress(normalizeFreeText(value)),
                editPersonDescriptor::setAddress, editPersonDescriptor::clearAddress);
        parseClearableField(argMultimap, PREFIX_GUARDIAN_NAME,
                value -> ParserUtil.parseName(normalizeFreeText(value)),
                editPersonDescriptor::setGuardianName, editPersonDescriptor::clearGuardianName);
        parseClearableField(argMultimap, PREFIX_GUARDIAN_PHONE, ParserUtil::parsePhone,
                editPersonDescriptor::setGuardianPhone, editPersonDescriptor::clearGuardianPhone);
        parseClearableField(argMultimap, PREFIX_GUARDIAN_EMAIL, ParserUtil::parseEmail,
                editPersonDescriptor::setGuardianEmail, editPersonDescriptor::clearGuardianEmail);
        parseClearableField(argMultimap, PREFIX_SUBJECT, ParserUtil::parseSubject,
                editPersonDescriptor::setSubject, editPersonDescriptor::clearSubject);
        parseClearableField(argMultimap, PREFIX_EDUCATION_LEVEL, ParserUtil::parseEducationLevel,
                editPersonDescriptor::setEducationLevel, editPersonDescriptor::clearEducationLevel);
        parseClearableField(argMultimap, PREFIX_LEARNING_FOCUS, ParserUtil::parseLearningFocus,
                editPersonDescriptor::setLearningFocus, editPersonDescriptor::clearLearningFocus);
        parseClearableField(argMultimap, PREFIX_NEXT_ACTION, ParserUtil::parseNextAction,
                editPersonDescriptor::setNextAction, editPersonDescriptor::clearNextAction);
        parseTagsForEdit(argMultimap.getAllValues(PREFIX_TAG)).ifPresent(editPersonDescriptor::setTags);

        if (!editPersonDescriptor.isAnyFieldEdited()) {
            throw new ParseException(EditCommand.MESSAGE_NOT_EDITED);
        }

        return new EditCommand(index, editPersonDescriptor);
    }

    private static String normalizeFreeText(String value) {
        return value.strip().replaceAll("\\s+", " ");
    }

    /**
     * Parses a clearable field when {@code prefix} is present. An empty value invokes {@code clearField}; otherwise,
     * the parsed value is passed to {@code setField}. An absent prefix leaves the descriptor unchanged.
     */
    private static <T> void parseClearableField(ArgumentMultimap argMultimap, Prefix prefix,
                                                ValueParser<T> valueParser, Consumer<T> setField,
                                                Runnable clearField) throws ParseException {
        Optional<String> value = argMultimap.getValue(prefix);
        if (value.isEmpty()) {
            return;
        }
        if (value.get().isEmpty()) {
            clearField.run();
            return;
        }
        setField.accept(valueParser.parse(value.get()));
    }

    /** Parses one non-empty edit-field value while preserving its {@link ParseException}. */
    @FunctionalInterface
    private interface ValueParser<T> {
        T parse(String value) throws ParseException;
    }

    /**
     * Parses {@code Collection<String> tags} into a {@code Set<Tag>} if {@code tags} is non-empty.
     * If {@code tags} contains only one element which is an empty string, it will be parsed into a
     * {@code Set<Tag>} containing zero tags.
     */
    private Optional<Set<Tag>> parseTagsForEdit(Collection<String> tags) throws ParseException {
        assert tags != null;

        if (tags.isEmpty()) {
            return Optional.empty();
        }
        if (tags.contains("") && tags.size() > 1) {
            throw new ParseException(MESSAGE_CONFLICTING_TAG_EDIT);
        }
        Collection<String> tagSet = tags.contains("") ? Collections.emptySet() : tags;
        return Optional.of(ParserUtil.parseTags(tagSet));
    }

}
