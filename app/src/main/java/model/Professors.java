package model;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Professors implements Serializable {

    private String id;
    private String email;
    private String firstName;
    private String lastName;
    private String birthday;
    private String street;
    private String houseNumber;
    private String plz;
    private String city;
    private String mobileNumber;
    private List<String> departments;

    private transient Bitmap image;

    public Professors(String email, String firstName, String lastName, String birthday, String street, String houseNumber, String plz, String city, List<String> departments, String id, String mobileNumber){

        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthday = birthday;
        this.street = street;
        this.houseNumber = houseNumber;
        this.plz = plz;
        this.city = city;
        this.departments = departments;
        this.mobileNumber = mobileNumber;
        this.image = null;

    }

    public Professors(String email, String firstName, String lastName, String birthday, String street, String houseNumber, String plz, String city, List<String> departments, String id, String mobileNumber, Bitmap image){

        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthday = birthday;
        this.street = street;
        this.houseNumber = houseNumber;
        this.plz = plz;
        this.city = city;
        this.departments = departments;
        this.mobileNumber = mobileNumber;
        this.image = image;
    }



    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getid() {
        return id;
    }
    public String getEmail() {
        return email;
    }
    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public String getBirthday() {
        return birthday;
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
    public List<String> getDepartments() { return departments; }
    public String getMobileNumber() {
        return mobileNumber;
    }
}
