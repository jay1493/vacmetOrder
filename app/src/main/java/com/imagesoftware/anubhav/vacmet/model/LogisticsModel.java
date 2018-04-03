package com.imagesoftware.anubhav.vacmet.model;

import java.io.Serializable;

/**
 * Created by Anubhav-Singh on 18-01-2018.
 */

public class LogisticsModel implements Serializable {

    private String blNo;
    private String containerNo;
    private String vesselNo;
    private String eta;
    private String ActInvNo;
    private String Country;
    private String Date;
    private String PartyNameForward;
    private String PurchaseNo;

    public LogisticsModel() {
        this.blNo = "Awaited";
        this.containerNo = "Awaited";
        this.vesselNo = "Awaited";
        this.eta = "Awaited";
    }

    public String getBlNo() {
        return blNo;
    }

    public void setBlNo(String blNo) {
        this.blNo = blNo;
    }

    public String getContainerNo() {
        return containerNo;
    }

    public void setContainerNo(String containerNo) {
        this.containerNo = containerNo;
    }

    public String getVesselNo() {
        return vesselNo;
    }

    public void setVesselNo(String vesselNo) {
        this.vesselNo = vesselNo;
    }

    public String getEta() {
        return eta;
    }

    public void setEta(String eta) {
        this.eta = eta;
    }

    public String getActInvNo() {
        return ActInvNo;
    }

    public void setActInvNo(String actInvNo) {
        ActInvNo = actInvNo;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getPartyNameForward() {
        return PartyNameForward;
    }

    public void setPartyNameForward(String partyNameForward) {
        PartyNameForward = partyNameForward;
    }

    public String getPurchaseNo() {
        return PurchaseNo;
    }

    public void setPurchaseNo(String purchaseNo) {
        PurchaseNo = purchaseNo;
    }
}
