package model;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.function.Predicate;

public class Order implements Serializable {
    private String userUID;
    private String profUID;
    private String street;
    private String houseNumber;
    private String plz;
    private String city;
    private String date;
    private String description;
    private String order_date;
    private String pattern = "dd.MM.yyyy";
    DateFormat df = new SimpleDateFormat(pattern);
    private long number;

    public Order(String userUID, String profUID, String street, String houseNumber, String plz, String city, String date, String description, long number) {
        this.userUID = userUID;
        this.profUID = profUID;
        this.street = street;
        this.houseNumber = houseNumber;
        this.plz = plz;
        this.city = city;
        this.date = date;
        this.description = description;
        this.order_date = df.format(Calendar.getInstance().getTime());
        this.number = number;
    }

    public long getNumber() {
        return number;
    }

    public String getOrder_date() {
        return order_date;
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
    public String getDate() {
        return date;
    }
    public String getDescription() {
        return description;
    }
}
