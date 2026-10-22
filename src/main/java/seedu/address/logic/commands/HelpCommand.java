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
            + "[gn/GUARDIAN_NAME] [gp/GUARDIAN_PHONE] [ge/GUARDIAN_EMAIL] "
            + "[s/SUBJECT] [l/EDUCATION_LEVEL] [lf/LEARNING_FOCUS] [na/NEXT_ACTION] [t/TAG]...\n"
            + ListCommand.COMMAND_WORD + "\n"
            + ListCommand.COMMAND_WORD + " archived\n"
            + FindCommand.COMMAND_WORD + " KEYWORD [MORE_KEYWORDS]\n"
            + ArchiveCommand.COMMAND_WORD + " INDEX\n"
            + UnarchiveCommand.COMMAND_WORD + " INDEX\n"
            + DeleteCommand.COMMAND_WORD + " INDEX\n"
            + ClearCommand.COMMAND_WORD + "\n"
            + COMMAND_WORD + "\n"
            + ExitCommand.COMMAND_WORD + "\n\n"
            + "Only n/NAME is required. All other add fields are optional and may appear in any order.\n"
            + "Use each add prefix once. Surrounding whitespace is ignored; repeated spaces in names, "
            + "subjects, and addresses are collapsed.\n"
            + "Wrap a value containing a prefix-like token in double quotes, e.g. a/\"Block n/7\".\n"
            + "Student names are unique after case and whitespace normalization. "
            + "Different students may share contact and guardian details.\n"
            + "Guardian phone or email requires gn/. Education levels: P1-P6, S1-S5, JC1-JC2.\n\n"
            + "Learning focus and next action are optional, contain 1 to 200 characters when present, "
            + "and cannot contain control characters.\n"
            + "archive INDEX uses the current active result view. list archived opens the archived view, "
            + "where unarchive INDEX restores a student.\n"
            + "Archived students remain saved until permanently deleted with delete INDEX.\n\n"
            + "Examples:\n"
            + "add n/Alex Tan s/H2 Mathematics l/JC1 lf/Quadratic graphs "
            + "na/Complete worksheet 4\n"
            + "edit 1 lf/Linear equations na/Review exercise 2\n"
            + "list\n"
            + "archive 1\n"
            + "list archived\n"
            + "unarchive 1\n"
            + "delete 1\n"
            + "Invalid: add n/Alex Tan gp/81234567 (guardian name missing)\n";

    @Override
    public CommandResult execute(Model model) {
        return new CommandResult(HELP_TEXT, true, false);
    }
}
