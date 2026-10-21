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
            + "edit INDEX [n/NAME] [p/PHONE] [e/EMAIL] [a/ADDRESS] [t/TAG]...\n"
            + ListCommand.COMMAND_WORD + "\n"
            + FindCommand.COMMAND_WORD + " KEYWORD [MORE_KEYWORDS]\n"
            + "filter [s/SUBJECT] [l/EDUCATION_LEVEL]\n"
            + DeleteCommand.COMMAND_WORD + " INDEX\n"
            + ScheduleCommand.FORMAT + "\n"
            + UnscheduleCommand.FORMAT + "\n"
            + ClearCommand.COMMAND_WORD + "\n"
            + COMMAND_WORD + "\n"
            + ExitCommand.COMMAND_WORD + "\n\n"
            + "Only n/NAME is required. All other add fields are optional and may appear in any order.\n"
            + "Use each add prefix once. Surrounding whitespace is ignored; repeated spaces in names, "
            + "subjects, and addresses are collapsed.\n"
            + "Wrap a value containing a prefix-like token in double quotes, e.g. a/\"Block n/7\".\n"
            + "Student names are unique after case and whitespace normalization. "
            + "Different students may share contact and guardian details.\n"
            + "Guardian phone or email requires gn/. Education levels: P1-P6, S1-S5, JC1-JC2.\n"
            + "Each student has at most one lesson. Lesson times use this computer's local time, must be in the "
            + "future, and replace any existing lesson.\n"
            + "Find uses case-insensitive partial-name matching with OR. Filter criteria use AND.\n"
            + "Displayed indices belong to the current result list; list restores all active students.\n\n"
            + "Examples:\n"
            + "add n/Alex Tan s/H2 Mathematics l/JC1 gn/Mary Tan gp/81234567\n"
            + "list\n"
            + "find ali\n"
            + "filter s/H2 Mathematics l/JC1\n"
            + "delete 1\n"
            + "schedule 1 at/2099-10-30 16:00\n"
            + "Invalid: add n/Alex Tan gp/81234567 (guardian name missing)\n";

    @Override
    public CommandResult execute(Model model) {
        return new CommandResult(HELP_TEXT, true, false);
    }
}
