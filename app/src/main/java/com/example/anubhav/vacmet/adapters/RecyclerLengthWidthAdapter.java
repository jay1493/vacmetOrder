package com.example.anubhav.vacmet.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.anubhav.vacmet.R;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by anubhav on 26/9/17.
 */

public class RecyclerLengthWidthAdapter extends RecyclerView.Adapter<RecyclerLengthWidthAdapter.CustomHolder> {

    private Context context;
    private List<String> lengthList;
    private List<String> widthList;
    private List<Double> despOrders;
    private List<Double> inProdOrders;
    public RecyclerLengthWidthAdapter(Context context, List<String> lengthList, List<String> widthList, List<Double> despList, List<Double> inProdList) {
        this.context = context;
        this.lengthList = lengthList;
        this.widthList = widthList;
        this.despOrders = despList;
        this.inProdOrders = inProdList;

    }

    @Override
    public CustomHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new CustomHolder(LayoutInflater.from(context).inflate(R.layout.length_width_recycler_item,null));
    }

    @Override
    public void onBindViewHolder(CustomHolder customHolder, int i) {
        customHolder.length.setText("0");
        customHolder.width.setText("0");
        customHolder.orderQty.setText("0/0");
        if(lengthList!=null && !TextUtils.isEmpty(lengthList.get(i))) {
            customHolder.length.setText(lengthList.get(i));
        }
        if(widthList!=null && !TextUtils.isEmpty(widthList.get(i))){
            customHolder.width.setText(widthList.get(i));
        }
        if((despOrders!=null && despOrders.size()>0 && i<=despOrders.size()-1 && despOrders.get(i)!=null) && (inProdOrders!=null && inProdOrders.size()>0 && i<=inProdOrders.size()-1 && inProdOrders.get(i)!=null)){
            customHolder.orderQty.setText(String.valueOf(despOrders.get(i))+"/"+String.valueOf(despOrders.get(i)+inProdOrders.get(i)));
        }
    }

    @Override
    public int getItemCount() {
        if(lengthList!=null || widthList!=null){
            if(lengthList!=null && widthList!=null){
                if(lengthList.size() >= widthList.size()){
                    return lengthList.size();
                }else{
                    return widthList.size();
                }
            }
            if(widthList == null){
                return lengthList.size();
            }else{
                return widthList.size();
            }
        }
        return 0;
    }

    class CustomHolder extends RecyclerView.ViewHolder {

        private TextView length;
        private TextView width;
        private TextView orderQty;

        public CustomHolder(View itemView) {
            super(itemView);
            length = (TextView) itemView.findViewById(R.id.txt_length);
            width = (TextView) itemView.findViewById(R.id.txt_width);
            orderQty = (TextView) itemView.findViewById(R.id.txt_orderQty);
        }
    }
}
