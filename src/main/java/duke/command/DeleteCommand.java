package duke.command;

import duke.logic.DukeException;
import duke.logic.Storage;
import duke.logic.TaskList;
import duke.logic.TaskStack;
import duke.logic.Ui;
import duke.task.Task;

/**
 * Represents a command that deletes a task.
 *
 * @author Peter
 */
public class DeleteCommand extends Command {
    /**
     * Index of task in a list of tasks that is to be deleted.
     */
    private final int index;

    /**
     * Constructor for a task delete command.
     *
     * @param index Index of task in a list of tasks that is to be deleted.
     */
    public DeleteCommand(int index) {
        super();
        this.index = index;
    }

    /**
     * Deletes the task located at the associated index in a given list of tasks, displays a
     * response message, and writes to a local file associated with the list of tasks.
     *
     * @param taskList List of tasks that is to have a task removed.
     * @param ui       UI responsible for displaying response from Duke.
     * @param storage  Storage responsible for writing to local file.
     * @return String response from Duke upon successful execution.
     * @throws DukeException If write to file is unsuccessful.
     */
    @Override
    public String execute(TaskList taskList, Ui ui, Storage storage, TaskStack taskStack) throws DukeException {
        Task task;

        if (taskList.isValidIndex(index)) {
            task = taskList.get(index);

            TaskList copiedTaskList = new TaskList();
            copiedTaskList.copy(taskList);
            taskStack.push(copiedTaskList);

            taskList.remove(index);
        } else {
            throw new DukeException("INVALID INDEX");
        }

        storage.writeToFile(taskList);
        String output = "TASK REMOVED:\n"
                + task + "\n"
                + taskList.size() + " TASK(S) NOW.";
        ui.showMessage(output);
        return output;
    }

    @Override
    public boolean isExitCommand() {
        return false;
    }
}
