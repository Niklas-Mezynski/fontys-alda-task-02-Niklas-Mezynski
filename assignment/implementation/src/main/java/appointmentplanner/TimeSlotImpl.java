package appointmentplanner;

import appointmentplanner.api.TimeSlot;

import java.time.Instant;
import java.util.Objects;

public class TimeSlotImpl implements TimeSlot {
    private final Instant start;
    private final Instant end;

    public TimeSlotImpl(Instant start, Instant end) {
        if (end.isBefore(start))
            throw new IllegalArgumentException("End time is not allowed to be before start time");
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TimeSlotImpl timeSlot = (TimeSlotImpl) o;
        return Objects.equals(start, timeSlot.start) && Objects.equals(end, timeSlot.end);
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, end);
    }
}
