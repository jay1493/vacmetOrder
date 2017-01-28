package com.example.anubhav.trial3;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.example.anubhav.trial3.adapters.ListOrderInformation;
import com.example.anubhav.trial3.model.ItemModel;
import com.example.anubhav.trial3.model.OrderModel;

import java.util.ArrayList;

/**
 * Created by anubhav on 27/1/17.
 */

public class OrderInformation extends AppCompatActivity {
    private OrderModel orderModel;
    private TextView partyName,orderNo,orderDate,orderQty,requestDispatchDate,
    despQty,prodQty;
    private ArrayList<ItemModel> itemList;
    private ListView itemsListView;
    private ListOrderInformation listAdapter;
    private Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_information);
        init();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.back_24dp));
        if(getIntent()!=null && getIntent().getExtras()!=null && getIntent().getExtras().getSerializable("OrderInfo")!=null){
            orderModel = (OrderModel) getIntent().getExtras().getSerializable("OrderInfo");
            partyName.setText(orderModel.getPartyName());
            orderNo.setText(orderModel.getOrderNo());
            orderDate.setText(orderModel.getOrderDate());
            requestDispatchDate.setText(orderModel.getDeliveryDate());
            orderQty.setText(orderModel.getOrderQty());
            despQty.setText(orderModel.getDespQty());
            prodQty.setText(orderModel.getInProdQty());
            itemList = orderModel.getItemModelArrayList();
            getSupportActionBar().setWindowTitle(orderModel.getPartyName());
            getSupportActionBar().setTitle(orderModel.getPartyName());
            toolbar.setTitle(orderModel.getPartyName());
        }else{
            partyName.setText("");
            orderNo.setText("");
            orderDate.setText("");
            requestDispatchDate.setText("");
            orderQty.setText("");
            despQty.setText("");
            prodQty.setText("");
            itemList = null;
        }

       if(itemList!=null){
           listAdapter = new ListOrderInformation(this,itemList);
           itemsListView.setAdapter(listAdapter);
       }
    }

    private void init() {
        partyName = (TextView) findViewById(R.id.orderInfo_partyName);
        orderNo = (TextView) findViewById(R.id.orderInfo_orderNo);
        orderDate = (TextView) findViewById(R.id.orderInfo_orderDate);
        requestDispatchDate = (TextView) findViewById(R.id.orderInfo_rdd);
        orderQty = (TextView) findViewById(R.id.orderInfo_orderQty);
        despQty = (TextView) findViewById(R.id.orderInfo_despQty);
        prodQty = (TextView) findViewById(R.id.orderInfo_prodQty);
        itemsListView = (ListView) findViewById(R.id.list_orderInformation);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
