package duke.logic;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.stream.Stream;

import duke.command.AddCommand;
import duke.command.ClearCommand;
import duke.command.Command;
import duke.command.DeleteCommand;
import duke.command.ExitCommand;
import duke.command.FindCommand;
import duke.command.ListCommand;
import duke.command.MarkCommand;
import duke.command.UndoCommand;
import duke.task.Deadline;
import duke.task.Event;
import duke.task.Todo;

/**
 * Represents a parser that parses commands fed to Duke.
 *
 * @author Peter
 */
public class Parser {
    private enum CommandType {
        BYE, LIST, UNDO, MARK, UNMARK, DELETE, TODO, DEADLINE, EVENT, FIND, CLEAR
    }

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(
            "dd-MM-yyyy HH:mm");

    private static int parseIndex(String index) {
        return Integer.parseInt(index) - 1;
    }

    /**
     * Parses the description portion of a command.
     *
     * @param options Arguments to the command that is to be split and parsed.
     * @param divider Divider that is used to split arguments.
     * @return Description of task.
     */
    private static String parseDescription(String options, String divider) {
        String[] splitCommand = options.split(divider);
        return splitCommand[0];
    }

    /**
     * Parses the time portion of a command.
     *
     * @param options Arguments to the command that is to be split and parsed.
     * @param divider Divider that is used to split arguments.
     * @return Date and time of task in LocalDateTime format.
     * @throws DukeException If format of arguments passed is incorrect.
     */
    private static LocalDateTime parseTime(String options, String divider) throws DukeException {
        String[] splitCommand = options.split(divider);
        if (splitCommand.length < 2) {
            throw new DukeException("NO TIME SUPPLIED");
        }

        LocalDateTime time;

        try {
            time = LocalDateTime.parse(splitCommand[1], formatter);
        } catch (DateTimeParseException e) {
            throw new DukeException("INVALID TIME FORMAT (dd-MM-yyyy HH:mm)");
        }

        return time;
    }

    /**
     * Parses an input string to the corresponding command along with additional arguments.
     *
     * @param input String that is to be parsed.
     * @return Command corresponding to the input string.
     * @throws DukeException If format of command is incorrect.
     */
    public static Command parse(String input) throws DukeException {
        String[] splitInput = input.split(" ", 2);

        CommandType commandType;
        Command command;

        try {
            commandType = CommandType.valueOf(splitInput[0].toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new DukeException("INVALID COMMAND");
        }

        try {
            switch (commandType) {
            case BYE:
                command = new ExitCommand();
                break;
            case LIST:
                command = new ListCommand();
                break;
            case UNDO:
                command = new UndoCommand();
                break;
            case MARK:
                command = new MarkCommand(parseIndex(splitInput[1]), true);
                break;
            case UNMARK:
                command = new MarkCommand(parseIndex(splitInput[1]), false);
                break;
            case DELETE:
                command = new DeleteCommand(parseIndex(splitInput[1]));
                break;
            case TODO:
                command = new AddCommand(new Todo(splitInput[1], false));
                break;
            case EVENT:
                command = new AddCommand(
                        new Event(parseDescription(splitInput[1], " /at "),
                                false, parseTime(splitInput[1], " /at ")));
                break;
            case DEADLINE:
                command = new AddCommand(
                        new Deadline(parseDescription(splitInput[1], " /by "),
                                false, parseTime(splitInput[1], " /by ")));
                break;
            case FIND:
                command = new FindCommand(Stream.of(splitInput[1].split(","))
                        .map(String::trim).toArray(String[]::new));
                break;
            case CLEAR:
                command = new ClearCommand();
                break;
            default:
                throw new DukeException("INVALID COMMAND");
            }
        } catch (IndexOutOfBoundsException e) {
            throw new DukeException("TOO FEW ARGUMENTS SUPPLIED");
        }

        return command;
    }
}
