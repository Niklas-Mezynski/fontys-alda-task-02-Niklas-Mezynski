package appointmentplanner;

import appointmentplanner.api.AppointmentData;
import appointmentplanner.api.AppointmentRequest;
import appointmentplanner.api.Priority;
import appointmentplanner.api.TimePreference;

import java.time.LocalTime;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AppointmentRequestImpl that = (AppointmentRequestImpl) o;
        return Objects.equals(appData, that.appData) && Objects.equals(prefStart, that.prefStart) && fallBack == that.fallBack;
    }

    @Override
    public int hashCode() {
        return Objects.hash(appData, prefStart, fallBack);
    }
}
