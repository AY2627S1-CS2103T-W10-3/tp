package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;

public class HelpCommandTest {
    private Model model = new ModelManager();
    private Model expectedModel = new ModelManager();

    @Test
    public void execute_help_success() {
        CommandResult expectedCommandResult = new CommandResult(HelpCommand.HELP_TEXT, true, false);
        assertCommandSuccess(new HelpCommand(), model, expectedCommandResult, expectedModel);
    }

    @Test
    public void execute_help_containsOfflineMvpFormats() {
        String help = new HelpCommand().execute(new ModelManager()).getFeedbackToUser();
        assertTrue(help.contains("add n/NAME"));
        assertTrue(help.contains("[s/SUBJECT]"));
        assertTrue(help.contains("[l/EDUCATION_LEVEL]"));
        assertTrue(help.contains("[gn/GUARDIAN_NAME]"));
        assertTrue(help.contains("[lf/LEARNING_FOCUS]"));
        assertTrue(help.contains("[na/NEXT_ACTION]"));
        assertTrue(help.contains("edit INDEX [n/NAME] [p/PHONE] [e/EMAIL] [a/ADDRESS] "
                + "[gn/GUARDIAN_NAME] [gp/GUARDIAN_PHONE] [ge/GUARDIAN_EMAIL] "
                + "[s/SUBJECT] [l/EDUCATION_LEVEL] [lf/LEARNING_FOCUS] [na/NEXT_ACTION] [t/TAG]..."));
        assertTrue(help.contains("archive INDEX"));
        assertTrue(help.contains("list\n"));
        assertTrue(help.contains("list archived"));
        assertTrue(help.contains("unarchive INDEX"));
        assertTrue(help.contains("find KEYWORD"));
        assertTrue(help.contains("delete INDEX"));
        assertTrue(help.contains("schedule INDEX at/YYYY-MM-DD HH:mm"));
        assertTrue(help.contains("unschedule INDEX"));
        assertTrue(help.contains("clear\n"));
        assertTrue(help.contains("help\n"));
        assertTrue(help.contains("exit\n"));
        assertTrue(help.contains("Guardian phone or email requires gn/"));
        assertTrue(help.contains("An empty optional edit prefix clears that field"));
        assertTrue(help.contains("Clearing gn/ also clears gp/ and ge/"));
        assertTrue(help.contains("Archived students remain saved until permanently deleted"));
    }
}
