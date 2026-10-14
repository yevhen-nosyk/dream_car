package ua.nure.nosyk.courseproject.entity;

public class Car {
    private Long id;
    private String vinCode;
    private String brand;
    private String model;
    private int year;
    private int mileage;
    private String vehicleCondition = "USED";
    private Long ownerId;

    public int getMileage() {
        return mileage;
    }

    public void setMileage(int mileage) {
        this.mileage = mileage;
    }

    public String getVehicleCondition() {
        return vehicleCondition;
    }

    public void setVehicleCondition(String vehicleCondition) {
        this.vehicleCondition = vehicleCondition;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long v) {
        id = v;
    }

    public String getVinCode() {
        return vinCode;
    }

    public void setVinCode(String v) {
        vinCode = v;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String v) {
        brand = v;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String v) {
        model = v;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int v) {
        year = v;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long v) {
        ownerId = v;
    }
}
