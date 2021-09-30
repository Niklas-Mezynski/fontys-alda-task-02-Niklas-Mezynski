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

    /**
     * Constructor used for appointment slots
     * @param appointmentData
     */
    public AllocationNode(Instant start, Instant end, AppointmentData appointmentData) {
        this.start = start;
        this.end = end;
        this.appData = appointmentData;
    }

    /**
     * Constructor used for free TimeSlots
     * @param start
     * @param end
     */
    public AllocationNode(Instant start, Instant end) {
        this(start, end, null);
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

    public void setStart(Instant start) {
        this.start = start;
    }

    public void setEnd(Instant end) {
        this.end = end;
    }
}
