package appointmentplanner;

import appointmentplanner.api.Priority;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

import java.time.Instant;

public class AppointmentTest {

    @Test
    void t01createAppointment() {
        AppointmentImpl appointment = new AppointmentImpl(TestData.TODAY.at(9, 30), TestData.TODAY.at(10, 0), TestData.AR2);

        SoftAssertions.assertSoftly(s -> {
            s.assertThat(appointment.getAppointmentData()).isEqualTo(TestData.DATA2);
            s.assertThat(appointment.getDescription()).isEqualTo("app2 30 min @10:30");
            s.assertThat(appointment.getDuration()).isEqualTo(TestData.D30);
            s.assertThat(appointment.getPriority()).isEqualTo(Priority.LOW);
            s.assertThat(appointment.getEnd()).isEqualTo(TestData.TODAY.at(10, 0));
            s.assertThat(appointment.getStart()).isEqualTo(TestData.TODAY.at(9, 30));
            s.assertThat(appointment.getRequest()).isEqualTo(TestData.AR2);
        });
    }

}
