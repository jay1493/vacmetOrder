package com.example.anubhav.vacmet.model;

import java.util.List;

/**
 * Created by anubhav on 7/3/17.
 */

public class UserModel {
    private String userName;
    private String userEmail;
    private String userPass;
    private String userContact;
    private String sapId;
    private String clientOrServer;
    private boolean isApproved;
    private List<String> approvedPartyNames;
    public UserModel(String userName, String userEmail, String userPass, String userContact , boolean approved,
                     List<String> partyNames, String id, String cOrS) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPass = userPass;
        this.userContact = userContact;
        this.isApproved = approved;
        this.approvedPartyNames = partyNames;
        this.sapId = id;
        this.clientOrServer = cOrS;
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

    public boolean isApproved() {
        return isApproved;
    }

    public UserModel setApproved(boolean approved) {
        isApproved = approved;
        return this;
    }

    public List<String> getApprovedPartyNames() {
        return approvedPartyNames;
    }

    public UserModel setApprovedPartyNames(List<String> approvedPartyNames) {
        this.approvedPartyNames = approvedPartyNames;
        return this;
    }

    public String getSapId() {
        return sapId;
    }

    public UserModel setSapId(String sapId) {
        this.sapId = sapId;
        return this;
    }

    public String getClientOrServer() {
        return clientOrServer;
    }

    public UserModel setClientOrServer(String clientOrServer) {
        this.clientOrServer = clientOrServer;
        return this;
    }
}
