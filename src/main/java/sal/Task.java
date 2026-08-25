package sal;

/**
 * Represents a task with a description and a done/not-done status.
 */
public class Task {
    protected String description;
    protected boolean isDone;

    /**
     * Creates a new unfinished task with the given description.
     *
     * @param description Text describing the task.
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /**
     * Returns {@code X} if the task is done, or a space if it is not.
     *
     * @return Status icon for display.
     */
    public String getStatusIcon() {
        return (isDone ? "X" : " ");
    }

    /**
     * Marks this task as done.
     */
    public void markAsDone() {
        this.isDone = true;
    }

    /**
     * Marks this task as not done.
     */
    public void markAsNotDone() {
        this.isDone = false;
    }

    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + description;
    }
}
