package seedu.address.logic.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import seedu.address.logic.parser.exceptions.ParseException;

/** Tokenizes command fields while preserving prefix-like text inside quoted values. */
final class CommandArgumentTokenizer {
    private static final Pattern PREFIX_TOKEN = Pattern.compile("[a-z][a-z0-9]?/");

    private CommandArgumentTokenizer() {
    }

    /**
     * Returns the preamble and values for the supplied prefixes. Prefix tokens inside a quoted
     * value are treated as text. Prefix-like words longer than two characters are also treated
     * as text because TutorContacts prefixes contain one or two lower-case characters.
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
            values.put(prefix, parseValue(args.substring(field.end(), end), commandWord));
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

    private static String parseValue(String rawValue, String commandWord) throws ParseException {
        String value = rawValue.strip();
        if (!value.contains("\"")) {
            return value;
        }
        if (value.length() < 2 || value.charAt(0) != '"' || value.charAt(value.length() - 1) != '"'
                || value.substring(1, value.length() - 1).contains("\"")) {
            throw new ParseException("Quoted values in " + commandWord
                    + " command must be enclosed by one pair of double quotes.");
        }
        return value.substring(1, value.length() - 1);
    }

    private record FieldPosition(String token, int start, int end) {
    }
}
