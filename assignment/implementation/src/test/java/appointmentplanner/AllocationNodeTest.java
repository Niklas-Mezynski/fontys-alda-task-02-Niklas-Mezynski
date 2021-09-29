package appointmentplanner;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AllocationNodeTest {
    @Test
    void t01() {
        AllocationNode allocationNode = new AllocationNode(new TimeSlotImpl(TestData.TODAY.ofLocalTime(TestData.T09_00), TestData.TODAY.ofLocalTime(TestData.T09_30)), TestData.DATA1);
        SoftAssertions.assertSoftly(s -> {
            s.assertThat(allocationNode.next).isNull();
            s.assertThat(allocationNode.prev).isNull();
            s.assertThat(allocationNode.getStart()).isEqualTo(TestData.TODAY.ofLocalTime(TestData.T09_00));
            s.assertThat(allocationNode.getEnd()).isEqualTo(TestData.TODAY.ofLocalTime(TestData.T09_30));
            s.assertThat(allocationNode.getPurpose()).isEqualTo(TestData.DATA1);
        });
    }
}