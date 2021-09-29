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
}
