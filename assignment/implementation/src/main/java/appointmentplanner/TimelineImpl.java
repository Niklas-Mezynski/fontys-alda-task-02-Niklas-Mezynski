package appointmentplanner;

import appointmentplanner.api.*;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class TimelineImpl implements Timeline {
    private AllocationNode head;
    private AllocationNode tail;
    private int nrOfAppointments;

    public TimelineImpl(Instant start, Instant end) {
        if (!start.isBefore(end))
            throw new IllegalArgumentException("start time must be before end time");

        this.nrOfAppointments = 0;
        this.head = new AllocationNode(start, start);
        this.tail = new AllocationNode(end, end);

        AllocationNode initialAllocation = new AllocationNode(start, end);
        this.head.next = initialAllocation;
        this.tail.prev = initialAllocation;
    }

    /**
     * Returns the number of appointments on a day.
     *
     * @return Number of appointments on this timeline.
     */
    @Override
    public int getNrOfAppointments() {
        return 0;
    }

    /**
     * Get the start of this timeline as instant.
     *
     * @return the starting instant.
     */
    @Override
    public Instant start() {
        return head.start;
    }

    /**
     * Get the end of this timeline.
     *
     * @return the end
     */
    @Override
    public Instant end() {
        return tail.end;
    }

    /**
     * Adds a new appointment to this day.Requirements:
     * <ul>
     * <li>If the appointmentData is null, an NullPointerException is
     * thrown.</li>
     * <li>An appointment can only be added between start time (including) and
     * end time (excluding) of the day.
     * <p>
     * AppointmentData having a duration greater than the length of the day in
     * minutes will result in null to be returned.</li>
     * <li>If the day does not have free space to accommodate the appointment,
     * null is returned.</li>
     * <li>Appointments aren't allowed to overlap.</li>
     * </ul>
     *
     * @param forDay         time partition to fit appointment
     * @param appointment    to add
     * @param timepreference or null if not applicable.
     * @return Appointment instance with all fields set according to
     * AppointmentData, or null if the constraints of this day and the requested
     * appointment can't be met.
     */
    @Override
    public Optional<Appointment> addAppointment(LocalDay forDay, AppointmentData appointment, TimePreference timepreference) {
        return Optional.empty();
    }

    /**
     * Add appointment with a fixed time.If the requested slot is available,
     * that is used and the appointment is returned. Otherwise null is returned.
     *
     * @param forDay      time partition to fit appointment
     * @param appointment to add
     * @param startTime   preferred start time of the appointment
     * @return the added appointment or null on failure.
     */
    @Override
    public Optional<Appointment> addAppointment(LocalDay forDay, AppointmentData appointment, LocalTime startTime) {
        return Optional.empty();
    }

    /**
     * Add appointment with a fixed time. If the requested slot is available,
     * that is used and the appointment is returned. Otherwise the fall back
     * time preference is tried.
     *
     * @param forDay      time partition to fit appointment
     * @param appointment to add
     * @param startTime   preferred start time of the appointment
     * @param fallback    time preference as fall back if the fixed time does not
     *                    apply.
     * @return the added appointment or null on failure.
     */
    @Override
    public Optional<Appointment> addAppointment(LocalDay forDay, AppointmentData appointment, LocalTime startTime, TimePreference fallback) {
        return Optional.empty();
    }

    /**
     * Removes the given appointment, returning the data of that appointment, if
     * found. This day is searched for a non-free time slot matching the
     * appointment. The returned data could be used to re-plan the appointment.
     *
     * @param appointment to remove
     * @return the data of the removed appointment and null if the appointment
     * is not found..
     */
    @Override
    public AppointmentRequest removeAppointment(Appointment appointment) {
        return null;
    }

    /**
     * Removes appointments with description that matches a filter.
     *
     * @param filter to determine which items to remove.
     * @return the list of removed appointments
     */
    @Override
    public List<AppointmentRequest> removeAppointments(Predicate<Appointment> filter) {
        return null;
    }

    /**
     * Finds all appointments matching given filter.
     *
     * @param filter to determine which items to select.
     * @return list of matching appointments.
     */
    @Override
    public List<Appointment> findAppointments(Predicate<Appointment> filter) {
        return null;
    }

    /**
     * All appointments streamed.
     *
     * @return list of all appointments.
     */
    @Override
    public Stream<Appointment> appointmentStream() {
        return null;
    }

    /**
     * Check if day contains appointment.
     *
     * @param appointment to search.
     * @return true if Appointment can be found, false otherwise.
     */
    @Override
    public boolean contains(Appointment appointment) {
        return false;
    }

    /**
     * This method finds all time gaps that can accommodate an appointment of
     * the given duration in natural order. This method returns the gaps in
     * ascending time which is the natural order.
     *
     * @param duration the requested duration for an appointment
     * @return a list of gaps in which the appointment can be scheduled.
     */
    @Override
    public List<TimeSlot> getGapsFitting(Duration duration) {
        return null;
    }

    /**
     * Check if an appointment of a duration could be accommodated.
     *
     * @param duration of the appointment
     * @return true is there is a sufficiently big gap.
     */
    @Override
    public boolean canAddAppointmentOfDuration(Duration duration) {
        return false;
    }

    /**
     * This method finds all time gaps that can accommodate an appointment of
     * the given duration in last to first order. This method returns the gaps
     * in descending time which is the reversed natural order.
     *
     * @param duration the requested duration for an appointment
     * @return a list of start times on which an appointment can be scheduled
     */
    @Override
    public List<TimeSlot> getGapsFittingReversed(Duration duration) {
        return null;
    }

    /**
     * Get the gaps matching the given duration, smallest fitting first.
     *
     * @param duration required
     * @return list of all gaps fitting, smallest gap first.
     */
    @Override
    public List<TimeSlot> getGapsFittingSmallestFirst(Duration duration) {
        return null;
    }

    /**
     * Get the gaps matching the given duration, largest fitting first.
     *
     * @param duration required
     * @return list of all gaps fitting, largest gap first.
     */
    @Override
    public List<TimeSlot> getGapsFittingLargestFirst(Duration duration) {
        return null;
    }

    /**
     * Find matching free time slots in this and other DayaPlans.To facilitate
     * appointment proposals.
     *
     * @param minLength minimum length required.
     * @param other     day plans
     * @return the list of free slots that all DayPlans share.
     */
    @Override
    public List<TimeSlot> getMatchingFreeSlotsOfDuration(Duration minLength, List<Timeline> other) {
        return null;
    }


    /**
     * This inner class is there
     */
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
}
