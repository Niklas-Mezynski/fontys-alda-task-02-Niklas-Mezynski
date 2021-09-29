package appointmentplanner;

import appointmentplanner.api.AppointmentData;
import appointmentplanner.api.TimeSlot;

import java.time.Instant;

public class AllocationNode implements TimeSlot {
    protected AllocationNode next;
    protected AllocationNode prev;
    private AppointmentData appData;
    private TimeSlot timeSlot;

    /**
     * Constructor used for appointment slots
     * @param appointmentData
     */
    public AllocationNode(TimeSlot timeSlot, AppointmentData appointmentData) {
        this.timeSlot = timeSlot;
        this.appData = appointmentData;
    }

    /**
     * Constructor used for free TimeSlots
     * @param start
     * @param end
     */
    public AllocationNode(TimeSlot timeSlot) {
        this(timeSlot, null);
    }

    @Override
    public Instant getStart() {
        return timeSlot.getStart();
    }

    @Override
    public Instant getEnd() {
        return timeSlot.getEnd();
    }

    public AppointmentData getPurpose() {
        return appData;
    }
}
