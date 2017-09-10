package com.example.anubhav.vacmet.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.anubhav.vacmet.R;
import com.example.anubhav.vacmet.interfaces.ItemClickListener;
import com.example.anubhav.vacmet.model.OrderModel;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by anubhav on 23/1/17.
 */

public class RecyclerviewAdapter extends RecyclerView.Adapter<RecyclerviewAdapter.Viewholder> {
    private final LayoutInflater inflater;
    Context context;
    List<OrderModel> list;
    private ItemClickListener itemClickListener;
    private DecimalFormat decimalFormat;
    public RecyclerviewAdapter(Context context, List<OrderModel> list, ItemClickListener itemClickListener) {
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.list = list;
        this.itemClickListener = itemClickListener;
        decimalFormat = new DecimalFormat("#.##");
    }

    @Override
    public Viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        switch (viewType){
            case 0:
                //Card layout
                itemView = LayoutInflater.
                        from(parent.getContext()).
                        inflate(R.layout.advanced_recyclerview_row, parent, false);
                break;
            case 1:
                //Simple TextView Layout
                itemView = LayoutInflater.
                        from(parent.getContext()).
                        inflate(R.layout.simple_textview_layout, parent, false);
                break;
        }

        return new Viewholder(itemView,viewType);
    }

    @Override
    public int getItemViewType(int position) {
        if(list.size()>0){
            return 0;
        }else{
            return 1;
        }

    }

    @Override
    public void onBindViewHolder(Viewholder holder, int position) {
        switch (holder.getItemViewType()){
            case 0:
                holder.partyName.setText(list.get(position).getPartyName());
                holder.orderNo.setText(list.get(position).getOrderNo());
                holder.orderDate.setText(list.get(position).getOrderDate());
                holder.orderQty.setText(String.valueOf(decimalFormat.format(Double.parseDouble(list.get(position).getDespQty())+Double.parseDouble(list.get(position).getInProdQty()))));
                holder.despQty.setText(list.get(position).getDespQty());
                holder.deliveryDate.setText(list.get(position).getDeliveryDate());
                break;
            case 1:
                holder.noRecords.setText(context.getResources().getString(R.string.No_records_found_for_this_query));
                break;
        }

    }

    @Override
    public int getItemCount() {
        return (list!=null)?list.size():0;
    }


    public class Viewholder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView partyName ;
        TextView orderNo;
        TextView orderDate;
        TextView orderQty;
        TextView despQty;
        TextView deliveryDate;
        TextView noRecords;
        public Viewholder(View itemView, int viewType) {
            super(itemView);
            switch (viewType){
                case 0:
                    partyName = (TextView) itemView.findViewById(R.id.partyName);
                    orderNo = (TextView) itemView.findViewById(R.id.tvOrderNo);
                    orderDate = (TextView) itemView.findViewById(R.id.tvOrderDate);
                    orderQty = (TextView) itemView.findViewById(R.id.tvOrderQty);
                    despQty = (TextView) itemView.findViewById(R.id.tvOrderDespQty);
                    deliveryDate = (TextView) itemView.findViewById(R.id.deliveryDate);
                    itemView.setOnClickListener(this);
                    break;
                case 1:
                    noRecords = (TextView) itemView.findViewById(R.id.txt_no_records_found);
                    break;
            }

         }

        @Override
        public void onClick(View view) {
            itemClickListener.onClick(view,getAdapterPosition());
        }
    }
}
