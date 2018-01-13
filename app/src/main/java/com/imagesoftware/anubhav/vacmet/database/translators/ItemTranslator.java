package com.imagesoftware.anubhav.vacmet.database.translators;

import android.text.TextUtils;

import com.imagesoftware.anubhav.vacmet.database.entities.ItemEntity;
import com.imagesoftware.anubhav.vacmet.database.entities.OrderEntity;
import com.imagesoftware.anubhav.vacmet.model.ItemModel;
import com.imagesoftware.anubhav.vacmet.model.OrderModel;

import java.util.ArrayList;

/**
 * Created by Anubhav-Singh on 12-01-2018.
 */

public class ItemTranslator {

    public ItemEntity translateEntityFromModel(ItemModel itemModel){
        ItemEntity itemEntity = null;
        if(itemModel!=null){
            itemEntity = new ItemEntity();
            if(!TextUtils.isEmpty(itemModel.getBillDate())) {
                itemEntity.setBillDate(itemModel.getBillDate());
            }
            if(!TextUtils.isEmpty(itemModel.getBillNo())) {
                itemEntity.setBillNo(itemModel.getBillNo());
            }
            if(!TextUtils.isEmpty(itemModel.getContainedOrderNo())) {
                itemEntity.setContainedOrderNo(itemModel.getContainedOrderNo());
            }

            if(itemModel.getContainedOrderNoList()!=null) {
                itemEntity.setContainedOrderNoList(new ArrayList<String>(itemModel.getContainedOrderNoList()));
            }
            itemEntity.setContainerNo(itemModel.getContainerNo());
            itemEntity.setContainerNoList(itemModel.getContainerNoList());
            itemEntity.setDeliveryDate(itemModel.getDeliveryDate());
            itemEntity.setDespQty(itemModel.getDespQty());
            itemEntity.setInProdQty(itemModel.getInProdQty());
            itemEntity.setInvoiceDate(itemModel.getInvoiceDate());
            itemEntity.setInvoiceNo(itemModel.getInvoiceNo());
            if(itemModel.getItemDeliveryDatesList()!=null) {
                itemEntity.setItemDeliveryDatesList(new ArrayList<String>(itemModel.getItemDeliveryDatesList()));
            }
            itemEntity.setItemName(itemModel.getItemName());
            itemEntity.setLength(itemModel.getLength());
            itemEntity.setItemNo(itemModel.getItemNo());
            if(itemModel.getLengthList()!=null) {
                itemEntity.setLengthList(new ArrayList<String>(itemModel.getLengthList()));
            }
            itemEntity.setMaterialNo(itemModel.getMaterialNo());
            itemEntity.setOrderDate(itemModel.getOrderDate());
            itemEntity.setOrderedQty(itemModel.getOrderedQty());
            itemEntity.setSelectedOrderNo(itemModel.getSelectedOrderNo());
            itemEntity.setShades(itemModel.getShades());
            if(itemModel.getShadesList()!=null) {
                itemEntity.setShadesList(new ArrayList<String>(itemModel.getShadesList()));
            }
            itemEntity.setStatus(itemModel.getStatus());
            itemEntity.setStockQty(itemModel.getStockQty());
            if(itemModel.getStockQtyList()!=null) {
                itemEntity.setStockQtyList(new ArrayList<String>(itemModel.getStockQtyList()));
            }
            if(itemModel.getTotalDespQtyList()!=null) {
                itemEntity.setTotalDespQty(new ArrayList<Double>(itemModel.getTotalDespQtyList()));
            }
            if(itemModel.getTotalInprodQty()!=null) {
                itemEntity.setTotalInprodQty(new ArrayList<Double>(itemModel.getTotalInprodQty()));
            }
            if(itemModel.getTotalOrderedQtyList()!=null) {
                itemEntity.setTotalOrderedQty(new ArrayList<Double>(itemModel.getTotalOrderedQtyList()));
            }
            itemEntity.setTotalQty(itemModel.getTotalQty());
            itemEntity.setTreatment1(itemModel.getTreatment1());
            if(itemModel.getTreatment1List()!=null) {
                itemEntity.setTreatment1List(new ArrayList<String>(itemModel.getTreatment1List()));
            }
            itemEntity.setTreatment2(itemModel.getTreatment2());
            if(itemModel.getTreatment2List()!=null) {
                itemEntity.setTreatment2List(new ArrayList<String>(itemModel.getTreatment2List()));
            }
            itemEntity.setWidth(itemModel.getWidth());
            if(itemModel.getWidthList()!=null) {
                itemEntity.setWidthList(new ArrayList<String>(itemModel.getWidthList()));
            }
        }
        return itemEntity;
    }

    public ItemModel translateModelFromEntity(ItemEntity itemEntity){
        ItemModel itemModel = null;
        if(itemEntity!=null){
            itemModel = new ItemModel();
            itemModel.setItemNo(itemEntity.getItemNo());
            itemModel.setBillDate(itemEntity.getBillDate());
            itemModel.setBillNo(itemEntity.getBillNo());
            itemModel.setContainedOrderNo(itemEntity.getBillNo());
            if(itemEntity.getContainedOrderNoList()!=null && itemEntity.getContainedOrderNoList().size()>0 && itemEntity.getContainedOrderNoList().size()>0) {
                itemModel.setContainedOrderNoList(new ArrayList<String>(itemEntity.getContainedOrderNoList()));
            }
            itemModel.setContainerNo(itemEntity.getContainerNo());
            itemModel.setContainerNoList(itemEntity.getContainerNoList());
            itemModel.setDeliveryDate(itemEntity.getDeliveryDate());
            itemModel.setDespQty(itemEntity.getDespQty());
            itemModel.setInProdQty(itemEntity.getInProdQty());
            itemModel.setInvoiceDate(itemEntity.getInvoiceDate());
            itemModel.setInvoiceNo(itemEntity.getInvoiceNo());
            if(itemEntity.getItemDeliveryDatesList()!=null && itemEntity.getItemDeliveryDatesList().size()>0 &&  itemEntity.getItemDeliveryDatesList().size()>0) {
                itemModel.setItemDeliveryDatesList(new ArrayList<String>(itemEntity.getItemDeliveryDatesList()));
            }
            itemModel.setItemName(itemEntity.getItemName());
            itemModel.setLength(itemEntity.getLength());
            if(itemEntity.getLengthList()!=null && itemEntity.getLengthList().size()>0  &&  itemEntity.getLengthList().size()>0) {
                itemModel.setLengthList(new ArrayList<String>(itemEntity.getLengthList()));
            }
            itemModel.setMaterialNo(itemEntity.getMaterialNo());
            itemModel.setOrderDate(itemEntity.getOrderDate());
            itemModel.setOrderedQty(itemEntity.getOrderedQty());
            itemModel.setSelectedOrderNo(itemEntity.getSelectedOrderNo());
            itemModel.setShades(itemEntity.getShades());
            if(itemEntity.getShadesList()!=null && itemEntity.getShadesList().size()>0  && itemEntity.getShadesList().size()>0) {
                itemModel.setShadesList(new ArrayList<String>(itemEntity.getShadesList()));
            }
            itemModel.setStatus(itemEntity.getStatus());
            itemModel.setStockQty(itemEntity.getStockQty());
            if(itemEntity.getStockQtyList()!=null && itemEntity.getStockQtyList().size()>0  &&   itemEntity.getStockQtyList().size()>0) {
                itemModel.setStockQtyList(new ArrayList<String>(itemEntity.getStockQtyList()));
            }
            if(itemEntity.getTotalDespQty()!=null && itemEntity.getTotalDespQty().size()>0  &&  itemEntity.getTotalDespQty().size()>0) {
                itemModel.setTotalDespQty(new ArrayList<Double>(itemEntity.getTotalDespQty()));
            }
            if(itemEntity.getTotalInprodQty()!=null && itemEntity.getTotalInprodQty().size()>0  && itemEntity.getTotalInprodQty().size()>0) {
                itemModel.setTotalInprodQty(new ArrayList<Double>(itemEntity.getTotalInprodQty()));
            }
            if(itemEntity.getTotalOrderedQty()!=null && itemEntity.getTotalOrderedQty().size()>0  && itemEntity.getTotalOrderedQty().size()>0) {
                itemModel.setTotalOrderedQty(new ArrayList<Double>(itemEntity.getTotalOrderedQty()));
            }
            itemModel.setTotalQty(itemEntity.getTotalQty());
            itemModel.setTreatment1(itemEntity.getTreatment1());
            if(itemEntity.getTreatment1List()!=null && itemEntity.getTreatment1List().size()>0  && itemEntity.getTreatment1List().size()>0) {
                itemModel.setTreatment1List(new ArrayList<String>(itemEntity.getTreatment1List()));
            }
            itemModel.setTreatment2(itemEntity.getTreatment2());
            if(itemEntity.getTreatment2List()!=null && itemEntity.getTreatment2List().size()>0  && itemEntity.getTreatment2List().size()>0) {
                itemModel.setTreatment2List(new ArrayList<String>(itemEntity.getTreatment2List()));
            }
            itemModel.setWidth(itemEntity.getWidth());
            if(itemEntity.getWidthList()!=null && itemEntity.getWidthList().size()>0  && itemEntity.getWidthList().size()>0) {
                itemModel.setWidthList(new ArrayList<String>(itemEntity.getWidthList()));
            }
        }
        return itemModel;
    }

}
