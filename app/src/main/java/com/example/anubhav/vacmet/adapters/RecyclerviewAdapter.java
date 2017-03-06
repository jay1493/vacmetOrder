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

import java.util.List;

/**
 * Created by anubhav on 23/1/17.
 */

public class RecyclerviewAdapter extends RecyclerView.Adapter<RecyclerviewAdapter.Viewholder> {
    private final LayoutInflater inflater;
    Context context;
    List<OrderModel> list;
    private ItemClickListener itemClickListener;
    public RecyclerviewAdapter(Context context, List<OrderModel> list, ItemClickListener itemClickListener) {
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.list = list;
        this.itemClickListener = itemClickListener;
    }

    @Override
    public Viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.advanced_recyclerview_row, parent, false);
        return new Viewholder(itemView);
    }

    @Override
    public void onBindViewHolder(Viewholder holder, int position) {
        holder.partyName.setText(list.get(position).getPartyName());
        holder.orderNo.setText(list.get(position).getOrderNo());
        holder.orderDate.setText(list.get(position).getOrderDate());
        holder.orderQty.setText(list.get(position).getOrderQty());
        holder.despQty.setText(list.get(position).getDespQty());
        holder.deliveryDate.setText(list.get(position).getDeliveryDate());
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
        public Viewholder(View itemView) {
            super(itemView);
            partyName = (TextView) itemView.findViewById(R.id.partyName);
            orderNo = (TextView) itemView.findViewById(R.id.tvOrderNo);
            orderDate = (TextView) itemView.findViewById(R.id.tvOrderDate);
            orderQty = (TextView) itemView.findViewById(R.id.tvOrderQty);
            despQty = (TextView) itemView.findViewById(R.id.tvOrderDespQty);
            deliveryDate = (TextView) itemView.findViewById(R.id.deliveryDate);
            itemView.setOnClickListener(this);
         }

        @Override
        public void onClick(View view) {
            itemClickListener.onClick(view,getAdapterPosition());
        }
    }
}
