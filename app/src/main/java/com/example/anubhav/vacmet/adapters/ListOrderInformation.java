package com.example.anubhav.vacmet.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.anubhav.vacmet.R;
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

    public ListOrderInformation(Context context, List<ItemModel> itemModelArrayList) {
        this.context = context;
        this.itemModelArrayList = itemModelArrayList;
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
    public View getView(int i, View view, ViewGroup viewGroup) {

        if(view == null){
            holder = new ListHolder();
            view = LayoutInflater.from(context).inflate(R.layout.order_information_row,null);
            holder.itemName = (TextView) view.findViewById(R.id.orderInfo_itemName);
            holder.despQty = (TextView) view.findViewById(R.id.orderInfo_itemQtyStatus);
            holder.mainLayout = (LinearLayout) view.findViewById(R.id.main_layout_order_info);
            view.setTag(holder);
        }else{
            holder = (ListHolder) view.getTag();
        }
        if(((ItemModel)getItem(i)).getStatus().equalsIgnoreCase(context.getString(R.string.closed))){
            holder.mainLayout.setBackgroundColor(context.getResources().getColor(R.color.red_transperant));
        }else{
            holder.mainLayout.setBackgroundColor(context.getResources().getColor(R.color.white));
        }
        holder.itemName.setText(((ItemModel)getItem(i)).getItemName());
        holder.despQty.setText(((ItemModel)getItem(i)).getDespQty()+"/"+((ItemModel)getItem(i)).getTotalQty());
        return view;
    }

   private class ListHolder{
       LinearLayout mainLayout;
       TextView itemName;
       TextView despQty;
    }
}
