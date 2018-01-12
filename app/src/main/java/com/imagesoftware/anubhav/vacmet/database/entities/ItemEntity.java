package com.imagesoftware.anubhav.vacmet.database.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.Nullable;

import com.imagesoftware.anubhav.vacmet.model.OrderModel;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Anubhav-Singh on 12-01-2018.
 */
@Entity(tableName = "vacmet_item_for_orders",indices = {@Index(value = "selectedOrderNo")})
public class ItemEntity implements Serializable {

        @PrimaryKey(autoGenerate = true)
        private int itemNo;
        @ForeignKey(entity = OrderModel.class,parentColumns = "orderNo",childColumns = "selectedOrderNo", onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE)
        private String selectedOrderNo;
        private String itemName;
        private String materialNo;
        @Nullable
        private String status;
        @Nullable
        private String despQty;
        @Nullable
        private String orderedQty;
        @Nullable
        private String totalQty;
        @Nullable
        private String inProdQty;
        @Nullable
        private String stockQty;
        @Nullable
        private String billNo;
        @Nullable
        private String billDate;
        @Nullable
        private String length;
        @Nullable
        private String width;
        @Nullable
        private String containedOrderNo;
    @Nullable
        private String treatment1;
    @Nullable
    private String treatment2;
    @Nullable
    private String shades;
    @Nullable
    private String containerNo;
    @Nullable
    private String orderDate;
    @Nullable
    private String deliveryDate;
    @Nullable
    private String invoiceDate;
    @Nullable
    private String invoiceNo;
    @Nullable
    private ArrayList<String> lengthList;
    @Nullable
        private ArrayList<String> widthList;
    @Nullable
        private ArrayList<Double> totalDespQty;
    @Nullable
        private ArrayList<Double> totalInprodQty;
    @Nullable
        private ArrayList<Double> totalOrderedQty;
    @Nullable
        private ArrayList<String> treatment1List;
    @Nullable
        private ArrayList<String> treatment2List;
    @Nullable
        private ArrayList<String> itemDeliveryDatesList;
    @Nullable
        private ArrayList<String> shadesList;
    @Nullable
        private ArrayList<String> containerNoList;
    @Nullable
        private ArrayList<String> stockQtyList;
    @Nullable
        private ArrayList<String> containedOrderNoList;

    public int getItemNo() {
        return itemNo;
    }

    public void setItemNo(int itemNo) {
        this.itemNo = itemNo;
    }

    public String getSelectedOrderNo() {
        return selectedOrderNo;
    }

    public void setSelectedOrderNo(String selectedOrderNo) {
        this.selectedOrderNo = selectedOrderNo;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getMaterialNo() {
        return materialNo;
    }

    public void setMaterialNo(String materialNo) {
        this.materialNo = materialNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDespQty() {
        return despQty;
    }

    public void setDespQty(String despQty) {
        this.despQty = despQty;
    }

    public String getOrderedQty() {
        return orderedQty;
    }

    public void setOrderedQty(String orderedQty) {
        this.orderedQty = orderedQty;
    }

    public String getTotalQty() {
        return totalQty;
    }

    public void setTotalQty(String totalQty) {
        this.totalQty = totalQty;
    }

    public String getInProdQty() {
        return inProdQty;
    }

    public void setInProdQty(String inProdQty) {
        this.inProdQty = inProdQty;
    }

    public String getStockQty() {
        return stockQty;
    }

    public void setStockQty(String stockQty) {
        this.stockQty = stockQty;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public String getBillDate() {
        return billDate;
    }

    public void setBillDate(String billDate) {
        this.billDate = billDate;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getContainedOrderNo() {
        return containedOrderNo;
    }

    public void setContainedOrderNo(String containedOrderNo) {
        this.containedOrderNo = containedOrderNo;
    }

    public String getTreatment1() {
        return treatment1;
    }

    public void setTreatment1(String treatment1) {
        this.treatment1 = treatment1;
    }

    public String getTreatment2() {
        return treatment2;
    }

    public void setTreatment2(String treatment2) {
        this.treatment2 = treatment2;
    }

    public String getShades() {
        return shades;
    }

    public void setShades(String shades) {
        this.shades = shades;
    }

    public String getContainerNo() {
        return containerNo;
    }

    public void setContainerNo(String containerNo) {
        this.containerNo = containerNo;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public ArrayList<String> getLengthList() {
        return lengthList;
    }

    public void setLengthList(ArrayList<String> lengthList) {
        this.lengthList = lengthList;
    }

    public ArrayList<String> getWidthList() {
        return widthList;
    }

    public void setWidthList(ArrayList<String> widthList) {
        this.widthList = widthList;
    }

    public ArrayList<Double> getTotalDespQty() {
        return totalDespQty;
    }

    public void setTotalDespQty(ArrayList<Double> totalDespQty) {
        this.totalDespQty = totalDespQty;
    }

    public ArrayList<Double> getTotalInprodQty() {
        return totalInprodQty;
    }

    public void setTotalInprodQty(ArrayList<Double> totalInprodQty) {
        this.totalInprodQty = totalInprodQty;
    }

    public ArrayList<Double> getTotalOrderedQty() {
        return totalOrderedQty;
    }

    public void setTotalOrderedQty(ArrayList<Double> totalOrderedQty) {
        this.totalOrderedQty = totalOrderedQty;
    }

    public ArrayList<String> getTreatment1List() {
        return treatment1List;
    }

    public void setTreatment1List(ArrayList<String> treatment1List) {
        this.treatment1List = treatment1List;
    }

    public ArrayList<String> getTreatment2List() {
        return treatment2List;
    }

    public void setTreatment2List(ArrayList<String> treatment2List) {
        this.treatment2List = treatment2List;
    }

    public ArrayList<String> getItemDeliveryDatesList() {
        return itemDeliveryDatesList;
    }

    public void setItemDeliveryDatesList(ArrayList<String> itemDeliveryDatesList) {
        this.itemDeliveryDatesList = itemDeliveryDatesList;
    }

    public ArrayList<String> getShadesList() {
        return shadesList;
    }

    public void setShadesList(ArrayList<String> shadesList) {
        this.shadesList = shadesList;
    }

    public ArrayList<String> getContainerNoList() {
        return containerNoList;
    }

    public void setContainerNoList(ArrayList<String> containerNoList) {
        this.containerNoList = containerNoList;
    }

    public ArrayList<String> getStockQtyList() {
        return stockQtyList;
    }

    public void setStockQtyList(ArrayList<String> stockQtyList) {
        this.stockQtyList = stockQtyList;
    }

    public ArrayList<String> getContainedOrderNoList() {
        return containedOrderNoList;
    }

    public void setContainedOrderNoList(ArrayList<String> containedOrderNoList) {
        this.containedOrderNoList = containedOrderNoList;
    }
}
