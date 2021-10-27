package appointmentplanner;

import appointmentplanner.api.*;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static appointmentplanner.TestData.*;
import static org.assertj.core.api.Assertions.*;

public class TimelineTest {

    private Timeline timeline = new TimelineImpl(TODAY.ofLocalTime(TestData.T08_30), TestData.TODAY.ofLocalTime(TestData.T17_30));

    private  Timeline getTimelineWithAppointments() {
        TimelineImpl timelineWApps = new TimelineImpl(TODAY.ofLocalTime(TestData.T08_30), TODAY.ofLocalTime(TestData.T17_30));
        timelineWApps.addAppointment(TODAY, TestData.DATA0, TestData.T08_30);
        timelineWApps.addAppointment(TODAY, TestData.DATA4, TestData.T10_45);
        timelineWApps.addAppointment(TODAY, TestData.DATA6, TestData.T14_30);
        timelineWApps.addAppointment(TODAY, DATA7, T16_00);
        return timelineWApps;
    }

    @Test
    void t01createTimeline() {
        SoftAssertions.assertSoftly(s -> {
            s.assertThat(timeline).isNotNull();
            s.assertThat(timeline.start()).isEqualTo(TODAY.ofLocalTime(TestData.T08_30));
            s.assertThat(timeline.end()).isEqualTo(TODAY.ofLocalTime(TestData.T17_30));
        });
    }

    @Test
    void t02constructionIllegalArguments() {
        ThrowableAssert.ThrowingCallable code = () -> new TimelineImpl(TODAY.ofLocalTime(TestData.T09_00), TestData.TODAY.ofLocalTime(TestData.T08_30));
        assertThatCode(code).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void t03addAppointmentFixedTimeEasyCase() {
        Optional<Appointment> appointment = timeline.addAppointment(TODAY, TestData.DATA3, TestData.T10_30);
        assertThat(appointment.isEmpty()).isFalse();
        SoftAssertions.assertSoftly(s -> {
            s.assertThat(appointment.get().getStart()).isEqualTo(TODAY.ofLocalTime(TestData.T10_30));
            s.assertThat(appointment.get().getAppointmentData()).isEqualTo(TestData.DATA3);
        });
    }

    private final Map<String, AppointmentData> appointmentDataMap = Map.of(
            "DATA0", TestData.DATA0,
            "DATA1", TestData.DATA1,
            "DATA2", TestData.DATA2,
            "DATA3", TestData.DATA3,
            "DATA4", TestData.DATA4,
            "DATA5", TestData.DATA5,
            "DATA6", TestData.DATA6,
            "DATA7", TestData.DATA7
    );

    private final Map<String, LocalTime> localTimeMap = Map.of(
            "08:30", TestData.T08_30,
            "09:00", TestData.T09_00,
            "10:30", TestData.T10_30,
            "10:45", TestData.T10_45,
            "11:10", TestData.T11_10,
            "14:30", TestData.T14_30,
            "16:00", TestData.T16_00
    );

    private final Map<String, Instant> instantMap = Map.of(
            "08:30", TODAY.ofLocalTime(TestData.T08_30),
            "09:00", TODAY.ofLocalTime(TestData.T09_00),
            "10:30", TODAY.ofLocalTime(TestData.T10_30),
            "11:00", TODAY.ofLocalTime(TestData.T11_00),
            "11:10", TODAY.ofLocalTime(TestData.T11_10),
            "14:00", TODAY.ofLocalTime(TestData.T14_00),
            "14:30", TODAY.ofLocalTime(TestData.T14_30),
            "15:45", TODAY.ofLocalTime(TestData.T15_45),
            "16:00", TODAY.ofLocalTime(TestData.T16_00)
    );

    private final Map<String, TimePreference> timePrefMap = Map.of(
            "EARLIEST", TimePreference.EARLIEST,
            "LATEST", TimePreference.LATEST,
            "EARLIEST_AFTER", TimePreference.EARLIEST_AFTER,
            "LATEST_BEFORE", TimePreference.LATEST_BEFORE,
            "UNSPECIFIED", TimePreference.UNSPECIFIED
    );

    @CsvSource({
            "DATA0,08:30,FALSE",
            "DATA1,09:00,TRUE",
            "DATA3,10:30,TRUE",
            "DATA2,10:30,FALSE",
            "DATA5,11:10,TRUE",
            "DATA7,16:00,FALSE"
    })
    @ParameterizedTest
    void t04addAppointmentFixedTime(String appDataString, String localTimeString, boolean shouldWork) {
        AppointmentData appointmentData = appointmentDataMap.get(appDataString);
        LocalTime localTime = localTimeMap.get(localTimeString);
        Optional<Appointment> appointment = getTimelineWithAppointments().addAppointment(TODAY, appointmentData, localTime);
        assertThat(appointment.isPresent()).isEqualTo(shouldWork);
    }

    @CsvSource({
            "DATA1, EARLIEST, 09:00",
            "DATA5, EARLIEST, 11:00",
            "DATA3, LATEST, 15:45",
    })
    @ParameterizedTest
    void t05addAppointmentWithTimePreference(String appDataString, String timePrefString, String outputAppStartString) {
        AppointmentData appointmentData = appointmentDataMap.get(appDataString);
        TimePreference timePreference = timePrefMap.get(timePrefString);
        Instant expectedStart = instantMap.get(outputAppStartString);
        Optional<Appointment> appointment = getTimelineWithAppointments().addAppointment(TODAY, appointmentData, timePreference);
        Assumptions.assumeTrue(appointment.isPresent());
        SoftAssertions.assertSoftly(s -> {
            s.assertThat(appointment.get().getStart()).isEqualTo(expectedStart);
            s.assertThat(appointment.get().getRequest().getTimePreference()).isEqualTo(timePreference);
        });
    }

    @Test
    void t051addMultipleAppointmentsAtOnce() {
        Timeline timeline = getTimelineWithAppointments();
        Optional<Appointment> appointment1 = timeline.addAppointment(TODAY, DATA1, TimePreference.EARLIEST);
        Optional<Appointment> appointment2 = timeline.addAppointment(TODAY, DATA2, TimePreference.EARLIEST);
        Optional<Appointment> appointment3 = timeline.addAppointment(TODAY, DATA1, TimePreference.LATEST);
        Assumptions.assumeTrue(appointment1.isPresent());
        Assumptions.assumeTrue(appointment2.isPresent());
        Assumptions.assumeTrue(appointment3.isPresent());
        SoftAssertions.assertSoftly(s -> {
            s.assertThat(appointment1.get().getStart()).isEqualTo(TODAY.ofLocalTime(T09_00));
            s.assertThat(appointment2.get().getStart()).isEqualTo(TODAY.ofLocalTime(T09_30));
            s.assertThat(appointment3.get().getStart()).isEqualTo(TODAY.ofLocalTime(T15_30));
        });
    }

    @CsvSource({
            "DATA1, UNSPECIFIED",
            "DATA5, LATEST_BEFORE",
            "DATA3, EARLIEST_AFTER",
    })
    @ParameterizedTest
    void t06addAppointmentWithTimePreferenceFailing(String appDataString, String timePrefString) {
        AppointmentData appointmentData = appointmentDataMap.get(appDataString);
        TimePreference timePreference = timePrefMap.get(timePrefString);
        Optional<Appointment> appointment = getTimelineWithAppointments().addAppointment(TODAY, appointmentData, timePreference);
        assertThat(appointment.isEmpty()).isTrue();
    }

    @Test
    void t07addAppointmentWithTimePreferenceFailing() {
        Timeline timelineWithAppointments = getTimelineWithAppointments();
        timelineWithAppointments.addAppointment(TODAY, DATA5, TestData.T11_10);
        Optional<Appointment> appointment1 = timelineWithAppointments.addAppointment(TODAY, DATA5, TimePreference.EARLIEST);
        Optional<Appointment> appointment2 = timelineWithAppointments.addAppointment(TODAY, DATA5, TimePreference.LATEST);
        SoftAssertions.assertSoftly(s -> {
            s.assertThat(appointment1.isEmpty()).isTrue();
            s.assertThat(appointment2.isEmpty()).isTrue();
        });
    }

    @CsvSource({
            "DATA0,08:30,EARLIEST_AFTER,09:00",
            "DATA1,09:00,LATEST_BEFORE,09:00",
            "DATA3,10:45,EARLIEST_AFTER,11:00",
            "DATA3,10:45,LATEST_BEFORE,10:30",
    })
    @ParameterizedTest
    void t08addAppointmentWithFallback(String appDataString, String localTimeString, String fallbackTimePrefString, String expOutputStartTime) {
        AppointmentData appointmentData = appointmentDataMap.get(appDataString);
        TimePreference fallbackTimePref = timePrefMap.get(fallbackTimePrefString);
        LocalTime startTime = localTimeMap.get(localTimeString);
        Instant expectedStart = instantMap.get(expOutputStartTime);
        Optional<Appointment> appointment = getTimelineWithAppointments().addAppointment(TODAY, appointmentData, startTime, fallbackTimePref);
        Assumptions.assumeTrue(appointment.isPresent());
        assertThat(appointment.get().getStart()).isEqualTo(expectedStart);
    }

    @Test
    void t09addAppWithFallbackNoFreeSlot() {
        Timeline timelineWithAppointments = getTimelineWithAppointments();
        timelineWithAppointments.addAppointment(TODAY, DATA5, TestData.T11_10);
        LocalTime localTime = localTimeMap.get("10:30");
        Optional<Appointment> appointment1 = timelineWithAppointments.addAppointment(TODAY, DATA5, localTime, TimePreference.EARLIEST_AFTER);
        Optional<Appointment> appointment2 = timelineWithAppointments.addAppointment(TODAY, DATA5, localTime, TimePreference.LATEST_BEFORE);
        SoftAssertions.assertSoftly(s -> {
            s.assertThat(appointment1.isEmpty()).isTrue();
            s.assertThat(appointment2.isEmpty()).isTrue();
        });
    }

    @Test
    void t10removeSingleApp() {
        Timeline timelineWithAppointments = getTimelineWithAppointments();
        Optional<Appointment> appointment = timelineWithAppointments.addAppointment(TODAY, DATA3, T10_30);
        Assumptions.assumeTrue(appointment.isPresent());
        AppointmentRequest appointmentRequest = timelineWithAppointments.removeAppointment(appointment.get());
        Assumptions.assumeTrue(appointmentRequest != null);
        assertThat(appointmentRequest).isEqualTo(appointment.get().getRequest());
    }

    @Test
    void t11removeMultipleApps() {
        Timeline timelineWithAppointments = getTimelineWithAppointments();
        Optional<Appointment> appointment1 = timelineWithAppointments.addAppointment(TODAY, DATA8, T09_00);
        Optional<Appointment> appointment2 = timelineWithAppointments.addAppointment(TODAY, DATA7, T11_10);
        Assumptions.assumeTrue(appointment1.isPresent() && appointment2.isPresent());
        List<AppointmentRequest> appointmentRequests = timelineWithAppointments.removeAppointments(appointment -> appointment.getDuration().compareTo(D30) > 0);
        SoftAssertions.assertSoftly(s -> {
            s.assertThat(appointmentRequests.size()).isEqualTo(3);
            s.assertThat(appointmentRequests.contains(appointment1.get().getRequest())).isTrue();
            s.assertThat(appointmentRequests.contains(appointment2.get().getRequest())).isTrue();
        });
    }

    @Test
    void t12noOfAppointments() {
        Timeline timelineWithAppointments = getTimelineWithAppointments();
        timelineWithAppointments.addAppointment(TODAY, DATA8, T09_00);
        timelineWithAppointments.addAppointment(TODAY, DATA7, T11_10);
        timelineWithAppointments.removeAppointments(appointment -> appointment.getDuration().compareTo(D30) > 0);
        assertThat(timelineWithAppointments.getNrOfAppointments()).isEqualTo(3);
    }

    @Test
    void t13findAppointments() {
        Timeline timelineWithAppointments = getTimelineWithAppointments();
        Optional<Appointment> appointment1 = timelineWithAppointments.addAppointment(TODAY, DATA8, T09_00);
        Optional<Appointment> appointment2 = timelineWithAppointments.addAppointment(TODAY, DATA8, T11_10);
        Assumptions.assumeTrue(appointment1.isPresent() && appointment2.isPresent());
        List<Appointment> appointments = timelineWithAppointments.findAppointments(appointment -> appointment.getAppointmentData().equals(DATA8));
        assertThat(appointments).containsExactlyInAnyOrder(appointment1.get(), appointment2.get());
    }

    @Test
    void t14containsAppointment() {
        Timeline timelineWithAppointments = getTimelineWithAppointments();
        Optional<Appointment> appointment1 = timelineWithAppointments.addAppointment(TODAY, DATA8, T09_00);
        Optional<Appointment> appointment2 = timelineWithAppointments.addAppointment(TODAY, DATA8, T11_10);
        Assumptions.assumeTrue(appointment1.isPresent() && appointment2.isPresent());
        timelineWithAppointments.removeAppointment(appointment2.get());
        SoftAssertions.assertSoftly(s -> {
            s.assertThat(timelineWithAppointments.contains(appointment1.get())).isTrue();
            s.assertThat(timelineWithAppointments.contains(appointment2.get())).isFalse();
        });
    }

    @Test
    void t15gapsFitting() {
        Timeline timelineWithAppointments = getTimelineWithAppointments();
        List<TimeSlot> gapsFitting = timelineWithAppointments.getGapsFitting(D90);
        Assumptions.assumeTrue(gapsFitting.size() == 2);
        SoftAssertions.assertSoftly(s -> {
            s.assertThat(gapsFitting.get(0).duration()).isEqualTo(Duration.ofMinutes(105));
            s.assertThat(gapsFitting.get(1).duration()).isEqualTo(Duration.ofMinutes(210));
        });
    }

    @Test
    void t16getGapsFittingReversed() {
        Timeline timelineWithAppointments = getTimelineWithAppointments();
        List<TimeSlot> gapsFitting = timelineWithAppointments.getGapsFittingReversed(D90);
        Assumptions.assumeTrue(gapsFitting.size() == 2);
        SoftAssertions.assertSoftly(s -> {
            s.assertThat(gapsFitting.get(0).duration()).isEqualTo(Duration.ofMinutes(210));
            s.assertThat(gapsFitting.get(1).duration()).isEqualTo(Duration.ofMinutes(105));
        });
    }

    @Test
    void t17canAddAppointmentOfDuration() {
        Timeline timelineWithAppointments = getTimelineWithAppointments();
        timelineWithAppointments.addAppointment(TODAY, DATA5, T11_10);
        SoftAssertions.assertSoftly(s -> {
            s.assertThat(timelineWithAppointments.canAddAppointmentOfDuration(D90)).isTrue();
            s.assertThat(timelineWithAppointments.canAddAppointmentOfDuration(D200)).isFalse();
        });
    }

    @Test
    void t18getGapsFittingSmallestFirst() {
        Timeline timelineWithAppointments = getTimelineWithAppointments();
        List<TimeSlot> gapsFitting = timelineWithAppointments.getGapsFittingSmallestFirst(D90);
        Assumptions.assumeTrue(gapsFitting.size() == 2);
        SoftAssertions.assertSoftly(s -> {
            s.assertThat(gapsFitting.get(0).duration()).isEqualTo(Duration.ofMinutes(105));
            s.assertThat(gapsFitting.get(1).duration()).isEqualTo(Duration.ofMinutes(210));
        });
    }

    @Test
    void t19getGapsFittingLargestFirst() {
        Timeline timelineWithAppointments = getTimelineWithAppointments();
        List<TimeSlot> gapsFitting = timelineWithAppointments.getGapsFittingLargestFirst(D90);
        Assumptions.assumeTrue(gapsFitting.size() == 2);
        SoftAssertions.assertSoftly(s -> {
            s.assertThat(gapsFitting.get(0).duration()).isEqualTo(Duration.ofMinutes(210));
            s.assertThat(gapsFitting.get(1).duration()).isEqualTo(Duration.ofMinutes(105));
        });
    }

    @Test
    void t20getMatchingFreeSlotsOfDuration() {
        Timeline timeline1 = getTimelineWithAppointments();
        Timeline timeline2 = getTimelineWithAppointments();
        Timeline timeline3 = getTimelineWithAppointments();
        timeline2.addAppointment(TODAY, DATA3, T10_30);
        timeline2.addAppointment(TODAY, DATA3, T15_30);
        timeline3.addAppointment(TODAY, DATA1, T09_00);
        timeline3.addAppointment(TODAY, DATA1, T14_00);
        List<Timeline> otherTimelines = List.of(timeline2, timeline3);
        List<TimeSlot> freeSlots = timeline1.getMatchingFreeSlotsOfDuration(D30, otherTimelines);
        SoftAssertions.assertSoftly(s -> {
            s.assertThat(freeSlots.size()).isEqualTo(3);
            s.assertThat(freeSlots.get(0).getStart()).isEqualTo(TODAY.ofLocalTime(T09_30));
            s.assertThat(freeSlots.get(0).getEnd()).isEqualTo(TODAY.ofLocalTime(T10_30));
            s.assertThat(freeSlots.get(1).getStart()).isEqualTo(TODAY.ofLocalTime(T11_00));
            s.assertThat(freeSlots.get(1).getEnd()).isEqualTo(TODAY.ofLocalTime(T14_00));
            s.assertThat(freeSlots.get(2).getStart()).isEqualTo(TODAY.ofLocalTime(T15_00));
            s.assertThat(freeSlots.get(2).getEnd()).isEqualTo(TODAY.ofLocalTime(T15_30));
        });
    }

}
