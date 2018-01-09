package com.example.anubhav.vacmet.database.daos;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.anubhav.vacmet.model.ItemModel;
import com.example.anubhav.vacmet.model.OrderModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anubhav-Singh on 06-01-2018.
 */

@Dao
public interface DatabaseRequestsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrders(List<OrderModel> orderModels);
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertItems(List<ItemModel> itemModels);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateOrders(OrderModel... ordersToUpdate);
    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateItems(ItemModel... itemsToUpdate);


    @Query("SELECT * FROM vacmet_orders WHERE sapId LIKE :sapId AND isPending = :orderType")
    ArrayList<OrderModel> getOrdersForSapIdAndOrderType(String sapId, int orderType);
    @Query("SELECT * FROM vacmet_item_for_orders WHERE selectedOrderNo LIKE :orderNo")
    ArrayList<ItemModel> getItemsForOrderId(String orderNo);

}
