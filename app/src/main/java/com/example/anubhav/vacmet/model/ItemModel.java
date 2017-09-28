package com.example.anubhav.vacmet.model;

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
    private List<String> lengthList;
    private List<String> widthList;
    private List<Double> totalDespQty;
    private List<Double> totalInprodQty;
    private List<Double> totalOrderedQty;
    private List<String> treatment1List;
    private List<String> treatment2List;
    private List<String> itemDeliveryDatesList;
    private List<String> shadesList;
    private List<String> containerNoList;
    private List<String> stockQtyList;

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
