package sal;

import java.io.IOException;

/**
 * Entry point and orchestrator for the Sal chatbot.
 * Wires together {@link Ui}, {@link Storage}, {@link Parser}, and {@link TaskList}
 * so each class has a single responsibility.
 */
public class Sal {
    private static final String DATA_FILE_PATH = "data/sal.txt";

    private final Ui ui;
    private final Storage storage;
    private final TaskList tasks;

    /**
     * Creates Sal with UI, storage, and a task list loaded from disk when possible.
     *
     * @param filePath Relative path to the task data file.
     */
    public Sal(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        tasks = loadTasks();
    }

    /**
     * Starts the interactive command loop until the user says bye.
     */
    public void run() {
        ui.showWelcome();
        boolean isExit = false;
        while (!isExit) {
            String input = ui.readCommand();
            try {
                String command = Parser.getCommandWord(input);
                switch (command) {
                case "bye":
                    ui.showGoodbye();
                    ui.close();
                    isExit = true;
                    break;
                case "list":
                    ui.showTaskList(tasks);
                    break;
                case "mark":
                    markTask(input);
                    break;
                case "unmark":
                    unmarkTask(input);
                    break;
                case "todo":
                    addTask(Parser.parseTodo(input));
                    break;
                case "deadline":
                    addTask(Parser.parseDeadline(input));
                    break;
                case "event":
                    addTask(Parser.parseEvent(input));
                    break;
                case "delete":
                    deleteTask(input);
                    break;
                case "find":
                    ui.showFoundTasks(tasks.find(Parser.parseFind(input)));
                    break;
                default:
                    throw new SalException("Command not recognised.");
                }
            } catch (SalException e) {
                ui.showError(e.getMessage());
            }
        }
    }

    /**
     * Loads tasks from disk, or starts empty if loading fails.
     */
    private TaskList loadTasks() {
        try {
            return new TaskList(storage.load());
        } catch (IOException e) {
            ui.showLoadingError();
            return new TaskList();
        }
    }

    /**
     * Persists the current task list to disk.
     */
    private void saveTasks() throws SalException {
        try {
            storage.save(tasks);
        } catch (IOException e) {
            throw new SalException("Could not save tasks to disk.");
        }
    }

    private void markTask(String input) throws SalException {
        int index = Parser.parseTaskIndex(input, "mark", "Correct format: mark <task number>");
        Task task = tasks.markAsDone(index);
        saveTasks();
        ui.showMarked(task);
    }

    private void unmarkTask(String input) throws SalException {
        int index = Parser.parseTaskIndex(input, "unmark", "Correct format: unmark <task number>");
        Task task = tasks.markAsNotDone(index);
        saveTasks();
        ui.showUnmarked(task);
    }

    private void deleteTask(String input) throws SalException {
        int index = Parser.parseTaskIndex(input, "delete", "Correct format: delete <task number>");
        Task removed = tasks.delete(index);
        saveTasks();
        ui.showTaskDeleted(removed, tasks.size());
    }

    private void addTask(Task task) throws SalException {
        tasks.add(task);
        saveTasks();
        ui.showTaskAdded(task, tasks.size());
    }

    /**
     * Launches Sal using the default data file path.
     *
     * @param args Command-line arguments (unused).
     */
    public static void main(String[] args) {
        new Sal(DATA_FILE_PATH).run();
    }
}
