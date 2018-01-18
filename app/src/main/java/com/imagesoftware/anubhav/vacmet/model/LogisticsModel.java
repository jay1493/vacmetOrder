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
}
