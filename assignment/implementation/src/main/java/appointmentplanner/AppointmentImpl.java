package appointmentplanner;

import appointmentplanner.api.Appointment;
import appointmentplanner.api.AppointmentData;
import appointmentplanner.api.AppointmentRequest;
import appointmentplanner.api.Priority;

import java.time.Duration;
import java.time.Instant;

public class AppointmentImpl implements Appointment {

    private final Instant start;
    private final Instant end;
    private final AppointmentData appData;
    private final AppointmentRequest appReq;

    public AppointmentImpl(Instant start, Instant end, AppointmentData appData, AppointmentRequest appReq) {
        this.start = start;
        this.end = end;
        this.appData = appData;
        this.appReq = appReq;
    }

    /**
     * An appointment always has some no-zero length.
     *
     * @return the duration of the appointment.
     */
    @Override
    public Duration getDuration() {
        return appData.getDuration();
    }

    /**
     * There is also a non-empty description.
     *
     * @return non empty string describing the appointment.
     */
    @Override
    public String getDescription() {
        return appData.getDescription();
    }

    /**
     * Get the priority of this appointment.
     *
     * @return Priority.
     */
    @Override
    public Priority getPriority() {
        return appData.getPriority();
    }

    /**
     * Get the appointment data for this appointment.
     *
     * @return the data
     */
    @Override
    public AppointmentData getAppointmentData() {
        return appData;
    }

    /**
     * Get the request that led to this appointment.
     *
     * @return the request.
     */
    @Override
    public AppointmentRequest getRequest() {
        return appReq;
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
