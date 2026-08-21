import java.util.Scanner;

/**
 * Entry point for the Sal chatbot.
 * Level-4 behavior: support {@code todo}, {@code deadline}, and {@code event} task types
 * via inheritance ({@link Todo}, {@link Deadline}, {@link Event}).
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
            String input = scanner.nextLine();
            if (input.equals("bye")) {
                System.out.println(LINE);
                System.out.println("Bye. Hope to see you again soon!");
                System.out.println(LINE);
                break;
            }

            if (input.equals("list")) {
                System.out.println(LINE);
                System.out.println("Here are the tasks in your list:");
                for (int i = 0; i < taskCount; i++) {
                    System.out.println((i + 1) + "." + tasks[i]);
                }
                System.out.println(LINE);
                continue;
            }

            if (input.startsWith("mark ")) {
                int index = Integer.parseInt(input.substring(5).trim()) - 1;
                tasks[index].markAsDone();
                System.out.println(LINE);
                System.out.println("Nice! I've marked this task as done:");
                System.out.println("  " + tasks[index]);
                System.out.println(LINE);
                continue;
            }

            if (input.startsWith("unmark ")) {
                int index = Integer.parseInt(input.substring(7).trim()) - 1;
                tasks[index].markAsNotDone();
                System.out.println(LINE);
                System.out.println("OK, I've marked this task as not done yet:");
                System.out.println("  " + tasks[index]);
                System.out.println(LINE);
                continue;
            }

            if (input.startsWith("todo ")) {
                String description = input.substring(5);
                Task task = new Todo(description);
                tasks[taskCount] = task;
                taskCount++;
                printAdded(task, taskCount);
                continue;
            }

            if (input.startsWith("deadline ")) {
                String rest = input.substring(9);
                String[] parts = rest.split(" /by ", 2);
                Task task = new Deadline(parts[0], parts[1]);
                tasks[taskCount] = task;
                taskCount++;
                printAdded(task, taskCount);
                continue;
            }

            if (input.startsWith("event ")) {
                String rest = input.substring(6);
                String[] fromParts = rest.split(" /from ", 2);
                String[] toParts = fromParts[1].split(" /to ", 2);
                Task task = new Event(fromParts[0], toParts[0], toParts[1]);
                tasks[taskCount] = task;
                taskCount++;
                printAdded(task, taskCount);
                continue;
            }
        }
        scanner.close();
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
