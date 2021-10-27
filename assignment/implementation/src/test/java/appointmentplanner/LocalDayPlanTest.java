package appointmentplanner;

import appointmentplanner.api.AbstractAPFactory;
import appointmentplanner.api.LocalDay;
import appointmentplanner.api.LocalDayPlan;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

import java.time.*;

public class LocalDayPlanTest {

    private final AbstractAPFactory fac = ServiceFinder.getFactory();

    private final Instant today09_00 = TestData.TODAY.ofLocalTime(TestData.T09_00);
    private final Instant today16_00 = TestData.TODAY.ofLocalTime(TestData.T16_00);

    @Test
    void t01creation() {
        TimelineImpl timeline = new TimelineImpl(today09_00, today16_00);
        LocalDayPlan localDayPlan1 = fac.createLocalDayPlan(ZoneId.systemDefault(), LocalDate.now(), timeline);
        LocalDayPlan localDayPlan2 = fac.createLocalDayPlan(TestData.TODAY, today09_00, today16_00);

        SoftAssertions.assertSoftly(s -> {
            s.assertThat(localDayPlan1.getDay()).isEqualTo(TestData.TODAY);
            s.assertThat(localDayPlan1.earliest()).isEqualTo(today09_00);
            s.assertThat(localDayPlan1.tooLate()).isEqualTo(today16_00);
            s.assertThat(localDayPlan1.getTimeline()).isSameAs(timeline);

            s.assertThat(localDayPlan2.getDay()).isEqualTo(TestData.TODAY);
            s.assertThat(localDayPlan2.earliest()).isEqualTo(today09_00);
            s.assertThat(localDayPlan2.tooLate()).isEqualTo(today16_00);
        });
    }

    @Test
    void t02toString() {
        Instant instant08_30 = LocalDateTime.of(2020, 4, 20, 8, 30).atZone(ZoneId.of("Europe/Berlin")).toInstant();
        Instant instant17_00 = LocalDateTime.of(2020, 4, 20, 17, 0).atZone(ZoneId.of("Europe/Berlin")).toInstant();
        LocalDayPlan localDayPlan = fac.createLocalDayPlan(new LocalDay(ZoneId.of("Europe/Berlin"), LocalDate.of(2020, 4, 20)), instant08_30, instant17_00);
        localDayPlan.addAppointment(TestData.DATA1, TestData.T09_00); //30 min
        localDayPlan.addAppointment(TestData.DATA2, TestData.T10_30); //30 min
        localDayPlan.addAppointment(TestData.DATA3, TestData.T14_00); //15 min
        assertThat(localDayPlan.toString()).contains("2020-04-20", "09:00", "09:30", "10:30", "11:00", "14:00", "14:15");
    }

    @Test
    void t03toStringWithoutAnyApps() {
        Instant instant08_30 = LocalDateTime.of(2020, 4, 20, 8, 30).atZone(ZoneId.of("Europe/Berlin")).toInstant();
        Instant instant17_00 = LocalDateTime.of(2020, 4, 20, 17, 0).atZone(ZoneId.of("Europe/Berlin")).toInstant();
        LocalDayPlan localDayPlan = fac.createLocalDayPlan(new LocalDay(ZoneId.of("Europe/Berlin"), LocalDate.of(2020, 4, 20)), instant08_30, instant17_00);
        assertThat(localDayPlan.toString()).isEqualTo("There are no appointments for this day");
    }

}
