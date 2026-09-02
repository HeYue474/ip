package sal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Tests {@link Sal#getResponse(String)} command handling used by the GUI.
 */
public class SalTest {
    @TempDir
    Path tempDir;

    private Sal sal;

    @BeforeEach
    public void setUp() {
        sal = new Sal(tempDir.resolve("sal.txt").toString());
    }

    @Test
    public void getWelcomeMessage_returnsGreeting() {
        String welcome = sal.getWelcomeMessage();
        assertTrue(welcome.contains("Hello! I'm Sal."));
        assertTrue(welcome.contains("What can I do for you?"));
    }

    @Test
    public void getResponse_todo_addsTask() {
        String response = sal.getResponse("todo read book");
        assertTrue(response.contains("Got it. I've added this task:"));
        assertTrue(response.contains("read book"));
        assertTrue(response.contains("Now you have 1 tasks in the list."));
        assertFalse(sal.isExit());
    }

    @Test
    public void getResponse_listAfterAdding_showsTask() {
        sal.getResponse("todo read book");
        String response = sal.getResponse("list");
        assertTrue(response.contains("Here are the tasks in your list:"));
        assertTrue(response.contains("1.[T][ ] read book"));
    }

    @Test
    public void getResponse_markAndUnmark_updatesStatus() {
        sal.getResponse("todo read book");
        String marked = sal.getResponse("mark 1");
        assertTrue(marked.contains("Nice! I've marked this task as done:"));
        assertTrue(marked.contains("[T][X] read book"));

        String unmarked = sal.getResponse("unmark 1");
        assertTrue(unmarked.contains("OK, I've marked this task as not done yet:"));
        assertTrue(unmarked.contains("[T][ ] read book"));
    }

    @Test
    public void getResponse_delete_removesTask() {
        sal.getResponse("todo read book");
        String response = sal.getResponse("delete 1");
        assertTrue(response.contains("Noted. I've removed this task:"));
        assertTrue(response.contains("Now you have 0 tasks in the list."));
        assertTrue(sal.getResponse("list").endsWith("Here are the tasks in your list:"));
    }

    @Test
    public void getResponse_find_returnsMatches() {
        sal.getResponse("todo read book");
        sal.getResponse("todo join sports club");
        String response = sal.getResponse("find book");
        assertTrue(response.contains("Here are the matching tasks in your list:"));
        assertTrue(response.contains("read book"));
        assertFalse(response.contains("join sports club"));
    }

    @Test
    public void getResponse_unknownCommand_returnsError() {
        assertEquals("Command not recognised.", sal.getResponse("blah"));
    }

    @Test
    public void getResponse_invalidTodo_returnsFormatHint() {
        assertEquals("Correct format: todo <task name>", sal.getResponse("todo"));
    }

    @Test
    public void getResponse_bye_setsExitFlag() {
        String response = sal.getResponse("bye");
        assertEquals("Bye. Hope to see you again soon!", response);
        assertTrue(sal.isExit());
    }
}
