package duke.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents an event task.
 *
 * @author Peter
 */
public class Event extends Task {
    /**
     * Time associated with the event task.
     */
    private final LocalDateTime atTime;

    /**
     * Constructor for an event task.
     *
     * @param description Description associated with the event task.
     * @param isMarked    Boolean flag of whether the event task is done.
     * @param atTime      Time associated with the event task.
     */
    public Event(String description, boolean isMarked, LocalDateTime atTime) {
        super(description, isMarked);
        this.atTime = atTime;
    }

    /**
     * Converts the event task to a form legible by the storage.
     *
     * @return Data representation of the event task.
     */
    @Override
    public String toData() {
        return "E | " + this.isMarked + " | " + this.description + " | " + this.atTime;
    }

    /**
     * Creates a copy of the event task.
     *
     * @return Copy of the event task.
     */
    @Override
    public Event copy() {
        return new Event(this.description, this.isMarked, this.atTime);
    }

    /**
     * Reformats and returns string representation of event task.
     *
     * @return Reformatted string representation of event task.
     */
    @Override
    public String toString() {
        String atTimeFormatted = this.atTime.format(DateTimeFormatter.ofPattern(
                "HH:mm, MMM dd yyyy"));
        return "[E]" + super.toString() + " (at: " + atTimeFormatted + ")";
    }
}
