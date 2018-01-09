package com.imagesoftware.anubhav.vacmet.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.imagesoftware.anubhav.vacmet.R;

import java.text.DecimalFormat;

/**
 * Created by anubhav on 1/10/17.
 */

public class ItemDetailsViewPagerAdapter extends PagerAdapter {

    private final DecimalFormat decimalFormat;
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
    private TextView length,width,orderQty;
    private TextView deliveryDate,treatment1,treatment2,stockQty;
    private LinearLayout llTreatments;
    private View mCurrentView;

    public ItemDetailsViewPagerAdapter(Context context, String lengthList, String widthList, Double list, Double orderedList, String itemDeliveryDatesList, String stockQtyList, String treatment1List, String treatment2List, String shadesList, Double inProdList) {

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
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = null;
        switch (position){
            case 0:
                itemView = LayoutInflater.from(context).inflate(R.layout.item_info_layout,null);
                length = (TextView) itemView.findViewById(R.id.txt_length);
                width = (TextView) itemView.findViewById(R.id.txt_width);
                orderQty = (TextView) itemView.findViewById(R.id.txt_orderQty);
                length.setText("0");
                width.setText("0");
                orderQty.setText("0/0");
                if(lengthList!=null && !TextUtils.isEmpty(lengthList)) {
                    length.setText(lengthList);
                }
                if(widthList!=null && !TextUtils.isEmpty(widthList)){
                    width.setText(widthList);
                }
                if(despOrders!=null && totalOrderedItemsList!=null) {
                    orderQty.setText(decimalFormat.format(despOrders) + "/" + decimalFormat.format(totalOrderedItemsList));
                }

                break;
            case 1:
                itemView = LayoutInflater.from(context).inflate(R.layout.item_details_layout,null);
                length = (TextView) itemView.findViewById(R.id.txt_length);
                width = (TextView) itemView.findViewById(R.id.txt_width);
                orderQty = (TextView) itemView.findViewById(R.id.txt_orderQty);
                deliveryDate = (TextView) itemView.findViewById(R.id.txt_item_delivery_date);
                stockQty = (TextView) itemView.findViewById(R.id.txt_item_stock);
                treatment1 = (TextView) itemView.findViewById(R.id.txt_treatment1);
                treatment2 = (TextView) itemView.findViewById(R.id.txt_treatment2);
                llTreatments = (LinearLayout) itemView.findViewById(R.id.ll_treatments);

                length.setText("0");
                width.setText("0");
                orderQty.setText("0/0");
                stockQty.setText("0");
                if(itemDeliveryDatesList!=null){
                    deliveryDate.setText(itemDeliveryDatesList);
                }
                if(stockList!=null ){
                    stockQty.setText(stockList);
                }
                if((treatment1List!=null && !TextUtils.isEmpty(treatment1List)) || (treatment2List!=null  && !TextUtils.isEmpty(treatment2List))){
                    llTreatments.setVisibility(View.VISIBLE);
                    if(treatment1List!=null){
                        treatment1.setText(treatment1List);
                    }
                    if(treatment2List!=null){
                        treatment2.setText(treatment2List);
                    }
                }else{
                    llTreatments.setVisibility(View.GONE);
                }


                if(lengthList!=null && !TextUtils.isEmpty(lengthList)) {
                    length.setText(lengthList);
                }
                if(widthList!=null && !TextUtils.isEmpty(widthList)){
                    width.setText(widthList);
                }

                if(despOrders!=null && totalOrderedItemsList!=null) {
                    orderQty.setText(decimalFormat.format(despOrders) + "/" + decimalFormat.format(totalOrderedItemsList));
                }
                
                break;
        }
        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return super.getPageTitle(position);
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        mCurrentView = (View)object;
    }

    public View getmCurrentView(){
        return mCurrentView;
    }

    @Override
    public int getCount() {
        return 2;
    }

    /**
     *
     * @param view
     * @param o
     * @return
     * The object returned by instantiateItem() is a key/identifier.
     * This method checks whether the View passed to it (representing the page) is
     * associated with that key or not. It is required by a PagerAdapter to function properly.
     * For our example, the implementation of this method is really simple, we just compare the
     * two instances and return the evaluated boolean.
     *
     */

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return view==(RelativeLayout)o;
    }
}
