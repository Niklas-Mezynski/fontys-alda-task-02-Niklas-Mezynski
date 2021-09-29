package appointmentplanner;

import appointmentplanner.api.AppointmentData;
import appointmentplanner.api.TimeSlot;

import java.time.Instant;

public class AllocationNode implements TimeSlot {
    protected AllocationNode next;
    protected AllocationNode prev;
    private AppointmentData appData;
    private Instant start;
    private Instant end;

    public AllocationNode(Instant start, Instant end, AppointmentData appointmentData) {
        this.start = start;
        this.end = end;
        this.appData = appointmentData;
    }

    @Override
    public Instant getStart() {
        return start;
    }

    @Override
    public Instant getEnd() {
        return end;
    }

    public AppointmentData getPurpose() {
        return appData;
    }
}
