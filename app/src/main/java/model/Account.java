package model;

import java.time.LocalDate;

public class Account {
    private String userUID;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String birthday;
    private String street;
    private String houseNumber;
    private int plz;
    private String city;
    private static Account OBJ;

    private Account(String userUID, String username, String email) {
        this.userUID = userUID;
        this.username = username;
        this.email = email;
    }

    public Account() {
    }

    public static Account getInstance(){
        if(OBJ == null){
            OBJ = new Account();
        }

        return OBJ;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserUID() {
        return userUID;
    }
    public void setUserUID(String userUID) {
        this.userUID = userUID;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getBirthday() {
        return birthday;
    }
    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getStreet() {
        return street;
    }
    public void setStreet(String street) {
        this.street = street;
    }

    public String getHouseNumber() {
        return houseNumber;
    }
    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public int getPlz() {
        return plz;
    }
    public void setPlz(int plz) {
        this.plz = plz;
    }

    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }

    public void setAccount(String userUID, String userName, String email, String firstname, String lastName, String birthday, String city, String street, String houseNumber, int plz){
        this.userUID = userUID;
        this.username = userName;
        this.email = email;
        this.firstName = firstname;
        this.lastName = lastName;
        this.birthday = birthday;
        this.city = city;
        this.street = street;
        this.houseNumber = houseNumber;
        this.plz = plz;
    }
}
