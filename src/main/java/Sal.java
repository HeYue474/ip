import java.io.IOException;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Entry point for the Sal chatbot.
 * Level-8 behavior: parse deadline dates/times and persist tasks to disk.
 */
public class Sal {
    private static final String LINE = "____________________________________________________________";
    private static final String DATA_FILE_PATH = "data/sal.txt";

    public static void main(String[] args) {
        String banner = " ____        _ \n"
                + "/ ___|  __ _| |\n"
                + "\\___ \\ / _` | |\n"
                + " ___) | (_| | |\n"
                + "|____/ \\__,_|_|\n";

        System.out.println(LINE);
        System.out.println(banner);
        System.out.println("Hello! I'm Sal.");
        System.out.println("What can I do for you?");
        System.out.println(LINE);

        Storage storage = new Storage(DATA_FILE_PATH);
        ArrayList<Task> tasks = loadTasks(storage);

        Scanner scanner = new Scanner(System.in);
        while (true) {
            String input = scanner.nextLine().trim();
            try {
                if (input.isEmpty()) {
                    throw new SalException("Command not recognised.");
                }

                String command = input.split("\\s+", 2)[0];
                switch (command) {
                case "bye":
                    System.out.println(LINE);
                    System.out.println("Bye. Hope to see you again soon!");
                    System.out.println(LINE);
                    scanner.close();
                    return;
                case "list":
                    printList(tasks);
                    break;
                case "mark":
                    markTask(input, tasks, storage);
                    break;
                case "unmark":
                    unmarkTask(input, tasks, storage);
                    break;
                case "todo":
                    addTodo(input, tasks, storage);
                    break;
                case "deadline":
                    addDeadline(input, tasks, storage);
                    break;
                case "event":
                    addEvent(input, tasks, storage);
                    break;
                case "delete":
                    deleteTask(input, tasks, storage);
                    break;
                default:
                    throw new SalException("Command not recognised.");
                }
            } catch (SalException e) {
                System.out.println(LINE);
                System.out.println(e.getMessage());
                System.out.println(LINE);
            }
        }
    }

    /**
     * Prints all tasks currently stored.
     */
    private static void printList(ArrayList<Task> tasks) {
        System.out.println(LINE);
        System.out.println("Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + "." + tasks.get(i));
        }
        System.out.println(LINE);
    }

    /**
     * Loads tasks from disk, or returns an empty list if no saved data is available.
     */
    private static ArrayList<Task> loadTasks(Storage storage) {
        try {
            return storage.load();
        } catch (IOException e) {
            System.out.println(LINE);
            System.out.println("Could not load saved tasks. Starting with an empty list.");
            System.out.println(LINE);
            return new ArrayList<>();
        }
    }

    /**
     * Persists the current task list to disk.
     */
    private static void saveTasks(Storage storage, ArrayList<Task> tasks) throws SalException {
        try {
            storage.save(tasks);
        } catch (IOException e) {
            throw new SalException("Could not save tasks to disk.");
        }
    }

    /**
     * Marks a task as done. Expected format: {@code mark <task number>}.
     */
    private static void markTask(String input, ArrayList<Task> tasks, Storage storage) throws SalException {
        try {
            int index = Integer.parseInt(input.substring("mark".length()).trim()) - 1;
            if (index < 0 || index >= tasks.size()) {
                throw new SalException("Task number out of bounds (0-" + tasks.size());
            }
            tasks.get(index).markAsDone();
            saveTasks(storage, tasks);
            System.out.println(LINE);
            System.out.println("Nice! I've marked this task as done:");
            System.out.println("  " + tasks.get(index));
            System.out.println(LINE);
        } catch (SalException e) {
            throw e;
        } catch (Exception e) {
            throw new SalException("Correct format: mark <task number>");
        }
    }

    /**
     * Marks a task as not done. Expected format: {@code unmark <task number>}.
     */
    private static void unmarkTask(String input, ArrayList<Task> tasks, Storage storage) throws SalException {
        try {
            int index = Integer.parseInt(input.substring("unmark".length()).trim()) - 1;
            if (index < 0 || index >= tasks.size()) {
                throw new SalException("Task number out of bounds (0-" + tasks.size());
            }
            tasks.get(index).markAsNotDone();
            saveTasks(storage, tasks);
            System.out.println(LINE);
            System.out.println("OK, I've marked this task as not done yet:");
            System.out.println("  " + tasks.get(index));
            System.out.println(LINE);
        } catch (SalException e) {
            throw e;
        } catch (Exception e) {
            throw new SalException("Correct format: unmark <task number>");
        }
    }

    /**
     * Deletes a task. Expected format: {@code delete <task number>}.
     */
    private static void deleteTask(String input, ArrayList<Task> tasks, Storage storage) throws SalException {
        try {
            int index = Integer.parseInt(input.substring("delete".length()).trim()) - 1;
            if (index < 0 || index >= tasks.size()) {
                throw new SalException("Task number out of bounds (0-" + tasks.size());
            }
            Task removed = tasks.remove(index);
            saveTasks(storage, tasks);
            System.out.println(LINE);
            System.out.println("Noted. I've removed this task:");
            System.out.println("  " + removed);
            System.out.println("Now you have " + tasks.size() + " tasks in the list.");
            System.out.println(LINE);
        } catch (SalException e) {
            throw e;
        } catch (Exception e) {
            throw new SalException("Correct format: delete <task number>");
        }
    }

    /**
     * Adds a todo. Expected format: {@code todo <task name>}.
     */
    private static void addTodo(String input, ArrayList<Task> tasks, Storage storage) throws SalException {
        try {
            String description = input.substring("todo".length()).trim();
            if (description.isEmpty()) {
                throw new SalException("Correct format: todo <task name>");
            }
            Task task = new Todo(description);
            tasks.add(task);
            saveTasks(storage, tasks);
            printAdded(task, tasks.size());
        } catch (SalException e) {
            throw e;
        } catch (Exception e) {
            throw new SalException("Correct format: todo <task name>");
        }
    }

    /**
     * Adds a deadline. Expected format: {@code deadline <task name> /by <date/time>}.
     */
    private static void addDeadline(String input, ArrayList<Task> tasks, Storage storage) throws SalException {
        try {
            String rest = input.substring("deadline".length()).trim();
            String[] parts = rest.split(" /by ", 2);
            if (parts.length < 2 || parts[0].trim().isEmpty() || parts[1].trim().isEmpty()) {
                throw new SalException("Correct format: deadline <task name> /by <date/time>");
            }
            TaskDateTime by = DateTimeParser.parse(parts[1].trim());
            Task task = new Deadline(parts[0].trim(), by);
            tasks.add(task);
            saveTasks(storage, tasks);
            printAdded(task, tasks.size());
        } catch (SalException e) {
            throw e;
        } catch (DateTimeParseException e) {
            throw new SalException(invalidDateTimeMessage());
        } catch (Exception e) {
            throw new SalException("Correct format: deadline <task name> /by <date/time>");
        }
    }

    /**
     * Adds an event. Expected format: {@code event <task name> /from <start> /to <end>}.
     */
    private static void addEvent(String input, ArrayList<Task> tasks, Storage storage) throws SalException {
        try {
            String rest = input.substring("event".length()).trim();
            String[] fromParts = rest.split(" /from ", 2);
            if (fromParts.length < 2 || fromParts[0].trim().isEmpty()) {
                throw new SalException("Correct format: event <task name> /from <start> /to <end>");
            }
            String[] toParts = fromParts[1].split(" /to ", 2);
            if (toParts.length < 2 || toParts[0].trim().isEmpty() || toParts[1].trim().isEmpty()) {
                throw new SalException("Correct format: event <task name> /from <start> /to <end>");
            }
            TaskDateTime from = DateTimeParser.parse(toParts[0].trim());
            TaskDateTime to = DateTimeParser.parse(toParts[1].trim());
            Task task = new Event(fromParts[0].trim(), from, to);
            tasks.add(task);
            saveTasks(storage, tasks);
            printAdded(task, tasks.size());
        } catch (SalException e) {
            throw e;
        } catch (DateTimeParseException e) {
            throw new SalException(invalidDateTimeMessage());
        } catch (Exception e) {
            throw new SalException("Correct format: event <task name> /from <start> /to <end>");
        }
    }

    /**
     * Returns the shared error message for unsupported date/time formats.
     */
    private static String invalidDateTimeMessage() {
        return "Invalid date/time format. Use yyyy-mm-dd (e.g., 2019-10-15) "
                + "or d/M/yyyy HHmm (e.g., 2/12/2019 1800).";
    }

    /**
     * Prints the confirmation shown after a task is added.
     *
     * @param task Newly added task.
     * @param taskCount Current number of tasks in the list.
     */
    private static void printAdded(Task task, int taskCount) {
        System.out.println(LINE);
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + task);
        System.out.println("Now you have " + taskCount + " tasks in the list.");
        System.out.println(LINE);
    }
}
