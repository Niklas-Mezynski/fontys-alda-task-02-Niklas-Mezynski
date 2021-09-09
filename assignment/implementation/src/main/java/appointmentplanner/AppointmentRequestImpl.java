package appointmentplanner;

import appointmentplanner.api.AppointmentData;
import appointmentplanner.api.AppointmentRequest;
import appointmentplanner.api.Priority;
import appointmentplanner.api.TimePreference;

import java.time.LocalTime;

public class AppointmentRequestImpl implements AppointmentRequest {


    private final AppointmentData appData;
    private final LocalTime prefStart;
    private final TimePreference fallBack;

    public AppointmentRequestImpl(AppointmentData appData, LocalTime prefStart, TimePreference fallBack) {
        this.appData = appData;
        this.prefStart = prefStart;
        this.fallBack = fallBack;
    }

    @Override
    public String getDescription() {
        return appData.getDescription();
    }

    @Override
    public Priority getPriority() {
        return appData.getPriority();
    }

    @Override
    public LocalTime getStartTime() {
        return prefStart;
    }

    @Override
    public AppointmentData getAppointmentData() {
        return appData;
    }

    @Override
    public TimePreference getTimePreference() {
            return fallBack;
    }
}
