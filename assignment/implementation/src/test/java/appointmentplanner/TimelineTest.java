package appointmentplanner;

import appointmentplanner.api.Timeline;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.Test;

import static appointmentplanner.TestData.TODAY;
import static org.assertj.core.api.Assertions.*;

public class TimelineTest {

    private Timeline timeline = new TimelineImpl(TODAY.ofLocalTime(TestData.T08_30), TestData.TODAY.ofLocalTime(TestData.T17_30));

    @Test
    void t01createTimeline() {
        SoftAssertions.assertSoftly(s -> {
            s.assertThat(timeline).isNotNull();
            s.assertThat(timeline.start()).isEqualTo(TODAY.ofLocalTime(TestData.T08_30));
            s.assertThat(timeline.end()).isEqualTo(TODAY.ofLocalTime(TestData.T17_30));
        });
    }

    @Test
    void t02constructionIllegalArguments() {
        ThrowableAssert.ThrowingCallable code = () -> new TimelineImpl(TODAY.ofLocalTime(TestData.T09_00), TestData.TODAY.ofLocalTime(TestData.T08_30));
        assertThatCode(code).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void t03addAppointmentFixedTime() {

    }
}
