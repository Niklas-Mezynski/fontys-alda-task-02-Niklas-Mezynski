package appointmentplanner;

import appointmentplanner.api.AppointmentData;
import appointmentplanner.api.Priority;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

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
}
