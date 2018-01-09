package com.imagesoftware.anubhav.vacmet.adapters;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.imagesoftware.anubhav.vacmet.R;
import com.imagesoftware.anubhav.vacmet.interfaces.ItemClickListener;
import com.imagesoftware.anubhav.vacmet.model.OrderModel;

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
    private boolean isDispatched;
    private OpenPdfClicked openPdfClicked;

    public RecyclerviewAdapter(Context context, List<OrderModel> list, ItemClickListener itemClickListener, boolean isDispatched, OpenPdfClicked openPdf) {
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.list = list;
        this.itemClickListener = itemClickListener;
        decimalFormat = new DecimalFormat("#.##");
        this.isDispatched = isDispatched;
        this.openPdfClicked = openPdf;
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
    public void onBindViewHolder(final Viewholder holder, final int position) {
        switch (holder.getItemViewType()){
            case 0:
                holder.partyName.setText(list.get(position).getPartyName());

                holder.orderQty.setText(list.get(position).getOrderQty());
                holder.despQty.setText(list.get(position).getDespQty());

                ViewCompat.setTransitionName(holder.orderNo,list.get(position).getOrderNo());
                holder.llMain.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        itemClickListener.onClick(v,position,holder.orderNo);
                    }
                });
                if(isDispatched){
                    holder.llInvoice.setVisibility(View.VISIBLE);
                    holder.txtInvoice.setText(list.get(position).getInvoiceNo());
                    holder.llOrderNo.setVisibility(View.GONE);
                    holder.orderDate.setText(list.get(position).getInvoiceDate());
                    holder.txtOrderDateHeading.setText(context.getResources().getString(R.string.invoice_date));
                    holder.deliveryDate.setVisibility(View.GONE);

                    holder.openPdf.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            openPdfClicked.onPdfClick(position);
                        }
                    });
                }else{
                    holder.llInvoice.setVisibility(View.GONE);
                    holder.llOrderNo.setVisibility(View.VISIBLE);
                    holder.orderDate.setText(list.get(position).getOrderDate());
                    holder.txtOrderDateHeading.setText(context.getResources().getString(R.string.Order_Date));
                    holder.orderNo.setText(list.get(position).getOrderNo());
                    holder.deliveryDate.setText(list.get(position).getDeliveryDate());
                    holder.deliveryDate.setVisibility(View.VISIBLE);

                }
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
        LinearLayout llOrderDate;
        LinearLayout llMain;
        TextView partyName ;
        TextView orderNo;
        TextView orderDate;
        TextView orderQty;
        TextView despQty;
        TextView deliveryDate;
        TextView txtOrderDateHeading;
        TextView noRecords;
        LinearLayout llInvoice;
        LinearLayout llOrderNo;
        TextView txtInvoice;
        ImageView openPdf;
        public Viewholder(View itemView, int viewType) {
            super(itemView);
            switch (viewType){
                case 0:
                    llMain = (LinearLayout) itemView.findViewById(R.id.ll_orderStatus);
                    partyName = (TextView) itemView.findViewById(R.id.partyName);
                    txtOrderDateHeading = (TextView) itemView.findViewById(R.id.tvOrderDateHeading);
                    orderNo = (TextView) itemView.findViewById(R.id.tvOrderNo);
                    orderDate = (TextView) itemView.findViewById(R.id.tvOrderDate);
                    llOrderDate = (LinearLayout) itemView.findViewById(R.id.ll_OrderDate);
                    orderQty = (TextView) itemView.findViewById(R.id.tvOrderQty);
                    despQty = (TextView) itemView.findViewById(R.id.tvOrderDespQty);
                    deliveryDate = (TextView) itemView.findViewById(R.id.deliveryDate);
                    llInvoice = (LinearLayout) itemView.findViewById(R.id.ll_invoiceNo);
                    llOrderNo = (LinearLayout) itemView.findViewById(R.id.ll_OrderNo);
                    txtInvoice = (TextView) itemView.findViewById(R.id.tvInvoiceNo);
                    openPdf = (ImageView) itemView.findViewById(R.id.open_invoice_pdf);
                    itemView.setOnClickListener(this);
                    break;
                case 1:
                    noRecords = (TextView) itemView.findViewById(R.id.txt_no_records_found);
                    break;
            }

         }

        @Override
        public void onClick(View view) {
//            itemClickListener.onClick(view,getAdapterPosition());
        }
    }

    public interface OpenPdfClicked{
        void onPdfClick(int pos);
    }
}
