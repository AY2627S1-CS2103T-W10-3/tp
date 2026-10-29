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

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.EditCommand.EditPersonDescriptor;
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
        if (argMultimap.getValue(PREFIX_PHONE).isPresent()) {
            String value = argMultimap.getValue(PREFIX_PHONE).get();
            if (value.isEmpty()) {
                editPersonDescriptor.clearPhone();
            } else {
                editPersonDescriptor.setPhone(ParserUtil.parsePhone(value));
            }
        }
        if (argMultimap.getValue(PREFIX_EMAIL).isPresent()) {
            String value = argMultimap.getValue(PREFIX_EMAIL).get();
            if (value.isEmpty()) {
                editPersonDescriptor.clearEmail();
            } else {
                editPersonDescriptor.setEmail(ParserUtil.parseEmail(value));
            }
        }
        if (argMultimap.getValue(PREFIX_ADDRESS).isPresent()) {
            String value = argMultimap.getValue(PREFIX_ADDRESS).get();
            if (value.isEmpty()) {
                editPersonDescriptor.clearAddress();
            } else {
                editPersonDescriptor.setAddress(ParserUtil.parseAddress(normalizeFreeText(value)));
            }
        }
        if (argMultimap.getValue(PREFIX_GUARDIAN_NAME).isPresent()) {
            String value = argMultimap.getValue(PREFIX_GUARDIAN_NAME).get();
            if (value.isEmpty()) {
                editPersonDescriptor.clearGuardianName();
            } else {
                editPersonDescriptor.setGuardianName(ParserUtil.parseName(normalizeFreeText(value)));
            }
        }
        if (argMultimap.getValue(PREFIX_GUARDIAN_PHONE).isPresent()) {
            String value = argMultimap.getValue(PREFIX_GUARDIAN_PHONE).get();
            if (value.isEmpty()) {
                editPersonDescriptor.clearGuardianPhone();
            } else {
                editPersonDescriptor.setGuardianPhone(ParserUtil.parsePhone(value));
            }
        }
        if (argMultimap.getValue(PREFIX_GUARDIAN_EMAIL).isPresent()) {
            String value = argMultimap.getValue(PREFIX_GUARDIAN_EMAIL).get();
            if (value.isEmpty()) {
                editPersonDescriptor.clearGuardianEmail();
            } else {
                editPersonDescriptor.setGuardianEmail(ParserUtil.parseEmail(value));
            }
        }
        if (argMultimap.getValue(PREFIX_SUBJECT).isPresent()) {
            String value = argMultimap.getValue(PREFIX_SUBJECT).get();
            if (value.isEmpty()) {
                editPersonDescriptor.clearSubject();
            } else {
                editPersonDescriptor.setSubject(ParserUtil.parseSubject(value));
            }
        }
        if (argMultimap.getValue(PREFIX_EDUCATION_LEVEL).isPresent()) {
            String value = argMultimap.getValue(PREFIX_EDUCATION_LEVEL).get();
            if (value.isEmpty()) {
                editPersonDescriptor.clearEducationLevel();
            } else {
                editPersonDescriptor.setEducationLevel(ParserUtil.parseEducationLevel(value));
            }
        }
        if (argMultimap.getValue(PREFIX_LEARNING_FOCUS).isPresent()) {
            String value = argMultimap.getValue(PREFIX_LEARNING_FOCUS).get();
            if (value.isEmpty()) {
                editPersonDescriptor.clearLearningFocus();
            } else {
                editPersonDescriptor.setLearningFocus(ParserUtil.parseLearningFocus(value));
            }
        }
        if (argMultimap.getValue(PREFIX_NEXT_ACTION).isPresent()) {
            String value = argMultimap.getValue(PREFIX_NEXT_ACTION).get();
            if (value.isEmpty()) {
                editPersonDescriptor.clearNextAction();
            } else {
                editPersonDescriptor.setNextAction(ParserUtil.parseNextAction(value));
            }
        }
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
