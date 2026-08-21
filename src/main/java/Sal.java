import java.util.Scanner;

/**
 * Entry point for the Sal chatbot.
 * Level-5 behavior: handle invalid/unknown user input via {@link SalException}.
 */
public class Sal {
    private static final String LINE = "____________________________________________________________";
    private static final int MAX_TASKS = 100;

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

        Task[] tasks = new Task[MAX_TASKS];
        int taskCount = 0;

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
                    printList(tasks, taskCount);
                    break;
                case "mark":
                    markTask(input, tasks, taskCount);
                    break;
                case "unmark":
                    unmarkTask(input, tasks, taskCount);
                    break;
                case "todo":
                    taskCount = addTodo(input, tasks, taskCount);
                    break;
                case "deadline":
                    taskCount = addDeadline(input, tasks, taskCount);
                    break;
                case "event":
                    taskCount = addEvent(input, tasks, taskCount);
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
    private static void printList(Task[] tasks, int taskCount) {
        System.out.println(LINE);
        System.out.println("Here are the tasks in your list:");
        for (int i = 0; i < taskCount; i++) {
            System.out.println((i + 1) + "." + tasks[i]);
        }
        System.out.println(LINE);
    }

    /**
     * Marks a task as done. Expected format: {@code mark <task number>}.
     */
    private static void markTask(String input, Task[] tasks, int taskCount) throws SalException {
        try {
            int index = Integer.parseInt(input.substring("mark".length()).trim()) - 1;
            if (index < 0 || index >= taskCount) {
                throw new SalException("Task number out of bounds (0-" + taskCount);
            }
            tasks[index].markAsDone();
            System.out.println(LINE);
            System.out.println("Nice! I've marked this task as done:");
            System.out.println("  " + tasks[index]);
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
    private static void unmarkTask(String input, Task[] tasks, int taskCount) throws SalException {
        try {
            int index = Integer.parseInt(input.substring("unmark".length()).trim()) - 1;
            if (index < 0 || index >= taskCount) {
                throw new SalException("Task number out of bounds (0-" + taskCount);
            }
            tasks[index].markAsNotDone();
            System.out.println(LINE);
            System.out.println("OK, I've marked this task as not done yet:");
            System.out.println("  " + tasks[index]);
            System.out.println(LINE);
        } catch (SalException e) {
            throw e;
        } catch (Exception e) {
            throw new SalException("Correct format: unmark <task number>");
        }
    }

    /**
     * Adds a todo. Expected format: {@code todo <task name>}.
     *
     * @return Updated task count after adding.
     */
    private static int addTodo(String input, Task[] tasks, int taskCount) throws SalException {
        try {
            String description = input.substring("todo".length()).trim();
            if (description.isEmpty()) {
                throw new SalException("Correct format: todo <task name>");
            }
            Task task = new Todo(description);
            tasks[taskCount] = task;
            taskCount++;
            printAdded(task, taskCount);
            return taskCount;
        } catch (SalException e) {
            throw e;
        } catch (Exception e) {
            throw new SalException("Correct format: todo <task name>");
        }
    }

    /**
     * Adds a deadline. Expected format: {@code deadline <task name> /by <date/time>}.
     *
     * @return Updated task count after adding.
     */
    private static int addDeadline(String input, Task[] tasks, int taskCount) throws SalException {
        try {
            String rest = input.substring("deadline".length()).trim();
            String[] parts = rest.split(" /by ", 2);
            if (parts.length < 2 || parts[0].trim().isEmpty() || parts[1].trim().isEmpty()) {
                throw new SalException("Correct format: deadline <task name> /by <date/time>");
            }
            Task task = new Deadline(parts[0].trim(), parts[1].trim());
            tasks[taskCount] = task;
            taskCount++;
            printAdded(task, taskCount);
            return taskCount;
        } catch (SalException e) {
            throw e;
        } catch (Exception e) {
            throw new SalException("Correct format: deadline <task name> /by <date/time>");
        }
    }

    /**
     * Adds an event. Expected format: {@code event <task name> /from <start> /to <end>}.
     *
     * @return Updated task count after adding.
     */
    private static int addEvent(String input, Task[] tasks, int taskCount) throws SalException {
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
            Task task = new Event(fromParts[0].trim(), toParts[0].trim(), toParts[1].trim());
            tasks[taskCount] = task;
            taskCount++;
            printAdded(task, taskCount);
            return taskCount;
        } catch (SalException e) {
            throw e;
        } catch (Exception e) {
            throw new SalException("Correct format: event <task name> /from <start> /to <end>");
        }
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
