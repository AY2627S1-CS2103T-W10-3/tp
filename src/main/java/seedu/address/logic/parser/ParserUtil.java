package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.StringUtil;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Address;
import seedu.address.model.person.EducationLevel;
import seedu.address.model.person.Email;
import seedu.address.model.person.Guardian;
import seedu.address.model.person.LearningFocus;
import seedu.address.model.person.Lesson;
import seedu.address.model.person.Name;
import seedu.address.model.person.NextAction;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Subject;
import seedu.address.model.tag.Tag;

/**
 * Contains utility methods used for parsing strings in the various *Parser classes.
 */
public class ParserUtil {

    public static final String MESSAGE_INVALID_INDEX = "Index must be a positive integer.";

    /**
     * Parses {@code oneBasedIndex} into an {@code Index} and returns it. Leading and trailing whitespaces will be
     * trimmed.
     * @throws ParseException if the specified index is invalid (not a non-zero unsigned integer).
     */
    public static Index parseIndex(String oneBasedIndex) throws ParseException {
        String trimmedIndex = oneBasedIndex.trim();
        if (!StringUtil.isNonZeroUnsignedInteger(trimmedIndex)) {
            throw new ParseException(MESSAGE_INVALID_INDEX);
        }
        return Index.fromOneBased(Integer.parseInt(trimmedIndex));
    }

    /**
     * Parses a {@code String name} into a {@code Name}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code name} is invalid.
     */
    public static Name parseName(String name) throws ParseException {
        requireNonNull(name);
        String trimmedName = name.trim();
        if (!Name.isValidName(trimmedName)) {
            throw new ParseException(Name.MESSAGE_CONSTRAINTS);
        }
        return new Name(trimmedName);
    }

    /**
     * Parses a {@code String phone} into a {@code Phone}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code phone} is invalid.
     */
    public static Phone parsePhone(String phone) throws ParseException {
        requireNonNull(phone);
        String trimmedPhone = phone.trim();
        if (!Phone.isValidPhone(trimmedPhone)) {
            throw new ParseException(Phone.MESSAGE_CONSTRAINTS);
        }
        return new Phone(trimmedPhone);
    }

    /**
     * Parses a {@code String address} into an {@code Address}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code address} is invalid.
     */
    public static Address parseAddress(String address) throws ParseException {
        requireNonNull(address);
        String trimmedAddress = address.trim();
        if (!Address.isValidAddress(trimmedAddress)) {
            throw new ParseException(Address.MESSAGE_CONSTRAINTS);
        }
        return new Address(trimmedAddress);
    }

    /**
     * Parses a {@code String email} into an {@code Email}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code email} is invalid.
     */
    public static Email parseEmail(String email) throws ParseException {
        requireNonNull(email);
        String trimmedEmail = email.trim();
        if (!Email.isValidEmail(trimmedEmail)) {
            throw new ParseException(Email.MESSAGE_CONSTRAINTS);
        }
        return new Email(trimmedEmail);
    }

    /**
     * Parses a guardian name and optional contact values into a {@code Guardian}.
     */
    public static Guardian parseGuardian(String name, Optional<String> phone, Optional<String> email)
            throws ParseException {
        requireNonNull(name);
        requireNonNull(phone);
        requireNonNull(email);
        Name parsedName = parseName(name);
        Optional<Phone> parsedPhone = phone.isPresent()
                ? Optional.of(parsePhone(phone.get())) : Optional.empty();
        Optional<Email> parsedEmail = email.isPresent()
                ? Optional.of(parseEmail(email.get())) : Optional.empty();
        return new Guardian(parsedName, parsedPhone, parsedEmail);
    }

    /**
     * Parses a {@code String subject} into a {@code Subject}.
     * Leading and trailing whitespace is trimmed, and repeated ordinary spaces are collapsed. Tabs and other control
     * characters inside the value are kept, so they are rejected instead of being silently converted to spaces.
     *
     * @throws ParseException if the given {@code subject} is invalid.
     */
    public static Subject parseSubject(String subject) throws ParseException {
        requireNonNull(subject);
        String normalizedSubject = normalizeOrdinarySpaces(subject);
        if (!Subject.isValidSubject(normalizedSubject)) {
            throw new ParseException(Subject.MESSAGE_CONSTRAINTS);
        }
        return new Subject(normalizedSubject);
    }

    /**
     * Parses a {@code String educationLevel} into an {@code EducationLevel}.
     */
    public static EducationLevel parseEducationLevel(String educationLevel) throws ParseException {
        requireNonNull(educationLevel);
        String trimmedEducationLevel = educationLevel.trim();
        if (!EducationLevel.isValidEducationLevel(trimmedEducationLevel)) {
            throw new ParseException(EducationLevel.MESSAGE_CONSTRAINTS);
        }
        return new EducationLevel(trimmedEducationLevel);
    }

    /** Parses and normalizes a learning-focus value. */
    public static LearningFocus parseLearningFocus(String learningFocus) throws ParseException {
        requireNonNull(learningFocus);
        String normalizedLearningFocus = normalizeOrdinarySpaces(learningFocus);
        if (!LearningFocus.isValidLearningFocus(normalizedLearningFocus)) {
            throw new ParseException(LearningFocus.MESSAGE_CONSTRAINTS);
        }
        return new LearningFocus(normalizedLearningFocus);
    }

    /** Parses and normalizes a next-action value. */
    public static NextAction parseNextAction(String nextAction) throws ParseException {
        requireNonNull(nextAction);
        String normalizedNextAction = normalizeOrdinarySpaces(nextAction);
        if (!NextAction.isValidNextAction(normalizedNextAction)) {
            throw new ParseException(NextAction.MESSAGE_CONSTRAINTS);
        }
        return new NextAction(normalizedNextAction);
    }

    /**
     * Parses a {@code String lesson} into a {@code Lesson}.
     * Leading and trailing whitespace is trimmed, and repeated spaces between the date and time are collapsed.
     *
     * @throws ParseException if the given {@code lesson} is not a valid date and time.
     */
    public static Lesson parseLesson(String lesson) throws ParseException {
        requireNonNull(lesson);
        String normalizedLesson = normalizeOrdinarySpaces(lesson);
        if (!Lesson.isValidLesson(normalizedLesson)) {
            throw new ParseException(Lesson.MESSAGE_CONSTRAINTS);
        }
        return new Lesson(normalizedLesson);
    }

    /** Trims field boundaries and collapses repeated ordinary spaces. */
    private static String normalizeOrdinarySpaces(String value) {
        return value.strip().replaceAll(" +", " ");
    }

    /**
     * Parses a {@code String tag} into a {@code Tag}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code tag} is invalid.
     */
    public static Tag parseTag(String tag) throws ParseException {
        requireNonNull(tag);
        String trimmedTag = tag.trim();
        if (!Tag.isValidTagName(trimmedTag)) {
            throw new ParseException(Tag.MESSAGE_CONSTRAINTS);
        }
        return new Tag(trimmedTag);
    }

    /**
     * Parses {@code Collection<String> tags} into a {@code Set<Tag>}.
     */
    public static Set<Tag> parseTags(Collection<String> tags) throws ParseException {
        requireNonNull(tags);
        final Set<Tag> tagSet = new HashSet<>();
        for (String tagName : tags) {
            tagSet.add(parseTag(tagName));
        }
        return tagSet;
    }
}
