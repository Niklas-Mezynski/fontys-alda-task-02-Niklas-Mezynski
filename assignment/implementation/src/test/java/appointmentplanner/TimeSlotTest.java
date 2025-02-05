package appointmentplanner;

import appointmentplanner.api.AbstractAPFactory;
import appointmentplanner.api.AppointmentRequest;
import appointmentplanner.api.TimePreference;
import appointmentplanner.api.TimeSlot;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

import java.time.*;

import static org.assertj.core.api.Assertions.*;

public class TimeSlotTest {

    private final AbstractAPFactory fac = ServiceFinder.getFactory();

    @Test
    void t01creationAndGetters() {
        TimeSlot timeSlot = fac.between(TestData.TODAY, TestData.T09_00, TestData.T16_00);
        LocalDateTime localDateTimeToday09_00 = LocalDateTime.of(TestData.TODAY.getDate(), TestData.T09_00);
        Instant instantToday09_00 = localDateTimeToday09_00.atZone(ZoneId.systemDefault()).toInstant();
        LocalDateTime localDateTimeToday16_00 = LocalDateTime.of(TestData.TODAY.getDate(), TestData.T16_00);
        Instant instantToday16_00 = localDateTimeToday16_00.atZone(ZoneId.systemDefault()).toInstant();
        SoftAssertions.assertSoftly(s -> {
            s.assertThat(timeSlot.getStart()).isEqualTo(instantToday09_00);
            s.assertThat(timeSlot.getEnd()).isEqualTo(instantToday16_00);
        });
    }

    @Test
    void t02endBeforeStart() {
        LocalDateTime localDateTimeToday09_00 = LocalDateTime.of(TestData.TODAY.getDate(), TestData.T09_00);
        Instant instantToday09_00 = localDateTimeToday09_00.atZone(ZoneId.systemDefault()).toInstant();
        LocalDateTime localDateTimeToday16_00 = LocalDateTime.of(TestData.TODAY.getDate(), TestData.T16_00);
        Instant instantToday16_00 = localDateTimeToday16_00.atZone(ZoneId.systemDefault()).toInstant();
        assertThatCode(() -> fac.between(instantToday16_00, instantToday09_00))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void t03testEqualsAndHashcode() {
        TimeSlotImpl ts1 = new TimeSlotImpl(TestData.TODAY.ofLocalTime(TestData.T09_30), TestData.TODAY.ofLocalTime(TestData.T10_30));
        TimeSlotImpl ts2 = new TimeSlotImpl(TestData.TODAY.ofLocalTime(TestData.T09_30), TestData.TODAY.ofLocalTime(TestData.T10_30));
        TimeSlotImpl ts3 = new TimeSlotImpl(TestData.TODAY.ofLocalTime(TestData.T10_00), TestData.TODAY.ofLocalTime(TestData.T10_30));
        TimeSlotImpl ts4 = new TimeSlotImpl(TestData.TODAY.ofLocalTime(TestData.T09_30), TestData.TODAY.ofLocalTime(TestData.T11_10));
        TestData.verifyEqualsAndHashCode(ts1,ts2,ts3,ts4);
    }

}
