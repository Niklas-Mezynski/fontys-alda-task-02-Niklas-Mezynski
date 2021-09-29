package appointmentplanner;

import appointmentplanner.api.AppointmentData;
import appointmentplanner.api.TimeSlot;

import java.time.Instant;

public class AllocationNode implements TimeSlot {
    protected AllocationNode next;
    protected AllocationNode prev;
    private AppointmentData appData;
    private Instant start;

    public AllocationNode(AppointmentData appointmentData, Instant start) {
        this.appData = appointmentData;
        this.start = start;
    }

    @Override
    public Instant getStart() {
        return start;
    }

    @Override
    public Instant getEnd() {
        return start.plus(appData.getDuration());
    }

    public AppointmentData getPurpose() {
        return appData;
    }
}
