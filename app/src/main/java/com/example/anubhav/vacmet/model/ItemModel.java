package com.example.anubhav.vacmet.model;

import java.io.Serializable;

/**
 * Created by anubhav on 27/1/17.
 */

public class ItemModel implements Serializable {
    private String itemName;
    private String materialNo;
    private String status;
    private String despQty;
    private String totalQty;
    private String inProdQty;
    public ItemModel(String itemName, String despQty, String totalQty) {
        this.itemName = itemName;
        this.despQty = despQty;
        this.totalQty = totalQty;
    }

    public ItemModel() {
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getDespQty() {
        return despQty;
    }

    public void setDespQty(String despQty) {
        this.despQty = despQty;
    }

    public String getTotalQty() {
        totalQty = String.valueOf(Double.parseDouble(getDespQty())+Double.parseDouble(getInProdQty()));
        return totalQty;
    }

    public void setTotalQty(String totalQty) {
        this.totalQty = totalQty;
    }

    public String getMaterialNo() {
        return materialNo;
    }

    public ItemModel setMaterialNo(String materialNo) {
        this.materialNo = materialNo;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public ItemModel setStatus(String status) {
        this.status = status;
        return this;
    }

    public String getInProdQty() {
        return inProdQty;
    }

    public ItemModel setInProdQty(String inProdQty) {
        this.inProdQty = inProdQty;
        return this;
    }
}
