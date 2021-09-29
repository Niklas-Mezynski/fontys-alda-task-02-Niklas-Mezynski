package appointmentplanner;

import static org.assertj.core.api.Assertions.*;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;

public class AllocationListTest {

    @Test
    void t01creation() {
        AllocationList allocationList = new AllocationList(new AllocationNode(TestData.TODAY.ofLocalTime(TestData.T08_30), TestData.TODAY.ofLocalTime(TestData.T17_30)));
        assertThat(allocationList).isNotNull();
    }

    @Test
    void t02creationWithData() {
        ThrowingCallable code = () -> new AllocationList(new AllocationNode(TestData.TODAY.ofLocalTime(TestData.T08_30), TestData.TODAY.ofLocalTime(TestData.T17_30), TestData.DATA1));
        assertThatCode(code).isInstanceOf(IllegalArgumentException.class);
    }

}
