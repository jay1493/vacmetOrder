package com.example.anubhav.vacmet.model;

import java.io.Serializable;

/**
 * Created by anubhav on 27/1/17.
 */

public class ItemModel implements Serializable {
    private String itemName;
    private String despQty;
    private String totalQty;

    public ItemModel(String itemName, String despQty, String totalQty) {
        this.itemName = itemName;
        this.despQty = despQty;
        this.totalQty = totalQty;
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
        return totalQty;
    }

    public void setTotalQty(String totalQty) {
        this.totalQty = totalQty;
    }
}
