package sal;

import java.time.format.DateTimeParseException;

/**
 * Makes sense of raw user command strings.
 * Validates format and turns arguments into indexes or {@link Task} objects.
 * Does not execute commands or talk to the user — that stays in {@link Sal} and {@link Ui}.
 */
public class Parser {
    /**
     * Returns the first word of the input (the command name).
     *
     * @param input Full user input, already trimmed.
     * @return Command word such as {@code todo} or {@code list}.
     * @throws SalException If the input is empty.
     */
    public static String getCommandWord(String input) throws SalException {
        if (input.isEmpty()) {
            throw new SalException("Command not recognised.");
        }
        return input.split("\\s+", 2)[0];
    }

    /**
     * Parses a one-based task number after a command word into a zero-based index.
     *
     * @param input Full command line, e.g. {@code delete 2}.
     * @param commandWord Leading command word to strip, e.g. {@code delete}.
     * @param formatHint Message shown if the number is missing or not an integer.
     * @return Zero-based task index.
     * @throws SalException If the index cannot be parsed.
     */
    public static int parseTaskIndex(String input, String commandWord, String formatHint)
            throws SalException {
        try {
            return Integer.parseInt(input.substring(commandWord.length()).trim()) - 1;
        } catch (Exception e) {
            throw new SalException(formatHint);
        }
    }

    /**
     * Parses a todo command into a {@link Todo}.
     * Expected format: {@code todo <task name>}.
     *
     * @param input Full command line.
     * @return A new unfinished todo.
     * @throws SalException If the description is missing or the format is wrong.
     */
    public static Todo parseTodo(String input) throws SalException {
        try {
            String description = input.substring("todo".length()).trim();
            if (description.isEmpty()) {
                throw new SalException("Correct format: todo <task name>");
            }
            return new Todo(description);
        } catch (SalException e) {
            throw e;
        } catch (Exception e) {
            throw new SalException("Correct format: todo <task name>");
        }
    }

    /**
     * Parses a deadline command into a {@link Deadline}.
     * Expected format: {@code deadline <task name> /by <date/time>}.
     *
     * @param input Full command line.
     * @return A new unfinished deadline.
     * @throws SalException If the format or date/time is invalid.
     */
    public static Deadline parseDeadline(String input) throws SalException {
        try {
            String rest = input.substring("deadline".length()).trim();
            String[] parts = rest.split(" /by ", 2);
            if (parts.length < 2 || parts[0].trim().isEmpty() || parts[1].trim().isEmpty()) {
                throw new SalException("Correct format: deadline <task name> /by <date/time>");
            }
            TaskDateTime by = DateTimeParser.parse(parts[1].trim());
            return new Deadline(parts[0].trim(), by);
        } catch (SalException e) {
            throw e;
        } catch (DateTimeParseException e) {
            throw new SalException(invalidDateTimeMessage());
        } catch (Exception e) {
            throw new SalException("Correct format: deadline <task name> /by <date/time>");
        }
    }

    /**
     * Parses an event command into an {@link Event}.
     * Expected format: {@code event <task name> /from <start> /to <end>}.
     *
     * @param input Full command line.
     * @return A new unfinished event.
     * @throws SalException If the format or date/time is invalid.
     */
    public static Event parseEvent(String input) throws SalException {
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
            return new Event(fromParts[0].trim(), from, to);
        } catch (SalException e) {
            throw e;
        } catch (DateTimeParseException e) {
            throw new SalException(invalidDateTimeMessage());
        } catch (Exception e) {
            throw new SalException("Correct format: event <task name> /from <start> /to <end>");
        }
    }

    /**
     * Shared error message for unsupported date/time formats.
     */
    private static String invalidDateTimeMessage() {
        return "Invalid date/time format. Use yyyy-mm-dd (e.g., 2019-10-15) "
                + "or d/M/yyyy HHmm (e.g., 2/12/2019 1800).";
    }
}
