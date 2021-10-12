package appointmentplanner;

import appointmentplanner.api.Appointment;
import appointmentplanner.api.AppointmentData;
import appointmentplanner.api.TimePreference;
import appointmentplanner.api.Timeline;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.Instant;
import java.time.LocalTime;
import java.util.Map;
import java.util.Optional;

import static appointmentplanner.TestData.TODAY;
import static org.assertj.core.api.Assertions.*;

public class TimelineTest {

    private Timeline timeline = new TimelineImpl(TODAY.ofLocalTime(TestData.T08_30), TestData.TODAY.ofLocalTime(TestData.T17_30));

    private  Timeline getTimelineWithAppointments() {
        TimelineImpl timelineWApps = new TimelineImpl(TODAY.ofLocalTime(TestData.T08_30), TODAY.ofLocalTime(TestData.T17_30));
        timelineWApps.addAppointment(TODAY, TestData.DATA0, TestData.T08_30);
        timelineWApps.addAppointment(TODAY, TestData.DATA4, TestData.T10_45);
        timelineWApps.addAppointment(TODAY, TestData.DATA6, TestData.T14_30);
        timelineWApps.addAppointment(TODAY, TestData.DATA7, TestData.T16_00);
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
            "LATEST_BEFORE", TimePreference.LATEST_BEFORE
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
}
