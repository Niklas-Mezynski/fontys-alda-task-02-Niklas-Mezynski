package appointmentplanner;

import java.time.Instant;

public class AllocationList {
    private AllocationNode head;
    private AllocationNode tail;

    public AllocationList(Instant start, Instant end) {
        if (!start.isBefore(end))
            throw new IllegalArgumentException("start time must be before end time");
        this.head = new AllocationNode(start, start);
        this.tail = new AllocationNode(end, end);

    }
}
