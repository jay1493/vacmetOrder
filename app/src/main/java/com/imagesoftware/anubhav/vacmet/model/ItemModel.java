package com.imagesoftware.anubhav.vacmet.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.text.TextUtils;

import org.w3c.dom.Text;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by anubhav on 27/1/17.
 */


public class ItemModel implements Serializable {
    private int itemNo;
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
    private int totalDespQtyList;
    private int totalOrderedQtyList;

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

    public int getItemNo() {
        return itemNo;
    }

    public void setItemNo(int itemNo) {
        this.itemNo = itemNo;
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
        if(!TextUtils.isEmpty(despQty)) {
            DecimalFormat decimalFormat = new DecimalFormat("#.##");
            despQty = decimalFormat.format(Double.parseDouble(despQty));
            if (totalDespQty != null) {
                totalDespQty.add(Double.valueOf(despQty));
            }
            this.despQty = despQty;
        }
    }

    public String getTotalQty() {
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        totalQty = String.valueOf(decimalFormat.format(Double.parseDouble(getDespQty())+Double.parseDouble(getInProdQty())));
        return totalQty;
    }

    public void setTotalQty(String totalQty) {
        if(!TextUtils.isEmpty(totalQty)) {
            DecimalFormat decimalFormat = new DecimalFormat("#.##");
            totalQty = decimalFormat.format(Double.parseDouble(totalQty));
            this.totalQty = totalQty;
        }
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
        if(!TextUtils.isEmpty(inProdQty)) {
            DecimalFormat decimalFormat = new DecimalFormat("#.##");
            inProdQty = decimalFormat.format(Double.parseDouble(inProdQty));
            if (totalInprodQty != null) {
                totalInprodQty.add(Double.valueOf(inProdQty));
            }
            this.inProdQty = inProdQty;
        }

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
        if(!TextUtils.isEmpty(length) && length.contains(".")){
            length = length.replaceAll(Pattern.quote("."),"");
        }
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
        if(!TextUtils.isEmpty(width) && width.contains(".")){
            width = width.replaceAll(Pattern.quote("."),"");
        }
        if(!TextUtils.isEmpty(width) && width.length() > 4){
            width = width.substring(0,width.length()-4);
        }
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
        if(!TextUtils.isEmpty(orderedQty)) {
            DecimalFormat decimalFormat = new DecimalFormat("#.##");
            orderedQty = decimalFormat.format(Double.parseDouble(orderedQty));
            if (totalOrderedQty != null) {
                totalOrderedQty.add(Double.valueOf(orderedQty));
            }
            this.orderedQty = orderedQty;
        }
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
        if(!TextUtils.isEmpty(stockQty)) {
            DecimalFormat decimalFormat = new DecimalFormat("#.##");
            stockQty = decimalFormat.format(Double.parseDouble(stockQty));
            if (stockQtyList != null) {
                stockQtyList.add(stockQty);
            }
            this.stockQty = stockQty;
        }
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

    public void setLengthList(ArrayList<String> lengthList) {
        this.lengthList = lengthList;
    }

    public void setWidthList(ArrayList<String> widthList) {
        this.widthList = widthList;
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

    public void setTotalOrderedQty(ArrayList<Double> totalOrderedQty) {
        this.totalOrderedQty = totalOrderedQty;
    }

    public void setTreatment1List(ArrayList<String> treatment1List) {
        this.treatment1List = treatment1List;
    }

    public void setTreatment2List(ArrayList<String> treatment2List) {
        this.treatment2List = treatment2List;
    }

    public void setItemDeliveryDatesList(ArrayList<String> itemDeliveryDatesList) {
        this.itemDeliveryDatesList = itemDeliveryDatesList;
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

    public void setStockQtyList(ArrayList<String> stockQtyList) {
        this.stockQtyList = stockQtyList;
    }

    public void setContainedOrderNoList(ArrayList<String> containedOrderNoList) {
        this.containedOrderNoList = containedOrderNoList;
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

    public List<Double> getTotalDespQtyList() {
        return totalDespQty;
    }

    public List<Double>  getTotalOrderedQtyList() {
        return totalOrderedQty;
    }
}
