package ua.nure.nosyk.courseproject.entity;

import java.time.LocalDateTime;

public class Request {
    private Long id;
    private String status;
    private LocalDateTime sendingDate;
    private Long proposedCarId;
    private Deposit deposit;
    private Long receiverId;
    private Long senderId;

    public Long getId(){return id;} public void setId(Long v){id=v;}
    public String getStatus(){return status;} public void setStatus(String v){status=v;}
    public LocalDateTime getSendingDate(){return sendingDate;} public void setSendingDate(LocalDateTime v){sendingDate=v;}
    public Long getProposedCarId(){return proposedCarId;} public void setProposedCarId(Long v){proposedCarId=v;}
    public Deposit getDeposit(){return deposit;} public void setDeposit(Deposit v){deposit=v;}
    public Long getReceiverId(){return receiverId;} public void setReceiverId(Long v){receiverId=v;}
    public Long getSenderId(){return senderId;} public void setSenderId(Long v){senderId=v;}
}
