package appointmentplanner;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

public class TimelineTest {
    @Test
    void t01createTimeline() {
        TimelineImpl timeline = new TimelineImpl(TestData.TODAY.ofLocalTime(TestData.T09_00), TestData.TODAY.ofLocalTime(TestData.T16_00));
        SoftAssertions.assertSoftly(s -> {
            s.assertThat(timeline).isNotNull();
            s.assertThat(timeline.start()).isEqualTo(TestData.TODAY.ofLocalTime(TestData.T09_00));
            s.assertThat(timeline.end()).isEqualTo(TestData.TODAY.ofLocalTime(TestData.T16_00));
        });
    }
}
