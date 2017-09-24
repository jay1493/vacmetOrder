package com.example.anubhav.vacmet.model;

import android.os.Build;

import java.io.Serializable;
import java.text.DecimalFormat;
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
    private String stockQty;
    private ArrayList<ItemModel> itemModelArrayList;

    public OrderModel() {
        itemModelArrayList = new ArrayList<>();
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
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        orderQty = decimalFormat.format(Double.parseDouble(orderQty));
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
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        despQty = decimalFormat.format(Double.parseDouble(despQty));
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
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        inProdQty = decimalFormat.format(Double.parseDouble(inProdQty));
        this.inProdQty = inProdQty;
    }

    public void addItemInOrder(ItemModel itemModel){
        if(itemModelArrayList!=null){
            itemModelArrayList.add(itemModel);
        }
    }

    public ArrayList<ItemModel> getItemList(){
        return itemModelArrayList;
    }

    public String getStatus() {
        return status;
    }

    public OrderModel setStatus(String status) {
        this.status = status;
        return this;
    }

    public String getStockQty() {
        return stockQty;
    }

    public OrderModel setStockQty(String stockQty) {
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        stockQty = decimalFormat.format(Double.parseDouble(stockQty));
        this.stockQty = stockQty;
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
