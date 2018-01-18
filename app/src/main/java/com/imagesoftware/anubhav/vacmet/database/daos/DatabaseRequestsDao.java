package com.imagesoftware.anubhav.vacmet.database.daos;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;


import com.imagesoftware.anubhav.vacmet.database.entities.ItemEntity;
import com.imagesoftware.anubhav.vacmet.database.entities.OrderEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anubhav-Singh on 06-01-2018.
 */

@Dao
public interface DatabaseRequestsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrders(List<OrderEntity> orderEntities);
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertItems(List<ItemEntity> itemEntities);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateOrders(OrderEntity... ordersToUpdate);
    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateItems(ItemEntity... itemsToUpdate);


    @Query("SELECT * FROM vacmet_orders WHERE sapId LIKE :sapId AND isPending = :orderType")
    List<OrderEntity> getOrdersForSapIdAndOrderType(String sapId, int orderType);
    @Query("SELECT * FROM vacmet_item_for_orders WHERE selectedOrderNo LIKE :orderNo")
    List<ItemEntity> getItemsForOrderId(String orderNo);
    @Query("SELECT * FROM vacmet_orders WHERE sapId LIKE :sapId AND isPending = :orderType AND orderNo LIKE :orderNo")
    OrderEntity getOrderForOrderNo(String sapId, int orderType, String orderNo);
    @Query("SELECT * FROM vacmet_orders WHERE sapId LIKE :sapId AND isPending = :orderType AND invoiceNo LIKE :invoiceNo")
    OrderEntity getOrderForInvoiceNo(String sapId, int orderType, String invoiceNo);

    @Query("DELETE FROM vacmet_orders")
    void deleteOrders();
    @Query("DELETE FROM vacmet_item_for_orders")
    void deleteItems();


}
