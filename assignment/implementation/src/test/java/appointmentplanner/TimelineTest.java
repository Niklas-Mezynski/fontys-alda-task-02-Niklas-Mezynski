package appointmentplanner;

import appointmentplanner.api.Appointment;
import appointmentplanner.api.AppointmentData;
import appointmentplanner.api.Timeline;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

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
            "DATA5", TestData.DATA5
    );

    private final Map<String, LocalTime> localTimeMap = Map.of(
            "08:30", TestData.T08_30,
            "09:00", TestData.T09_00,
            "10:30", TestData.T10_30,
            "11:10", TestData.T11_10
    );

    @CsvSource({
            "DATA0,08:30,FALSE",
            "DATA1,09:00,TRUE",
            "DATA3,10:30,TRUE",
            "DATA2,10:30,FALSE",
            "DATA5,11:10,TRUE",
    })
    @ParameterizedTest
    void t04addAppointmentFixedTimeDifferentScenarios(String appDataString, String localTimeString, boolean shouldWork) {
        AppointmentData appointmentData = appointmentDataMap.get(appDataString);
        LocalTime localTime = localTimeMap.get(localTimeString);
        Optional<Appointment> appointment = getTimelineWithAppointments().addAppointment(TODAY, appointmentData, localTime);
        assertThat(appointment.isPresent()).isEqualTo(shouldWork);
    }
}
