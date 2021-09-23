package appointmentplanner;

import appointmentplanner.api.AbstractAPFactory;
import appointmentplanner.api.LocalDayPlan;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

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

}
