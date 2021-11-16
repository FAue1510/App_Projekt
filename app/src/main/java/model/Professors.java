package model;

public class Professors {

    private String id;
    private String email;
    private String firstName;
    private String lastName;
    private String birthday;
    private String street;
    private String houseNumber;
    private String plz;
    private String city;
    private String subject;

    public Professors(String email, String firstName, String lastName, String birthday, String street, String houseNumber, String plz, String city, String subject, String id){

        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthday = birthday;
        this.street = street;
        this.houseNumber = houseNumber;
        this.plz = plz;
        this.city = city;
        this.subject = subject;

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
    public String getSubject() {
        return subject;
    }
}
