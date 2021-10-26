package appointmentplanner;

import appointmentplanner.api.AppointmentData;
import appointmentplanner.api.AppointmentRequest;
import appointmentplanner.api.Priority;
import appointmentplanner.api.TimePreference;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.*;

public class AppointmentRequestTest {

    @Test
    void t01GetterTest() {
        Duration duration = Duration.ofHours(1);
        AppointmentData appointmentData = FactoryTest.fac.createAppointmentData("Doctor", duration, Priority.MEDIUM);
        AppointmentRequest appointmentRequest1 = FactoryTest.fac.createAppointmentRequest(appointmentData, LocalTime.of(10, 30), TimePreference.LATEST);

        SoftAssertions.assertSoftly(s -> {
            s.assertThat(appointmentRequest1.getAppointmentData()).isSameAs(appointmentData);
            s.assertThat(appointmentRequest1.getDuration()).isSameAs(duration);
            s.assertThat(appointmentRequest1.getTimePreference()).isEqualTo(TimePreference.LATEST);
            s.assertThat(appointmentRequest1.getStartTime()).isEqualTo(LocalTime.of(10,30));
            s.assertThat(appointmentRequest1.getPriority()).isEqualTo(Priority.MEDIUM);
            s.assertThat(appointmentRequest1.getDescription()).isEqualTo("Doctor");
//            s.assertThat(appointmentRequest1.getStart()).isEqualTo();
        });
    }

    @Test
    void t02testEqualsAndHashcode() {
        AppointmentRequest ar1 = new AppointmentRequestImpl(TestData.DATA1, TestData.T09_00, TimePreference.UNSPECIFIED);
        AppointmentRequest ar2 = new AppointmentRequestImpl(TestData.DATA1, TestData.T09_00, TimePreference.UNSPECIFIED);
        AppointmentRequest ar3 = new AppointmentRequestImpl(TestData.DATA1, TestData.T09_00, TimePreference.LATEST);
        AppointmentRequest ar4 = new AppointmentRequestImpl(TestData.DATA1, TestData.T16_00, TimePreference.UNSPECIFIED);
        AppointmentRequest ar5 = new AppointmentRequestImpl(TestData.DATA5, TestData.T09_00, TimePreference.UNSPECIFIED);
        TestData.verifyEqualsAndHashCode(ar1,ar2,ar3,ar4,ar5);
    }
}
