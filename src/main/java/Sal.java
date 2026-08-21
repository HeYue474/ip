import java.util.Scanner;

/**
 * Entry point for the Sal chatbot.
 * Level-2 behavior: store user-entered tasks, list them on request, and exit on {@code bye}.
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

        String[] tasks = new String[MAX_TASKS];
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
                for (int i = 0; i < taskCount; i++) {
                    System.out.println((i + 1) + ". " + tasks[i]);
                }
                System.out.println(LINE);
                continue;
            }

            // Any other input is treated as a new task description to store.
            tasks[taskCount] = input;
            taskCount++;
            System.out.println(LINE);
            System.out.println("added: " + input);
            System.out.println(LINE);
        }
        scanner.close();
    }
}
