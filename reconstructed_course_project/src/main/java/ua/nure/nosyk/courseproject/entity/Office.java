package ua.nure.nosyk.courseproject.entity;

public class Office {
    private Long id;
    private String address;
    private String workHours;
    private String accessability;

    public Long getId(){return id;} public void setId(Long v){id=v;}
    public String getAddress(){return address;} public void setAddress(String v){address=v;}
    public String getWorkHours(){return workHours;} public void setWorkHours(String v){workHours=v;}
    public String getAccessability(){return accessability;} public void setAccessability(String v){accessability=v;}
}
