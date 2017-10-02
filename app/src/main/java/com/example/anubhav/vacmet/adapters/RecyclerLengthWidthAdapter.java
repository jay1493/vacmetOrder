package com.example.anubhav.vacmet.adapters;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.anubhav.vacmet.CustomView.CustomViewPager;
import com.example.anubhav.vacmet.R;

import java.util.List;

/**
 * Created by anubhav on 26/9/17.
 */

public class RecyclerLengthWidthAdapter extends RecyclerView.Adapter<RecyclerLengthWidthAdapter.CustomHolder> {

    private Context context;
    private List<String> lengthList;
    private List<String> widthList;
    private List<Double> despOrders;
    private List<Double> totalOrderedItemsList;
    private List<String> itemDeliveryDatesList;
    private List<String> stockList;
    private List<String> treatment1List;
    private List<String> treatment2List;
    private List<String> shadesList;
    private List<Double> inProdList;
    private HorizontalRecyclerView itemDetailsViewPagerAdapter;

    public RecyclerLengthWidthAdapter(Context context, List<String> lengthList, List<String> widthList, List<Double> list, List<Double> orderedList, List<String> itemDeliveryDatesList, List<String> stockQtyList, List<String> treatment1List, List<String> treatment2List, List<String> shadesList, List<Double> inProdList) {
        this.context = context;
        this.lengthList = lengthList;
        this.widthList = widthList;
        this.despOrders = list;
        this.totalOrderedItemsList = orderedList;
        this.itemDeliveryDatesList = itemDeliveryDatesList;
        this.stockList = stockQtyList;
        this.treatment1List = treatment1List;
        this.treatment2List = treatment2List;
        this.shadesList = shadesList;
        this.inProdList = inProdList;

    }

    @Override
    public CustomHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new CustomHolder(LayoutInflater.from(context).inflate(R.layout.length_width_recycler_item,null));
    }

    @Override
    public void onBindViewHolder(CustomHolder customHolder, int i) {

    /*
       itemDetailsViewPagerAdapter = new HorizontalRecyclerView(context,(lengthList!=null && lengthList.size()>0 && i<=lengthList.size()-1 && lengthList.get(i)!=null)?lengthList.get(i):null,
               (widthList!=null && widthList.size()>0 && i<=widthList.size()-1 && widthList.get(i)!=null)?widthList.get(i):null,
               (despOrders!=null && despOrders.size()>0 && i<=despOrders.size()-1 && despOrders.get(i)!=null)?despOrders.get(i):null,
               (totalOrderedItemsList !=null && totalOrderedItemsList.size()>0 && i<= totalOrderedItemsList.size()-1 && totalOrderedItemsList.get(i)!=null)?totalOrderedItemsList.get(i):null,
               (itemDeliveryDatesList !=null && itemDeliveryDatesList.size()>0 && i<= itemDeliveryDatesList.size()-1 && itemDeliveryDatesList.get(i)!=null)?itemDeliveryDatesList.get(i):null,
               (stockList !=null && stockList.size()>0 && i<= stockList.size()-1 && stockList.get(i)!=null)?stockList.get(i):null,
               (treatment1List !=null && treatment1List.size()>0 && i<= treatment1List.size()-1 && treatment1List.get(i)!=null)?treatment1List.get(i):null,
               (treatment2List !=null && treatment2List.size()>0 && i<= treatment2List.size()-1 && treatment2List.get(i)!=null)?treatment2List.get(i):null,
               (shadesList !=null && shadesList.size()>0 && i<= shadesList.size()-1 && shadesList.get(i)!=null)?shadesList.get(i):null,
               (inProdList !=null && inProdList.size()>0 && i<= inProdList.size()-1 && inProdList.get(i)!=null)?inProdList.get(i):null);

       *//* customHolder.viewPager.setItemDetailsViewPagerAdapter(itemDetailsViewPagerAdapter);
        customHolder.viewPager.setAdapter(itemDetailsViewPagerAdapter);
        customHolder.viewPager.setCurrentItem(0,true);*//*
       customHolder.viewPager.setAdapter(itemDetailsViewPagerAdapter);*/





        customHolder.length.setText("0");
        customHolder.width.setText("0");
        customHolder.orderQty.setText("0/0");


        customHolder.stockQty.setText("0");
        if(itemDeliveryDatesList!=null && itemDeliveryDatesList.size()>0 && i<=itemDeliveryDatesList.size()-1){
            customHolder.deliveryDate.setText(itemDeliveryDatesList.get(i));
        }
        if(stockList!=null && stockList.size()>0 && i<=stockList.size()-1){
            customHolder.stockQty.setText(stockList.get(i));
        }
        if((treatment1List!=null && treatment1List.size()>0 && i<=treatment1List.size()-1 && !TextUtils.isEmpty(treatment1List.get(i))) || (treatment2List!=null && treatment2List.size()>0 && i<=treatment2List.size()-1 && !TextUtils.isEmpty(treatment2List.get(i)))){
            if(treatment1List!=null && treatment1List.size()>0 && i<=treatment1List.size()-1){
                customHolder.treatment1.setText(treatment1List.get(i));
            }
            if(treatment2List!=null && treatment2List.size()>0 && i<=treatment2List.size()-1){
                customHolder.treatment2.setText(treatment2List.get(i));
            }
        }else{
            customHolder.treatment1.setText("NONE");
            customHolder.treatment2.setText("NONE");
        }


        if(lengthList!=null && !TextUtils.isEmpty(lengthList.get(i))) {
            customHolder.length.setText(lengthList.get(i));
        }
        if(widthList!=null && !TextUtils.isEmpty(widthList.get(i))){
            customHolder.width.setText(widthList.get(i));
        }

        if((despOrders!=null && despOrders.size()>0 && i<=despOrders.size()-1 && despOrders.get(i)!=null) && (totalOrderedItemsList !=null && totalOrderedItemsList.size()>0 && i<= totalOrderedItemsList.size()-1 && totalOrderedItemsList.get(i)!=null)){
            customHolder.orderQty.setText(String.valueOf(despOrders.get(i))+"/"+totalOrderedItemsList.get(i));
        }
    }

    @Override
    public int getItemCount() {
        if(lengthList!=null || widthList!=null){
            if(lengthList!=null && widthList!=null){
                if(lengthList.size() >= widthList.size()){
                    return lengthList.size();
                }else{
                    return widthList.size();
                }
            }
            if(widthList == null){
                return lengthList.size();
            }else{
                return widthList.size();
            }
        }
        return 0;
    }


    class CustomHolder extends RecyclerView.ViewHolder {

        private TextView length;
        private TextView width;
        private TextView orderQty;
        private TextView deliveryDate;
        private TextView stockQty;
        private TextView treatment1;
        private TextView treatment2;
//        private LinearLayout llTreatments;
        private RecyclerView viewPager;

        public CustomHolder(View itemView) {
            super(itemView);

            length = (TextView) itemView.findViewById(R.id.txt_length);
            width = (TextView) itemView.findViewById(R.id.txt_width);
            orderQty = (TextView) itemView.findViewById(R.id.txt_orderQty);
            deliveryDate = (TextView) itemView.findViewById(R.id.txt_item_delivery_date);
            stockQty = (TextView) itemView.findViewById(R.id.txt_item_stock);
            treatment1 = (TextView) itemView.findViewById(R.id.txt_treatment1);
            treatment2 = (TextView) itemView.findViewById(R.id.txt_treatment2);
//            llTreatments = (LinearLayout) itemView.findViewById(R.id.ll_treatments);

/*//            viewPager = (CustomViewPager) itemView.findViewById(R.id.itemDetailsViewPager);
            viewPager = (RecyclerView) itemView.findViewById(R.id.itemDetailsViewPager);
            viewPager.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false));*/


        }
    }
}
