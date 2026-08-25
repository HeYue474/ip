import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Reads and writes tasks to a file on disk.
 */
public class Storage {
    private final Path filePath;

    /**
     * Creates storage that uses the given relative file path from the project root.
     *
     * @param filePath Relative path to the task data file (OS-independent).
     */
    public Storage(String filePath) {
        this.filePath = Paths.get(filePath);
    }

    /**
     * Loads tasks from disk. Returns an empty list if the file or its parent folder does not exist.
     *
     * @return Tasks loaded from disk.
     * @throws IOException If the file exists but cannot be read or contains invalid data.
     */
    public ArrayList<Task> load() throws IOException {
        if (!Files.exists(filePath)) {
            return new ArrayList<>();
        }

        List<String> lines = Files.readAllLines(filePath);
        ArrayList<Task> tasks = new ArrayList<>();
        for (String line : lines) {
            if (line.trim().isEmpty()) {
                continue;
            }
            tasks.add(parseLine(line));
        }
        return tasks;
    }

    /**
     * Saves all tasks to disk, creating parent directories if needed.
     *
     * @param tasks Tasks to persist.
     * @throws IOException If writing fails.
     */
    public void save(ArrayList<Task> tasks) throws IOException {
        if (filePath.getParent() != null) {
            Files.createDirectories(filePath.getParent());
        }

        List<String> lines = new ArrayList<>();
        for (Task task : tasks) {
            lines.add(formatLine(task));
        }
        Files.write(filePath, lines);
    }

    /**
     * Converts one line from the data file into a {@link Task}.
     */
    private Task parseLine(String line) throws IOException {
        String[] parts = line.split(" \\| ", -1);
        if (parts.length < 3) {
            throw new IOException("Invalid task line: " + line);
        }

        String type = parts[0].trim();
        boolean isDone = parts[1].trim().equals("1");
        Task task;

        switch (type) {
        case "T":
            if (parts.length != 3) {
                throw new IOException("Invalid todo line: " + line);
            }
            task = new Todo(parts[2].trim());
            break;
        case "D":
            if (parts.length != 4) {
                throw new IOException("Invalid deadline line: " + line);
            }
            try {
                task = new Deadline(parts[2].trim(), DateTimeParser.parse(parts[3].trim()));
            } catch (DateTimeParseException e) {
                throw new IOException("Invalid deadline date/time: " + parts[3].trim(), e);
            }
            break;
        case "E":
            if (parts.length != 5) {
                throw new IOException("Invalid event line: " + line);
            }
            try {
                task = new Event(parts[2].trim(),
                        DateTimeParser.parse(parts[3].trim()),
                        DateTimeParser.parse(parts[4].trim()));
            } catch (DateTimeParseException e) {
                throw new IOException("Invalid event date/time in line: " + line, e);
            }
            break;
        default:
            throw new IOException("Unknown task type in line: " + line);
        }

        if (isDone) {
            task.markAsDone();
        }
        return task;
    }

    /**
     * Converts a {@link Task} into one line for the data file.
     */
    private String formatLine(Task task) {
        String status = task.isDone ? "1" : "0";

        if (task instanceof Event event) {
            return "E | " + status + " | " + event.description + " | "
                    + DateTimeParser.formatForStorage(event.from) + " | "
                    + DateTimeParser.formatForStorage(event.to);
        }
        if (task instanceof Deadline deadline) {
            return "D | " + status + " | " + deadline.description + " | "
                    + DateTimeParser.formatForStorage(deadline.by);
        }
        return "T | " + status + " | " + task.description;
    }
}
