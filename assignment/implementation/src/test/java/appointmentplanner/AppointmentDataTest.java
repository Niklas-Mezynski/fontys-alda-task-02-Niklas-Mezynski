package appointmentplanner;

import appointmentplanner.api.AppointmentData;
import appointmentplanner.api.Priority;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.Duration;

import static org.assertj.core.api.Assertions.*;

public class AppointmentDataTest {

    @Test
    void t01appDataGetterTest() {
        Duration duration = Duration.ofHours(1);
        AppointmentData appointment = FactoryTest.fac.createAppointmentData("Doctor", duration, Priority.MEDIUM);
        SoftAssertions.assertSoftly(s -> {
            s.assertThat(appointment.getDescription()).isEqualTo("Doctor");
            s.assertThat(appointment.getDuration()).isSameAs(duration);
            s.assertThat(appointment.getPriority()).isEqualTo(Priority.MEDIUM);
        });
    }

    @Test
    void t02throwsExceptionOnInvalidValues() {
        SoftAssertions.assertSoftly(s -> {
            s.assertThatCode(() -> FactoryTest.fac.createAppointmentData(null, Duration.ofHours(1)))
                    .isInstanceOf(IllegalArgumentException.class);
            s.assertThatCode(() -> FactoryTest.fac.createAppointmentData("", Duration.ofHours(1)))
                    .isInstanceOf(IllegalArgumentException.class);
            s.assertThatCode(() -> FactoryTest.fac.createAppointmentData("Meeting", Duration.ofHours(0)))
                    .isInstanceOf(IllegalArgumentException.class);
        });
    }

    @Test
    void t03testEqualsAndHashcode() {
        AppointmentDataImpl appData1 = new AppointmentDataImpl("Snens", Duration.ofHours(2));
        AppointmentDataImpl appData2 = new AppointmentDataImpl("Snens", Duration.ofHours(2));
        AppointmentDataImpl appData3 = new AppointmentDataImpl("Huhn", Duration.ofHours(2));
        AppointmentDataImpl appData4 = new AppointmentDataImpl("Snens", Duration.ofHours(3));
        AppointmentDataImpl appData5 = new AppointmentDataImpl("Snens", Duration.ofHours(2), Priority.HIGH);
        TestData.verifyEqualsAndHashCode(appData1, appData2, appData3, appData4, appData5);
    }
}
