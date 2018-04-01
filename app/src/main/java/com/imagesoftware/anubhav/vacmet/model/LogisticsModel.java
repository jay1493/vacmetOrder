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
    private String PartyName;
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

    public LogisticsModel setActInvNo(String actInvNo) {
        ActInvNo = actInvNo;
        return this;
    }

    public String getCountry() {
        return Country;
    }

    public LogisticsModel setCountry(String country) {
        Country = country;
        return this;
    }

    public String getDate() {
        return Date;
    }

    public LogisticsModel setDate(String date) {
        Date = date;
        return this;
    }

    public String getPartyName() {
        return PartyName;
    }

    public LogisticsModel setPartyName(String partyName) {
        PartyName = partyName;
        return this;
    }

    public String getPurchaseNo() {
        return PurchaseNo;
    }

    public LogisticsModel setPurchaseNo(String purchaseNo) {
        PurchaseNo = purchaseNo;
        return this;
    }
}
