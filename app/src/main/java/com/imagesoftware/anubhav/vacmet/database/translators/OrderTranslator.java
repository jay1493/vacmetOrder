package com.imagesoftware.anubhav.vacmet.database.translators;

import com.imagesoftware.anubhav.vacmet.database.entities.ItemEntity;
import com.imagesoftware.anubhav.vacmet.database.entities.OrderEntity;
import com.imagesoftware.anubhav.vacmet.model.ItemModel;
import com.imagesoftware.anubhav.vacmet.model.OrderModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anubhav-Singh on 12-01-2018.
 */

public class OrderTranslator {

    private ItemTranslator itemTranslator;

    public OrderEntity translateModelToEntity(OrderModel orderModel){
        OrderEntity orderEntity = null;
        if(orderModel!=null){
            List<ItemEntity> itemEntities = new ArrayList<>();
            itemTranslator = new ItemTranslator();
            orderEntity = new OrderEntity();
            orderEntity.setOrderId(orderModel.getOrderId());
            orderEntity.setDeliveryDate(orderModel.getDeliveryDate());
            orderEntity.setDespQty(orderModel.getDespQty());
            orderEntity.setInProdQty(orderModel.getInProdQty());
            orderEntity.setInvoiceDate(orderModel.getInvoiceDate());
            orderEntity.setInvoiceNo(orderModel.getInvoiceNo());
            orderEntity.setIsPending(orderModel.getIsPending()?1:0);
            orderEntity.setOrderDate(orderModel.getOrderDate());
            orderEntity.setOrderNo(orderModel.getOrderNo());
            orderEntity.setOrderQty(orderModel.getOrderQty());
            orderEntity.setPartyName(orderModel.getPartyName());
            orderEntity.setSapId(orderModel.getSapId());
            orderEntity.setStatus(orderModel.getStatus());
            orderEntity.setStockQty(orderModel.getStockQty());
            for(ItemModel itemModel : orderModel.getItemList()){
                itemEntities.add(itemTranslator.translateEntityFromModel(itemModel));
            }
            orderEntity.setItemModelArrayList(new ArrayList<ItemEntity>(itemEntities));
        }
        return orderEntity;
    }

    public OrderModel translateEntityToModel(OrderEntity orderEntity){
        OrderModel orderModel = null;
        if(orderEntity!=null){
            orderModel = new OrderModel();
            orderModel.setOrderId(orderEntity.getOrderId());
            orderModel.setDeliveryDate(orderEntity.getDeliveryDate());
            orderModel.setDespQty(orderEntity.getDespQty());
            orderModel.setInProdQty(orderEntity.getInProdQty());
            orderModel.setInvoiceDate(orderEntity.getInvoiceDate());
            orderModel.setInvoiceNo(orderEntity.getInvoiceNo());
            orderModel.setIsPending(orderEntity.getIsPending() == 1);
            orderModel.setOrderDate(orderEntity.getOrderDate());
            orderModel.setOrderNo(orderEntity.getOrderNo());
            orderModel.setOrderQty(orderEntity.getOrderQty());
            orderModel.setPartyName(orderEntity.getPartyName());
            orderModel.setSapId(orderEntity.getSapId());
            orderModel.setStatus(orderEntity.getStatus());
            orderModel.setStockQty(orderEntity.getStockQty());
        }
        return orderModel;
    }

}
