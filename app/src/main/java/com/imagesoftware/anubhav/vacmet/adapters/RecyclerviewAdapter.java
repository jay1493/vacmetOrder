package com.imagesoftware.anubhav.vacmet.adapters;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.imagesoftware.anubhav.vacmet.R;
import com.imagesoftware.anubhav.vacmet.interfaces.ItemClickListener;
import com.imagesoftware.anubhav.vacmet.model.LogisticsModel;
import com.imagesoftware.anubhav.vacmet.model.OrderModel;

import java.text.DecimalFormat;
import java.util.List;

import static com.imagesoftware.anubhav.vacmet.OrderStatus.convertStringInSpanColors;

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
    private DeliveryDateChanged deliveryDateChangedListener;
    private RemarkEntered remarkEntered;
    private boolean isAdmin;
    private UploadInvoiceListener uploadPdf;
    private LogisticsDetailsListener logisticsDetails;
    private ShowPrevModifiedDatesListener showPrevModifiedDates;

    public RecyclerviewAdapter(Context context, List<OrderModel> list, ItemClickListener itemClickListener, boolean isDispatched, OpenPdfClicked openPdf,DeliveryDateChanged deliveryDateChanged,
                               boolean admin, RemarkEntered remarkEnteredListener, UploadInvoiceListener uploadInvoiceListener, LogisticsDetailsListener logisticsDetailsListener,
                               ShowPrevModifiedDatesListener showPrevModifiedDatesListener) {
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.list = list;
        this.itemClickListener = itemClickListener;
        decimalFormat = new DecimalFormat("#.##");
        this.isDispatched = isDispatched;
        this.openPdfClicked = openPdf;
        this.deliveryDateChangedListener = deliveryDateChanged;
        this.isAdmin = admin;
        this.remarkEntered = remarkEnteredListener;
        this.uploadPdf = uploadInvoiceListener;
        this.logisticsDetails = logisticsDetailsListener;
        this.showPrevModifiedDates = showPrevModifiedDatesListener;
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
                if(!TextUtils.isEmpty(list.get(position).getCustomerPODate()) || !TextUtils.isEmpty(list.get(position).getCustomerPONo()) || !TextUtils.isEmpty(list.get(position).getPartyPODate())
                        || !TextUtils.isEmpty(list.get(position).getPartyPONo()) || !TextUtils.isEmpty(list.get(position).getPartyPOETA())){
                    holder.piLayout.setVisibility(View.VISIBLE);
                    if(!TextUtils.isEmpty(list.get(position).getCustomerPODate())){
                        holder.llBuyerPiDate.setVisibility(View.VISIBLE);
                        holder.piDate.setText(list.get(position).getCustomerPODate());
                    }else{
                        holder.llBuyerPiDate.setVisibility(View.GONE);
                    }
                    if(!TextUtils.isEmpty(list.get(position).getCustomerPONo())){
                        holder.llBuyerPiNo.setVisibility(View.VISIBLE);
                        holder.piNo.setText(list.get(position).getCustomerPONo());
                    }else{
                        holder.llBuyerPiNo.setVisibility(View.GONE);
                    }
                    if(!TextUtils.isEmpty(list.get(position).getPartyPONo())){
                        holder.llPartyPiNo.setVisibility(View.VISIBLE);
                        holder.partyPiNo.setText(list.get(position).getPartyPONo());
                    }else{
                        holder.llPartyPiNo.setVisibility(View.GONE);
                    }
                    if(!TextUtils.isEmpty(list.get(position).getPartyPODate())){
                        holder.llPartyPiDate.setVisibility(View.VISIBLE);
                        holder.partyPiDate.setText(list.get(position).getPartyPODate());
                    }else{
                        holder.llPartyPiDate.setVisibility(View.GONE);
                    }
                    if(!TextUtils.isEmpty(list.get(position).getPartyPOETA())){
                        holder.llPIETA.setVisibility(View.VISIBLE);
                        holder.piETA.setText(list.get(position).getPartyPOETA());
                    }else{
                        holder.llPIETA.setVisibility(View.GONE);
                    }

                }else{
                    holder.piLayout.setVisibility(View.GONE);
                }
                if(isDispatched){
                    holder.showPreviousModDates.setVisibility(View.GONE);
                    holder.showPreviousModDates.setOnClickListener(null);
                    holder.llInvoice.setVisibility(View.VISIBLE);
                    holder.txtInvoice.setText(list.get(position).getInvoiceNo());
                    holder.llOrderNo.setVisibility(View.GONE);
                    holder.orderDate.setText(list.get(position).getInvoiceDate());
                    holder.txtOrderDateHeading.setText(context.getResources().getString(R.string.invoice_date));
                    holder.deliveryDate.setVisibility(View.GONE);
                    holder.deliveryDate.setOnClickListener(null);
                    holder.llAdminNotes.setOnClickListener(null);
                    holder.llAdminNotes.setVisibility(View.GONE);
                    if(isAdmin) {
                        holder.uploadInvoice.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                uploadPdf.onUploadInvoicePdfClicked(view,position);
                            }
                        });
                        holder.uploadInvoice.setVisibility(View.VISIBLE);
                    }else{
                        holder.uploadInvoice.setOnClickListener(null);
                        holder.uploadInvoice.setVisibility(View.GONE);
                    }
                    holder.openPdf.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            openPdfClicked.onPdfClick(position);
                        }
                    });

                    if(isAdmin){
                        holder.llLogistics.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                logisticsDetails.onLogisticsDetailsInput(view,position);
                            }
                        });
                        holder.llMainLogistics.setVisibility(View.VISIBLE);
                        if(list.get(position).getLogisticsModel()!=null){
                            LogisticsModel logisticsModel = list.get(position).getLogisticsModel();
                            if(!TextUtils.isEmpty(logisticsModel.getEta()) || !TextUtils.isEmpty(logisticsModel.getContainerNo())
                                    || !TextUtils.isEmpty(logisticsModel.getVesselNo()) || !TextUtils.isEmpty(logisticsModel.getBlNo())){
                                StringBuilder logisticsStringBuilder = new StringBuilder();
                                if(!TextUtils.isEmpty(logisticsModel.getBlNo())){
                                    logisticsStringBuilder.append("Bl No: "+logisticsModel.getBlNo());
                                }
                                if(!TextUtils.isEmpty(logisticsModel.getContainerNo())){
                                    if(logisticsStringBuilder.length()>0){
                                        logisticsStringBuilder.append(" || Container No: "+logisticsModel.getContainerNo());
                                    }else{
                                        logisticsStringBuilder.append("Container No: "+logisticsModel.getContainerNo());
                                    }

                                }
                                if(!TextUtils.isEmpty(logisticsModel.getVesselNo())){
                                    if(logisticsStringBuilder.length()>0){
                                        logisticsStringBuilder.append(" || Vessel No: "+logisticsModel.getVesselNo());
                                    }else{
                                        logisticsStringBuilder.append("Vessel No: "+logisticsModel.getVesselNo());
                                    }
                                }
                                if(!TextUtils.isEmpty(logisticsModel.getEta())){
                                    if(logisticsStringBuilder.length()>0){
                                        logisticsStringBuilder.append(" || E.T.A: "+logisticsModel.getEta());
                                    }else{
                                        logisticsStringBuilder.append("E.T.A: "+logisticsModel.getEta());
                                    }
                                }
                                holder.txtHeaderLogistics.setText(context.getResources().getString(R.string.logistics_details));
                                holder.txtLogistics.setText(convertStringInSpanColors(logisticsStringBuilder.toString(),new String[]{"Bl No:","Container No:","Vessel No:","E.T.A:"}));
                            }else{
                                holder.txtHeaderLogistics.setText(context.getResources().getString(R.string.input_logistics_details));
                            }
                        }else{
                            holder.txtHeaderLogistics.setText(context.getResources().getString(R.string.input_logistics_details));
                        }
                    }else{
                        holder.llLogistics.setOnClickListener(null);
                        if(list.get(position).getLogisticsModel()!=null){
                            LogisticsModel logisticsModel = list.get(position).getLogisticsModel();
                            if(!TextUtils.isEmpty(logisticsModel.getEta()) || !TextUtils.isEmpty(logisticsModel.getContainerNo())
                                    || !TextUtils.isEmpty(logisticsModel.getVesselNo()) || !TextUtils.isEmpty(logisticsModel.getBlNo())){
                                StringBuilder logisticsStringBuilder = new StringBuilder();
                                if(!TextUtils.isEmpty(logisticsModel.getBlNo())){

                                    logisticsStringBuilder.append("Bl No: "+logisticsModel.getBlNo());
                                }
                                if(!TextUtils.isEmpty(logisticsModel.getContainerNo())){
                                    if(logisticsStringBuilder.length()>0){
                                        logisticsStringBuilder.append(" || Container No: "+logisticsModel.getContainerNo());
                                    }else{
                                        logisticsStringBuilder.append("Container No: "+logisticsModel.getContainerNo());
                                    }

                                }
                                if(!TextUtils.isEmpty(logisticsModel.getVesselNo())){
                                    if(logisticsStringBuilder.length()>0){
                                        logisticsStringBuilder.append(" || Vessel No: "+logisticsModel.getVesselNo());
                                    }else{
                                        logisticsStringBuilder.append("Vessel No: "+logisticsModel.getVesselNo());
                                    }
                                }
                                if(!TextUtils.isEmpty(logisticsModel.getEta())){
                                    if(logisticsStringBuilder.length()>0){
                                        logisticsStringBuilder.append(" || E.T.A: "+logisticsModel.getEta());
                                    }else{
                                        logisticsStringBuilder.append("E.T.A: "+logisticsModel.getEta());
                                    }
                                }
                                holder.txtHeaderLogistics.setText(context.getResources().getString(R.string.logistics_details));
                                holder.txtLogistics.setText(convertStringInSpanColors(logisticsStringBuilder.toString(),new String[]{"Bl No:","Container No:","Vessel No:","E.T.A:"}));
                                holder.llMainLogistics.setVisibility(View.VISIBLE);
                            }else{
                                holder.llMainLogistics.setVisibility(View.GONE);
                                holder.llLogistics.setOnClickListener(null);
                            }
                        }else{
                            holder.llMainLogistics.setVisibility(View.GONE);
                            holder.llLogistics.setOnClickListener(null);
                        }
                    }

                }else{
                    if(list.get(position).getOldModifiedDates()!=null && list.get(position).getOldModifiedDates().size()>0) {
                        holder.showPreviousModDates.setVisibility(View.VISIBLE);
                        holder.showPreviousModDates.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                showPrevModifiedDates.showPreviousDates(view, position);
                            }
                        });
                    }else{
                        holder.showPreviousModDates.setVisibility(View.GONE);
                        holder.showPreviousModDates.setOnClickListener(null);
                    }
                    holder.llMainLogistics.setVisibility(View.GONE);
                    holder.llLogistics.setOnClickListener(null);
                    holder.uploadInvoice.setVisibility(View.GONE);
                    holder.llInvoice.setVisibility(View.GONE);
                    holder.llOrderNo.setVisibility(View.VISIBLE);
                    holder.orderDate.setText(list.get(position).getOrderDate());
                    holder.txtOrderDateHeading.setText(context.getResources().getString(R.string.Order_Date));
                    holder.orderNo.setText(list.get(position).getOrderNo());
                    if(!isAdmin) {
                        if (!TextUtils.isEmpty(list.get(position).getAdminNotes())) {
                            holder.llAdminNotes.setVisibility(View.VISIBLE);
                            holder.txtAdminNotes.setText("Remarks from Admin: " + list.get(position).getAdminNotes());
                            holder.txtAdminNotes.setSelected(true);
                        } else {
                            holder.llAdminNotes.setVisibility(View.GONE);
                        }
                    }else{
                        holder.llAdminNotes.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                remarkEntered.onRemarkEnteredFromRecylerItem(view,position);
                            }
                        });
                        holder.llAdminNotes.setVisibility(View.VISIBLE);
                        if (!TextUtils.isEmpty(list.get(position).getAdminNotes())) {
                            holder.txtAdminNotes.setText("Remarks: " + list.get(position).getAdminNotes());
                            holder.txtAdminNotes.setSelected(true);
                        }else{
                            holder.txtAdminNotes.setText("Enter Remarks ");
                        }
                    }
                    if(!TextUtils.isEmpty(list.get(position).getDeliveryDate())) {
                        holder.deliveryDate.setText(list.get(position).getDeliveryDate());
                        holder.deliveryDate.setVisibility(View.VISIBLE);
                    }else{
                        holder.deliveryDate.setVisibility(View.GONE);
                    }
                    if(isAdmin) {
                        holder.deliveryDate.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                deliveryDateChangedListener.onDateClickListenerCall(v, position);
                            }
                        });
                        holder.deliveryDate.setVisibility(View.VISIBLE);
                        if(!TextUtils.isEmpty(list.get(position).getDeliveryDate())) {
                            holder.deliveryDate.setText(list.get(position).getDeliveryDate());
                        }else{
//                            holder.deliveryDate.setText(context.getResources().getString(R.string.estimate_delivery_date));
                        }
                    }else{
                        holder.deliveryDate.setOnClickListener(null);
                    }

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
        LinearLayout llAdminNotes;
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
        LinearLayout llLogistics;
        LinearLayout llMainLogistics;
        LinearLayout llPIETA;
        LinearLayout llBuyerPiDate;
        LinearLayout llPartyPiDate;
        LinearLayout llBuyerPiNo;
        LinearLayout llPartyPiNo;
        TextView txtInvoice;
        TextView txtAdminNotes;
        TextView txtLogistics;
        TextView txtHeaderLogistics;
        ImageView openPdf;
        ImageView showPreviousModDates;
        RelativeLayout uploadInvoice;
        RelativeLayout piLayout;
        TextView piDate;
        TextView piNo;
        TextView partyPiNo;
        TextView partyPiDate;
        TextView piETA;


        public Viewholder(View itemView, int viewType) {
            super(itemView);
            switch (viewType){
                case 0:
                    llMain = (LinearLayout) itemView.findViewById(R.id.ll_orderStatus);
                    llAdminNotes = (LinearLayout) itemView.findViewById(R.id.ll_adminNotes);
                    partyName = (TextView) itemView.findViewById(R.id.partyName);
                    uploadInvoice = (RelativeLayout) itemView.findViewById(R.id.rl_uploadInvoiceAdmin);
                    piLayout = (RelativeLayout) itemView.findViewById(R.id.rl_customerPiDetails);
                    txtOrderDateHeading = (TextView) itemView.findViewById(R.id.tvOrderDateHeading);
                    piDate = (TextView) itemView.findViewById(R.id.customerPi_piDate);
                    piNo = (TextView) itemView.findViewById(R.id.customerPi_piNo);
                    partyPiNo = (TextView) itemView.findViewById(R.id.partyPi_piNo);
                    partyPiDate = (TextView) itemView.findViewById(R.id.partyPi_piDate);
                    piETA = (TextView) itemView.findViewById(R.id.pi_eta);
                    orderNo = (TextView) itemView.findViewById(R.id.tvOrderNo);
                    orderDate = (TextView) itemView.findViewById(R.id.tvOrderDate);
                    llOrderDate = (LinearLayout) itemView.findViewById(R.id.ll_OrderDate);
                    llLogistics = (LinearLayout) itemView.findViewById(R.id.ll_logistics);
                    llMainLogistics = (LinearLayout) itemView.findViewById(R.id.mainLogistics);
                    llPIETA = (LinearLayout) itemView.findViewById(R.id.ll_piETA);
                    llPartyPiDate = (LinearLayout) itemView.findViewById(R.id.ll_partyPIDate);
                    llPartyPiNo = (LinearLayout) itemView.findViewById(R.id.ll_partyPINo);
                    llBuyerPiDate = (LinearLayout) itemView.findViewById(R.id.ll_buyerPIDate);
                    llBuyerPiNo = (LinearLayout) itemView.findViewById(R.id.ll_buyerPINo);

                    orderQty = (TextView) itemView.findViewById(R.id.tvOrderQty);
                    despQty = (TextView) itemView.findViewById(R.id.tvOrderDespQty);
                    deliveryDate = (TextView) itemView.findViewById(R.id.deliveryDate);
                    llInvoice = (LinearLayout) itemView.findViewById(R.id.ll_invoiceNo);
                    llOrderNo = (LinearLayout) itemView.findViewById(R.id.ll_OrderNo);
                    txtInvoice = (TextView) itemView.findViewById(R.id.tvInvoiceNo);
                    txtAdminNotes = (TextView) itemView.findViewById(R.id.tv_adminNotes);
                    txtLogistics = (TextView) itemView.findViewById(R.id.tv_logistics);
                    txtHeaderLogistics = (TextView) itemView.findViewById(R.id.tv_headerLogistics);
                    openPdf = (ImageView) itemView.findViewById(R.id.open_invoice_pdf);
                    showPreviousModDates = (ImageView) itemView.findViewById(R.id.prevModifiedDates);
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
    public interface DeliveryDateChanged{
        void onDateClickListenerCall(View view, int pos);
    }
    public interface RemarkEntered{
        void onRemarkEnteredFromRecylerItem(View view,int pos);
    }
    public interface UploadInvoiceListener{
        void onUploadInvoicePdfClicked(View view, int pos);
    }

    public interface LogisticsDetailsListener{
        void onLogisticsDetailsInput(View view,int pos);
    }

    public interface ShowPrevModifiedDatesListener{
        void showPreviousDates(View view, int pos);
    }

}
