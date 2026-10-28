package seedu.address.ui;

import java.util.logging.Logger;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.stage.Stage;
import seedu.address.commons.core.LogsCenter;

/**
 * Controller for a help page
 */
public class HelpWindow extends UiPart<Stage> {

    public static final String USERGUIDE_URL = "https://ay2627s1-cs2103t-w10-3.github.io/tp/UserGuide.html";

    private static final Logger logger = LogsCenter.getLogger(HelpWindow.class);
    private static final String FXML = "HelpWindow.fxml";

    @FXML
    private Button copyButton;

    @FXML
    private Button overviewButton;
    @FXML
    private Button studentsButton;
    @FXML
    private Button searchButton;
    @FXML
    private Button schedulingButton;
    @FXML
    private Button archivingButton;

    @FXML
    private ScrollPane overviewPage;
    @FXML
    private ScrollPane studentsPage;
    @FXML
    private ScrollPane searchPage;
    @FXML
    private ScrollPane schedulingPage;
    @FXML
    private ScrollPane archivingPage;

    /**
     * Creates a new HelpWindow.
     *
     * @param root Stage to use as the root of the HelpWindow.
     */
    public HelpWindow(Stage root) {
        super(FXML, root);
    }

    /**
     * Creates a new HelpWindow.
     */
    public HelpWindow() {
        this(new Stage());
    }

    /**
     * Shows the help window.
     * @throws IllegalStateException
     *     <ul>
     *         <li>
     *             if this method is called on a thread other than the JavaFX Application Thread.
     *         </li>
     *         <li>
     *             if this method is called during animation or layout processing.
     *         </li>
     *         <li>
     *             if this method is called on the primary stage.
     *         </li>
     *         <li>
     *             if {@code dialogStage} is already showing.
     *         </li>
     *     </ul>
     */
    public void show() {
        logger.fine("Showing help page about the application.");
        getRoot().show();
        getRoot().centerOnScreen();
    }

    /**
     * Returns true if the help window is currently being shown.
     */
    public boolean isShowing() {
        return getRoot().isShowing();
    }

    /**
     * Hides the help window.
     */
    public void hide() {
        getRoot().hide();
    }

    /**
     * Focuses on the help window.
     */
    public void focus() {
        getRoot().requestFocus();
    }

    @FXML
    private void showOverview() {
        showPage(overviewPage, overviewButton);
    }

    @FXML
    private void showStudents() {
        showPage(studentsPage, studentsButton);
    }

    @FXML
    private void showSearch() {
        showPage(searchPage, searchButton);
    }

    @FXML
    private void showScheduling() {
        showPage(schedulingPage, schedulingButton);
    }

    @FXML
    private void showArchiving() {
        showPage(archivingPage, archivingButton);
    }

    private void showPage(ScrollPane selectedPage, Button selectedButton) {
        ScrollPane[] pages = {overviewPage, studentsPage, searchPage, schedulingPage, archivingPage};
        Button[] buttons = {overviewButton, studentsButton, searchButton, schedulingButton, archivingButton};

        for (ScrollPane page : pages) {
            boolean selected = page == selectedPage;
            page.setVisible(selected);
            page.setManaged(selected);
            if (selected) {
                page.setVvalue(0);
            }
        }

        for (Button button : buttons) {
            button.getStyleClass().remove("help-nav-button-active");
        }
        selectedButton.getStyleClass().add("help-nav-button-active");
    }

    /**
     * Copies the URL to the user guide to the clipboard.
     */
    @FXML
    private void copyUrl() {
        final Clipboard clipboard = Clipboard.getSystemClipboard();
        final ClipboardContent url = new ClipboardContent();
        url.putString(USERGUIDE_URL);
        clipboard.setContent(url);
    }
}
