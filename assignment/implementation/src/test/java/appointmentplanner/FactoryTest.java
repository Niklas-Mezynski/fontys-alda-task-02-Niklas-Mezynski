package appointmentplanner;

import appointmentplanner.ServiceFinder;
import appointmentplanner.api.*;

import java.time.Duration;
import java.time.LocalTime;
import static org.assertj.core.api.Assertions.*;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Example service invocation and factory test.
 *
 * @author Pieter van den Hombergh {@code p.vandenhombergh@fontys.nl}
 */
public class FactoryTest {

    static AbstractAPFactory fac;

    @BeforeAll
    static void assumeFactory() {
        fac = ServiceFinder.getFactory();
    }

//    @Test
//    void factoryCreatesDayPlan() {
//        LocalDay day = LocalDay.now();
//        LocalDayPlan ldp = fac.createLocalDayPlan( day, LocalTime.parse( "08:00" ), LocalTime.parse( "17:30" ) );
//        assertThat( ldp ).as( fac.getClass().getName() + " should not return null object" ).isNotNull();
//    }

    @Test
    void t01createAppointmentData() {
        AppointmentData appointment1 = fac.createAppointmentData("Doctor", Duration.ofHours(1));
        AppointmentData appointment2 = fac.createAppointmentData("Doctor", Duration.ofHours(1), Priority.MEDIUM);
        SoftAssertions.assertSoftly(s -> {
            s.assertThat(appointment1).isNotNull();
            s.assertThat(appointment2).isNotNull();
        });
    }

}
