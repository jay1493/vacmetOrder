package com.example.anubhav.vacmet.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.anubhav.vacmet.R;
import com.example.anubhav.vacmet.interfaces.OrderDetailsClickListener;
import com.example.anubhav.vacmet.model.ItemModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anubhav on 27/1/17.
 */

public class ListOrderInformation extends BaseAdapter {

    private Context context;
    private List<ItemModel> itemModelArrayList;
    private ListHolder holder;
    private OrderDetailsClickListener orderDetailsClickListener;

    public ListOrderInformation(Context context, List<ItemModel> itemModelArrayList, OrderDetailsClickListener listener) {
        this.context = context;
        this.itemModelArrayList = itemModelArrayList;
        this.orderDetailsClickListener = listener;
    }

    @Override
    public int getCount() {
        return itemModelArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return itemModelArrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        if(view == null){
            holder = new ListHolder();
            view = LayoutInflater.from(context).inflate(R.layout.order_information_row,null);
            holder.itemName = (TextView) view.findViewById(R.id.orderInfo_itemName);
            holder.despQty = (TextView) view.findViewById(R.id.orderInfo_itemQtyStatus);
            holder.mainLayout = (LinearLayout) view.findViewById(R.id.main_layout_order_info);
            holder.itemStatus = (TextView) view.findViewById(R.id.txt_detailsStatus);
            holder.itemNo = (TextView) view.findViewById(R.id.txt_materialNo);
            holder.itemBillNo = (TextView) view.findViewById(R.id.txt_detailsBillNo);
            holder.itemBillDate = (TextView) view.findViewById(R.id.txt_detailsBillDate);
            holder.itemLength = (TextView) view.findViewById(R.id.txt_detailsLength);
            holder.itemWidth = (TextView) view.findViewById(R.id.txt_detailsWidth);
            holder.itemTreatment1 = (TextView) view.findViewById(R.id.txt_detailsTreatment1);
            holder.itemTreatment2 = (TextView) view.findViewById(R.id.txt_detailsTreatment2);
            holder.itemContainerNo = (TextView) view.findViewById(R.id.txt_detailsContainerNo);
            holder.itemShades = (TextView) view.findViewById(R.id.txt_detailsShades);
            view.setTag(holder);
        }else{
            holder = (ListHolder) view.getTag();
        }
        if(((ItemModel)getItem(i)).getStatus().equalsIgnoreCase(context.getString(R.string.closed))){
            holder.mainLayout.setBackgroundColor(context.getResources().getColor(R.color.red_transperant));
        }else{
            holder.mainLayout.setBackgroundColor(context.getResources().getColor(R.color.white));
        }
        final View finalView = view;
        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderDetailsClickListener.onClick(finalView,i);
            }
        });
        holder.itemName.setText(((ItemModel)getItem(i)).getItemName());
        if(!TextUtils.isEmpty(((ItemModel)getItem(i)).getStatus())) {
            holder.itemStatus.setText(((ItemModel) getItem(i)).getStatus());
        }
        holder.itemNo.setText(((ItemModel)getItem(i)).getMaterialNo());
        if(!TextUtils.isEmpty(((ItemModel)getItem(i)).getBillNo())) {
            holder.itemBillNo.setText(((ItemModel) getItem(i)).getBillNo());
        }
        holder.itemBillDate.setText(((ItemModel)getItem(i)).getBillDate());
        if(!TextUtils.isEmpty(((ItemModel)getItem(i)).getLength())) {
            holder.itemLength.setText(((ItemModel) getItem(i)).getLength());
        }
        if(!TextUtils.isEmpty(((ItemModel)getItem(i)).getWidth())) {
            holder.itemWidth.setText(((ItemModel) getItem(i)).getWidth());
        }
        if(!TextUtils.isEmpty(((ItemModel)getItem(i)).getContainerNo())) {
            holder.itemContainerNo.setText(((ItemModel) getItem(i)).getContainerNo());
        }
        holder.itemShades.setText(((ItemModel) getItem(i)).getShades());
        holder.despQty.setText(((ItemModel)getItem(i)).getDespQty()+"/"+((ItemModel)getItem(i)).getTotalQty());
        return view;
    }

   private class ListHolder{
       LinearLayout mainLayout;
       TextView itemName;
       TextView despQty;
       TextView itemStatus;
       TextView itemNo;
       TextView itemLength;
       TextView itemWidth;
       TextView itemBillNo;
       TextView itemBillDate;
       TextView itemTreatment1;
       TextView itemTreatment2;
       TextView itemShades;
       TextView itemContainerNo;
    }
}
