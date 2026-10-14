package ua.nure.nosyk.courseproject.entity;

public class Deposit {
    private Long id;
    private int sum;
    private String status;
    private String paymentMethod;
    private Long userId;

    public Long getId(){return id;} public void setId(Long v){id=v;}
    public int getSum(){return sum;} public void setSum(int v){sum=v;}
    public String getStatus(){return status;} public void setStatus(String v){status=v;}
    public String getPaymentMethod(){return paymentMethod;} public void setPaymentMethod(String v){paymentMethod=v;}
    public Long getUserId(){return userId;} public void setUserId(Long v){userId=v;}
}
