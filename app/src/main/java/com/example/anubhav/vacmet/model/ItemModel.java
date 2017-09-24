package com.example.anubhav.vacmet.model;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Created by anubhav on 27/1/17.
 */

public class ItemModel implements Serializable {
    private String itemName;
    private String materialNo;
    private String status;
    private String despQty;
    private String orderedQty;
    private String totalQty;
    private String inProdQty;
    private String stockQty;
    private String billNo;
    private String billDate;
    private String length;
    private String width;
    private String treatment1;
    private String treatment2;
    private String shades;
    private String containerNo;
    private String orderDate;
    private String deliveryDate;
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
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        despQty = decimalFormat.format(Double.parseDouble(despQty));
        this.despQty = despQty;
    }

    public String getTotalQty() {
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        totalQty = String.valueOf(decimalFormat.format(Double.parseDouble(getDespQty())+Double.parseDouble(getInProdQty())));
        return totalQty;
    }

    public void setTotalQty(String totalQty) {
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        totalQty = decimalFormat.format(Double.parseDouble(totalQty));
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

    public void setInProdQty(String inProdQty) {
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        inProdQty = decimalFormat.format(Double.parseDouble(inProdQty));
        this.inProdQty = inProdQty;

    }

    public String getBillNo() {
        return billNo;
    }

    public ItemModel setBillNo(String billNo) {
        this.billNo = billNo;
        return this;
    }

    public String getBillDate() {
        return billDate;
    }

    public ItemModel setBillDate(String billDate) {
        this.billDate = billDate;
        return this;
    }

    public String getLength() {
        return length;
    }

    public ItemModel setLength(String length) {
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        length = decimalFormat.format(Double.parseDouble(length));
        this.length = length;
        return this;
    }

    public String getWidth() {
        return width;
    }

    public ItemModel setWidth(String width) {
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        width = decimalFormat.format(Double.parseDouble(width));
        this.width = width;
        return this;
    }

    public String getTreatment1() {
        return treatment1;
    }

    public ItemModel setTreatment1(String treatment1) {
        this.treatment1 = treatment1;
        return this;
    }

    public String getTreatment2() {
        return treatment2;
    }

    public ItemModel setTreatment2(String treatment2) {
        this.treatment2 = treatment2;
        return this;
    }

    public String getShades() {
        return shades;
    }

    public ItemModel setShades(String shades) {
        this.shades = shades;
        return this;
    }

    public String getContainerNo() {
        return containerNo;
    }

    public ItemModel setContainerNo(String containerNo) {
        this.containerNo = containerNo;
        return this;
    }

    public String getOrderedQty() {
        return orderedQty;
    }

    public ItemModel setOrderedQty(String orderedQty) {
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        orderedQty = decimalFormat.format(Double.parseDouble(orderedQty));
        this.orderedQty = orderedQty;
        return this;
    }

    public String getStockQty() {
        return stockQty;
    }

    public ItemModel setStockQty(String stockQty) {
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        stockQty = decimalFormat.format(Double.parseDouble(stockQty));
        this.stockQty = stockQty;
        return this;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public ItemModel setOrderDate(String orderDate) {
        this.orderDate = orderDate;
        return this;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public ItemModel setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
        return this;
    }
}
