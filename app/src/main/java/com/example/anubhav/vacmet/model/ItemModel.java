package com.example.anubhav.vacmet.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Created by anubhav on 27/1/17.
 */

@Entity(tableName = "vacmet_item_for_orders",indices = {@Index(value = "selectedOrderNo")})
public class ItemModel implements Serializable {
    @ForeignKey(entity = OrderModel.class,parentColumns = "orderNo",childColumns = "selectedOrderNo", onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE)
    private String selectedOrderNo;
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
    private String containedOrderNo;
    private String treatment1;
    private String treatment2;
    private String shades;
    private String containerNo;
    private String orderDate;
    private String deliveryDate;
    private String invoiceDate;
    private String invoiceNo;
    private ArrayList<String> lengthList;
    private ArrayList<String> widthList;
    private ArrayList<Double> totalDespQty;
    private ArrayList<Double> totalInprodQty;
    private ArrayList<Double> totalOrderedQty;
    private ArrayList<String> treatment1List;
    private ArrayList<String> treatment2List;
    private ArrayList<String> itemDeliveryDatesList;
    private ArrayList<String> shadesList;
    private ArrayList<String> containerNoList;
    private ArrayList<String> stockQtyList;
    private ArrayList<String> containedOrderNoList;

    public ItemModel() {
        lengthList = new ArrayList<>();
        widthList = new ArrayList<>();
        totalDespQty = new ArrayList<>();
        totalInprodQty = new ArrayList<>();
        totalOrderedQty = new ArrayList<>();
        treatment1List = new ArrayList<>();
        treatment2List = new ArrayList<>();
        itemDeliveryDatesList = new ArrayList<>();
        shadesList = new ArrayList<>();
        containerNoList = new ArrayList<>();
        stockQtyList = new ArrayList<>();
        containedOrderNoList = new ArrayList<>();
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
        if(totalDespQty!=null) {
            totalDespQty.add(Double.valueOf(despQty));
        }
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
        if(totalInprodQty!=null){
            totalInprodQty.add(Double.valueOf(inProdQty));
        }
        this.inProdQty = inProdQty;

    }
    public List<Double> getDespList(){
        return totalDespQty;
    }
    public List<Double> getInProdList(){
        return totalInprodQty;
    }
    public List<Double> getOrderedList(){
        return totalOrderedQty;
    }
    public List<String> getStockQtyList(){
        return stockQtyList;
    }
    public List<String> getItemDeliveryDatesList(){
        return itemDeliveryDatesList;
    }
    public List<String> getTreatment1List(){
        return treatment1List;
    }
    public List<String> getTreatment2List(){
        return treatment2List;
    }
    public List<String> getShadesList(){
        return shadesList;
    }
    public List<String> getContainedOrderNoList(){
        return containedOrderNoList;
    }
    public String getTotalDespQty(){
        if(totalDespQty!=null){
            double sum = 0;
            for(double d: totalDespQty){
                sum += d;
            }
            return String.valueOf(sum);
        }
        return "0";
    }

    public String getTotalInProdQty(){
        if(totalInprodQty!=null){
            double sum = 0;
            for(double d: totalInprodQty){
                sum += d;
            }
            return String.valueOf(sum);
        }
        return "0";
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
        /*DecimalFormat decimalFormat = new DecimalFormat("#.##");
        length = decimalFormat.format(Double.parseDouble(length));*/
//        length = NumberFormat.getInstance().format(length);
        /*if(length.contains(",")){
            length.replace(",","");
        }
        int w = (int) Double.parseDouble(length);*/
        if(lengthList!=null){
            lengthList.add(length);
        }
        this.length = length;
        return this;
    }

    public String getWidth() {
        return width;
    }

    public List<String> getLengthList() {
        return lengthList;
    }

    public List<String> getWidthList() {
        return widthList;
    }

    public ItemModel setWidth(String width) {
        /*DecimalFormat decimalFormat = new DecimalFormat("#.##");
        width = decimalFormat.format(Double.parseDouble(width));*/
//        width = NumberFormat.getInstance().format(width);
/*        if(width.contains(",")){
            width.replace(",","");
        }
        int w = (int) Double.parseDouble(width);*/
        if(widthList!=null){
            widthList.add(width);
        }
        this.width = width;
        return this;
    }

    public String getTreatment1() {
        return treatment1;
    }

    public ItemModel setTreatment1(String treatment1) {
        if(treatment1List!=null){
            treatment1List.add(treatment1);
        }
        this.treatment1 = treatment1;
        return this;
    }

    public String getTreatment2() {
        return treatment2;
    }

    public ItemModel setTreatment2(String treatment2) {
        if(treatment2List!=null){
            treatment2List.add(treatment2);
        }
        this.treatment2 = treatment2;
        return this;
    }

    public String getShades() {
        return shades;
    }

    public ItemModel setShades(String shades) {
        if(shadesList!=null){
            shadesList.add(shades);
        }
        this.shades = shades;
        return this;
    }

    public String getContainerNo() {
        return containerNo;
    }

    public ItemModel setContainerNo(String containerNo) {
        if(containerNoList!=null){
            containerNoList.add(containerNo);
        }
        this.containerNo = containerNo;
        return this;
    }

    public String getOrderedQty() {
        return orderedQty;
    }

    public ItemModel setOrderedQty(String orderedQty) {
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        orderedQty = decimalFormat.format(Double.parseDouble(orderedQty));
        if(totalOrderedQty!=null){
            totalOrderedQty.add(Double.valueOf(orderedQty));
        }
        this.orderedQty = orderedQty;
        return this;
    }
    public String getTotalOrderedQty(){
        if(totalOrderedQty!=null){
            double sum = 0;
            for(double d: totalOrderedQty){
                sum += d;
            }
            return String.valueOf(sum);
        }
        return "0";
    }

    public String getStockQty() {
        return stockQty;
    }

    public ItemModel setStockQty(String stockQty) {
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        stockQty = decimalFormat.format(Double.parseDouble(stockQty));
        if(stockQtyList!=null){
            stockQtyList.add(stockQty);
        }
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
        if(itemDeliveryDatesList!=null){
            itemDeliveryDatesList.add(deliveryDate);
        }
        this.deliveryDate = deliveryDate;
        return this;
    }

    public String getContainedOrderNo() {
        return containedOrderNo;
    }

    public ItemModel setContainedOrderNo(String containedOrderNo) {
        if(containedOrderNoList!=null){
                containedOrderNoList.add(containedOrderNo);
          }
        this.containedOrderNo = containedOrderNo;
        return this;
    }

    public String getInvoiceDate() {
        return invoiceDate;
    }

    public ItemModel setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
        return this;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public ItemModel setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
        return this;
    }

    public String getSelectedOrderNo() {
        return selectedOrderNo;
    }

    public void setSelectedOrderNo(String selectedOrderNo) {
        this.selectedOrderNo = selectedOrderNo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemModel itemModel = (ItemModel) o;
        return (materialNo == itemModel.materialNo) || (materialNo != null && materialNo.equals(itemModel.materialNo));
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(new Object[]{materialNo});
    }
}
