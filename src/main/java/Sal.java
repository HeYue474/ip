import java.util.Scanner;

/**
 * Entry point for the Sal chatbot.
 * Level-1 behavior: greet the user, echo each command, and exit on {@code bye}.
 */
public class Sal {
    private static final String LINE = "____________________________________________________________";

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

        Scanner scanner = new Scanner(System.in);
        while (true) {
            String input = scanner.nextLine();
            if (input.equals("bye")) {
                System.out.println(LINE);
                System.out.println("Bye. Hope to see you again soon!");
                System.out.println(LINE);
                break;
            }

            System.out.println(LINE);
            System.out.println(input);
            System.out.println(LINE);
        }
        scanner.close();
    }
}
