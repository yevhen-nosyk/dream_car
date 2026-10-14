package ua.nure.nosyk.courseproject.entity;

public class User {
    private Long id;
    private String role;
    private String accountStatus = "ACTIVE";
    private String name;
    private String email;
    private String password;
    private String phone;
    private String passport;
    private String address;
    private float rating;

    public Long getId() {
        return id;
    }

    public void setId(Long v) {
        id = v;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String v) {
        role = v;
    }

    public String getName() {
        return name;
    }

    public void setName(String v) {
        name = v;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String v) {
        email = v;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String v) {
        password = v;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String v) {
        phone = v;
    }

    public String getPassport() {
        return passport;
    }

    public void setPassport(String v) {
        passport = v;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String v) {
        address = v;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float v) {
        rating = v;
    }

    public String getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
    }
}
