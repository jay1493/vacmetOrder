package com.example.anubhav.trial3.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.anubhav.trial3.R;
import com.example.anubhav.trial3.model.ItemModel;

import java.util.ArrayList;

/**
 * Created by anubhav on 27/1/17.
 */

public class ListOrderInformation extends BaseAdapter {

    private Context context;
    private ArrayList<ItemModel> itemModelArrayList;
    private ListHolder holder;

    public ListOrderInformation(Context context, ArrayList<ItemModel> itemModelArrayList) {
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
            view.setTag(holder);
        }else{
            holder = (ListHolder) view.getTag();
        }
        holder.itemName.setText(((ItemModel)getItem(i)).getItemName());
        holder.despQty.setText(((ItemModel)getItem(i)).getDespQty()+" wt/"+((ItemModel)getItem(i)).getTotalQty()+" wt");
        return view;
    }

   private class ListHolder{
       TextView itemName;
       TextView despQty;
    }
}
