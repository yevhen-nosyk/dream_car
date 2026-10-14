package ua.nure.nosyk.courseproject.form;

public class RequestForm {
    private Long receiverId, proposedCarId;
    private int depositSum;
    private String paymentMethod;

    public Long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Long v) {
        receiverId = v;
    }

    public Long getProposedCarId() {
        return proposedCarId;
    }

    public void setProposedCarId(Long v) {
        proposedCarId = v;
    }

    public int getDepositSum() {
        return depositSum;
    }

    public void setDepositSum(int v) {
        depositSum = v;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String v) {
        paymentMethod = v;
    }
}