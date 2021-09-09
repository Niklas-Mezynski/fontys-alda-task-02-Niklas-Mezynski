package appointmentplanner;

import appointmentplanner.api.AppointmentData;
import appointmentplanner.api.Priority;

import java.time.Duration;

public class AppointmentDataImpl implements AppointmentData {


    private final String description;
    private final Duration duration;
    private Priority priority;

    public AppointmentDataImpl(String description, Duration duration) {
        this.description = description;
        this.duration = duration;
    }

    public AppointmentDataImpl(String description, Duration duration, Priority priority) {
        this(description, duration);
        this.priority = priority;
    }

    @Override
    public Duration getDuration() {
        return duration;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public Priority getPriority() {
        return priority;
    }
}
