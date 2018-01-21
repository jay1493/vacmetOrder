package com.imagesoftware.anubhav.vacmet.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.imagesoftware.anubhav.vacmet.R;
import com.imagesoftware.anubhav.vacmet.interfaces.OrderDetailsClickListener;
import com.imagesoftware.anubhav.vacmet.model.ItemModel;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by anubhav on 27/1/17.
 */

public class ListOrderInformation extends BaseAdapter {

    private DecimalFormat decimalFormat;
    private Context context;
    private List<ItemModel> itemModelArrayList;
    private ListHolder holder;
    private OrderDetailsClickListener orderDetailsClickListener;
    private RecyclerLengthWidthAdapter recyclerLengthWidthAdapter;
    private boolean isDispatched;

    public ListOrderInformation(Context context, List<ItemModel> itemModelArrayList, OrderDetailsClickListener listener, boolean isDispatched) {
        this.context = context;
        this.itemModelArrayList = itemModelArrayList;
        this.orderDetailsClickListener = listener;
        this.isDispatched = isDispatched;
        decimalFormat = new DecimalFormat("#.##");
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

//        if(view == null){
            holder = new ListHolder();
            view = LayoutInflater.from(context).inflate(R.layout.order_information_row,null);
            holder.itemName = (TextView) view.findViewById(R.id.orderInfo_itemName);
            holder.labelSalesOrder = (TextView) view.findViewById(R.id.label_sales_order_no);
            holder.despQty = (TextView) view.findViewById(R.id.orderInfo_itemQtyStatus);
            holder.mainLayout = (LinearLayout) view.findViewById(R.id.main_layout_order_info);

            holder.itemNo = (TextView) view.findViewById(R.id.txt_materialNo);

            holder.expandedItemLayout = (LinearLayout) view.findViewById(R.id.expanded_item_details);

            holder.tableLayout = (TableLayout) view.findViewById(R.id.table_layout);
//            holder.adaptiveTableLayout = (AdaptiveTableLayout) view.findViewById(R.id.tableLayout);
//            holder.lengthWidthRecycler = (RecyclerView) view.findViewById(R.id.lengthWidthRecycler);
//            holder.lengthWidthRecycler.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false));
            holder.itemTreatment1 = (TextView) view.findViewById(R.id.txt_detailsTreatment1);
            holder.itemTreatment2 = (TextView) view.findViewById(R.id.txt_detailsTreatment2);
            holder.itemContainerNo = (TextView) view.findViewById(R.id.txt_detailsContainerNo);
            holder.itemShades = (TextView) view.findViewById(R.id.txt_detailsShades);
            holder.containerNo = (TextView) view.findViewById(R.id.label_containerNo);
            view.setTag(holder);
        if(!isDispatched){
            holder.containerNo.setVisibility(View.GONE);
            holder.labelSalesOrder.setVisibility(View.GONE);
        }else{
            holder.containerNo.setVisibility(View.VISIBLE);
            holder.labelSalesOrder.setVisibility(View.VISIBLE);
        }
        /*}else{
            holder = (ListHolder) view.getTag();
        }*/

        final View finalView = view;
        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderDetailsClickListener.onClick(finalView,i);
            }
        });
        holder.itemName.setText(((ItemModel)getItem(i)).getItemName());
        /*if(!TextUtils.isEmpty(((ItemModel)getItem(i)).getStatus())) {
            holder.itemStatus.setText(((ItemModel) getItem(i)).getStatus());
        }*/
        holder.itemNo.setText(((ItemModel)getItem(i)).getMaterialNo());


        if((((ItemModel)getItem(i)).getLengthList()!=null && ((ItemModel)getItem(i)).getLengthList().size()>0) ||
                (((ItemModel)getItem(i)).getWidthList()!=null && ((ItemModel)getItem(i)).getWidthList().size()>0)){
            /*holder.lengthWidthRecycler.setVisibility(View.VISIBLE);
            recyclerLengthWidthAdapter = new RecyclerLengthWidthAdapter(context,((ItemModel)getItem(i)).getLengthList(),((ItemModel)getItem(i)).getWidthList() , ((ItemModel)getItem(i)).getDespList() , ((ItemModel)getItem(i)).getOrderedList() ,
                    ((ItemModel)getItem(i)).getItemDeliveryDatesList() , ((ItemModel)getItem(i)).getStockQtyList() , ((ItemModel)getItem(i)).getTreatment1List() , ((ItemModel)getItem(i)).getTreatment2List() , ((ItemModel)getItem(i)).getShadesList() , ((ItemModel)getItem(i)).getInProdList());
            holder.lengthWidthRecycler.setAdapter(recyclerLengthWidthAdapter);*/
            if(holder.tableLayout.getChildCount()>1){
                for(int childPos = 1; childPos<holder.tableLayout.getChildCount(); childPos++){
                    if(holder.tableLayout.getChildAt(childPos)!=null) {
                        holder.tableLayout.removeView(holder.tableLayout.getChildAt(childPos));
                    }
                }
            }
            for(int p=0;p<rowsCount(i);p++){
                View itemView = LayoutInflater.from(context).inflate(R.layout.table_row_layout,null,false);
                TextView length;
                TextView width;
                TextView orderQty;
                TextView deliveryDate;
                TextView stockQty;
                TextView treatment1;
                TextView treatment2;
                TextView salesOrder;
                TextView shades;
                TextView containerNo;
                length = (TextView) itemView.findViewById(R.id.txt_length);
                shades = (TextView) itemView.findViewById(R.id.txt_shades);
                salesOrder = (TextView) itemView.findViewById(R.id.txt_sales_order);
                width = (TextView) itemView.findViewById(R.id.txt_width);
                orderQty = (TextView) itemView.findViewById(R.id.txt_orderQty);
                deliveryDate = (TextView) itemView.findViewById(R.id.txt_item_delivery_date);
                stockQty = (TextView) itemView.findViewById(R.id.txt_item_stock);
                treatment1 = (TextView) itemView.findViewById(R.id.txt_treatment1);
                treatment2 = (TextView) itemView.findViewById(R.id.txt_treatment2);
                containerNo = (TextView) itemView.findViewById(R.id.txt_containerNo);
                length.setText("0");
                width.setText("0");
                orderQty.setText("0/0");
                if(isDispatched){
                    salesOrder.setVisibility(View.VISIBLE);
                    if(((ItemModel)getItem(i)).getContainedOrderNoList()!=null && ((ItemModel)getItem(i)).getContainedOrderNoList().size()>0 && p<=((ItemModel)getItem(i)).getContainedOrderNoList().size()-1){
                        salesOrder.setText(((ItemModel)getItem(i)).getContainedOrderNoList().get(p));
                    }
                    containerNo.setVisibility(View.VISIBLE);
                    if(((ItemModel)getItem(i)).getContainerNoList()!=null && ((ItemModel)getItem(i)).getContainerNoList().size()>0 && p<=((ItemModel)getItem(i)).getContainerNoList().size()-1){
                        containerNo.setText(((ItemModel)getItem(i)).getContainerNoList().get(p));
                    }
                }else{
                    containerNo.setVisibility(View.GONE);
                    salesOrder.setVisibility(View.GONE);
                }


                stockQty.setText("0");
                if(((ItemModel)getItem(i)).getItemDeliveryDatesList()!=null && ((ItemModel)getItem(i)).getItemDeliveryDatesList().size()>0 && p<=((ItemModel)getItem(i)).getItemDeliveryDatesList().size()-1){
                    deliveryDate.setText(((ItemModel)getItem(i)).getItemDeliveryDatesList().get(p));
                }
                if(((ItemModel)getItem(i)).getStockQtyList()!=null && ((ItemModel)getItem(i)).getStockQtyList().size()>0 && p<=((ItemModel)getItem(i)).getStockQtyList().size()-1){
                    stockQty.setText(((ItemModel)getItem(i)).getStockQtyList().get(p));
                }
                if((((ItemModel)getItem(i)).getTreatment1List()!=null && ((ItemModel)getItem(i)).getTreatment1List().size()>0 && p<=((ItemModel)getItem(i)).getTreatment1List().size()-1 && !TextUtils.isEmpty(((ItemModel)getItem(i)).getTreatment1List().get(p))) || (((ItemModel)getItem(i)).getTreatment2List()!=null && ((ItemModel)getItem(i)).getTreatment2List().size()>0 && p<=((ItemModel)getItem(i)).getTreatment2List().size()-1 && !TextUtils.isEmpty(((ItemModel)getItem(i)).getTreatment2List().get(p)))){
                    if(((ItemModel)getItem(i)).getTreatment1List()!=null && ((ItemModel)getItem(i)).getTreatment1List().size()>0 && p<=((ItemModel)getItem(i)).getTreatment1List().size()-1){
                        treatment1.setText(((ItemModel)getItem(i)).getTreatment1List().get(p));
                    }
                    if(((ItemModel)getItem(i)).getTreatment2List()!=null && ((ItemModel)getItem(i)).getTreatment2List().size()>0 && p<=((ItemModel)getItem(i)).getTreatment2List().size()-1){
                        treatment2.setText(((ItemModel)getItem(i)).getTreatment2List().get(p));
                    }
                }else{
                    treatment1.setText("NONE");
                    treatment2.setText("NONE");
                }


                if(((ItemModel)getItem(i)).getLengthList()!=null && ((ItemModel)getItem(i)).getLengthList().size()>0 && p<=((ItemModel)getItem(i)).getLengthList().size()-1  && !TextUtils.isEmpty(((ItemModel)getItem(i)).getLengthList().get(p))) {
                    length.setText(((ItemModel)getItem(i)).getLengthList().get(p));
                }
                if(((ItemModel)getItem(i)).getWidthList()!=null && ((ItemModel)getItem(i)).getWidthList().size()>0 && p<=((ItemModel)getItem(i)).getWidthList().size()-1 && !TextUtils.isEmpty(((ItemModel)getItem(i)).getWidthList().get(p))){
                    width.setText(((ItemModel)getItem(i)).getWidthList().get(p));
                }

                if((((ItemModel)getItem(i)).getDespList()!=null && ((ItemModel)getItem(i)).getDespList().size()>0 && p<=((ItemModel)getItem(i)).getDespList().size()-1 && ((ItemModel)getItem(i)).getDespList().get(p)!=null) && (((ItemModel)getItem(i)).getOrderedList() !=null && ((ItemModel)getItem(i)).getOrderedList().size()>0 && p<= ((ItemModel)getItem(i)).getOrderedList().size()-1 && ((ItemModel)getItem(i)).getOrderedList().get(p)!=null)){
                    orderQty.setText(String.valueOf(((ItemModel)getItem(i)).getDespList().get(p))+"/"+((ItemModel)getItem(i)).getOrderedList().get(p));
                }

                if(((ItemModel)getItem(i)).getShadesList()!=null && ((ItemModel)getItem(i)).getShadesList().size()>0 && p<=((ItemModel)getItem(i)).getShadesList().size()-1){
                    shades.setText(((ItemModel)getItem(i)).getShadesList().get(p));
                }

                holder.tableLayout.addView(itemView);
            }
            
        }else{
//            holder.lengthWidthRecycler.setVisibility(View.GONE);
        }
        if(holder.tableLayout.getChildCount() != rowsCount(i)+1){
            for(int childPos = 1; childPos<=(holder.tableLayout.getChildCount()-1)-rowsCount(i); childPos++){
                if(holder.tableLayout.getChildAt(childPos)!=null) {
                    holder.tableLayout.removeView(holder.tableLayout.getChildAt(childPos));
                }
            }
        }
       /* if(!TextUtils.isEmpty(((ItemModel)getItem(i)).getContainerNo())) {
            holder.itemContainerNo.setText(((ItemModel) getItem(i)).getContainerNo());
        }
        holder.itemShades.setText(((ItemModel) getItem(i)).getShades());*/
        if((((ItemModel)getItem(i)).getTotalDespQty()!=null && !((ItemModel)getItem(i)).getTotalDespQty().equalsIgnoreCase("0"))||
                (((ItemModel)getItem(i)).getTotalOrderedQty()!=null && !((ItemModel)getItem(i)).getTotalOrderedQty().equalsIgnoreCase("0"))){
            holder.despQty.setText( decimalFormat.format(Double.parseDouble(((ItemModel) getItem(i)).getTotalDespQty()))+ "/" + decimalFormat.format(Double.parseDouble(((ItemModel) getItem(i)).getTotalOrderedQty())));

        }else {
            holder.despQty.setText(((ItemModel) getItem(i)).getDespQty() + "/" + ((ItemModel) getItem(i)).getOrderedQty());
        }
        return view;
    }

    private int rowsCount(int itemPos) {
        if(((ItemModel)getItem(itemPos)).getLengthList()!=null || ((ItemModel)getItem(itemPos)).getWidthList()!=null){
            if(((ItemModel)getItem(itemPos)).getLengthList()!=null && ((ItemModel)getItem(itemPos)).getWidthList()!=null){
                if(((ItemModel)getItem(itemPos)).getLengthList().size() >= ((ItemModel)getItem(itemPos)).getWidthList().size()){
                    return ((ItemModel)getItem(itemPos)).getLengthList().size();
                }else{
                    return ((ItemModel)getItem(itemPos)).getWidthList().size();
                }
            }
            if(((ItemModel)getItem(itemPos)).getWidthList() == null){
                return ((ItemModel)getItem(itemPos)).getLengthList().size();
            }else{
                return ((ItemModel)getItem(itemPos)).getWidthList().size();
            }
        }
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return getCount();
    }

    private class ListHolder{
       LinearLayout mainLayout;
       LinearLayout expandedItemLayout;
       TextView itemName;
       TextView despQty;
       TextView itemStatus;
       TextView itemNo;
       TextView labelSalesOrder;
//       RecyclerView lengthWidthRecycler;
//       AdaptiveTableLayout adaptiveTableLayout;
       TableLayout tableLayout;
       TextView itemBillNo;
       TextView itemBillDate;
       TextView itemTreatment1;
       TextView itemTreatment2;
       TextView itemShades;
       TextView itemContainerNo;
       TextView containerNo;
    }
}
