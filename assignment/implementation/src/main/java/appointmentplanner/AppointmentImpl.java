package appointmentplanner;

import appointmentplanner.api.Appointment;
import appointmentplanner.api.AppointmentData;
import appointmentplanner.api.AppointmentRequest;
import appointmentplanner.api.Priority;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

public class AppointmentImpl implements Appointment {

    private final Instant start;
    private final Instant end;
    private final AppointmentRequest appReq;

    public AppointmentImpl(Instant start, Instant end, AppointmentRequest appReq) {
        this.start = start;
        this.end = end;
        this.appReq = appReq;
    }

    @Override
    public Priority getPriority() {
        return appReq.getAppointmentData().getPriority();
    }

    @Override
    public AppointmentData getAppointmentData() {
        return appReq.getAppointmentData();
    }

    @Override
    public AppointmentRequest getRequest() {
        return appReq;
    }

    @Override
    public Duration getDuration() {
        return appReq.getAppointmentData().getDuration();
    }

    @Override
    public String getDescription() {
        return appReq.getAppointmentData().getDescription();
    }

    @Override
    public Instant getStart() {
        return start;
    }

    @Override
    public Instant getEnd() {
        return end;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AppointmentImpl that = (AppointmentImpl) o;
        return Objects.equals(start, that.start) && Objects.equals(end, that.end) && Objects.equals(appReq, that.appReq);
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, end, appReq);
    }
}
