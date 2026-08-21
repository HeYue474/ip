/**
 * Entry point for the Sal chatbot.
 * Level-0 behavior: greet the user, then exit.
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
        System.out.println("Bye. Hope to see you again soon!");
        System.out.println(LINE);
    }
}
