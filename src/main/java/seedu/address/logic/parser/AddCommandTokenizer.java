package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/** Tokenizes add fields and rejects prefix-like tokens that the add command does not support. */
final class AddCommandTokenizer {
    private static final Pattern PREFIX_TOKEN = Pattern.compile("(?<!\\S)([A-Za-z][A-Za-z0-9]*/)");

    private AddCommandTokenizer() {
    }

    /**
     * Returns values for supported prefixes in any order. A token must start at the beginning of
     * the arguments or after whitespace; free text before the first token is invalid.
     */
    static ArgumentMultimap tokenize(String args, Prefix... prefixes) throws ParseException {
        Map<String, Prefix> supported = Arrays.stream(prefixes)
                .collect(Collectors.toMap(Prefix::toString, Function.identity()));
        Matcher matcher = PREFIX_TOKEN.matcher(args);
        List<FieldPosition> fields = new ArrayList<>();
        while (matcher.find()) {
            fields.add(new FieldPosition(matcher.group(1), matcher.start(1), matcher.end(1)));
        }
        if (fields.isEmpty() || !args.substring(0, fields.getFirst().start()).isBlank()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
        }

        ArgumentMultimap values = new ArgumentMultimap();
        for (int i = 0; i < fields.size(); i++) {
            FieldPosition field = fields.get(i);
            Prefix prefix = supported.get(field.token());
            if (prefix == null) {
                throw new ParseException("Unsupported prefix in add command: " + field.token());
            }
            int end = i + 1 < fields.size() ? fields.get(i + 1).start() : args.length();
            values.put(prefix, args.substring(field.end(), end).strip());
        }
        return values;
    }

    private record FieldPosition(String token, int start, int end) {
    }
}
