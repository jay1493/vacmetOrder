package com.example.anubhav.vacmet.model;

/**
 * Created by anubhav on 7/3/17.
 */

public class UserModel {
    private String userName;
    private String userEmail;
    private String userPass;
    private String userContact;

    public UserModel(String userName, String userEmail, String userPass, String userContact) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPass = userPass;
        this.userContact = userContact;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPass() {
        return userPass;
    }

    public void setUserPass(String userPass) {
        this.userPass = userPass;
    }

    public String getUserContact() {
        return userContact;
    }

    public void setUserContact(String userContact) {
        this.userContact = userContact;
    }
}
