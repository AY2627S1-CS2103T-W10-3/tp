package seedu.address.logic.parser;

import static seedu.address.logic.parser.CliSyntax.PREFIX_GUARDIAN_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import seedu.address.logic.parser.exceptions.ParseException;

/** Tokenizes command fields while preserving prefix-like text inside quoted values. */
final class CommandArgumentTokenizer {
    private static final Pattern PREFIX_TOKEN = Pattern.compile("[a-z][a-z0-9]?/");
    private static final Set<String> NAME_FIELDS = Set.of(PREFIX_NAME.toString(), PREFIX_GUARDIAN_NAME.toString());
    // s/o (son of), d/o (daughter of), a/l (anak lelaki), and a/p (anak perempuan) as whole words in names
    private static final Pattern NAME_RELATIONSHIP_MARKER = Pattern.compile("(?:[sd]/o|a/[lp])(?=\\s|$)");

    private CommandArgumentTokenizer() {
    }

    /**
     * Returns the preamble and values for the supplied prefixes. Prefix tokens inside a quoted
     * value are treated as text. Prefix-like words longer than two characters are also treated
     * as text because TutorContacts prefixes contain one or two lower-case characters. In a name
     * or guardian name, the relationship markers s/o, d/o, a/l, and a/p are also treated as text.
     */
    static ArgumentMultimap tokenize(String args, String commandWord, Prefix... prefixes) throws ParseException {
        Map<String, Prefix> supported = Arrays.stream(prefixes)
                .collect(Collectors.toMap(Prefix::toString, Function.identity()));
        List<FieldPosition> fields = findFieldPositions(args, commandWord);

        ArgumentMultimap values = new ArgumentMultimap();
        int firstFieldStart = fields.isEmpty() ? args.length() : fields.getFirst().start();
        values.put(new Prefix(""), args.substring(0, firstFieldStart).strip());

        for (int i = 0; i < fields.size(); i++) {
            FieldPosition field = fields.get(i);
            Prefix prefix = supported.get(field.token());
            if (prefix == null) {
                throw new ParseException("Unsupported prefix in " + commandWord + " command: " + field.token());
            }
            int end = i + 1 < fields.size() ? fields.get(i + 1).start() : args.length();
            values.put(prefix, parseValue(args.substring(field.end(), end)));
        }
        return values;
    }

    private static List<FieldPosition> findFieldPositions(String args, String commandWord) throws ParseException {
        List<FieldPosition> fields = new ArrayList<>();
        boolean isInsideQuotes = false;
        for (int i = 0; i < args.length(); i++) {
            if (args.charAt(i) == '"') {
                isInsideQuotes = !isInsideQuotes;
                continue;
            }
            if (isInsideQuotes || i > 0 && !Character.isWhitespace(args.charAt(i - 1))) {
                continue;
            }
            Matcher marker = NAME_RELATIONSHIP_MARKER.matcher(args);
            marker.region(i, args.length());
            if (isInNameField(fields) && marker.lookingAt()) {
                i = marker.end() - 1;
                continue;
            }
            Matcher matcher = PREFIX_TOKEN.matcher(args);
            matcher.region(i, args.length());
            if (matcher.lookingAt()) {
                fields.add(new FieldPosition(matcher.group(), matcher.start(), matcher.end()));
                i = matcher.end() - 1;
            }
        }
        if (isInsideQuotes) {
            throw new ParseException("Unclosed quoted value in " + commandWord + " command.");
        }
        return fields;
    }

    private static boolean isInNameField(List<FieldPosition> fields) {
        return !fields.isEmpty() && NAME_FIELDS.contains(fields.getLast().token());
    }

    private static String parseValue(String rawValue) {
        String value = rawValue.strip();
        if (value.length() >= 2 && value.charAt(0) == '"' && value.charAt(value.length() - 1) == '"') {
            return value.substring(1, value.length() - 1);
        }
        return value;
    }

    private record FieldPosition(String token, int start, int end) {
    }
}
