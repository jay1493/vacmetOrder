package com.example.anubhav.vacmet.model;

import android.os.Build;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

/**
 * Created by anubhav on 23/1/17.
 */

public class OrderModel implements Serializable {

    private String partyName;
    private String orderNo;
    private String orderQty;
    private String orderDate;
    private String despQty;
    private String deliveryDate;
    private String inProdQty;
    private String status;
    private ArrayList<ItemModel> itemModelArrayList;

    public OrderModel() {
    }

    public OrderModel(String partyName, String orderNo, String orderQty, String orderDate, String despQty, String deliveryDate, String inProdQty, ArrayList<ItemModel> itemModelArrayList) {
        this.partyName = partyName;
        this.orderNo = orderNo;
        this.orderQty = orderQty;
        this.orderDate = orderDate;
        this.despQty = despQty;
        this.deliveryDate = deliveryDate;
        this.inProdQty = inProdQty;
        this.itemModelArrayList = itemModelArrayList;
    }

    public String getPartyName() {
        return partyName;
    }

    public void setPartyName(String partyName) {
        this.partyName = partyName;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOrderQty() {
        return orderQty;
    }

    public void setOrderQty(String orderQty) {
        this.orderQty = orderQty;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getDespQty() {
        return despQty;
    }

    public void setDespQty(String despQty) {
        this.despQty = despQty;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getInProdQty() {
        return inProdQty;
    }

    public void setInProdQty(String inProdQty) {
        this.inProdQty = inProdQty;
    }

    public ArrayList<ItemModel> getItemModelArrayList() {
        return itemModelArrayList;
    }

    public void setItemModelArrayList(ArrayList<ItemModel> itemModelArrayList) {
        this.itemModelArrayList = itemModelArrayList;
    }

    public String getStatus() {
        return status;
    }

    public OrderModel setStatus(String status) {
        this.status = status;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderModel that = (OrderModel) o;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return Objects.equals(orderNo, that.orderNo);
        }else{
            return (orderNo != null && orderNo.equals(that.orderNo));
        }
    }

    @Override
    public int hashCode() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return Objects.hash(orderNo);
        }else{
            return Arrays.hashCode(new Object[]{orderNo});
        }
    }
}
