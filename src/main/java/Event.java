/**
 * A task that spans a start and end date/time (stored as text).
 */
public class Event extends Task {
    protected String from;
    protected String to;

    /**
     * Creates an event with the given description and start/end text.
     *
     * @param description Text describing the event.
     * @param from Start date/time as plain text.
     * @param to End date/time as plain text.
     */
    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + from + " to: " + to + ")";
    }
}
