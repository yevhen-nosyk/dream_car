package ua.nure.nosyk.courseproject.entity;

public class Contract {
    private Long id; private String status; private String body; private Long relatedRequestId;
    public Long getId(){return id;} public void setId(Long v){id=v;}
    public String getStatus(){return status;} public void setStatus(String v){status=v;}
    public String getBody(){return body;} public void setBody(String v){body=v;}
    public Long getRelatedRequestId(){return relatedRequestId;} public void setRelatedRequestId(Long v){relatedRequestId=v;}
}
