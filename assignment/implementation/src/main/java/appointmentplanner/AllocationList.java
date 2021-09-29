package appointmentplanner;

public class AllocationList {


    public AllocationList(AllocationNode allocationNode) {
        if (allocationNode.getPurpose() != null)
            throw new IllegalArgumentException("In the beginning an AllocationList should be 'empty'");
    }
}
