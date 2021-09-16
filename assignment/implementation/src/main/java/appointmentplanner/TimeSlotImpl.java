package appointmentplanner;

import appointmentplanner.api.TimeSlot;

import java.time.Instant;

public class TimeSlotImpl implements TimeSlot {
    private final Instant start;
    private final Instant end;

    public TimeSlotImpl(Instant start, Instant end) {
        this.start = start;
        this.end = end;
    }

    /**
     * Get the start of the free time range. The start time is included in the
     * range.
     *
     * @return the start time
     */
    @Override
    public Instant getStart() {
        return start;
    }

    /**
     * Get the end of the free time range. The end time is NOT included in the
     * range.
     *
     * @return the end time
     */
    @Override
    public Instant getEnd() {
        return end;
    }
}
