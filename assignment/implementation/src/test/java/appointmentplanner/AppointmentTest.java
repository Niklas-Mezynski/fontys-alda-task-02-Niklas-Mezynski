package appointmentplanner;

import appointmentplanner.api.Priority;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static appointmentplanner.TestData.*;

public class AppointmentTest {

    @Test
    void t01createAppointment() {
        AppointmentImpl appointment = new AppointmentImpl(TODAY.at(9, 30), TODAY.at(10, 0), AR2);

        SoftAssertions.assertSoftly(s -> {
            s.assertThat(appointment.getAppointmentData()).isEqualTo(DATA2);
            s.assertThat(appointment.getDescription()).isEqualTo("app2 30 min @10:30");
            s.assertThat(appointment.getDuration()).isEqualTo(D30);
            s.assertThat(appointment.getPriority()).isEqualTo(Priority.LOW);
            s.assertThat(appointment.getEnd()).isEqualTo(TODAY.at(10, 0));
            s.assertThat(appointment.getStart()).isEqualTo(TODAY.at(9, 30));
            s.assertThat(appointment.getRequest()).isEqualTo(AR2);
        });
    }

    @Test
    void t02equalsAndHashcode() {
        AppointmentImpl app1 = new AppointmentImpl(TODAY.ofLocalTime(T10_30), TODAY.ofLocalTime(T11_00), AR2);
        AppointmentImpl app2 = new AppointmentImpl(TODAY.ofLocalTime(T10_30), TODAY.ofLocalTime(T11_00), AR2);
        AppointmentImpl app3 = new AppointmentImpl(TODAY.ofLocalTime(T10_00), TODAY.ofLocalTime(T11_00), AR2);
        AppointmentImpl app4 = new AppointmentImpl(TODAY.ofLocalTime(T10_30), TODAY.ofLocalTime(T11_10), AR2);
        AppointmentImpl app5 = new AppointmentImpl(TODAY.ofLocalTime(T10_30), TODAY.ofLocalTime(T11_00), AR3);
        verifyEqualsAndHashCode(app1,app2,app3,app4,app5);
    }

}
