package appointmentplanner;

import appointmentplanner.api.LocalDay;
import appointmentplanner.api.LocalDayPlan;
import appointmentplanner.api.Timeline;

import java.time.Instant;
import java.time.temporal.TemporalAmount;

public class LocalDayPlanImpl implements LocalDayPlan {

    private final LocalDay day;
    private final Timeline timeline;

    public LocalDayPlanImpl(LocalDay day, Timeline timeline) {
        this.day = day;
        this.timeline = timeline;
    }

    public LocalDayPlanImpl(LocalDay day, Instant start, Instant end) {
        this(day, new TimelineImpl(start, end));
    }

    /**
     * LocalDay specifies date and time zone.
     *
     * @return the day,
     */
    @Override
    public LocalDay getDay() {
        return this.day;
    }

    /**
     * Start, inclusive.
     *
     * @return lower limit
     */
    @Override
    public Instant earliest() {
        return timeline.start();
    }

    /**
     * End, exclusive.
     *
     * @return upper limit
     */
    @Override
    public Instant tooLate() {
        return timeline.end();
    }

    /**
     * Get the time line to combine appointments in different time zones.
     *
     * @return the timeline kept by this local day plan
     */
    @Override
    public Timeline getTimeline() {
        return timeline;
    }
}
