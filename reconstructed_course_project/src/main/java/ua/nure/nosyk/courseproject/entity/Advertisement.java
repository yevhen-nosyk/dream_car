package ua.nure.nosyk.courseproject.entity;

import java.time.LocalDateTime;

public class Advertisement {
    private Long id;
    private String description;
    private LocalDateTime creationDate;
    private String status;
    private Long ownerId;
    private Long carId;

    public Long getId(){return id;} public void setId(Long v){id=v;}
    public String getDescription(){return description;} public void setDescription(String v){description=v;}
    public LocalDateTime getCreationDate(){return creationDate;} public void setCreationDate(LocalDateTime v){creationDate=v;}
    public String getStatus(){return status;} public void setStatus(String v){status=v;}
    public Long getOwnerId(){return ownerId;} public void setOwnerId(Long v){ownerId=v;}
    public Long getCarId(){return carId;} public void setCarId(Long v){carId=v;}
}
