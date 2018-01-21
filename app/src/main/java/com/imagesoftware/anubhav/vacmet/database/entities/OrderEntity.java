package com.imagesoftware.anubhav.vacmet.database.entities;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.imagesoftware.anubhav.vacmet.model.ItemModel;
import com.imagesoftware.anubhav.vacmet.model.LogisticsModel;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Anubhav-Singh on 12-01-2018.
 */
@Entity(tableName = "vacmet_orders")
public class OrderEntity implements Serializable {

        @PrimaryKey(autoGenerate = true)
        private int orderId;
        private String partyName;
        @Nullable
        private String orderNo;
        @Nullable
        private String orderQty;
        @Nullable
        private String orderDate;
        @Nullable
        private String despQty;
        @Nullable
        private String deliveryDate;
        @Nullable
        private String sapId;
        @Nullable
        private String inProdQty;
        @Nullable
        private String status;
        @Nullable
        private String stockQty;
        @Nullable
        private String invoiceNo;
        @Nullable
        private String invoiceDate;
        private int isPending;

        @Nullable
        private ArrayList<String> oldModifiedDates;

        @Nullable
        private String adminNotes;

        @Nullable
        @Embedded
        private LogisticsModel logisticsModel;

        @Ignore
        private ArrayList<ItemEntity> itemModelArrayList;

        @Nullable
        private String customerPONo;
        @Nullable
        private String customerPODate;
        @Nullable
        private String partyPONo;
        @Nullable
        private String partyPODate;
        @Nullable
        private String partyPOETA;


    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
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

    public String getSapId() {
        return sapId;
    }

    public void setSapId(String sapId) {
        this.sapId = sapId;
    }

    public String getInProdQty() {
        return inProdQty;
    }

    public void setInProdQty(String inProdQty) {
        this.inProdQty = inProdQty;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStockQty() {
        return stockQty;
    }

    public void setStockQty(String stockQty) {
        this.stockQty = stockQty;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public int getIsPending() {
        return isPending;
    }

    public void setIsPending(int isPending) {
        this.isPending = isPending;
    }

    public ArrayList<ItemEntity> getItemModelArrayList() {
        return itemModelArrayList;
    }

    @Nullable
    public ArrayList<String> getOldModifiedDates() {
        return oldModifiedDates;
    }

    public void setOldModifiedDates(@Nullable ArrayList<String> oldModifiedDates) {
        this.oldModifiedDates = oldModifiedDates;
    }

    @Nullable
    public String getAdminNotes() {
        return adminNotes;
    }

    public void setAdminNotes(@Nullable String adminNotes) {
        this.adminNotes = adminNotes;
    }

    public void setItemModelArrayList(ArrayList<ItemEntity> itemModelArrayList) {
        this.itemModelArrayList = itemModelArrayList;
    }

    @Nullable
    public LogisticsModel getLogisticsModel() {
        return logisticsModel;
    }

    public void setLogisticsModel(@Nullable LogisticsModel logisticsModel) {
        this.logisticsModel = logisticsModel;
    }

    @Nullable
    public String getCustomerPONo() {
        return customerPONo;
    }

    public void setCustomerPONo(@Nullable String customerPONo) {
        this.customerPONo = customerPONo;
    }

    @Nullable
    public String getCustomerPODate() {
        return customerPODate;
    }

    public void setCustomerPODate(@Nullable String customerPODate) {
        this.customerPODate = customerPODate;
    }

    @Nullable
    public String getPartyPONo() {
        return partyPONo;
    }

    public void setPartyPONo(@Nullable String partyPONo) {
        this.partyPONo = partyPONo;
    }

    @Nullable
    public String getPartyPODate() {
        return partyPODate;
    }

    public void setPartyPODate(@Nullable String partyPODate) {
        this.partyPODate = partyPODate;
    }

    @Nullable
    public String getPartyPOETA() {
        return partyPOETA;
    }

    public void setPartyPOETA(@Nullable String partyPOETA) {
        this.partyPOETA = partyPOETA;
    }
}
