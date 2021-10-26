package appointmentplanner;

import appointmentplanner.api.AppointmentData;
import appointmentplanner.api.Priority;

import java.time.Duration;
import java.util.Objects;

public class AppointmentDataImpl implements AppointmentData {


    private final String description;
    private final Duration duration;
    private Priority priority;

    public AppointmentDataImpl(String description, Duration duration) {
        this(description, duration, Priority.LOW);
    }

    public AppointmentDataImpl(String description, Duration duration, Priority priority) {
        if (duration.isZero() || duration.isNegative() || description == null)
            throw new IllegalArgumentException("Duration must be positive and there must be a description");
        if (description.equals(""))
            throw new IllegalArgumentException("Duration must be positive and there must be a description");
        this.description = description;
        this.duration = duration;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AppointmentDataImpl that = (AppointmentDataImpl) o;
        return Objects.equals(description, that.description) && Objects.equals(duration, that.duration) && priority == that.priority;
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, duration, priority);
    }
}
