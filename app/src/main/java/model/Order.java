package model;

public class Order {
    private String userUID;
    private String profUID;
    private String street;
    private String houseNumber;
    private String plz;
    private String city;
    private String description;

    public Order(String userUID, String profUID, String street, String houseNumber, String plz, String city, String description) {
        this.userUID = userUID;
        this.profUID = profUID;
        this.street = street;
        this.houseNumber = houseNumber;
        this.plz = plz;
        this.city = city;
        this.description = description;

    }
    public String getProfUID() {
        return profUID;
    }
    public String getUserUID() {
        return userUID;
    }
    public String getStreet() {
        return street;
    }
    public String getHouseNumber() {
        return houseNumber;
    }
    public String getPlz() {
        return plz;
    }
    public String getCity() {
        return city;
    }
    public String getDescription() {
        return description;
    }
}
