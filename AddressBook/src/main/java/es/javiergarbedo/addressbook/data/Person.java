package es.javiergarbedo.addressbook.data;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Person {
    private int id;
    private String name = "";
    private String surnames = "";
    private String alias = "";
    private String email = "";
    private String phoneNumber = "";
    private String mobileNumber = "";
    private String address = "";
    private String postCode = "";
    private String city = "";
    private String province = "";
    private String country = "";
    private Date birthDate = null;
    private String comments = "";
    private String photoFileName = "";

    public Person() {
    }

    public Person(int id, String name, String surnames, String alias, String email, String phoneNumber, String mobileNumber, String address, String postCode, String city, String province, String country, Date birthDate, String comments, String photoFileName) {
        this.id = id;
        this.name = name;
        this.surnames = surnames;
        this.alias = alias;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.mobileNumber = mobileNumber;
        this.address = address;
        this.postCode = postCode;
        this.city = city;
        this.province = province;
        this.country = country;
        this.birthDate = birthDate;
        this.comments = comments;
        this.photoFileName = photoFileName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurnames() {
        return surnames;
    }

    public void setSurnames(String surnames) {
        this.surnames = surnames;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Date getBirthDate() {
        return birthDate;
    }
    
    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getPhotoFileName() {
        return photoFileName;
    }

    public void setPhotoFileName(String photoFileName) {
        this.photoFileName = photoFileName;
    }

    @Override
    public String toString() {
        return name + " " + surnames;
    }

}

