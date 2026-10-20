package seedu.address.logic.commands;

import seedu.address.model.Model;

/**
 * Formats full help instructions for every command for display.
 */
public class HelpCommand extends Command {

    public static final String COMMAND_WORD = "help";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Shows program usage instructions.\n"
            + "Example: " + COMMAND_WORD;

    /** Command formats and essential validation rules are bundled with the application for offline use. */
    public static final String HELP_TEXT = "TutorContacts commands:\n"
            + AddCommand.FORMAT + "\n"
            + "edit INDEX [n/NAME] [p/PHONE] [e/EMAIL] [a/ADDRESS] "
            + "[gn/GUARDIAN_NAME] [gp/GUARDIAN_PHONE] [ge/GUARDIAN_EMAIL] [t/TAG]...\n"
            + ListCommand.COMMAND_WORD + "\n"
            + FindCommand.COMMAND_WORD + " KEYWORD [MORE_KEYWORDS]\n"
            + DeleteCommand.COMMAND_WORD + " INDEX\n"
            + ClearCommand.COMMAND_WORD + "\n"
            + COMMAND_WORD + "\n"
            + ExitCommand.COMMAND_WORD + "\n\n"
            + "Only n/NAME is required. All other add fields are optional and may appear in any order.\n"
            + "Use each add prefix once. Surrounding whitespace is ignored; repeated spaces in names, "
            + "subjects, and addresses are collapsed.\n"
            + "Student names are unique after case and whitespace normalization. "
            + "Different students may share contact and guardian details.\n"
            + "Adding guardian phone or email requires gn/. When editing an existing guardian, "
            + "unspecified guardian fields are preserved.\n"
            + "Education levels: P1-P6, S1-S5, JC1-JC2.\n\n"
            + "Examples:\n"
            + "add n/Alex Tan s/H2 Mathematics l/JC1 gn/Mary Tan gp/81234567\n"
            + "edit 1 gp/92345678\n"
            + "list\n"
            + "delete 1\n"
            + "Invalid: add n/Alex Tan gp/81234567 (guardian name missing)\n";

    @Override
    public CommandResult execute(Model model) {
        return new CommandResult(HELP_TEXT, true, false);
    }
}
