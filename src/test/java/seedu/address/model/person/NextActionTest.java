package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class NextActionTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new NextAction(null));
    }

    @Test
    public void constructor_invalidNextAction_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new NextAction(""));
        assertThrows(IllegalArgumentException.class, () -> new NextAction("   "));
        assertThrows(IllegalArgumentException.class, () -> new NextAction("call\tguardian"));
        assertThrows(IllegalArgumentException.class,
                () -> new NextAction("a".repeat(NextAction.MAX_LENGTH + 1)));
    }

    @Test
    public void isValidNextAction() {
        assertFalse(NextAction.isValidNextAction(""));
        assertFalse(NextAction.isValidNextAction("\n"));
        assertFalse(NextAction.isValidNextAction("Complete worksheet\u0000"));
        assertFalse(NextAction.isValidNextAction("a".repeat(NextAction.MAX_LENGTH + 1)));

        assertTrue(NextAction.isValidNextAction("a"));
        assertTrue(NextAction.isValidNextAction("Complete Q1–10, then check errors."));
        assertTrue(NextAction.isValidNextAction("Ask: ‘Which step was unclear?’"));
        assertTrue(NextAction.isValidNextAction("a".repeat(NextAction.MAX_LENGTH)));
    }

    @Test
    public void getValueAndToString_validNextAction_returnsStoredValue() {
        NextAction nextAction = new NextAction("Complete worksheet 2");

        assertEquals("Complete worksheet 2", nextAction.getValue());
        assertEquals("Complete worksheet 2", nextAction.toString());
    }

    @Test
    public void equals() {
        NextAction nextAction = new NextAction("Complete worksheet 2");
        NextAction nextActionCopy = new NextAction("Complete worksheet 2");

        assertTrue(nextAction.equals(nextAction));
        assertTrue(nextAction.equals(nextActionCopy));
        assertEquals(nextAction.hashCode(), nextActionCopy.hashCode());
        assertFalse(nextAction.equals(null));
        assertFalse(nextAction.equals("Complete worksheet 2"));
        assertFalse(nextAction.equals(new NextAction("Attempt practice paper")));
    }
}
