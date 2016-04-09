package go.takethespread.NT;

public enum NTOrderStatus {
    INITIALIZED,
    PENDING_SUBMIT,
    ACCEPTED,
    WORKING,
    PENDING_CHANGE,
    PENDING_CANCEL,
    CANCELLED,
    REJECTED,
    PART_FILLED,
    FILLED
}
