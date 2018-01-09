package com.imagesoftware.anubhav.vacmet.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.imagesoftware.anubhav.vacmet.model.ItemModel;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by anubhav on 3/10/17.
 */

public class CustomExpandableListAdapter extends BaseExpandableListAdapter {

    private DecimalFormat decimalFormat;
    private Context context;
    private List<ItemModel> itemModelArrayList;

    public CustomExpandableListAdapter(Context context, List<ItemModel> itemModelArrayList) {
        this.context = context;
        this.itemModelArrayList = itemModelArrayList;
        decimalFormat = new DecimalFormat("#.##");
    }

    @Override
    public int getGroupCount() {
        return itemModelArrayList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if(itemModelArrayList.get(groupPosition).getLengthList()!=null || itemModelArrayList.get(groupPosition).getWidthList()!=null){
            if(itemModelArrayList.get(groupPosition).getLengthList()!=null && itemModelArrayList.get(groupPosition).getWidthList()!=null){
                if(itemModelArrayList.get(groupPosition).getLengthList().size() >= itemModelArrayList.get(groupPosition).getWidthList().size()){
                    return itemModelArrayList.get(groupPosition).getLengthList().size();
                }else{
                    return itemModelArrayList.get(groupPosition).getWidthList().size();
                }
            }
            if(itemModelArrayList.get(groupPosition).getWidthList() == null){
                return itemModelArrayList.get(groupPosition).getLengthList().size();
            }else{
                return itemModelArrayList.get(groupPosition).getWidthList().size();
            }
        }
        return 0;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return itemModelArrayList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        return null;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        return null;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    private class GroupHolder{
        LinearLayout mainLayout;
        LinearLayout expandedItemLayout;
        TextView itemName;
        TextView despQty;
        TextView itemStatus;
        TextView itemNo;
        TextView itemBillNo;
        TextView itemBillDate;

    }
}
