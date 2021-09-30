package appointmentplanner;

import static org.assertj.core.api.Assertions.*;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;

public class AllocationListTest {

    @Test
    void t01creation() {
        AllocationList allocationList = new AllocationList((TestData.TODAY.ofLocalTime(TestData.T08_30)), (TestData.TODAY.ofLocalTime(TestData.T17_30)));
        assertThat(allocationList).isNotNull();
    }

    @Test
    void t02creationWithIllegal() {
        ThrowingCallable code = () -> new AllocationList((TestData.TODAY.ofLocalTime(TestData.T09_00)), (TestData.TODAY.ofLocalTime(TestData.T08_30)));
    }

}
