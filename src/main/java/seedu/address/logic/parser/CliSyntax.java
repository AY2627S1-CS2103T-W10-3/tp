package seedu.address.logic.parser;

import java.util.List;

/**
 * Contains Command Line Interface (CLI) syntax definitions common to multiple commands
 */
public class CliSyntax {

    /* Prefix definitions */
    public static final Prefix PREFIX_NAME = new Prefix("n/");
    public static final Prefix PREFIX_PHONE = new Prefix("p/");
    public static final Prefix PREFIX_EMAIL = new Prefix("e/");
    public static final Prefix PREFIX_ADDRESS = new Prefix("a/");
    public static final Prefix PREFIX_TAG = new Prefix("t/");
    public static final Prefix PREFIX_GUARDIAN_NAME = new Prefix("gn/");
    public static final Prefix PREFIX_GUARDIAN_PHONE = new Prefix("gp/");
    public static final Prefix PREFIX_GUARDIAN_EMAIL = new Prefix("ge/");
    public static final Prefix PREFIX_SUBJECT = new Prefix("s/");
    public static final Prefix PREFIX_EDUCATION_LEVEL = new Prefix("l/");
    public static final Prefix PREFIX_LEARNING_FOCUS = new Prefix("lf/");
    public static final Prefix PREFIX_NEXT_ACTION = new Prefix("na/");
    public static final Prefix PREFIX_LESSON = new Prefix("at/");

    public static final List<Prefix> ALL_PREFIXES = List.of(PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL, PREFIX_ADDRESS,
            PREFIX_TAG, PREFIX_GUARDIAN_NAME, PREFIX_GUARDIAN_PHONE, PREFIX_GUARDIAN_EMAIL, PREFIX_SUBJECT,
            PREFIX_EDUCATION_LEVEL, PREFIX_LEARNING_FOCUS, PREFIX_NEXT_ACTION, PREFIX_LESSON);

}
