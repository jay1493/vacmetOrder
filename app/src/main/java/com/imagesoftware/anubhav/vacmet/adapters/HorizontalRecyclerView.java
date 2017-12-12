package com.imagesoftware.anubhav.vacmet.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.imagesoftware.anubhav.vacmet.R;

import java.text.DecimalFormat;

/**
 * Created by anubhav on 1/10/17.
 */

public class HorizontalRecyclerView extends RecyclerView.Adapter<HorizontalRecyclerView.CustomHolder> {

    private DecimalFormat decimalFormat;
    private Context context;
    private String lengthList;
    private String widthList;
    private Double despOrders;
    private Double totalOrderedItemsList;
    private String itemDeliveryDatesList;
    private String stockList;
    private String treatment1List;
    private String treatment2List;
    private String shadesList;
    private Double inProdList;

    private View mCurrentView;

    public HorizontalRecyclerView(Context context, String lengthList, String widthList, Double list, Double orderedList, String itemDeliveryDatesList, String stockQtyList, String treatment1List, String treatment2List, String shadesList, Double inProdList) {

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
        decimalFormat = new DecimalFormat("##.##");
    }

    @Override
    public CustomHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View rootView = null;
        RecyclerView.LayoutParams lp = null;
        switch (i){
            case 0:
                rootView = LayoutInflater.from(context).inflate(R.layout.item_info_layout,null,false);
                rootView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, rootView.getMeasuredHeight());

                rootView.setLayoutParams(lp);
                return new CustomHolder(rootView,i);
            case 1:
                rootView = LayoutInflater.from(context).inflate(R.layout.item_details_layout,null,false);
                rootView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, rootView.getMeasuredHeight());
                rootView.setLayoutParams(lp);
                return new CustomHolder(rootView,i);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(CustomHolder customHolder, int i) {
       switch (i){
           case 0:
               customHolder.length.setText("0");
               customHolder.width.setText("0");
               customHolder.orderQty.setText("0/0");
               if(lengthList!=null && !TextUtils.isEmpty(lengthList)) {
                   customHolder.length.setText(lengthList);
               }
               if(widthList!=null && !TextUtils.isEmpty(widthList)){
                   customHolder.width.setText(widthList);
               }
               if(despOrders!=null && totalOrderedItemsList!=null) {
                   customHolder.orderQty.setText(decimalFormat.format(despOrders) + "/" + decimalFormat.format(totalOrderedItemsList));
               }

               break;
           case 1:
               customHolder.length.setText("0");
               customHolder.width.setText("0");
               customHolder.orderQty.setText("0/0");
               customHolder.stockQty.setText("0");
               if(itemDeliveryDatesList!=null){
                   customHolder.deliveryDate.setText(itemDeliveryDatesList);
               }
               if(stockList!=null ){
                   customHolder.stockQty.setText(stockList);
               }
               if((treatment1List!=null && !TextUtils.isEmpty(treatment1List)) || (treatment2List!=null  && !TextUtils.isEmpty(treatment2List))){
                   customHolder.llTreatments.setVisibility(View.VISIBLE);
                   if(treatment1List!=null){
                       customHolder.treatment1.setText(treatment1List);
                   }
                   if(treatment2List!=null){
                       customHolder.treatment2.setText(treatment2List);
                   }
               }else{
                   customHolder.llTreatments.setVisibility(View.GONE);
               }


               if(lengthList!=null && !TextUtils.isEmpty(lengthList)) {
                   customHolder.length.setText(lengthList);
               }
               if(widthList!=null && !TextUtils.isEmpty(widthList)){
                   customHolder.width.setText(widthList);
               }

               if(despOrders!=null && totalOrderedItemsList!=null) {
                   customHolder.orderQty.setText(decimalFormat.format(despOrders) + "/" + decimalFormat.format(totalOrderedItemsList));
               }

               break;
       }
    }

    @Override
    public int getItemCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        switch (position){
            case 0:
                return 0;
            case 1:
                return 1;
        }
        return -1;
    }

    class CustomHolder extends RecyclerView.ViewHolder{
        private TextView length,width,orderQty;
        private TextView deliveryDate,treatment1,treatment2,stockQty;
        private LinearLayout llTreatments;
        public CustomHolder(View itemView, int viewType) {
            super(itemView);
            switch (viewType){
                case 0:
                    length = (TextView) itemView.findViewById(R.id.txt_length);
                    width = (TextView) itemView.findViewById(R.id.txt_width);
                    orderQty = (TextView) itemView.findViewById(R.id.txt_orderQty);
                    break;
                case 1:
                    length = (TextView) itemView.findViewById(R.id.txt_length);
                    width = (TextView) itemView.findViewById(R.id.txt_width);
                    orderQty = (TextView) itemView.findViewById(R.id.txt_orderQty);
                    deliveryDate = (TextView) itemView.findViewById(R.id.txt_item_delivery_date);
                    stockQty = (TextView) itemView.findViewById(R.id.txt_item_stock);
                    treatment1 = (TextView) itemView.findViewById(R.id.txt_treatment1);
                    treatment2 = (TextView) itemView.findViewById(R.id.txt_treatment2);
                    llTreatments = (LinearLayout) itemView.findViewById(R.id.ll_treatments);
                    break;
            }
        }
    }
}
