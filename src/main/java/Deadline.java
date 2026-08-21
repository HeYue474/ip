/**
 * A task that must be done by a specific date/time (stored as text).
 */
public class Deadline extends Task {
    protected String by;

    /**
     * Creates a deadline with the given description and due date/time text.
     *
     * @param description Text describing the deadline.
     * @param by Due date/time as plain text.
     */
    public Deadline(String description, String by) {
        super(description);
        this.by = by;
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + by + ")";
    }
}
