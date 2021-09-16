package appointmentplanner;

import appointmentplanner.api.LocalDay;
import appointmentplanner.api.LocalDayPlan;
import appointmentplanner.api.Timeline;

import java.time.Instant;

public class LocalDayPlanImpl implements LocalDayPlan {

    private final LocalDay day;
    private final Instant start;
    private final Instant end;

    public LocalDayPlanImpl(LocalDay day, Instant start, Instant end) {
        this.day = day;
        this.start = start;
        this.end = end;
    }

    /**
     * LocalDay specifies date and time zone.
     *
     * @return the day,
     */
    @Override
    public LocalDay getDay() {
        return null;
    }

    /**
     * Start, inclusive.
     *
     * @return lower limit
     */
    @Override
    public Instant earliest() {
        return null;
    }

    /**
     * End, exclusive.
     *
     * @return upper limit
     */
    @Override
    public Instant tooLate() {
        return null;
    }

    /**
     * Get the time line to combine appointments in different time zones.
     *
     * @return the timeline kept by this local day plan
     */
    @Override
    public Timeline getTimeline() {
        return null;
    }
}
