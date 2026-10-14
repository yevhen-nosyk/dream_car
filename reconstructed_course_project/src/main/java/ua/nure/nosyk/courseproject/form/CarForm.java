package ua.nure.nosyk.courseproject.form;

public class CarForm {
    private String vinCode, brand, model;
    private int year;
    private int mileage;
    private String vehicleCondition = "USED";

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
}