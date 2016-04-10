package go.takethespread;

public enum OrderStatus {
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

    //    Order State

    //      Initialized
//    Order information validated on local PC
//    Yellow
//      PendingSubmit
//    Order submitted to the connectivity provider
//    Orange
//      Accepted
//    Order confirmation received by broker
//    Light blue
//      Working
//    Order confirmation received by exchange
//    Green
//      PendingChange
//    Order modification submitted to the connectivity provider
//    Orange
//      PendingCancel
//    Order cancellation submitted to the connectivity provider/exchange
//    Red
//      Cancelled
//    Order cancellation confirmed cancelled by exchange
//    Red
//       Rejected
//    Order rejected locally, by connectivity provider or exchange
//    Red
//       PartFilled
//    Order partially filled
//    Red
//       Filled
//    Order completely filled
//    Green
}
