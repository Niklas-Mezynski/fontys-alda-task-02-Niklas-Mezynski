package appointmentplanner;

import appointmentplanner.api.*;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalTime;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.util.Spliterator.ORDERED;

public class TimelineImpl implements Timeline, Iterable<TimelineImpl.AllocationNode> {
    private AllocationNode head;
    private AllocationNode tail;
    private int nrOfAppointments;

    public TimelineImpl(Instant start, Instant end) {
        if (end.isBefore(start))
            throw new IllegalArgumentException("end time can't be before start time");

        this.nrOfAppointments = 0;
        this.head = new AllocationNode(start, start);
        this.tail = new AllocationNode(end, end);

        AllocationNode initialAllocation = new AllocationNode(start, end);
        initialAllocation.prev = this.head;
        initialAllocation.next = this.tail;
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
        return nrOfAppointments;
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
        //If no time preference is specified, no appointment can be added
        if (!(timepreference == TimePreference.EARLIEST || timepreference == TimePreference.LATEST)) {
            return Optional.empty();
        }
        switch (timepreference) {
            case EARLIEST:
                //Find the earliest possible place for the appointment
                Optional<AllocationNode> optFirstFreeNode = stream().filter(allocationNode ->
                        allocationNode.appData == null &&
                                (allocationNode.start.plus(appointment.getDuration()).compareTo(allocationNode.end) <= 0                                )
                ).findFirst();
                if (optFirstFreeNode.isEmpty()) {
                    return Optional.empty();
                }
                AllocationNode firstFreeNode = optFirstFreeNode.get();
                AllocationNode newNodeForTheAppointment = insertNode(firstFreeNode, firstFreeNode.start, firstFreeNode.start.plus(appointment.getDuration()));
                AppointmentRequest appointmentRequest = new AppointmentRequestImpl(appointment, null, timepreference);
                Appointment newAppointment = new AppointmentImpl(newNodeForTheAppointment.start, newNodeForTheAppointment.end, appointmentRequest);
                newNodeForTheAppointment.appData = newAppointment;
                nrOfAppointments++;
                return Optional.of(newAppointment);
            case LATEST:
                //Do the latest possible place for the appointment
                Optional<AllocationNode> optLastFreeNode = reversedStream().filter(allocationNode ->
                        allocationNode.appData == null &&
                                (allocationNode.end.minus(appointment.getDuration()).compareTo(allocationNode.start) >= 0)
                ).findFirst();
                if (optLastFreeNode.isEmpty()) {
                    return Optional.empty();
                }
                AllocationNode lastFreeNode = optLastFreeNode.get();
                AllocationNode newNodeForTheApp = insertNode(lastFreeNode, lastFreeNode.end.minus(appointment.getDuration()), lastFreeNode.end);
                AppointmentRequest appRequest = new AppointmentRequestImpl(appointment, null, timepreference);
                Appointment newApp = new AppointmentImpl(newNodeForTheApp.start, newNodeForTheApp.end, appRequest);
                newNodeForTheApp.appData = newApp;
                nrOfAppointments++;
                return Optional.of(newApp);

        }

        //If nothing found, return an empty Optional
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
        Instant startTimeInstant = forDay.ofLocalTime(startTime);
        Instant endTimeInstant = forDay.ofLocalTime(startTime).plus(appointment.getDuration());

        //Finding out if there is a free allocation for given time by streaming and filtering the linked list.
        Optional<AllocationNode> first = stream()
                .filter(allocationNode ->
                        allocationNode.getPurpose() == null &&
                                allocationNode.getStart().compareTo(startTimeInstant) <= 0 &&
                                allocationNode.getEnd().compareTo(endTimeInstant) >= 0)
                .findFirst();

        //IF the free slot was found -> Calling the insertNode() to rearrange the list and creating the appointment
        if (first.isPresent()) {
            AllocationNode freeAllocationNode = first.get();
            AllocationNode appAllocationNode = insertNode(freeAllocationNode, startTimeInstant, endTimeInstant);
            AppointmentRequest appointmentRequest = new AppointmentRequestImpl(appointment, startTime, TimePreference.UNSPECIFIED);
            Appointment appointment1 = new AppointmentImpl(startTimeInstant, endTimeInstant, appointmentRequest);
            appAllocationNode.appData = appointment1;
            nrOfAppointments++;
            return Optional.of(appointment1);
        }

        //If slot is allocated -> return an empty optional
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
        //Try to add the appointment at the fixed time
        Optional<Appointment> appWFixedTime = addAppointment(forDay, appointment, startTime);
        if (appWFixedTime.isPresent()) {
            return appWFixedTime;
        }

        //When fallback time preference is earliest or latest, just use the  method above
        if (fallback == TimePreference.EARLIEST || fallback == TimePreference.LATEST) {
            return addAppointment(forDay, appointment, fallback);
        }

        //If the appointment can't be added at the fixed timeslot, try to use the fallback time preference
        switch (fallback) {
            case EARLIEST_AFTER:
                //Try to find the earliest available slot after the given time

                //Find the first free allocation node after the specified time, which fits the app duration
                Optional<AllocationNode> first = stream().filter(allocationNode -> allocationNode.appData == null &&
                                allocationNode.start.compareTo(forDay.ofLocalTime(startTime)) >= 0 &&
                                allocationNode.getDuration().compareTo(appointment.getDuration()) >= 0)
                        .findFirst();

                //If it was found, add the appointment to the beginning of this node
                if (first.isPresent()) {
                    AllocationNode earliestPossNode = insertNode(first.get(), first.get().getStart(), first.get().getStart().plus(appointment.getDuration()));
                    AppointmentRequest appointmentRequest = new AppointmentRequestImpl(appointment, startTime, fallback);
                    Appointment appointment1 = new AppointmentImpl(earliestPossNode.getStart(), earliestPossNode.getEnd(), appointmentRequest);
                    earliestPossNode.appData = appointment1;
                    nrOfAppointments++;
                    return Optional.of(appointment1);
                }

            case LATEST_BEFORE:
                //Try to find the latest available slot before the given time

                //Find the first free allocation node before the specified time, which fits the app duration
                Optional<AllocationNode> first1 = reversedStream().filter(allocationNode -> allocationNode.appData == null &&
                                allocationNode.end.compareTo(forDay.ofLocalTime(startTime)) <= 0 &&
                                allocationNode.getDuration().compareTo(appointment.getDuration()) >= 0)
                        .findFirst();

                //If it was found, add the appointment to the end of this node
                if (first1.isPresent()) {
                    AllocationNode earliestPossNode = insertNode(first1.get(), first1.get().getEnd().minus(appointment.getDuration()), first1.get().getEnd());
                    AppointmentRequest appointmentRequest = new AppointmentRequestImpl(appointment, startTime, fallback);
                    Appointment appointment1 = new AppointmentImpl(earliestPossNode.getStart(), earliestPossNode.getEnd(), appointmentRequest);
                    earliestPossNode.appData = appointment1;
                    nrOfAppointments++;
                    return Optional.of(appointment1);
                }
        }

        //If nothing was found, return an empty optional
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
        for (AllocationNode node : this) {
            if (node.appData == appointment) {
                removeAllocatedNode(node);
                nrOfAppointments--;
                return appointment.getRequest();
            }
        }
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
        List<AppointmentRequest> appRequests = new ArrayList<>();
        appointmentStream().filter(filter)
                .forEach(appointment -> {
                    removeAppointment(appointment);
                    appRequests.add(appointment.getRequest());
                });
        return appRequests;
    }


    /**
     * Helper method to remove an appointment and restructure the linked list
     *
     * @param node which should contain the appointment
     */
    private void removeAllocatedNode(AllocationNode node) {
        //First remove the appointment

        node.appData = null;

        //Check if the prev node is also null and combine both nodes if needed.
        if (node.prev.appData == null && node.prev != head) {
            node.start = node.prev.start;
            node.prev = node.prev.prev;
            node.prev.next = node;
        }

        //Check if the right side is also null and combine both when needed.
        if (node.next.appData == null && node.next != tail) {
            node.end = node.next.end;
            node.next = node.next.next;
            node.next.prev = node;
        }
    }

    /**
     * Finds all appointments matching given filter.
     *
     * @param filter to determine which items to select.
     * @return list of matching appointments.
     */
    @Override
    public List<Appointment> findAppointments(Predicate<Appointment> filter) {
        return appointmentStream()
                .filter(filter)
                .collect(Collectors.toList());
    }

    /**
     * All appointments streamed.
     *
     * @return list of all appointments.
     */
    @Override
    public Stream<Appointment> appointmentStream() {
        return stream().filter(allocationNode -> allocationNode.appData != null)
                .map(allocationNode -> allocationNode.appData);
    }

    /**
     * Check if day contains appointment.
     *
     * @param appointment to search.
     * @return true if Appointment can be found, false otherwise.
     */
    @Override
    public boolean contains(Appointment appointment) {
        Optional<Appointment> first = appointmentStream().filter(app -> app.equals(appointment)).findFirst();
        return first.isPresent();
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
        return stream()
                .filter(allocationNode -> allocationNode.appData == null &&
                        allocationNode.getDuration().compareTo(duration) >= 0)
                .map(allocationNode -> (TimeSlot) allocationNode)
                .collect(Collectors.toList());
    }

    /**
     * Check if an appointment of a duration could be accommodated.
     *
     * @param duration of the appointment
     * @return true is there is a sufficiently big gap.
     */
    @Override
    public boolean canAddAppointmentOfDuration(Duration duration) {
        return !getGapsFitting(duration).isEmpty();
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
        return reversedStream()
                .filter(allocationNode -> allocationNode.appData == null &&
                        allocationNode.getDuration().compareTo(duration) >= 0)
                .map(allocationNode -> (TimeSlot) allocationNode)
                .collect(Collectors.toList());
    }

    /**
     * Get the gaps matching the given duration, smallest fitting first.
     *
     * @param duration required
     * @return list of all gaps fitting, smallest gap first.
     */
    @Override
    public List<TimeSlot> getGapsFittingSmallestFirst(Duration duration) {
        return getGapsFitting(duration).stream().sorted(Comparator.comparing(TimeSlot::duration)).collect(Collectors.toList());
    }

    /**
     * Get the gaps matching the given duration, largest fitting first.
     *
     * @param duration required
     * @return list of all gaps fitting, largest gap first.
     */
    @Override
    public List<TimeSlot> getGapsFittingLargestFirst(Duration duration) {
        return getGapsFitting(duration).stream().sorted((a,b) -> b.duration().compareTo(a.duration())).collect(Collectors.toList());
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
        List<TimeSlot> finalTimeSlots = new ArrayList<>();

        //Creating a list which for each timeline has a list of all free timeSlots
        List<List<TimeSlot>> timelinesFreeSlots = new ArrayList<>();
        timelinesFreeSlots.add(this.getGapsFitting(minLength));
        other.forEach(timeline -> timelinesFreeSlots.add(timeline.getGapsFitting(minLength)));

        //Continuing as long as there are free slots in each timeline
        while (freeSlotsInAllTimelines(timelinesFreeSlots)) {
            List<TimeSlot> firstSlots = getFirstSlots(timelinesFreeSlots);

            //Getting the "startEdge"
            Instant startEdge = firstSlots.stream().max(Comparator.comparing(TimeSlot::getStart)).get().getStart();

            //Checking if there are slots to be eliminated before the start edge -> if so, start the process all over again
            if (eliminateAllSlotsBefore(timelinesFreeSlots, startEdge)) {
                continue;
            }

            //Getting the "endEdge"
            Instant endEdge = firstSlots.stream().min(Comparator.comparing(TimeSlot::getEnd)).get().getEnd();

            //if the duration between start and end edge is greater or equal to the minLength we can add a new TimeSLot to our finalTimeSlots list
            if (Duration.between(startEdge, endEdge).compareTo(minLength) >= 0) {
                finalTimeSlots.add(new TimeSlotImpl(startEdge, endEdge));
            }

            //Now just eliminate all the first end before or at the endEdge and continue
            eliminateAllSlotsBefore(timelinesFreeSlots, endEdge);
        }

        return finalTimeSlots;
    }

    //Helper method to check if there are any free slots in all the timelines
    private boolean freeSlotsInAllTimelines(List<List<TimeSlot>> timelinesFreeSlots) {
        for (List<TimeSlot> list: timelinesFreeSlots) {
            if (list.isEmpty())
                return false;
        }
        return true;
    }

    //Helper that returns a list of all first slots
    private List<TimeSlot> getFirstSlots(List<List<TimeSlot>> timelinesFreeSlots) {
        return timelinesFreeSlots.stream()
                .map(list -> list.get(0))
                .collect(Collectors.toList());
    }

    //Helper that removes all free slots that end before the given start time or at the given start time
    private boolean eliminateAllSlotsBefore(List<List<TimeSlot>> timelinesFreeSlots, Instant start) {
        boolean anySlotEliminated = false;
        for (List<TimeSlot> timelineFreeSlots : timelinesFreeSlots) {
            List<TimeSlot> toRemove = new ArrayList<>();
            for (TimeSlot timeSlot : timelineFreeSlots) {
                if (timeSlot.getEnd().compareTo(start) <= 0) {
                    toRemove.add(timeSlot);
                    anySlotEliminated = true;
                }
            }
            timelineFreeSlots.removeAll(toRemove);
        }
        return anySlotEliminated;
    }


    /**
     * Helper method to insert new nodes with in a specific time range.
     *
     * @param node1 the free allocationNode the where the new allocation should be inserted
     * @param start             the start time of the new node
     * @param end               the end time of the new node
     * @return returns the newly inserted allocationNode which will be placed between (including) the start and end time
     */
    private AllocationNode insertNode(AllocationNode node1, Instant start, Instant end) {
        //Creating two new nodes left and right of the node where the app shall be inserted
        AllocationNode node0 = new AllocationNode(node1.start, start);
        AllocationNode node2 = new AllocationNode(end, node1.end);

        //Rearranging the linked list structure
        node0.prev = node1.prev;
        node0.prev.next = node0;
        node0.next = node1;
        node2.next = node1.next;
        node2.next.prev = node2;
        node2.prev = node1;
        node1.prev = node0;
        node1.next = node2;

        //Setting start and end time of middle node
        node1.start = node0.end;
        node1.end = node2.start;

        //Checking if the free timeslot on the left side has a duration of 0 and deleting it if necessary
        if (node0.getDuration().equals(Duration.ZERO)) {
            node1.prev = node0.prev;
            node1.prev.next = node1;
        }

        //Checking if the free timeslot on the right side has a duration of 0 and deleting it if necessary
        if (node2.getDuration().equals(Duration.ZERO)) {
            node1.next = node2.next;
            node1.next.prev = node1;
        }

        return node1;
    }

    /**
     * Returns an iterator over elements of type {@code T}.
     *
     * @return an Iterator.
     */
    @Override
    public Iterator<AllocationNode> iterator() {
        return new TimelineIterator(this);
    }

    public Iterator<AllocationNode> reversedIterator() {
        return new TimelineReverseIterator(this);
    }

    Stream<AllocationNode> stream() {
        Spliterator<AllocationNode> spliterator = Spliterators.spliteratorUnknownSize(iterator(), ORDERED);
        return StreamSupport.stream(spliterator, false);
    }

    Stream<AllocationNode> reversedStream() {
        Spliterator<AllocationNode> spliterator = Spliterators.spliteratorUnknownSize(reversedIterator(), ORDERED);
        return StreamSupport.stream(spliterator, false);
    }


    /**
     * This inner class is there
     */
    protected class AllocationNode implements TimeSlot {
        protected AllocationNode next;
        protected AllocationNode prev;
        private Appointment appData;
        private Instant start;
        private Instant end;

        /**
         * Constructor used for appointment slots
         *
         * @param appointmentData the appointment data of the allocated TimeSlot
         */
        public AllocationNode(Instant start, Instant end, Appointment appointmentData) {
            this.start = start;
            this.end = end;
            this.appData = appointmentData;
        }

        /**
         * Constructor used for free TimeSlots
         *
         * @param start Start time of the allocated TimeSlot
         * @param end End time of the allocated TimeSlot
         */
        public AllocationNode(Instant start, Instant end) {
            this(start, end, null);
        }

        @Override
        public Instant getStart() {
            return start;
        }

        public Duration getDuration() {
            return Duration.between(start, end);
        }

        @Override
        public Instant getEnd() {
            return end;
        }

        public Appointment getPurpose() {
            return appData;
        }

        @Override
        public String toString() {
            return "AllocationNode{" +
                    ", start=" + start +
                    ", end=" + end +
                    ", appointment=" + appData +
                    '}';
        }
    }

    private class TimelineIterator implements Iterator<AllocationNode> {

        private AllocationNode current;
        private final TimelineImpl timeline;

        public TimelineIterator(TimelineImpl timeline) {
            this.current = timeline.head;
            this.timeline = timeline;
        }

        /**
         * Returns {@code true} if the iteration has more elements.
         * (In other words, returns {@code true} if {@link #next} would
         * return an element rather than throwing an exception.)
         *
         * @return {@code true} if the iteration has more elements
         */
        @Override
        public boolean hasNext() {
            return current.next != timeline.tail;
        }

        /**
         * Returns the next element in the iteration.
         *
         * @return the next element in the iteration
         * @throws NoSuchElementException if the iteration has no more elements
         */
        @Override
        public AllocationNode next() {
            if (!hasNext()) {
                throw new NoSuchElementException("The list has reached it's end");
            }
            current = current.next;
            return current;
        }
    }


    private class TimelineReverseIterator implements Iterator<AllocationNode> {

        private AllocationNode current;
        private final TimelineImpl timeline;

        public TimelineReverseIterator(TimelineImpl timeline) {
            this.current = timeline.tail;
            this.timeline = timeline;
        }

        /**
         * Returns {@code true} if the iteration has more elements.
         * (In other words, returns {@code true} if {@link #next} would
         * return an element rather than throwing an exception.)
         *
         * @return {@code true} if the iteration has more elements
         */
        @Override
        public boolean hasNext() {
            return current.prev != timeline.head;
        }

        /**
         * Returns the next element in the iteration.
         *
         * @return the next element in the iteration
         * @throws NoSuchElementException if the iteration has no more elements
         */
        @Override
        public AllocationNode next() {
            if (!hasNext()) {
                throw new NoSuchElementException("The list has reached it's end");
            }
            current = current.prev;
            return current;
        }
    }
}
