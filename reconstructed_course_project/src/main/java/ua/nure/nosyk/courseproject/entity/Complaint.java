package ua.nure.nosyk.courseproject.entity;

public class Complaint {
    private Long id; private String content; private Long complainantId; private String status;
    public Long getId(){return id;} public void setId(Long v){id=v;}
    public String getContent(){return content;} public void setContent(String v){content=v;}
    public Long getComplainantId(){return complainantId;} public void setComplainantId(Long v){complainantId=v;}
    public String getStatus(){return status;} public void setStatus(String v){status=v;}
}
