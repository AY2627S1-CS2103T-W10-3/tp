package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.Test;

public class PersonListPanelTest {

    @Test
    public void getEmptyListMessage_usesRecordedStudentCount() throws Exception {
        Optional<Method> messageMethod = Arrays.stream(PersonListPanel.class.getDeclaredMethods())
                .filter(method -> method.getName().equals("getEmptyListMessage"))
                .findFirst();
        assertTrue(messageMethod.isPresent(), "PersonListPanel should select its message from the total record count");

        Method method = messageMethod.get();
        method.setAccessible(true);
        assertEquals("No students recorded. Use add to create your first student.", method.invoke(null, 0));
        assertEquals("No students in this view. Use list or list archived.", method.invoke(null, 1));
    }
}
