package sal;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Handles all interactions with the user: reading commands and printing messages.
 * Keeps {@code System.out}/{@code Scanner} usage out of the rest of the app.
 */
public class Ui {
    private static final String LINE = "____________________________________________________________";
    private static final String BANNER = " ____        _ \n"
            + "/ ___|  __ _| |\n"
            + "\\___ \\ / _` | |\n"
            + " ___) | (_| | |\n"
            + "|____/ \\__,_|_|\n";

    private final Scanner scanner;

    /**
     * Creates a UI that reads from standard input.
     */
    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Prints the welcome banner and greeting.
     */
    public void showWelcome() {
        showLine();
        System.out.println(BANNER);
        System.out.println("Hello! I'm Sal.");
        System.out.println("What can I do for you?");
        showLine();
    }

    /**
     * Prints the goodbye message.
     */
    public void showGoodbye() {
        showLine();
        System.out.println("Bye. Hope to see you again soon!");
        showLine();
    }

    /**
     * Prints a horizontal divider line.
     */
    public void showLine() {
        System.out.println(LINE);
    }

    /**
     * Reads the next command from the user and trims surrounding whitespace.
     *
     * @return The trimmed command line.
     */
    public String readCommand() {
        return scanner.nextLine().trim();
    }

    /**
     * Closes the input scanner when the app exits.
     */
    public void close() {
        scanner.close();
    }

    /**
     * Prints an error message between divider lines.
     *
     * @param message Error text to show the user.
     */
    public void showError(String message) {
        showLine();
        System.out.println(message);
        showLine();
    }

    /**
     * Tells the user that saved tasks could not be loaded.
     */
    public void showLoadingError() {
        showError("Could not load saved tasks. Starting with an empty list.");
    }

    /**
     * Prints the full task list.
     *
     * @param tasks Current tasks.
     */
    public void showTaskList(TaskList tasks) {
        showLine();
        System.out.println("Here are the tasks in your list:");
        ArrayList<Task> list = tasks.getTasks();
        for (int i = 0; i < list.size(); i++) {
            System.out.println((i + 1) + "." + list.get(i));
        }
        showLine();
    }

    /**
     * Confirms that a task was added.
     *
     * @param task Newly added task.
     * @param taskCount Current number of tasks.
     */
    public void showTaskAdded(Task task, int taskCount) {
        showLine();
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + task);
        System.out.println("Now you have " + taskCount + " tasks in the list.");
        showLine();
    }

    /**
     * Confirms that a task was deleted.
     *
     * @param task Removed task.
     * @param taskCount Remaining number of tasks.
     */
    public void showTaskDeleted(Task task, int taskCount) {
        showLine();
        System.out.println("Noted. I've removed this task:");
        System.out.println("  " + task);
        System.out.println("Now you have " + taskCount + " tasks in the list.");
        showLine();
    }

    /**
     * Confirms that a task was marked as done.
     *
     * @param task Updated task.
     */
    public void showMarked(Task task) {
        showLine();
        System.out.println("Nice! I've marked this task as done:");
        System.out.println("  " + task);
        showLine();
    }

    /**
     * Confirms that a task was marked as not done.
     *
     * @param task Updated task.
     */
    public void showUnmarked(Task task) {
        showLine();
        System.out.println("OK, I've marked this task as not done yet:");
        System.out.println("  " + task);
        showLine();
    }
}
